package cc.eguid.FFmpegCommandManager.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 读取配置文件并加载FFmpeg
 * 
 * @author eguid
 * @since jdk1.7
 * @version 2016年10月29日
 */
public class LoadConfig {
	private volatile static boolean isHave = false;
	private volatile static String ffmpegPath = null;

	public LoadConfig() {
		super();
		if (readConfig()) {
			System.out.println("读取FFmpeg执行路径成功！");
			isHave = true;
		} else if (initConfInfo()) {
			// 配置文件读取失败，自动从项目路径加载ffmpeg
			isHave = true;
		} else {
			isHave = false;
		}

	}

	protected boolean readConfig() {
		String path = null;
		File confFile = new File(getClass().getResource("/").getPath() + "loadFFmpeg.properties");
		System.out.println("读取FFMPEG配置文件:" + confFile.getPath());
		if (confFile != null && confFile.exists() && confFile.isFile() && confFile.canRead()) {
			Properties prop = new Properties();
			try {
				prop.load(new FileInputStream(confFile));
				path = prop.getProperty("path");
				if (path != null) {
					System.out.println("读取配置文件中的ffmpeg路径：" + path);
					ffmpegPath = path;
					return true;
				}
			} catch (IOException e) {
				System.err.println("读取配置文件失败!");
				return false;
			}
		}
		System.err.println("读取配置文件失败!");
		return false;
	}

	/**
	 * 从配置文件中初始化参数
	 */
	protected boolean initConfInfo() {
		String path = getClass().getResource("../").getPath() + "ffmpeg/ffmpeg.exe";
		System.out.print("预加载默认项目路径FFMPEG配置:" + path);
		File ffmpeg = new File(path);
		ffmpegPath = ffmpeg.getPath();
		if (isHave = ffmpeg.isFile()) {
			return true;
		}
		System.out.println("加载ffmpeg失败！");
		return false;
	}

	/**
	 * 判断ffmpeg环境配置
	 * 
	 * @return true：配置成功；false：配置失败
	 */
	public boolean isHave() {
		return isHave;
	}

	/**
	 * 获取ffmpeg环境调用地址 添加方法功能描述
	 * 
	 * @return
	 */
	public String getPath() {
		return ffmpegPath;
	}

	public static void main(String[] args) {
		LoadConfig conf = new LoadConfig();
	}
}
