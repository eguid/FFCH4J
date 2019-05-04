/**
 * 
 */
package cc.eguid.commandManager;

import static cc.eguid.commandManager.util.PropertiesUtil.load;

import cc.eguid.commandManager.callback.EventCallback;
import cc.eguid.commandManager.callback.EventCallbackType;
import cc.eguid.commandManager.commandbuidler.CommandAssembly;
import cc.eguid.commandManager.commandbuidler.CommandAssemblyImpl;
import cc.eguid.commandManager.config.LocalGlobalConfig;
import cc.eguid.commandManager.config.ProgramConfig;
import cc.eguid.commandManager.data.CommandTasker;
import cc.eguid.commandManager.data.TaskDao;
import cc.eguid.commandManager.data.TaskDaoImpl;
import cc.eguid.commandManager.handler.DefaultOutHandlerMethod;
import cc.eguid.commandManager.handler.KeepAliveHandler;
import cc.eguid.commandManager.handler.OutHandlerMethod;
import cc.eguid.commandManager.handler.TaskHandler;
import cc.eguid.commandManager.handler.TaskHandlerImpl;

/**
 * @author eguid
 *
 */
public abstract class CommandManagerTemplate implements CommandManager {

	/**
	 * 程序全局配置
	 */
	protected ProgramConfig globalConfig=null;
	
	/**
	 * 任务持久化器
	 */
	protected  TaskDao taskDao = null;
	/**
	 * 任务执行处理器
	 */
	protected TaskHandler taskHandler = null;
	/**
	 * 命令组装器
	 */
	protected CommandAssembly commandAssembly = null;
	/**
	 * 任务消息处理器
	 */
	protected OutHandlerMethod ohm = null;
	
	/**
	 * 保活处理器
	 */
	protected  KeepAliveHandler keepAliveHandler=null;
	
	/**
	 * 事件回调处理器
	 */
	protected EventCallback eventCallback=null;
	
	public KeepAliveHandler getKeepAliveHandler() {
		return keepAliveHandler;
	}

	public void setKeepAliveHandler(KeepAliveHandler keepAliveHandler) {
		this.keepAliveHandler = keepAliveHandler;
	}
	
	public EventCallback getEventCallback() {
		return eventCallback;
	}

	public void setEventCallback(EventCallback eventCallback) {
		this.eventCallback = eventCallback;
	}
	
	public TaskDao getTaskDao() {
		return taskDao;
	}

	public TaskHandler getTaskHandler() {
		return taskHandler;
	}

	public CommandAssembly getCommandAssembly() {
		return commandAssembly;
	}

	public OutHandlerMethod getOhm() {
		return ohm;
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

	public void setOhm(OutHandlerMethod ohm) {
		this.ohm = ohm;
	}

	/**
	 * 全部默认初始化，自动查找配置文件
	 */
	public CommandManagerTemplate() {
		this(DEFAULT_SIZE);
	}
	
	/**
	 * 指定任务池大小的初始化，其他使用默认
	 * @param size -任务池大小
	 */
	public CommandManagerTemplate(int size) {
		this(null,size);
	}

	/**
	 * 指定全局配置进行初始化程序（不使用配置文件加载配置）
	 * @param config -全局配置
	 */
	public CommandManagerTemplate(ProgramConfig config) {
		this(config,DEFAULT_SIZE);
	}
	
	/**
	 * 使用全局配置初始化程序
	 * @param cofnig-全局配置
	 * @param size-任务池大小
	 */
	public CommandManagerTemplate(ProgramConfig config,int size) {
		this(null,null,null,null,null,config,size);
	}
	
	/**
	 * 初始化
	 * @param taskDao
	 * @param taskHandler
	 * @param commandAssembly
	 * @param ohm
	 * @param size
	 */
	public CommandManagerTemplate(TaskDao taskDao, TaskHandler taskHandler, CommandAssembly commandAssembly, OutHandlerMethod ohm,Integer size) {
		this(taskDao,taskHandler,commandAssembly,ohm,null,null,size);
	}
	
	/**
	 * 初始化
	 * @param taskDao
	 * @param taskHandler
	 * @param commandAssembly
	 * @param ohm
	 * @param eventCallback -事件回调处理器
	 * @param size
	 */
	public CommandManagerTemplate(TaskDao taskDao, TaskHandler taskHandler, CommandAssembly commandAssembly, OutHandlerMethod ohm,EventCallback eventCallback,Integer size) {
		this(taskDao,taskHandler,commandAssembly,ohm,eventCallback,null,size);
	}
	
	/**
	 * 初始化
	 * @param taskDao
	 * @param taskHandler
	 * @param commandAssembly
	 * @param ohm
	 * @param eventCallback -事件回调处理器
	 * @param config -全局配置
	 * @param size -任务池大小
	 */
	public CommandManagerTemplate(TaskDao taskDao, TaskHandler taskHandler, CommandAssembly commandAssembly, OutHandlerMethod ohm,EventCallback eventCallback,ProgramConfig config,Integer size) {
		this.taskDao = taskDao;
		this.taskHandler = taskHandler;
		this.commandAssembly = commandAssembly;
		this.ohm = ohm;
		this.eventCallback=eventCallback;
		//初始化
		init(config,size);
		
	}

	/**
	 * 初始化，如果几个处理器未注入，则使用默认处理器
	 * 
	 * @param size
	 */
	public void init(ProgramConfig config, Integer size) {
		//如果没有手动配置配置文件，则加载配置文件
		if (config == null) {
			System.err.println("没有设置全局配置，准备查找并加载配置文件");
			config=load("loadFFmpeg.properties", ProgramConfig.class);
		}
		//如果还是为空
		if (config == null) {
			System.err.println("没有设置全局配置或者未找到配置文件，程序加载失败！");
			return;
		}
		globalConfig=config;
		
		LocalGlobalConfig.LOCAL_GLOBAL_CONFIG=globalConfig;
		
		LocalGlobalConfig.debugInfo("程序全局配置："+config);
		
		boolean iskeepalive=false;
		if (size == null) {
			size = config.getSize() == null ? DEFAULT_SIZE : config.getSize();
			iskeepalive=config.isKeepalive();
		}
		//默认消息处理方法
		if (this.ohm == null) {
			this.ohm = new DefaultOutHandlerMethod();
		}
		//默认缓存
		if (this.taskDao == null) {
			this.taskDao = new TaskDaoImpl(size);
			//初始化保活线程
			if(iskeepalive) {
				keepAliveHandler = new KeepAliveHandler(taskDao);
				keepAliveHandler.start();
			}
		}
		
		//默认任务处理器
		if (this.taskHandler == null) {
			this.taskHandler = new TaskHandlerImpl(this.ohm);
		}
		
		//默认命令行组装器
		if (this.commandAssembly == null) {
			this.commandAssembly = new CommandAssemblyImpl();
		}
		//默认事件处理器为空
//		if(eventCallback ==null) {
//			eventCallback=new DefaultEventCallbackHandler();
//		}
		
	}
	
	/**
	 * 是否已经初始化
	 * 
	 * @param 如果未初始化时是否初始化
	 * @return
	 */
	public boolean isInit(boolean b) {
		boolean ret = this.ohm == null || this.taskDao == null || this.taskHandler == null|| this.commandAssembly == null;
		if (ret && b) {
			init(globalConfig,null);
		}
		return ret;
	}

	/**
	 * 事件回调
	 * @param type -事件类型
	 * @param tasker -任务信息
	 * @return boolean -true:成功,false:失败
	 */
	protected boolean callback(EventCallbackType type,CommandTasker tasker) {
		if(eventCallback==null) {
			return true;
		}
		return eventCallback.callback(type, tasker);
	}
}
