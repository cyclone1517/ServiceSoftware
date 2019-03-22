package team.hnuwt.servicesoftware.synchronizer.constant;

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
	CTRL_ON("CTRL_ON"),		    /* 开阀 */
	CTRL_OFF("CTRL_OFF"),		/* 关阀 */
	OPER_RE("OPER_RE"),			/* 操作成功|失败确认 */
	UPLOAD_ON("UPLOAD_ON"),		/* 允许上报 */
	UPLOAD_OFF("UPLOAD_OFF"),	/* 停止上报 */
	READ_UPLOAD("READ_UPLOAD"),/* 读取上报允许 */

	/*
	 * 上行报文标签类型
	 */
	HEARTBEAT("HEARTBEAT"),
	LOGIN("LOGIN"),
	AUTO_UPLOAD("AUTO_UPLOAD"),

    /*
     * 前置机的内部请求
     */
    OFFLINE("OFFLINE"),

    /*
     * 消息推送相关
     */
    COLC_STATE("COLC_STATE"),
	PUBL_DATA("PUBL_DATA");


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
		tags.put("OPER_RE", OPER_RE);
		tags.put("OFFLINE", OFFLINE);
		tags.put("PUBL_DATA", PUBL_DATA);
		tags.put("COLC_STATE", COLC_STATE);
		tags.put("AUTO_UPLOAD", AUTO_UPLOAD);
		tags.put("UPLOAD_ON", UPLOAD_ON);
		tags.put("UPLOAD_OFF", UPLOAD_OFF);
		tags.put("READ_UPLOAD", READ_UPLOAD);
	}

	@Override
	public String toString(){
		return str;
	}
	
}
