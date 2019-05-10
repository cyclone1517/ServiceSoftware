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
	READ_METER("READ_METER"),					/* 远程抄表 */
	CTRL_ON("CTRL_ON"),		    				/* 开阀 */
	CTRL_OFF("CTRL_OFF"),						/* 关阀 */
	CTRL_ONOFF("CTRL_ONOFF"),					/* 操作回复没有指明开阀还是关阀，笼统标识 */

	ARCHIVE_DOWNLOAD("ARCHIVE_DOWNLOAD"),		/* 下载档案 */
	ARCHIVE_CLOSE("ARCHIVE_CLOSE"),				/* 关闭档案 */
	ARCHIVE_GET("ARCHIVE_GET"),					/* 读取档案 */

	TYPE_METER_GET("TYPE_METER_GET"),			/* 读取指定类型表序号 */
	GPS_SET("GPS_SET"),							/* 设置GPS参数 */
	GPS_GET("GPS_GET"),							/* 读取GPS参数 */

	UPLOAD_ON("UPLOAD_ON"),						/* 允许上报 */
	UPLOAD_OFF("UPLOAD_OFF"),					/* 停止上报 */
	READ_UPLOAD("READ_UPLOAD"),					/* 读取上报允许 */
	UPLOAD_TIME_SET("UPLOAD_TIME_SET"),			/* 设置上报时间 */
	UPLOAD_TIME_GET("UPLOAD_TIME_GET"),			/* 读取上报时间 */

	CTRL_TIME("CTRL_TIME"),						/* 设置时钟 */
	TIME_GET("TIME_GET"),						/* 读时钟 */
	VERSION_GET("VERSION_GET"),					/* 读集中器版本 */
	ADDR_SET("ADDR_SET"),						/* 设置集中器地址 */

	DATA_INIT("DATA_INIT"),						/* 数据初始化 */
	CTRL_ON_TIME("CTRL_ON_TIME"),				/* 定时开阀 */
	CTRL_OFF_TIME("CTRL_ON_TIME"),				/* 定时关阀 */

	READ_FREQ("READ_FREQ"),						/* 读取集中器频率 */
	BIGMETER_PERIOD_SET("BIGMETER_PERIOD_SET"),/* 设置大表采集周期 */
	BIGMETER_AUTO_UPLOAD("BIGMETER_AUTO_UPLOAD"),

	END_RESTART("END_RESTART"),					/* 终端重启 */
	METER_BASENUM_SET("METER_BASENUM_SET"),		/* 设置表底数 */
	METER_EXCP_CLEAR("METER_EXCP_CLEAR"),		/* 清除表异常 */


	/*
	 * 下发命令回复
	 */
	OPER_RE("OPER_RE"),							/* 操作成功|失败确认 */

	/*
	 * 上行报文标签类型
	 */
	HEARTBEAT("HEARTBEAT"),						/* 心跳 */
	LOGIN("LOGIN"),								/* 登录 */
	LOGIN_PIPE("LOGIN_PIPE"),					/* 登录管道，将登录设备传递到mysql存储登录详情 */
	TIME_UPLOAD_STATE("TIME_UPLOAD_STATE"),		/* 上报定时开关阀门后阀门状态 */
	AUTO_UPLOAD("AUTO_UPLOAD"),					/* 定时自动上传 */

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
		tags.put("CTRL_ONOFF", CTRL_ONOFF);
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
		tags.put("ARCHIVE_DOWNLOAD", ARCHIVE_DOWNLOAD);
		tags.put("ARCHIVE_CLOSE", ARCHIVE_CLOSE);
		tags.put("LOGIN_PIPE", LOGIN_PIPE);
	}

	@Override
	public String toString(){
		return str;
	}
	
}
