package cc.eguid.commandManager.callback;

import cc.eguid.commandManager.data.CommandTasker;

/**
 * 事件回调
 * @author eguid
 *
 */
public interface EventCallback {
	/**
	 * 回调成功返回值
	 */
	public static final String SUCCESS_CODE="0";
	
	/**
	 * 命令行执行开始事件
	 * @param t -事件类型
	 * @param tasker -任务信息
	 */
	boolean callback(EventCallbackType t,CommandTasker tasker);
}
