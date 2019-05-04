package cc.eguid.commandManager.config;

/**
 * 程序基础配置
 * 
 * @author eguid
 * 
 */
public class ProgramConfig {
	
	private String path;//默认命令行执行根路径
	private boolean debug;//是否开启debug模式
	private Integer size;//任务池大小
	private String callback;//回调通知地址
	private boolean keepalive;//是否开启保活

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public boolean isKeepalive() {
		return keepalive;
	}

	public void setKeepalive(boolean keepalive) {
		this.keepalive = keepalive;
	}

	@Override
	public String toString() {
		return "ProgramConfig [path=" + path + ", debug=" + debug + ", size=" + size + ", callback=" + callback
				+ ", keepalive=" + keepalive + "]";
	}
}
