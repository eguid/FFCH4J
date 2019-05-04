package cc.eguid.commandManager;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import cc.eguid.commandManager.callback.EventCallback;
import cc.eguid.commandManager.commandbuidler.CommandAssembly;
import cc.eguid.commandManager.commandbuidler.CommandBuidler;
import cc.eguid.commandManager.config.LocalGlobalConfig;
import cc.eguid.commandManager.config.ProgramConfig;
import cc.eguid.commandManager.data.CommandTasker;
import cc.eguid.commandManager.data.TaskDao;
import cc.eguid.commandManager.handler.OutHandlerMethod;
import cc.eguid.commandManager.handler.TaskHandler;

/**
 * FFmpeg命令操作管理器
 * 
 * @author eguid
 * @since jdk1.7
 * @version 2017年10月13日
 */
public class CommandManagerImpl extends CommandManagerTemplate implements CommandManager {

	@Override
	public String start(String id, String command) {
		return start(id, command, false);
	}

	@Override
	public String start(String id, String command, boolean hasPath) {
		if (isInit(true)) {
			LocalGlobalConfig.debugInfo("执行失败，未进行初始化或初始化失败！");
			return null;
		}
		
		if (id != null && command != null) {
			CommandTasker tasker = taskHandler.process(id, hasPath ? command : globalConfig.getPath() + command);
			if (tasker != null) {
				int ret = taskDao.add(tasker);
				if (ret > 0) {
					return tasker.getId();
				} else {
					// 持久化信息失败，停止处理
					taskHandler.stop(tasker.getProcess(), tasker.getThread());
					LocalGlobalConfig.debugInfo("持久化失败，停止任务！");
				}
			}
		}
		return null;
	}

	@Override
	public String start(Map<String, String> assembly) {
		// ffmpeg环境是否配置正确
		if (checkConfig()) {
			LocalGlobalConfig.debugInfo("配置未正确加载，无法执行");
			return null;
		}
		// 参数是否符合要求
		if (assembly == null || assembly.isEmpty() || !assembly.containsKey("appName")) {
			LocalGlobalConfig.debugInfo("参数不正确，无法执行");
			return null;
		}
		String appName = (String) assembly.get("appName");
		if (appName != null && "".equals(appName.trim())) {
			LocalGlobalConfig.debugInfo("appName不能为空");
			return null;
		}
		assembly.put("ffmpegPath", globalConfig.getPath() + "ffmpeg");
		String command = commandAssembly.assembly(assembly);
		if (command != null) {
			return start(appName, command, true);
		}

		return null;
	}

	@Override
	public String start(String id,CommandBuidler commandBuidler) {
		// ffmpeg环境是否配置正确
		if (checkConfig()) {
			LocalGlobalConfig.debugInfo("配置未正确加载，无法执行");
			return null;
		}
		String command =commandBuidler.get();
		if (command != null) {
			return start(id, command, true);
		}
		return null;
	}

	private boolean checkConfig() {
		return globalConfig == null;
	}
	
	@Override
	public boolean stop(String id) {
		if (id != null && taskDao.isHave(id)) {
			LocalGlobalConfig.debugInfo("正在停止任务：" + id);
			CommandTasker tasker = taskDao.get(id);
			if (taskHandler.stop(tasker.getProcess(), tasker.getThread())) {
				taskDao.remove(id);
				return true;
			}
		}
		LocalGlobalConfig.debugInfo("停止任务失败！id=" + id);
		return false;
	}

	@Override
	public int stopAll() {
		Collection<CommandTasker> list = taskDao.getAll();
		Iterator<CommandTasker> iter = list.iterator();
		CommandTasker tasker = null;
		int index = 0;
		while (iter.hasNext()) {
			tasker = iter.next();
			if (taskHandler.stop(tasker.getProcess(), tasker.getThread())) {
				taskDao.remove(tasker.getId());
				index++;
			}
		}
		LocalGlobalConfig.debugInfo("停止了" + index + "个任务！");
		return index;
	}

	@Override
	public CommandTasker query(String id) {
		return taskDao.get(id);
	}

	@Override
	public Collection<CommandTasker> queryAll() {
		return taskDao.getAll();
	}

	@Override
	public void destory() {
		if(keepAliveHandler!=null) {
			//安全停止保活线程
			keepAliveHandler.interrupt();
		}
	}
	
	public CommandManagerImpl() {
		super();
	}
	public CommandManagerImpl(int size) {
		super(size);
	}
	public CommandManagerImpl(ProgramConfig config) {
		super(config);
	}
	public CommandManagerImpl(ProgramConfig config, int size) {
		super(config, size);
	}
	public CommandManagerImpl(TaskDao taskDao, TaskHandler taskHandler, CommandAssembly commandAssembly,
			OutHandlerMethod ohm, EventCallback eventCallback, Integer size) {
		super(taskDao, taskHandler, commandAssembly, ohm, eventCallback, size);
	}
	public CommandManagerImpl(TaskDao taskDao, TaskHandler taskHandler, CommandAssembly commandAssembly,
			OutHandlerMethod ohm, EventCallback eventCallback, ProgramConfig config, Integer size) {
		super(taskDao, taskHandler, commandAssembly, ohm, eventCallback, config, size);
	}
	public CommandManagerImpl(TaskDao taskDao, TaskHandler taskHandler, CommandAssembly commandAssembly,
			OutHandlerMethod ohm, Integer size) {
		super(taskDao, taskHandler, commandAssembly, ohm, size);
	}

}
