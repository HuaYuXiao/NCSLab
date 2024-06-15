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
from PIL import Image
import io

app = Flask(__name__)


@app.route('/')
def index():
    return render_template('index.html')


if __name__ == '__main__':
        # Use the SocketIO run method to handle both the web server and SocketIO events
    app.run(host='0.0.0.0', port=8080, debug=True)
