from utils import *


app = Flask(__name__, template_folder=FOLDER_TEMPLATE)
socketio = SocketIO(app)
# ??????????????
stream = BytesIO()


@app.route('/')
def home():
    return render_template('home.html')

@app.route('/main')
def main():
    return render_template('main.html')

@app.route('/3d-model')
def show_3d_model():
    return send_from_directory('static', FOLDER_3D_MODEL)

@app.route('/plant-information')
def show_plant_information():
    return send_from_directory('static', FOLDER_3D_MODEL)

@app.route('/camera')
def start_camera():
    # ????????
    camera_thread = threading.Thread(target=capture_image)
    camera_thread.start()
    return render_template('camera.html')

@app.route('/video_feed')
def video_feed():
    picam = Picamera2()
    response = Response(stream_with_context(video_generator(picam)), mimetype='multipart/x-mixed-replace; boundary=frame')
    # 设置响应头，告诉浏览器连接可以保持开启状态
    response.headers['Connection'] = 'keep-alive'
    return response

@app.route('/stop')
def stop():
    return redirect(url_for('home'))


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8090, threaded=True, debug=True)
