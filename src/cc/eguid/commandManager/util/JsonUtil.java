package cc.eguid.commandManager.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * json转换工具
 * @author eguid
 *
 */
public class JsonUtil {

	/**
	 * map转json
	 * @param map
	 * @return String
	 */
	public static String toJson(Map<String,String> map) {
		StringBuilder result=new StringBuilder("{");
		int index=map.size();
		for(Entry<String, String> e:map.entrySet()) {
			String key=e.getKey(),value=e.getValue();
			parseOne(result,key,value);
			index--;
			if(index>0) {
				result.append(",");
			}
		}
		result.append("}");
		return result.toString();
	}
	
	private static void parseOne(StringBuilder sb,String key,String value) {
		sb.append('"').append(key).append('"').append(":").append('"').append(value).append('"');
	}
	
	/**
	 * 一维json字符串转map
	 * @param json
	 * @return
	 */
	public static Map<String,String> toMap(String json) {
		int startIndex=json.indexOf("{");
		int endIndex=json.lastIndexOf("}");
		String s=null;
		s=json.substring(startIndex+1, endIndex);
		String[] results=s.split(",");
		int size=results.length;
		Map<String,String> map=new HashMap<String, String>(size);
		if(results!=null&&size>0) {
			for(String result:results) {
				String[] kv=result.split(":");
				map.put(kv[0], kv[1]);
			}
		}
		return map;
	}
	
//	public static void main(String[] args) {
//		Map<String,String> map=new HashMap<String, String>();
//		map.put("id", "1");
//		map.put("name","eguid");
//		map.put("name1","eguid");
//		map.put("name2","eguid");
//		map.put("name3","eguid");
//		map.put("name4","eguid");
//		System.err.println(toJson(map));
//		String json="{\"name4\":\"eguid\",\"name3\":\"eguid\",\"name\":\"eguid\",\"id\":\"1\",\"name2\":\"eguid\",\"name1\":\"eguid\",{\"name\":\"test\",\"name1\":\"test\"}}\"";
//		System.err.println(toMap(json));
//	}
	
}
