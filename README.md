# ![Alt Text](Img/logo.c5f1638c.png) NCSLab 

![Static Badge](https://img.shields.io/badge/Flask-3.0.3-000000?logo=flask)
![Static Badge](https://img.shields.io/badge/OpenCV-4.6.0-5C3EE8?logo=opencv)
![Static Badge](https://img.shields.io/badge/Python-3.8.10-3776AB?logo=python)
![Static Badge](https://img.shields.io/badge/HTML5-_-E34F26?logo=html5)
![Static Badge](https://img.shields.io/badge/Raspberry_Pi-4B-A22846?logo=raspberrypi)
![Static Badge](https://img.shields.io/badge/Debian-12-A81D33?logo=debian)


## Release Note

- v2.1.2:
  - `rectangle-module` and `submit`
- v2.1.1:
  - support video streaming
- v2.1.0: 
  - support continuous channel switch
  - support check for invalid input


## Launch

```bash
python3 src/online/main.py
```

### PWM波控制可变电源

输出电压与PWM波占空比关系如下：

![Alt Text](src/offline/PWM/Vout2PWM.png)

参考：
- [PWM原理 PWM频率与占空比详解](https://blog.csdn.net/as480133937/article/details/103439546)

输出电流稳定在20mA。


## Picamera2

update:2024.5.17
先运行/src/online/Camera_test/server_camera.py 在localhost:8091/video_feed下开启视频流
然后/src/online/main.py 运行主网页

Reference
- ⭐️ https://github.com/barry-ran/raspberry-webcam
- ⭐️ [How to Install & Setup OpenCV on Raspberry Pi 4](https://how2electronics.com/how-to-install-setup-opencv-on-raspberry-pi-4)
- ⭐️ https://github.com/raspberrypi/picamera2/blob/main/examples/preview.py
- [Installing OpenCV on Raspberry Pi 4B](https://www.youtube.com/watch?v=OugQIz_vcFo)


### 识别电压电流数据

测试图片如下：

![Alt Text](Camera/IMG_0774.jpeg)

注意到，底色和内容颜色都是单一的，因此我们首先通过二值化将两者分离开来。

![Alt Text](Camera/output.png)


## COntribution

- 华羽霄：可变电压源、网页设计
- 郝熙哲、汤竑敬：电路设计，PCB打板
- 钟梓轩：视频推流
- 张艺峰：文档撰写
