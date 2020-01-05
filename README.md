## 简易的网盘

### 客户端的功能

#### 注册\&登录

##### 注册规则：

·用户名未被占用

·两次输入密码一致

·密码为6位以上

·密码同时包含数字和字母



##### 登录界面：

·可保存用户登录记录

·可记住密码

#### 基础操作

查看空间使用(每个用户默认10G空间)

·显示当前路径

·关键字搜索文件

·新建/删除/重命名

·刷新当前界面/返回上一界面

 

#### 文件(夹)上传下载

可选择对文件或文件夹操作

·可多个操作同时进行

·显示操作进度及传输速度

·控制同时开始/暂停

·支持拖拽上传

#### **网盘空间分享**

选中个人网盘中的某个文件夹，

选定分享时限（临时/永久）及读写权限，

将生成一个分享密钥

其他用户使用密钥可对该文件夹进行操作

#### **网盘与本地绑定**

可将网盘指定文件夹与本地指定地址绑定

实现内容同步

### 服务端的功能

#### **建立服务器**

保存使用过的端口

选择文件的存放地点

#### **基础操作**

显示用户空间使用量

显示用户空间大小

删除用户

更改用户密码

记录用户的操作

生成并保存log文件

### 网络传输

使用java rmi实现

#### Net 接口

负责一些常规操作

![image.png](https://i.loli.net/2020/01/05/zyRVj1wmCTqZA2b.png)

#### NetReader \&  NetWriter

负责文件的传输

![image.png](https://i.loli.net/2020/01/05/aNGtJ4ZKBFdwkuP.png)



### 文件结构

文件结构被存成树形

服务端不保存文件夹

只保存文件

文件都会被分配一个唯一的id

![image.png](https://i.loli.net/2020/01/05/8gIMjweDzdNsfPC.png)

服务端的文件和文件夹被Disk UserDisk RealDisk 三个类管理

用文件大小和最后一次修改时间

作为hash值

保存hash值和目前的大小

用来实现断点续传

### 分享链接的结构

权限信息 + 分享者的信息 + 文件的id + 文件的parent的id

### 线程安全

syn

Vector代替ArrayList

ConcurrentHashMap代替HashMap

### GUI

通过 javafx + FXML实现

### 类的简单介绍

#### InfoFile

断点续传中记录信息

#### MyDate

获得时间戳，转换时间格式

#### MyDialog

预设的各种对话框

#### Password

判断密码是否合法

#### SimpleLogListProperty

保存传输信息的java bean

#### SimpleMyFileProperty

记录文件信息的java bean

#### UserData

处理记录在本地的端口，用户名，密码

#### YRL\&YRLList

保存传输信息，方便重连的断点续传

#### FileExistedException

用户已存在异常

#### FileStructureException

文件结构异常

#### NoAccessException

无权限异常

#### NoFileException

没有文件异常

#### NoSuchUserException

没有该用户异常

#### NoUserException

没有该用户异常

#### PasswordException

密码错误异常

#### PasswordTooFewException

密码太短异常

#### PasswordTooWeakException

密码太弱异常

#### UserExistedException

用户已存在异常

#### Client

用来与服务器通过Net接口通信



#### DownLoader

继承TransLoader 负责下载



#### Sync

 负责同步



#### SyncDownloader

继承Sync，负责同步下载



#### SyncUpLoader

继承Sync，负责同步上传



#### TransLoader

负责文件传输



#### Uploader

继承TranLoader，负责上传

Base64Converter：base64加密和解密



#### DataBase

存放本地数据



#### SimpleUserProperty

User的java bean



#### TransData & TransDataList

文件传输的记录



#### User

用户类，保存用户的信息



#### UserManager

查询修改User的密码



#### DownLoader处理文件下载



#### Server实现Net接口



#### ServerReader

实现NetReader接口



#### ServerWriter

实现NetWriter接口



#### Uploader

处理文件上传