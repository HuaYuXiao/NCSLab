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
socketio = SocketIO(app)
# ??????????????
stream = BytesIO()


@app.route('/')
def home():
    return render_template('home.html')

@app.route('/main', methods=['GET', 'POST'])
def main():
    if request.method == 'POST':
        # Get the values from the input boxes
        value1 = request.form.get('input1')
        value2 = request.form.get('input2')
        value3 = request.form.get('input3')

        print(value1, value2, value3)

        # Redirect back to the main page or to a different page if needed
        return redirect(url_for('main'))

    return render_template('main.html')

@app.route('/stop')
def stop():
    return redirect(url_for('home'))


if __name__ == '__main__':
        # Use the SocketIO run method to handle both the web server and SocketIO events
    app.run(host='0.0.0.0', port=8090, debug=True)
