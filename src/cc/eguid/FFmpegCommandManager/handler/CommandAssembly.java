package cc.eguid.FFmpegCommandManager.service;

import java.util.Map;
/**
 * 命令组装器接口
 * @author eguid
 * @since jdk1.7
 * @version 2016年10月29日
 */
public interface CommandAssembly {
	/**
	 * 将参数转为ffmpeg命令
	 * @param paramMap
	 * @return
	 */
	public String assembly(Map<String, String> paramMap);
}
