#!/usr/bin/python3

import RPi.GPIO as GPIO
import time


# 设置GPIO模式
GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)

# GPIO18: PWM wave
PIN_PWM = 18

# 设置引脚为PWM输出
GPIO.setup([PIN_PWM], GPIO.OUT)


if __name__ == "__main__":
    # 设置PWM频率（Hz）
    PWM_FREQUENCY = 50.0
    DUTY_CYCLE = 25

    pwm = GPIO.PWM(PIN_PWM, PWM_FREQUENCY)

    # 启动PWM，参数是占空比（0.0到100.0）
    pwm.start(DUTY_CYCLE)  # 50% 占空比

    try:
        while True:
            GPIO.output(18, GPIO.LOW)
            pass

    except KeyboardInterrupt:
        # 当按下 Ctrl+C 时，清理GPIO设置
        pwm.stop()
        GPIO.cleanup()

