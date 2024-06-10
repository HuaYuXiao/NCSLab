import cv2
import datetime
import os

def take_photo(save_path='photo.jpg'):
    # 确保保存图片的文件夹存在
    os.makedirs(os.path.dirname(save_path), exist_ok=True)
    
    # 初始化摄像头
    cap = cv2.VideoCapture(0)  # 0 通常是默认的摄像头
    if not cap.isOpened():
        print("Cannot open camera")
        exit()
    
    # 捕获一帧图像
    ret, frame = cap.read()
    
    # 如果正确捕获到一帧，ret为True
    if not ret:
        print("Can't receive frame (stream end?). Exiting ...")
        return
    
    # 保存照片
    cv2.imwrite(save_path, frame)
    print(f"Photo saved as {save_path}")
    
    # 释放摄像头
    cap.release()
    cv2.destroyAllWindows()

if __name__ == '__main__':
    # 创建pictures文件夹的路径
    base_dir = os.path.join(os.getcwd(), 'pictures')
    # 使用当前时间作为文件名，确保文件名唯一
    filename = datetime.datetime.now().strftime("%Y-%m-%d_%H-%M-%S.jpg")
    # 完整的保存路径
    save_path = os.path.join(base_dir, filename)
    
    take_photo(save_path=save_path)
