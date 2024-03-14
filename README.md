# ![Alt Text](https://github.com/HuaYuXiao/NCSLab/blob/main/Img/logo.c5f1638c.png) NCSLab 

![Static Badge](https://img.shields.io/badge/Debian-12-A81D33?logo=debian)
![Static Badge](https://img.shields.io/badge/Python-3.11.5-3776AB?logo=python)
![Static Badge](https://img.shields.io/badge/OpenCV-4.6.0-5C3EE8?logo=opencv)
![Static Badge](https://img.shields.io/badge/Raspberry_Pi-4B-A22846?logo=raspberrypi)


### PWM波控制可变电源

输出电压与PWM波占空比关系如下：

![Alt Text](https://github.com/HuaYuXiao/NCSLab/blob/main/PWM/Vout2PWM.png)

参考：
- [PWM原理 PWM频率与占空比详解](https://blog.csdn.net/as480133937/article/details/103439546)

输出电流稳定在20mA。


### 摄像头捕捉画

参考：
- ⭐️ [How to Install & Setup OpenCV on Raspberry Pi 4](https://how2electronics.com/how-to-install-setup-opencv-on-raspberry-pi-4)
- ⭐️ https://github.com/raspberrypi/picamera2/blob/main/examples/preview.py
- [Installing OpenCV on Raspberry Pi 4B](https://www.youtube.com/watch?v=OugQIz_vcFo)


### 识别电压电流数据

测试图片如下：

![Alt Text](https://github.com/HuaYuXiao/NCSLab/blob/main/Camera/IMG_0774.jpeg)

注意到，底色和内容颜色都是单一的，因此我们首先将两者分离开来。




## 分工（排名不分先后）
- 华羽霄：可变电压源、主控程序、数字识别
- 郝熙哲、汤竑敬：电路设计，PCB打板
- 张艺峰、钟梓轩：系统接入网页

## 谢辞
- 感谢刘国平教授的指导！
- 感谢尹悦琴、叶甥旺、吴科汉、朱晨等师兄师姐的技术支持！
