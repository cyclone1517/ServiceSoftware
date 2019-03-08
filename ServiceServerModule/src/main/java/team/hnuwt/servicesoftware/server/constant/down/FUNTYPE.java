package team.hnuwt.servicesoftware.server.constant.down;

import java.util.HashMap;

/**
 * 下发报文的所有业务类型
 */
@Deprecated
public enum FUNTYPE {

    QUERY("query"),
	CONTROL("control");

	private String    name;
	private static HashMap<String, FUNTYPE> funSet;

	FUNTYPE(String  name){
		this.name=name;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


	public static FUNTYPE getFUN(String FUN){
		return funSet.get(FUN);
	}

	static {
		funSet = new HashMap<>();
		funSet.put("query", QUERY);
		funSet.put("control", CONTROL);
	}
	
}
