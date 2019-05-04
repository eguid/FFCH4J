 [![LICENSE](https://img.shields.io/badge/license-Anti%20996-blue.svg)](https://github.com/996icu/996.ICU/blob/master/LICENSE)
 [![LICENSE](https://camo.githubusercontent.com/f969af70fa6573766a11cb0a968fc82b069298f1/68747470733a2f2f696d672e736869656c64732e696f2f6769746875622f6c6963656e73652f6c697a68696368616f2f6f6e652e737667)](https://github.com/eguid/FFCH4J/blob/master/LICENSE)
 # FFCH4J（原用名：FFmpegCommandHandler4java） 
 FFCH4J项目全称：FFmpeg命令处理器，鉴于很多小伙伴们反馈原名太长，改为‘FFCH4J’
 ## 说明
 java封装的提供ffmpeg命令执行、停止、查询功能的简单管理器 。
 FFCH4j不仅仅只支持ffmpeg命令，还支持执行多平台的命令行指令，不管是执行linux命令还是windows的命令行都是手到擒来（注意：本项目并未屏蔽某些敏感操作，比如rm -rf，当然这会产生一些风险，还请注意规避）。
 除了保证命令行运行，还拥有独立的轻量级的保活线程来重启因为异常故障导致中断的任务。
 ## 特性
 零依赖（不依赖任何第三方jar包，只需要java运行环境即可运行），完全接口化（所有内部组件都实现了完全接口化，开发人员可以方便的修改和扩展程序，比如自行实现持久层接口来替换默认的持久层）
 ## 版本说明 
 	本次更新说明
	1、配置文件增加保活线程和回调地址
	2、新增命令行流式组装执行器
 	3、新增保活线程处理器，用于在后台保证任务可靠运行，如果任务中途中断，则立即强制重启任务,定制需要实现OutHandlerMethod接口并注入到命令行管理器
 	4、新增事件回调，将下个版本将整合进主程序中
	
 	上个版本更新说明
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
	 //18.12.02新版本创建方式
	CommandManager manager=new CommandManagerImpl(10);

	//老版本创建方式：
	FFmpegManager manager=new FFmpegManagerImpl(10);
	//当然也可以这样
	FFmpegManager manager=new FFmpegManagerImpl();//这样会从配置文件中读取size的值作为初始化参数
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
	
	//流式命令行组装执行
	manager.start("test1", CommandBuidlerFactory.createBuidler()
				.add("ffmpeg").add("-i","rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov")
				.add("-rtsp_transport","tcp")
				.add("-vcodec","copy")
				.add("-acodec","copy")
				.add("-f","flv")
				.add("-y").add("rtmp://eguid.cc/rtmp/test1"));
	manager.stop("test1");//停止
	
	//执行原生ffmpeg命令（不包含ffmpeg的执行路径，该路径会从配置文件中自动读取）
	manager.start("test1", "ffmpeg -i input_file -vcodec copy -an output_file_video");
	//包含完整ffmpeg执行路径的命令
	manager.start("test2,","d:/ffmpeg/ffmpeg -i input_file -vcodec copy -an output_file_video",true);
	//停止全部任务
	manager.stopAll();
	
	//用于销毁保活线程等
	manager.destory();
```
关于FFmpegCommandHandler接口调用/使用方式也可以参考readme文件

 ## 下个版本构想
	1、新增控制台，web控制台和客户端控制台，可以使用web来管理任务信息查看、发布任务、停止任务等
	2、新增事件回调、命令的执行、停止、中断、保活等都将会使用外部接口回调通知或确认操作
	 目前事件回调已经完成，等待下个版本整合进主程序，保活处理器已经完成（已完成，下个版本中提供）
	3、提供web方式的api接口，方便远程调用api来控制
	4、不需要配置文件进行初始化（已完成，下个版本中提供）
	
	
