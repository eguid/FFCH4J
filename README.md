# FFmpegCommandHandler4java（FFmpeg命令执行管理器） 
 ## 说明
 java封装的提供ffmpeg命令执行、停止、查询功能的简单管理器 
 ## 版本说明 
 1、修改配置文件加载方式
 2、增加一个String start(String id,String commond,boolean hasPath)接口，用于区分是否使用配置文件中的FFmpeg执行路径
 3、增加一个debug配置，用于判断是否输出关键位置的debug消息
 ## 基于
 本项目基于jdk1.7开发，FFmpeg各版本支持的命令请参考FFmpeg官方文档
 ## 使用说明 
 初始化FFmpegManager时会自动加载项目根目录下的loadFFmpeg.properties配置文件
 更多使用实例请查看readme文件
