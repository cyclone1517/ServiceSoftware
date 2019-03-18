package team.hnuwt.servicesoftware.prtcplugin.constant;

import java.util.HashMap;

/**
 * @author yuanlong chen
 * 标签类型
 */
public enum TAG {

	/*
	 * 下发命令标签类型
	 */
	READ_METER("READ_METER"),
	CTRL_TIME("CTRL_TIME"),
	CTRL_ON("CTRL_ON"),		/* 下发命令要指明ON或OFF */
	CTRL_OFF("CTRL_OFF"),
	CTRL_ONOFF("CTRL_ONOFF"),	/* 反馈数据要分大类 */

	/*
	 * 上行报文标签类型
	 */
	HEARTBEAT("HEARTBEAT"),
	UPLOAD("UPLOAD"),
	LOGIN("LOGIN");

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
		tags.put("CTRL_ON", CTRL_ON);
		tags.put("CTRL_OFF", CTRL_OFF);
		tags.put("HEARTBEAT", HEARTBEAT);
		tags.put("LOGIN", LOGIN);
		tags.put("CTRL_ONOFF", CTRL_ONOFF);
		tags.put("UPLOAD", UPLOAD);

	}

	@Override
	public String toString(){
		return str;
	}
	
}
