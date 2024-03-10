#!/usr/bin/python3

from picamera2 import Picamera2
import RPi.GPIO as GPIO
import time


class service_CAM:
    def __init__(self):
        pass

    def start(self):
        pass

    def stop(self):
        pass


class service_PWM:
    def __init__(self, PIN, FREQUENCY_PWM,DUTY_CYCLE):
        self.PIN = PIN
        self.FREQUENCY_PWM = FREQUENCY_PWM
        self.DUTY_CYCLE = DUTY_CYCLE

        # 设置引脚为PWM输出
        GPIO.setup([self.PIN], GPIO.OUT)

        self.action_pwm = GPIO.PWM(self.PIN, self.FREQUENCY_PWM)

    def start(self):
        self.action_pwm.start(self.DUTY_CYCLE)

    def stop(self):
        self.action_pwm.stop()
        GPIO.output(self.PIN, GPIO.LOW)
        


if __name__ == "__main__":
    # 清理GPIO设置
    # GPIO.cleanup()
    # 设置GPIO模式
    GPIO.setmode(GPIO.BCM)
    GPIO.setwarnings(False)


    # GPIO18: PWM wave
    PIN_PWM = 18
    # 设置PWM频率（Hz）
    FREQUENCY_PWM = 50.0
    # 占空比（0.0到100.0）
    DUTY_CYCLE = 50


    my_service_PWM = service_PWM(PIN=PIN_PWM, FREQUENCY_PWM=FREQUENCY_PWM, DUTY_CYCLE=DUTY_CYCLE)

    # 启动PWM，参数是占空比
    my_service_PWM.start()  

    try:
        while True:
            pass

    except KeyboardInterrupt:
        # 当按下 Ctrl+C 时
        my_service_PWM.stop()


