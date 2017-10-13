# FFmpegCommandHandler4java（FFmpeg命令执行管理器） 
 ## 说明
 java封装的提供ffmpeg命令执行、停止、查询功能的简单管理器 
 ## 版本说明 
 1、本次更新主要针对配置文件的加载优化，详情见下面的使用说明
 2、修改配置文件加载方式
 3、增加一个String start(String id,String commond,boolean hasPath)接口，用于区分是否使用配置文件中的绝对路径，如果为false，请务必保证ffmpeg的路径可以正确加载
 4、增加一个debug配置，用于判断是否输出关键位置的debug消息
 ## 基于
 本项目基于jdk1.7开发，FFmpeg各版本支持的命令请参考FFmpeg官方文档
 ## 使用说明 
 初始化FFmpegManager时会自动查找loadFFmpeg.properties配置文件
配置文件的加载方式如下：
1、javaSE项目会自动从项目根目录加载
2、javaEE项目会自动从classes目录下加载（编写web项目的src目录下）
3、如果上述位置都没有找到配置文件，会自动加载默认配置，默认的配置文件在config包下的defaultFFmpegConfig.properties中

 FFmpegCommandHandler接口调用/使用方式请查看readme文件
 
 ## 下个版本构想
	1、提供一个新的命令组装器，支持所有ffmpeg命令的二次组装
	2、提供一个可视化的管理/监控界面