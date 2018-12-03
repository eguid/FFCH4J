package cc.eguid.commandManager.commandbuidler;

import cc.eguid.commandManager.CommandManager;

/**
 * 默认流式命令行构建器（非线程安全）
 * @author eguid
 */
public class DefaultCommandBuidler implements CommandBuidler{

	StringBuilder buidler=null;
	String command=null;
	
	public DefaultCommandBuidler() {
		create();
	}
	

	public DefaultCommandBuidler(String rootpath) {
		create(rootpath);
	}


	@Override
	public CommandBuidler create(String rootpath) {
		buidler=new StringBuilder(rootpath);
		return this;
	}

	@Override
	public CommandBuidler create() {
		return create(CommandManager.config.getPath());
	}

	@Override
	public CommandBuidler add(String key, String val) {
		return add(key).add(val);
	}

	@Override
	public CommandBuidler add(String val) {
		if(buidler!=null) {
			buidler.append(val);
			addBlankspace();
		}
		return this;
	}

	@Override
	public CommandBuidler build() {
		if(buidler!=null) {
			command=buidler.toString();
		}
		return this;
	}
	
	private void addBlankspace() {
		buidler.append(" ");
	}

	@Override
	public String get() {
		if(command==null) {
			build();
		}
		return command;
	}

}
