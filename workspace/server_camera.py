from flask import Flask, Response, stream_with_context
from picamera2 import Picamera2, Preview
from io import BytesIO
import time
from PIL import Image
import io


app = Flask(__name__)

def video_generator():
    # with picam:
        picam = Picamera2()

        picam.resolution = (640, 480)
        picam.framerate = 24

        try:
            # 开始预览
            picam.start_preview(Preview.NULL)

            picam.start()
            # 创建一个缓冲区来保存JPEG图像
            image_stream = BytesIO()

            # 捕获图像并发送
            while True:
                # 捕获一帧图像，得到的是RGBA四通道数据
                image_stream = picam.capture_array("main")

                # 将透明度信息舍去，留下RGB数据
                image_stream = image_stream[:,:,:3]

                # 将 NumPy 数组转换为 PIL 图像
                image_pil = Image.fromarray(image_stream, 'RGB')  # 假设 image_array 是 RGB 图像

                # 将 PIL 图像保存到 JPEG 格式的 BytesIO 对象中
                byte_io = io.BytesIO()
                image_pil.save(byte_io, format='JPEG')
                byte_io.seek(0)  # 移动到 BytesIO 对象的开头
                # 将图像发送给客户端
                yield (b'--frame\r\n'
                    b'Content-Type: image/jpeg\r\n\r\n' + 
                    byte_io.read() + b'\r\n\r\n')
                byte_io.seek(0)
                byte_io.truncate()

        finally:
            # 停止预览并释放相机资源
            picam.stop_preview()
            picam.close()

@app.route('/')
def video_feed():
    response = Response(stream_with_context(video_generator()), 
                        mimetype='multipart/x-mixed-replace; boundary=frame')
    # 设置响应头，告诉浏览器连接可以保持开启状态
    response.headers['Connection'] = 'keep-alive'
    return response

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8091, threaded=True, debug=True)
    