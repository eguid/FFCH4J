package cc.eguid.FFmpegCommandManager.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 反射操作工具
 * 
 * @author eguid
 *
 */
public class ReflectUtil {

	public static final String SET = "set";
	public static final String GET = "get";

	public static Object mapToObj(Map<String, Object> map, Class<?> oc)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method[] ms = oc.getDeclaredMethods();
		if (ms == null || ms.length < 1) {
			return null;
		}
		Object obj = getObject(oc);
		for (Method m : ms) {
			String methodName = m.getName();
			String fieldName = getMethodField(methodName, SET);
			Object value = map.get(fieldName);
			if (value != null) {
				setMethodValue(m, obj, typeConvert(value, m));
			}
		}
		return obj;
	}

	public static Object typeConvert(Object obj, Method m) {
		return typeConvert(obj, m.getParameterTypes()[0].getName());
	}

	public static Object typeConvert(Object obj, Field f) {
		return typeConvert(obj, f.getType().getName());
	}
	/**
	 * 基础数据转换
	 * @param obj
	 * @param typeName
	 * @return
	 */
	public static Object typeConvert(Object obj, String typeName) {
		// 基础数据都可以转为String
		String str = String.valueOf(obj);
		if ("int".equals(typeName) || "java.lang.Integer".equals(typeName)) {
			return Integer.valueOf(str.trim());
		} else if ("long".equals(typeName) || "java.lang.Long".equals(typeName)) {
			return Long.valueOf(str.trim());
		} else if ("byte".equals(typeName) || "java.lang.Byte".equals(typeName)) {
			return Byte.valueOf(str.trim());
		} else if ("short".equals(typeName) || "java.lang.Short".equals(typeName)) {
			return Short.valueOf(str.trim());
		} else if ("float".equals(typeName) || "java.lang.Float".equals(typeName)) {
			return Float.valueOf(str.trim());
		} else if ("double".equals(typeName) || "java.lang.Double".equals(typeName)) {
			return Double.valueOf(str.trim());
		} else if ("boolean".equals(typeName) || "java.lang.Boolean".equals(typeName)) {
			return CommonUtil.TRUE.equals(str)?true:false;
		} else if ("char".equals(typeName) || "java.lang.Character".equals(typeName)) {
			return Character.valueOf(str.trim().charAt(0));
		} else if ("java.lang.String".equals(typeName)) {
			return str;
		}
		return null;
	}

	public static Class<?> getFieldType(Class<?> cl, String fieldName) throws NoSuchFieldException, SecurityException {
		Field f = cl.getDeclaredField(fieldName);
		return f.getType();
	}

	public static Field findField(Class<?> cl, String fieldName) throws NoSuchFieldException, SecurityException {
		return cl.getDeclaredField(fieldName);
	}

	/**
	 * 执行方法
	 * 
	 * @param m
	 *            - 方法
	 * @param obj
	 *            - 对象
	 * @param value
	 *            - 参数
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static Object setMethodValue(Method m, Object obj, Object... value)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		m.getParameterTypes();
		return m.invoke(obj, value);
	}

	public static Object getFieldValue(Class<?> obj, String FieldName) throws NoSuchFieldException, SecurityException {
		return obj.getDeclaredField(FieldName);
	}

	/**
	 * 通过class实例化
	 * 
	 * @param oc
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static Object getObject(Class<?> oc) throws InstantiationException, IllegalAccessException {
		return oc.newInstance();
	}

	/**
	 * 获取方法字段
	 * 
	 * @param methodName
	 * @param prefix
	 * @param lowercase
	 * @return
	 */
	public static String getMethodField(String methodName, String prefix) {
		String m = null;
		if (prefix != null) {
			if (methodName.indexOf(prefix) >= 0) {
				m = methodName.substring(prefix.length());
				return stringFirstLower(m);
			}
		}
		return m;
	}

	/**
	 * 首字母大写
	 * 
	 * @param str
	 * @return
	 */
	public static String stringFirstUpper(String str) {
		char[] ch = str.toCharArray();
		if (ch[0] >= 'a' && ch[0] <= 'z') {
			ch[0] = (char) (ch[0] - 32);
		}
		return new String(ch);
	}

	/**
	 * 首字母小写
	 * 
	 * @param str
	 * @return
	 */
	public static String stringFirstLower(String str) {
		char[] ch = str.toCharArray();
		if (ch[0] >= 'A' && ch[0] <= 'Z') {
			ch[0] = (char) (ch[0] + 32);
		}
		return new String(ch);
	}
//
//	public static void showType(Class obj) {
//		for (Field f : obj.getDeclaredFields()) {
//			System.err.println(f.getType().getName());
//		}
//	}

}
