# 使用手册：

## 创建命令行管理器

```
//18.12.02新版本创建方式
CommandManager manager=new CommandManagerImpl(10);

//老版本创建方式：
FFmpegManager manager=new FFmpegManagerImpl(10);
//当然也可以这样
FFmpegManager manager=new FFmpegManagerImpl();//这样会从配置文件中读取size的值作为初始化参数
```
## 组装命令
```
Map map = new HashMap();
map.put("appName", "test123");
map.put("input","rtsp://admin:admin@192.168.2.236:37779/cam/realmonitor?channel=1&subtype=0");
map.put("output", "rtmp://192.168.30.21/live/");
map.put("codec","h264");
map.put("fmt", "flv");
map.put("fps", "25");
map.put("rs", "640x360");
map.put("twoPart","2");
```

## 执行任务，id就是appName，如果执行失败返回为null
```
String id=manager.start(map);
System.out.println(id);
```
## 通过id查询
```
TaskEntity info=manager.query(id);
System.out.println(info);
```
## 查询全部任务
```
Collection<TaskEntity> infoList=manager.queryAll();
System.out.println(infoList);
```

## 停止id对应的任务
```
manager.stop(id);
```
## 执行原生ffmpeg命令（不包含ffmpeg的执行路径，该路径会从配置文件中自动读取）
```
manager.start("test1", "ffmpeg -i input_file -vcodec copy -an output_file_video");
//包含完整ffmpeg执行路径的命令
manager.start("test2,","d:/ffmpeg/ffmpeg -i input_file -vcodec copy -an output_file_video",true);
```
## 停止全部任务
```
manager.stopAll();
```
