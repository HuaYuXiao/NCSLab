from utils import *


app = Flask(__name__, template_folder=FOLDER_TEMPLATE)
socketio = SocketIO(app)
# ??????????????
stream = BytesIO()

GPIO.setwarnings(False)
# 清理GPIO设置
GPIO.cleanup()
# 设置GPIO模式
GPIO.setmode(GPIO.BCM)
GPIO.setup(PIN_LIST, GPIO.OUT)
for pin in PIN_LIST:
    GPIO.output(pin, GPIO.LOW)


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

        set_GPIO(PIN_CHANNEL_1, value1)
        set_GPIO(PIN_CHANNEL_2, value2)
        set_GPIO(PIN_CHANNEL_3, value3)

        # Redirect back to the main page or to a different page if needed
        return redirect(url_for('main'))

    return render_template('main.html')

@app.route('/stop')
def stop():
    return redirect(url_for('home'))


if __name__ == '__main__':
        # Use the SocketIO run method to handle both the web server and SocketIO events
    app.run(host='0.0.0.0', port=8090, debug=True)
