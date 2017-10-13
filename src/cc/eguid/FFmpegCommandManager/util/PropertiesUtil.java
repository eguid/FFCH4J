package cc.eguid.FFmpegCommandManager.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import java.util.Map.Entry;
/**
 * properties配置文件读取
 * @author eguid
 *
 */
public class PropertiesUtil {
	/**
	 * 加载properties配置文件并读取配置项
	 * @param path
	 * @param cl
	 * @return
	 */
	public static Object load(String path, Class<?> cl) {
		InputStream is = null;
		try {
			is = getInputStream(path);
		} catch (FileNotFoundException e) {
			//尝试从web目录读取
			String newpath=CommonUtil.getProjectRootPath()+path;
			System.err.println("尝试从web目录读取配置文件："+newpath);
			try {
				is = getInputStream(newpath);
			System.err.println("web目录读取到配置文件："+newpath);
			} catch (FileNotFoundException e1) {
				System.err.println("没找到配置文件，读取默认配置文件");
				//尝试从jar包中读取默认配置文件
				ClassLoader classloader = Thread.currentThread().getContextClassLoader();
				try {
					is = classloader.getResourceAsStream("cc/eguid/FFmpegCommandManager/config/defaultFFmpegConfig.properties");
					System.err.println("读取默认配置文件：defaultFFmpegConfig.properties");
				} catch (Exception e2) {
					System.err.println("没找到默认配置文件:defaultFFmpegConfig.properties");
					return null;
				}
			}
		}
		if (is != null) {
			Properties pro = new Properties();
			try {
				System.err.println("加载配置文件...");
				pro.load(is);
				System.err.println("加载配置文件完毕");
				return load(pro, cl);
			} catch (IOException e) {
				System.err.println("加载配置文件失败");
				return null;
			}

		}
		return null;
	}
	/**
	 * 读取配置项并转换为对应对象
	 * @param pro
	 * @param cl
	 * @return
	 */
	public static Object load(Properties pro, Class<?> cl) {
		try {
			Map<String, Object> map = getMap(pro);
			System.err.println("读取的配置项：" + map);
			Object obj = ReflectUtil.mapToObj(map, cl);
			System.err.println("转换后的对象：" + obj);
			return obj;
		} catch (InstantiationException e) {
			System.err.println("加载配置文件失败");
			return null;
		} catch (IllegalAccessException e) {
			System.err.println("加载配置文件失败");
			return null;
		} catch (IllegalArgumentException e) {
			System.err.println("加载配置文件失败");
			return null;
		} catch (InvocationTargetException e) {
			System.err.println("加载配置文件失败");
			return null;
		}
	}
	/**
	 * 获取对应文件路径下的文件流
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 */
	public static InputStream getInputStream(String path) throws FileNotFoundException {
		return new FileInputStream(path);
	}
	/**
	 * 根据路径获取properties的Map格式内容
	 * @param path
	 * @return
	 */
	public static Map<String, Object> getMap(String path){
		Properties pro=new Properties();
		try {
			pro.load(getInputStream(path));
			return getMap(pro);
		} catch (IOException e) {
			return null;
		}
	}
	/**
	 * 根据路径获取properties的Map格式内容
	 * @param path
	 * @param isRootPath -是否在项目根目录中
	 * @return
	 */
	public static Map<String, Object> getMap(String path,boolean isRootPath){
		return getMap(isRootPath?CommonUtil.getProjectRootPath()+path:path);
	}
	/**
	 * Properties配置项转为Map<String, Object>
	 * @param pro
	 * @return
	 */
	public static Map<String, Object> getMap(Properties pro) {
		if (pro == null || pro.isEmpty() || pro.size() < 1) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		for (Entry<Object, Object> en : pro.entrySet()) {
			String key = (String) en.getKey();
			Object value = en.getValue();
			map.put(key, value);
		}
		return map;
	}
}
