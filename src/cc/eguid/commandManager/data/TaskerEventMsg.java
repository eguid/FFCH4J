package cc.eguid.commandManager.data;

import cc.eguid.commandManager.callback.EventCallbackType;

/**
 * 命令行事件消息
 * @author eguid
 *
 */
public class TaskerEventMsg {
	EventCallbackType ecbt;
	CommandTasker tasker;

	public TaskerEventMsg(EventCallbackType ecbt, CommandTasker tasker) {
		super();
		this.ecbt = ecbt;
		this.tasker = tasker;
	}

	public EventCallbackType getEcbt() {
		return ecbt;
	}

	public void setEcbt(EventCallbackType ecbt) {
		this.ecbt = ecbt;
	}

	public CommandTasker getTasker() {
		return tasker;
	}

	public void setTasker(CommandTasker tasker) {
		this.tasker = tasker;
	}

	@Override
	public String toString() {
		return "CommandEventMsg [ecbt=" + ecbt + ", tasker=" + tasker + "]";
	}

}
