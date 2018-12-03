package cc.eguid.FFmpegCommandManager.entity;
/**
 * 用于存放任务id,任务主进程，任务输出线程
 * @author eguid
 * @since jdk1.7
 * @version 2016年10月29日
 */
public class TaskEntity {
private final String id;//任务id
private final Process process;//任务主进程
private final Thread thread;//任务输出线程
public TaskEntity(String id,Process process,Thread thread) {
	this.id=id;
	this.process=process;
	this.thread=thread;
}
public String getId() {
	return id;
}
public Process getProcess() {
	return process;
}
public Thread getThread() {
	return thread;
}
@Override
public String toString() {
	return "TaskEntity [id=" + id + ", process=" + process + ", thread=" + thread + "]";
}

}
