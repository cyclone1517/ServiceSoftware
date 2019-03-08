package team.hnuwt.servicesoftware.server.constant.down;

import java.util.HashMap;

/**
 * 消费者工具中所有的标签类型
 */
public enum TAG {

    DIRECT("DIRECT",0),
	PLAIN("PLAIN",1),
	PLAINRE("PLAINRE",2),
	DIRECTRE("DIRECTRE",3);
	
	private String    name;
	private int       index;
	private static HashMap<String, TAG> tagSet;
	
	private TAG(String  name, int  index){
		
		this.name=name;
		this.index=index;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}

	public static TAG getTAG(String TAG){
		return tagSet.get(TAG);
	}

	static {
		tagSet = new HashMap<>();
		tagSet.put("DIRECT", DIRECT);
		tagSet.put("PLAIN", PLAIN);
		tagSet.put("PLAINRE", PLAINRE);
		tagSet.put("DIRECTRE", DIRECTRE);
	}
	
}
