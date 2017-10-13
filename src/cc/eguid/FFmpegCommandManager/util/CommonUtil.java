package cc.eguid.FFmpegCommandManager.util;

import java.io.File;
import java.util.UUID;
/**
 * 公共常用方法工具
 * @author eguid
 *
 */
public class CommonUtil {
	/**
	 * 当前项目根路径
	 */
	public static final String rootPath = getProjectRootPath();
	public static final String TRUE="true";
	public static final String NULL_STRING="";
	public static final String H_LINE="-";
	public static String getUUID(){
		return UUID.randomUUID().toString().trim().replaceAll(H_LINE, NULL_STRING); 
	}
	/**
	 * 是否为空
	 * 
	 * @param str
	 * @return boolean true:为空，false：不为空
	 */
	public static boolean isNull(String str) {
		return str == null || NULL_STRING.equals(str.trim());
	}
	/**
	 * 字符串是否是"true"
	 * @param str
	 * @return
	 */
	public static boolean isTrue(String str){
		return TRUE.equals(str)?true:false;
	}
	
	/**
	 * 获取项目根目录（静态）
	 * @return
	 */
	public static String getRootPath() {
		return rootPath;
	}
	/**
	 * 获取项目根目录（动态）
	 * @return
	 */
	public static String getProjectRootPath() {
		String path=null;
		try{
		path =CommonUtil.class.getResource("/").getPath();
		}catch(Exception e){
			File directory = new File(NULL_STRING);
		path= directory.getAbsolutePath()+File.separator;
		}
		return path;
	}
	/**
	 * 获取类路径
	 * @param cla
	 * @return
	 */
	public static String getClassPath(Class<?> cla){
		return cla.getResource("").getPath();
	}
	
}
