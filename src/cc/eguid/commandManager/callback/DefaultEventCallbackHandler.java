package cc.eguid.commandManager.callback;

import java.util.HashMap;
import java.util.Map;

import cc.eguid.commandManager.config.LocalGlobalConfig;
import cc.eguid.commandManager.data.CommandTasker;
import cc.eguid.commandManager.util.HttpUtil;

/**
 * 默认事件回调处理器
 * @author eguid
 *
 */
public class DefaultEventCallbackHandler implements EventCallback{

	/*
	 * @see cc.eguid.commandManager.callback.EventCallback#callback(cc.eguid.commandManager.callback.EventCallbackType, cc.eguid.commandManager.data.CommandTasker)
	 */
	@Override
	public boolean callback(EventCallbackType t, CommandTasker tasker) {
		//发送回调请求
		return httpCallback(t,tasker);
	}
	
	public static boolean httpCallback(EventCallbackType t, CommandTasker tasker) {
		//发送回调请求
		Map<String,String> param=new HashMap<String,String>();
		param.put("kind", t.toString());
		param.put("id", tasker.getId());
		param.put("cmd", tasker.getCommand());
		param.put("time", String.valueOf(System.currentTimeMillis()));
		String url=LocalGlobalConfig.LOCAL_GLOBAL_CONFIG.getCallback();
		String result=HttpUtil.post(url,param);
		
		System.err.println(result);
		//返回0表示成功，其他值失败
		if(SUCCESS_CODE.equals(result)) {
			return true;
		}
		return false;
	}

}
