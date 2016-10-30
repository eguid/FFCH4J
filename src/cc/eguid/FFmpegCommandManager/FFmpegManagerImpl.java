package cc.eguid.FFmpegCommandManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cc.eguid.FFmpegCommandManager.dao.TaskDao;
import cc.eguid.FFmpegCommandManager.dao.TaskDaoImpl;
import cc.eguid.FFmpegCommandManager.entity.TaskEntity;
import cc.eguid.FFmpegCommandManager.service.CommandAssembly;
import cc.eguid.FFmpegCommandManager.service.CommandAssemblyUtil;
import cc.eguid.FFmpegCommandManager.service.TaskHandler;
import cc.eguid.FFmpegCommandManager.service.TaskHandlerImpl;
import cc.eguid.FFmpegCommandManager.util.LoadConfig;

/**
 * FFmpeg命令操作管理器
 * 
 * @author eguid
 * @since jdk1.7
 * @version 2016年10月29日
 */
public class FFmpegManagerImpl implements FFmpegManager {
	private TaskDao taskDao = null;
	private TaskHandler taskHandler = null;
	private CommandAssembly commandAssembly = null;
	private LoadConfig loadConf = null;

	public FFmpegManagerImpl(boolean init) {
		if (loadConf == null) {
			loadConf = new LoadConfig();
		}
		if (init) {
			init(10);
		}
	}

	public FFmpegManagerImpl(int size) {
		if (loadConf == null) {
			loadConf = new LoadConfig();
		}
		init(size);
	}

	/**
	 * 初始化
	 * 
	 * @param size
	 */
	public void init(int size) {
		this.taskDao = new TaskDaoImpl(size);
		this.taskHandler = new TaskHandlerImpl();
		this.commandAssembly = new CommandAssemblyUtil();
	}

	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}

	public void setTaskHandler(TaskHandler taskHandler) {
		this.taskHandler = taskHandler;
	}

	public void setCommandAssembly(CommandAssembly commandAssembly) {
		this.commandAssembly = commandAssembly;
	}

	@Override
	public String start(String id, String command) {
		if (id != null && command != null) {
			TaskEntity tasker = taskHandler.process(id, command);
			if (tasker != null) {
				int ret = taskDao.add(tasker);
				if (ret > 0) {
					return tasker.getId();
				} else {
					// 持久化信息失败，停止处理
					taskHandler.stop(tasker.getProcess(), tasker.getThread());
					System.err.println("持久化失败，停止任务！");
				}
			}
		}
		return null;
	}

	@Override
	public String start(Map assembly) {
		// ffmpeg环境是否配置正确
		if (!loadConf.isHave()) {
			System.err.println("ffmpeg环境未配置，无法执行");
			return null;
		}
		// 参数是否符合要求
		if (assembly == null || assembly.isEmpty() || !assembly.containsKey("appName")) {
			System.err.println("参数不正确，无法执行");
			return null;
		}
		String appName = (String) assembly.get("appName");
		if (appName != null && "".equals(appName.trim())) {
			System.err.println("appName不能为空");
			return null;
		}
		assembly.put("ffmpegPath", loadConf.getPath());
		String command = commandAssembly.assembly(assembly);
		if (command != null) {
			return start(appName, command);
		}

		return null;
	}

	@Override
	public boolean stop(String id) {
		if (id != null && taskDao.isHave(id)) {
			System.out.println("正在停止任务：" + id);
			TaskEntity tasker = taskDao.get(id);
			if (taskHandler.stop(tasker.getProcess(), tasker.getThread())) {
				taskDao.remove(id);
				return true;
			}
		}
		System.err.println("停止任务失败！");
		return false;
	}

	@Override
	public int stopAll() {
		Collection<TaskEntity> list = taskDao.getAll();
		Iterator<TaskEntity> iter = list.iterator();
		TaskEntity tasker = null;
		int index = 0;
		while (iter.hasNext()) {
			tasker = iter.next();
			if (taskHandler.stop(tasker.getProcess(), tasker.getThread())) {
				taskDao.remove(tasker.getId());
				index++;
			}
		}
		System.out.println("停止了" + index + "个任务！");
		return index;
	}

	@Override
	public TaskEntity query(String id) {
		return taskDao.get(id);
	}

	@Override
	public Collection<TaskEntity> queryAll() {
		return taskDao.getAll();
	}

	public static void main(String[] args) {
		FFmpegManager manager = new FFmpegManagerImpl(10);
		Map map = new HashMap();
		map.put("appName", "test123");
		map.put("input", "rtsp://admin:admin@192.168.2.236:37779/cam/realmonitor?channel=1&subtype=0");
		map.put("output", "rtmp://192.168.30.21/live/");
		map.put("codec", "h264");
		map.put("fmt", "flv");
		map.put("fps", "25");
		map.put("rs", "640x360");
		map.put("twoPart", "2");
		// 执行任务，id就是appName，如果执行失败返回为null
		String id = manager.start(map);
		System.out.println(id);
		// 通过id查询
		TaskEntity info = manager.query(id);
		System.out.println(info);
		// 查询全部
		Collection<TaskEntity> infoList = manager.queryAll();
		System.out.println(infoList);
		// 停止id对应的任务
		manager.stop(id);

		manager.start("test1", "这里放原生的ffmpeg命令");
		// 停止全部任务
		manager.stopAll();
	}
}
