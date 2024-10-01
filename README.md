## 前言

**NeteaseCloudMusicDownloader**是基于 [网易云音乐API](https://gitlab.com/Binaryify/neteasecloudmusicapi) 的音乐下载工具

该工具只是一个简单的程序。

## 如何使用

1. 确保你的设备拥有运行环境：Windows操作系统，JDK或JRE。
2. 打开工具，软件启动后会在当前目录生成`login-info.txt`文件，该文件用于保存你的网易云账号、密码和Cookie（你的信息仅保存在本地且仅在调用接口时使用，不会被服务器保存），请不要随意将你的`login-info.txt`分享给别人。
3. 在登录界面输入账号和密码。“记住”选项会将你的信息保存至`login-info.txt`，下次启动程序时，会先进行登录状态检测，若仍在登录状态，则程序将直接进入主界面，无需再次登录。
4. 搜索歌曲。在搜索框输入歌曲名关键字，点击搜索，将会返回结果列表。
5. 下载。在结果列表中选中你想下载的曲目进行下载。

## 说明

在Release里下载的程序已经包含了API地址，可以直接运行和使用。
如果你想使用源代码自行编译，你需要自行搭建[NeteaseCloudMusicApi](https://gitlab.com/Binaryify/neteasecloudmusicapi)，并将`/src/main/java/ApiClient.java`中的`BASE_URL`设定为你自己的API地址（url末尾一定要有“/”，比如 http://api.abc.com/ （支持 数字:端口）

## 已知问题

- [ ] 返回的歌曲列表无法正常显示部分语言文字，可能会显示为空白或乱码。

