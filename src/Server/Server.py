from flask import Flask, render_template, redirect, url_for
from flask_socketio import SocketIO, emit
import subprocess
from picamera2 import Picamera2
import time
from io import BytesIO
import threading


app = Flask(__name__, template_folder='/home/hyx020222/NCSLab/src/Server/template')
socketio = SocketIO(app)

# ??????????????
stream = BytesIO()


@app.route('/')
def index():
    return render_template('index.html')

@app.route('/start')
def start():
    return redirect(url_for('start_page'))

@app.route('/start_page')
def start_page():
    return render_template('start_page.html')

@app.route('/camera')
def start_camera():
    # ????????
    camera_thread = threading.Thread(target=capture_image)
    camera_thread.start()
    return render_template('camera.html')

# ?????????????
def capture_image():
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

@app.route('/stop')
def stop():
    return redirect(url_for('index'))


if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')
