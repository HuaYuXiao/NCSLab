from flask import (
    Flask, 
    render_template, 
    redirect, 
    url_for, 
    Response, 
    stream_with_context, 
    send_from_directory,
    request, 
    jsonify
)
from flask_socketio import SocketIO, emit
import subprocess
import time
from io import BytesIO
import threading
from picamera2 import Picamera2, Preview
from PIL import Image
import io
import RPi.GPIO as GPIO


FOLDER_TEMPLATE = '/home/hyx020222/NCSLab/template'
FOLDER_3D_MODEL = '/home/hyx020222/NCSLab/Img/IMG_0746.jpeg'

PIN_CHANNEL_1 = 18
PIN_CHANNEL_2 = 23
PIN_CHANNEL_3 = 24
PIN_LIST = [PIN_CHANNEL_1, PIN_CHANNEL_2, PIN_CHANNEL_3]


# ?????????????
def capture_image(stream, socketio):
    try:
        global camera
        # ??? PiCamera
        camera = Picamera2()
        # ????????
        camera.resolution = (640, 480)
    except RuntimeError as e:
        print("\033[91m"+str(e)+"\033[0m")
    except NameError as e:
        print("\033[91m"+str(e)+"\033[0m")
    except AttributeError as e:
        print(e)

    while True:
        stream.seek(0)
        camera.capture(stream, 'jpeg')
        image = stream.getvalue()
        socketio.emit('image', {'image': image})
        time.sleep(0.1)  # ????????s

def video_generator(picam):
    with picam:
        picam.resolution = (640, 480)
        picam.framerate = 24  
        try:
            # 开始预览
            picam.start_preview(Preview.NULL)

            picam.start()
            time.sleep(2)
            print('[debug] start')
            # picam.capture_file("test.jpg")  # 拍摄照片并保存为 "test.jpg"
            # 创建一个缓冲区来保存JPEG图像
            image_stream = BytesIO()

            # 捕获图像并发送
            i = 0
            while True:
                # i=i+1
                # print(f'[debug]times:{i}')
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
                    b'Content-Type: image/jpeg\r\n\r\n' + byte_io.read() + b'\r\n\r\n')
                byte_io.seek(0)
                byte_io.truncate()

        finally:
            # 停止预览并释放相机资源
            picam.stop_preview()
            picam.close()
