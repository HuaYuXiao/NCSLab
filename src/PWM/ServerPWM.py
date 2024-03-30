from flask import Flask, render_template, request
import RPi.GPIO as GPIO
import time

# GPIO18: PWM
PIN_PWM = 18
# PWM??(Hz)
FREQUENCY_PWM = 50
# ???(0.0?100.0)
DUTY_CYCLE = 50

GPIO.cleanup()
GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)

class service_PWM:
    def __init__(self, PIN, FREQUENCY_PWM, DUTY_CYCLE):
        self.PIN = PIN
        self.FREQUENCY_PWM = FREQUENCY_PWM
        self.DUTY_CYCLE = DUTY_CYCLE

        GPIO.setup(self.PIN, GPIO.OUT)

        self.action_pwm = GPIO.PWM(self.PIN, self.FREQUENCY_PWM)

    def start(self):
        self.action_pwm.start(self.DUTY_CYCLE)

    def set(self, DUTY_CYCLE_new=DUTY_CYCLE, FREQUENCY_PWM_new=FREQUENCY_PWM):
        self.action_pwm.ChangeDutyCycle(DUTY_CYCLE_new)
        self.action_pwm.ChangeFrequency(FREQUENCY_PWM_new)

    def stop(self):
        self.action_pwm.stop()
        GPIO.output(self.PIN, GPIO.LOW)

app = Flask(__name__, template_folder='/home/hyx020222/NCSLab/src/PWM')
my_PWM = service_PWM(PIN=PIN_PWM, FREQUENCY_PWM=FREQUENCY_PWM, DUTY_CYCLE=DUTY_CYCLE)
my_PWM.start()

@app.route("/")
def index():
    return render_template("index.html", frequency=FREQUENCY_PWM, duty_cycle=DUTY_CYCLE)

@app.route("/update", methods=["POST"])
def update():
    global FREQUENCY_PWM, DUTY_CYCLE
    frequency = int(request.form["frequency"])
    duty_cycle = int(request.form["duty_cycle"])
    my_PWM.set(FREQUENCY_PWM_new=frequency, DUTY_CYCLE_new=duty_cycle)
    FREQUENCY_PWM = frequency
    DUTY_CYCLE = duty_cycle
    return "PWM settings updated successfully!"

def main():
    app.run(host="0.0.0.0", debug=True, port=8080)


if __name__ == "__main__":
    main()
