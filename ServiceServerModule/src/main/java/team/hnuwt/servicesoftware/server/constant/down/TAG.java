package team.hnuwt.servicesoftware.server.constant.down;

import java.util.HashMap;

/**
 * 消费者工具中所有的标签类型
 */
public enum TAG {

	READ_METER("READ_METER"),
	CTRL_TIME("CTRL_TIME"),
	CTRL_ONOFF("CTRL_ONOFF");

	private String str;

	TAG(String str){
		this.str = str;
	}

	public String getStr(){
		return str;
	}

	private static HashMap<String, TAG> tags;

	public static TAG getTAG(String TAG){
		return tags.get(TAG.toUpperCase());
	}

	static {
		tags = new HashMap<>();
		tags.put("READ_METER", READ_METER);
		tags.put("CTRL_TIME", CTRL_TIME);
		tags.put("CTRL_ONOFF", CTRL_ONOFF);
	}
	
}
