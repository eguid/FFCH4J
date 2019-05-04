package cc.eguid.commandManager.callback;

/**
 * 事件回调类型
 * @author eguid
 *
 */
public enum EventCallbackType {
	start,//执行命令后通知
	stop,//停止命令后通知
	interrupt,//中断通知
	restart,//重启通知
}
