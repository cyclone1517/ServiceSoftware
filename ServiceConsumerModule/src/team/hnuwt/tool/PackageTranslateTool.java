package team.hnuwt.tool;

/**
 * 上行数据包翻译器
 */
public class PackageTranslateTool {

    //以下数据段根据数据库存储数据设计
    private static final int PROPNUM = 20;
    private String[] strlist;

    private String head;
    private String collectorType;//厂商代码
    private String length;         //有效数据长度
    private String fucntionID;   //功能号
    private String subfunctionID;//子功能号
    private String meterID;     //表具ID
    private String collectorID; //集中器ID
    private String lastTotalAll;//最近抄表读数，水量是读表数的100倍，比如120表示1.2吨
    private String reservered1; //预留1
    private String reservered2; //预留2
    private String ReadDate; //抄表日期
    private String valueState;  //阀门状态
    private String BatteryState;//电池电压，实际读数是100倍
    private String temperature; //温度
    private String reservered3; //预留3
    private String HighState;   //状态位高位
    private String LowState;    //状态位低位
    private String operateCode; //操作代码
    private String checkCS;     //校验CS
    private String tail;        //报文尾

    //各个字段长度（单位字符）
    private static final int[] filedLenth = {
            1,      //报文头
            2,      //厂家代码
            2,      //长度
            2,      //功能号
            2,      //子功能号
            4,      //表号
            4,      //集中器ID
            4,      //水表读数
            4,      //预留
            4,      //预留
            3,      //抄表日期
            1,      //阀门状态
            2,      //电池电压
            1,      //温度
            5,      //预留
            1,      //状态位
            1,          //状态位分高低位，一共2位
            5,      //操作代码
            1,      //校验CS
            1       //报文尾
    };

    public PackageTranslateTool (String pkgData) {
        strlist = new String[PROPNUM];
        int cursor = 0;

        System.out.println("[PackageTranslateTool]"+pkgData);
        for(int i=0; i<PROPNUM; i++){
            strlist[i] = pkgData.substring(cursor,cursor+filedLenth[i]*2);
            cursor+=filedLenth[i]*2;
        }

        int cnt = 0;
        this.head = strlist[cnt++];
        this.collectorType = strlist[cnt++];//厂商代码
        this.length = strlist[cnt++];         //有效数据长度
        this.fucntionID = strlist[cnt++];   //功能号
        this.subfunctionID = strlist[cnt++];//子功能号
        this.meterID = strlist[cnt++];     //表具ID
        this.collectorID = strlist[cnt++]; //集中器ID
        this.lastTotalAll = strlist[cnt++];//最近抄表读数，水量是读表数的100倍，比如120表示1.2吨
        this.reservered1 = strlist[cnt++]; //预留1
        this.reservered2 = strlist[cnt++]; //预留2
        this.ReadDate = strlist[cnt++]; //抄表日期
        this.valueState = strlist[cnt++];  //阀门状态
        this.BatteryState = strlist[cnt++];//电池电压，实际读数是100倍
        this.temperature = strlist[cnt++]; //温度
        this.reservered3 = strlist[cnt++]; //预留3
        this.HighState = strlist[cnt++];   //状态位高位
        this.LowState = strlist[cnt++];    //状态位低位
        this.operateCode = strlist[cnt++]; //操作代码
        this.checkCS = strlist[cnt++];     //校验CS
        this.tail = strlist[cnt];        //报文尾
    }

    public String getHead() {
        return head;
    }

    public String getCollectorType() {
        return collectorType;
    }

    public String getLength() {
        return length;
    }

    public String getFucntionID() {
        return fucntionID;
    }

    public String getSubfunctionID() {
        return subfunctionID;
    }

    public String getMeterID() {
        return meterID;
    }

    public String getCollectorID() {
        return collectorID;
    }

    public String getLastTotalAll() {
        return lastTotalAll;
    }

    public String getReservered1() {
        return reservered1;
    }

    public String getReservered2() {
        return reservered2;
    }

    public String getReadDate() {
        return ReadDate;
    }

    public String getValueState() {
        return valueState;
    }

    public String getBatteryState() {
        return BatteryState;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getReservered3() {
        return reservered3;
    }

    public String getHighState() {
        return HighState;
    }

    public String getLowState() {
        return LowState;
    }

    public String getOperateCode() {
        return operateCode;
    }

    public String getCheckCS() {
        return checkCS;
    }

    public String getTail() {
        return tail;
    }

    @Override
    public String toString() {
        return "PackageTranslateTool{" +
                "head='" + head + '\'' +
                ", collectorType='" + collectorType + '\'' +
                ", length=" + length +
                ", fucntionID='" + fucntionID + '\'' +
                ", subfunctionID='" + subfunctionID + '\'' +
                ", meterID='" + meterID + '\'' +
                ", collectorID='" + collectorID + '\'' +
                ", lastTotalAll='" + lastTotalAll + '\'' +
                ", reservered1='" + reservered1 + '\'' +
                ", reservered2='" + reservered2 + '\'' +
                ", ReadDate=" + ReadDate +
                ", valueState='" + valueState + '\'' +
                ", BatteryState='" + BatteryState + '\'' +
                ", temperature='" + temperature + '\'' +
                ", reservered3='" + reservered3 + '\'' +
                ", HighState='" + HighState + '\'' +
                ", LowState='" + LowState + '\'' +
                ", operateCode='" + operateCode + '\'' +
                ", checkCS='" + checkCS + '\'' +
                ", tail='" + tail + '\'' +
                '}';
    }

    public static void main(String[] args) {
        System.out.println(new PackageTranslateTool("AA00002B0002050202A28DB910E0B92E0472060000000000" +
                "000000000011071C00570100000000000000000020E837003CFF").toString());
    }
}
