#!/usr/bin/python3

import RPi.GPIO as GPIO
import time



# GPIO18: PWM wave
PIN_PWM = 18
# 设置PWM频率（Hz）
FREQUENCY_PWM = 50
# 占空比（0.0到100.0）
DUTY_CYCLE = 50



# 清理GPIO设置
GPIO.cleanup()
# 设置GPIO模式
GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)


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

    def set(self, DUTY_CYCLE_new=DUTY_CYCLE, FREQUENCY_PWM_new=FREQUENCY_PWM):
        self.action_pwm.ChangeDutyCycle(DUTY_CYCLE_new)
        self.action_pwm.ChangeFrequency(FREQUENCY_PWM_new)

    def stop(self):
        self.action_pwm.stop()
        GPIO.output(self.PIN, GPIO.LOW)
        

def main():
    my_PWM = service_PWM(PIN=PIN_PWM, FREQUENCY_PWM=FREQUENCY_PWM, DUTY_CYCLE=DUTY_CYCLE)

    # 启动my_PWM
    my_PWM.start()

    try:
        time.sleep(30)
    except KeyboardInterrupt:
        # 当按下 Ctrl+C 时
        my_PWM.stop()



if __name__ == "__main__":
    main()
