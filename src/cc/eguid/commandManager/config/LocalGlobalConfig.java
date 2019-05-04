package cc.eguid.commandManager.config;

/**
 * 全局共享配置文件
 * @author eguid
 *
 */
public class LocalGlobalConfig {
	/**
	 * 全局配置
	 */
	public static ProgramConfig LOCAL_GLOBAL_CONFIG=null;
	
	/**
	 * debug控制台消息打印
	 * @param info
	 */
	public static void debugInfo(String info) {
		if(LOCAL_GLOBAL_CONFIG.isDebug())
			System.err.println(info);
	}
}
