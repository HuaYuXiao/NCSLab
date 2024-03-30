from flask import Flask, render_template, redirect, url_for
from flask_socketio import SocketIO, emit
import subprocess


app = Flask(__name__, template_folder='/home/hyx020222/NCSLab/src/Server/template')


@app.route('/')
def index():
    return render_template('index.html')

@app.route('/start')
def start():
    subprocess.Popen(['python3', 'src/hello.py'])
    return redirect(url_for('start_page'))

@app.route('/start_page')
def start_page():
    return render_template('start_page.html')

@app.route('/camera')
def start_camera():
    # ??????????????????
    # ????? Flask-SocketIO ?????????????
    return redirect(url_for('start_page'))

@app.route('/stop')
def stop():
    subprocess.Popen(['pkill', '-f', 'src/hello.py'])
    return redirect(url_for('index'))

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')
