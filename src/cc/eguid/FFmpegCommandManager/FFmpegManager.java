package cc.eguid.FFmpegCommandManager;

import java.util.Collection;
import java.util.Map;

import cc.eguid.FFmpegCommandManager.dao.TaskDao;
import cc.eguid.FFmpegCommandManager.entity.TaskEntity;
import cc.eguid.FFmpegCommandManager.service.CommandAssembly;
import cc.eguid.FFmpegCommandManager.service.TaskHandler;

/**
 * FFmpeg命令操作管理器，可执行FFmpeg命令/停止/查询任务信息
 * 
 * @author eguid
 * @since jdk1.7
 * @version 2016年10月29日
 */
public interface FFmpegManager {
	/**
	 * 注入自己实现的持久层
	 * 
	 * @param taskDao
	 */
	public void setTaskDao(TaskDao taskDao);

	/**
	 * 注入ffmpeg命令处理器
	 * 
	 * @param taskHandler
	 */
	public void setTaskHandler(TaskHandler taskHandler);

	/**
	 * 注入ffmpeg命令组装器
	 * 
	 * @param commandAssembly
	 */
	public void setCommandAssembly(CommandAssembly commandAssembly);

	/**
	 * 通过命令发布任务
	 * 
	 * @param id
	 *            -唯一标识符
	 * @param command
	 *            - ffmpeg命令
	 * @return
	 */
	public String start(String id, String command);

	/**
	 * 通过组装命令发布任务
	 * 
	 * @param assembly
	 *            -组装命令（详细请参照readme文档说明）
	 * @return
	 */
	public String start(Map assembly);

	/**
	 * 停止任务
	 * 
	 * @param id
	 * @return
	 */
	public boolean stop(String id);

	/**
	 * 停止全部任务
	 * 
	 * @return
	 */
	public int stopAll();

	/**
	 * 通过id查询任务信息
	 * 
	 * @param id
	 */
	public TaskEntity query(String id);

	/**
	 * 查询全部任务信息
	 * 
	 */
	public Collection<TaskEntity> queryAll();
}
