# FFCH4J（原名FFmpegComandHandler4java） 
 FFCH4J项目全称：FFmpeg命令行运行管理器，鉴于很多小伙伴们反馈原名太长，现项目改为缩写‘FFCH4J’
 ## 说明
 java封装的提供ffmpeg命令执行、停止、查询功能的简单管理器 。
 FFCH4j不仅仅只支持ffmpeg命令，还支持执行多平台的命令行指令，不管是执行linux命令还是windows的命令行都是手到擒来（注意：本项目并未屏蔽某些敏感操作，比如rm -rf，当然这会产生一些风险，还请注意规避）。
 ## 版本说明 
 	1、本次更新主要针对配置文件的加载优化，详情见下面的使用说明
	初始化FFmpegManager时会自动查找loadFFmpeg.properties配置文件
	 配置文件的加载方式如下：
	(1)、javaSE项目会自动从项目根目录加载
	(2)、javaEE项目会自动从classes目录下加载（编写web项目的src目录下）
	(3)、如果上述位置都没有找到配置文件，会自动加载默认配置，默认的配置文件在config包下的defaultFFmpegConfig.properties中

 	2、支持自定义的消息输出
	
	上个版本更新说明
 	3、增加一个String start(String id,String commond,boolean hasPath)接口，用于区分是否使用配置文件中的绝对路径，如果为false，请务必保证ffmpeg的路径可以正确加载
 	4、增加一个debug配置，用于判断是否输出关键位置的debug消息
 ## 基于
 本项目基于jdk1.7开发，FFmpeg各版本支持的命令请参考[FFmpeg官方文档](http://ffmpeg.org/ffmpeg.html)<br />
 ## 使用说明 
```Java 
	 FFmpegManager manager=new FFmpegManagerImpl(10);
	//当然也可以这样：FFmpegManager manager=new FFmpegManagerImpl();//这样会从配置文件中读取size的值作为初始化参数
	//组装命令
	Map map = new HashMap();
	map.put("appName", "test123");
	map.put("input","rtsp://admin:admin@192.168.2.236:37779/cam/realmonitor?channel=1&subtype=0");
	map.put("output", "rtmp://192.168.30.21/live/");
	map.put("codec","h264");
	map.put("fmt", "flv");
	map.put("fps", "25");
	map.put("rs", "640x360");
	map.put("twoPart","2");
	//执行任务，id就是appName，如果执行失败返回为null
	String id=manager.start(map);
	System.out.println(id);
	//通过id查询
	TaskEntity info=manager.query(id);
	System.out.println(info);
	//查询全部
	Collection<TaskEntity> infoList=manager.queryAll();
	System.out.println(infoList);

	//停止id对应的任务
	manager.stop(id);
	//执行原生ffmpeg命令（不包含ffmpeg的执行路径，该路径会从配置文件中自动读取）
	manager.start("test1", "ffmpeg -i input_file -vcodec copy -an output_file_video");
	//包含完整ffmpeg执行路径的命令
	manager.start("test2,","d:/ffmpeg/ffmpeg -i input_file -vcodec copy -an output_file_video",true);
	//停止全部任务
	manager.stopAll();
```
关于FFmpegCommandHandler接口调用/使用方式也可以参考readme文件

 ## 下个版本构想（准备一个大版本更新，API会努力做到与老版本兼容，本次更新会增加以下特性）
	1、一个新的流式命令组装器(stream command builder)，可以自行选择使用老的组装器(map command builder)还是新的流组装器（或者自定义的组装器实现）
	
	2、命令执行器更新，新增瞬时命令、持久命令两种命令行执行器，持久命令将包含一个保活线程和外部接口回调用于维持持久命令的有效运行
	3、新增事件回调、命令的执行、停止、中断、保活等都将会使用外部接口回调通知或确认操作
