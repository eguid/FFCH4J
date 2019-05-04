package cc.eguid.commandManager;

import java.util.Collection;
import java.util.Map;

import cc.eguid.commandManager.callback.EventCallback;
import cc.eguid.commandManager.commandbuidler.CommandAssembly;
import cc.eguid.commandManager.commandbuidler.CommandBuidler;
import cc.eguid.commandManager.data.CommandTasker;
import cc.eguid.commandManager.data.TaskDao;
import cc.eguid.commandManager.handler.TaskHandler;

/**
 * FFmpeg命令操作管理器，可执行FFmpeg命令/停止/查询任务信息
 * 
 * @author eguid
 * @since jdk1.7
 * @version 2016年10月29日
 */
public interface CommandManager {
	
	/**
	 * 默认任务池大小
	 */
	public static final int DEFAULT_SIZE=10;
	
	/**
	 * 注入自己实现的持久层
	 * 
	 * @param taskDao
	 */
	void setTaskDao(TaskDao taskDao);

	/**
	 * 注入ffmpeg命令处理器
	 * 
	 * @param taskHandler
	 */
	void setTaskHandler(TaskHandler taskHandler);

	/**
	 * 注入ffmpeg命令组装器
	 * 
	 * @param commandAssembly
	 */
	void setCommandAssembly(CommandAssembly commandAssembly);
	
	/**
	 * 设置事件回调处理器
	 * @param eventCallback
	 */
	void setEventCallback(EventCallback eventCallback);

	/**
	 * 通过命令发布任务（默认命令前不加FFmpeg路径）
	 * 
	 * @param id - 任务标识
	 * @param command - FFmpeg命令
	 * @return
	 */
	String start(String id, String command);
	/**
	 * 通过命令发布任务
	 * @param id - 任务标识
	 * @param commond - FFmpeg命令
	 * @param hasPath - 命令中是否包含FFmpeg执行文件的绝对路径
	 * @return
	 */
	String start(String id,String commond,boolean hasPath);

	/**
	 * 通过流式命令构建器发布任务
	 * @param commandBuidler
	 * @return
	 */
	String start(String id,CommandBuidler commandBuidler);
	
	/**
	 * 通过组装命令发布任务
	 * 
	 * @param assembly
	 *            -组装命令（详细请参照readme文档说明）
	 * @return
	 */
	String start(Map<String,String> assembly);

	/**
	 * 停止任务
	 * 
	 * @param id
	 * @return
	 */
	boolean stop(String id);

	/**
	 * 停止全部任务
	 * 
	 * @return
	 */
	int stopAll();

	/**
	 * 通过id查询任务信息
	 * 
	 * @param id
	 */
	CommandTasker query(String id);

	/**
	 * 查询全部任务信息
	 * 
	 */
	Collection<CommandTasker> queryAll();
	
	/**
	 * 销毁一些后台资源和保活线程
	 */
	void destory();
	
}
