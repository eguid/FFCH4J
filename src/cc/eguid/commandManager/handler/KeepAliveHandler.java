package cc.eguid.commandManager.handler;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import cc.eguid.commandManager.data.CommandTasker;
import cc.eguid.commandManager.data.TaskDao;
import cc.eguid.commandManager.util.ExecUtil;

/**
 * 任务保活处理器（一个后台保活线程，用于处理异常中断的持久任务）
 * @author eguid
 *
 */
public class KeepAliveHandler extends Thread{
	/**待处理队列*/
	private static Queue<String> queue=null;
	
	public int err_index=0;//错误计数
	
	public volatile int stop_index=0;//安全停止线程标记
	
	/** 任务持久化器*/
	private TaskDao taskDao = null;
	
	public KeepAliveHandler(TaskDao taskDao) {
		super();
		this.taskDao=taskDao;
		queue=new ConcurrentLinkedQueue<>();
	}

	public static void add(String id ) {
		if(queue!=null) {
			queue.offer(id);
		}
	}
	
	public boolean stop(Process process) {
		if (process != null) {
			process.destroy();
			return true;
		}
		return false;
	}
	
	@Override
	public void run() {
		for(;stop_index==0;) {
			if(queue==null) {
				continue;
			}
			String id=null;
			CommandTasker task=null;
			
			try {
				while(queue.peek() != null) {
					System.err.println("准备重启任务："+queue);
					id=queue.poll();
					task=taskDao.get(id);
					//重启任务
					ExecUtil.restart(task);
				}
			}catch(IOException e) {
				System.err.println(id+" 任务重启失败，详情："+task);
				//重启任务失败
				err_index++;
			}catch(Exception e) {
				
			}
		}
	}
	
	@Override
	public void interrupt() {
		stop_index=1;
	}
	
}
