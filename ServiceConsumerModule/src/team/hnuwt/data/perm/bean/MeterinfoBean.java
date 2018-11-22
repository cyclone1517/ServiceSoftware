package team.hnuwt.data.perm.bean;

import java.sql.Timestamp;

public class MeterinfoBean {

    private String MeterID;
    private String CollectorID;
    private String UserName;
    private String Address;
    private int LastTotalAll;
    private Timestamp readDate;
    private String ValveState;
    private int RssiValue;
    private int BatteryState;
    private String HighState;
    private String LowState;
    private String ConcentratorNo;
    private String MeterOrderNo;
    private Timestamp UploadTime;

    public String getMeterID() {
        return MeterID;
    }

    public void setMeterID(String meterID) {
        MeterID = meterID;
    }

    public String getCollectorID() {
        return CollectorID;
    }

    public void setCollectorID(String collectorID) {
        CollectorID = collectorID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getLastTotalAll() {
        return LastTotalAll;
    }

    public void setLastTotalAll(int lastTotalAll) {
        LastTotalAll = lastTotalAll;
    }

    public Timestamp getReadDate() {
        return readDate;
    }

    public void setReadDate(Timestamp readDate) {
        this.readDate = readDate;
    }

    public String getValveState() {
        return ValveState;
    }

    public void setValveState(String valveState) {
        ValveState = valveState;
    }

    public int getRssiValue() {
        return RssiValue;
    }

    public void setRssiValue(int rssiValue) {
        RssiValue = rssiValue;
    }

    public int getBatteryState() {
        return BatteryState;
    }

    public void setBatteryState(int batteryState) {
        BatteryState = batteryState;
    }

    public String getHighState() {
        return HighState;
    }

    public void setHighState(String highState) {
        HighState = highState;
    }

    public String getLowState() {
        return LowState;
    }

    public void setLowState(String lowState) {
        LowState = lowState;
    }

    public String getConcentratorNo() {
        return ConcentratorNo;
    }

    public void setConcentratorNo(String concentratorNo) {
        ConcentratorNo = concentratorNo;
    }

    public String getMeterOrderNo() {
        return MeterOrderNo;
    }

    public void setMeterOrderNo(String meterOrderNo) {
        MeterOrderNo = meterOrderNo;
    }

    public Timestamp getUploadTime() {
        return UploadTime;
    }

    public void setUploadTime(Timestamp uploadTime) {
        UploadTime = uploadTime;
    }
}
