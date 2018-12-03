package cc.eguid.FFmpegCommandManager.config;

public class FFmpegConfig {
	private String path;
	private boolean debug;
	private Integer size;

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

	@Override
	public String toString() {
		return "FFmpegConfig [path=" + path + ", debug=" + debug + ", size=" + size + "]";
	}
}
