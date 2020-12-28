# image-recognition
使用深度神经网络 Google Inception Net-V3 训练给定图片集合生成.pb识别模型，加入到Android端实现手机对拍照照片的识别。

0.需要用到tensorflow，建议使用Anaconda
1.将训练图片用文件夹分类后使用retrain.py进行训练，生成.pb训练模型
2.使用label_image.py测试训练成果
3.将.pb训练模型文件导入Android Project，对拍照所得照片进行判断
