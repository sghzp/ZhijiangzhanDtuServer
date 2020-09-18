package com.beans;

public class MixingPlant {

    private String totalCode;//整个数据
    private String StationID;//站号
    private String ProjectID;//项目号
    private String RecordID;//记录号
    private String Water;//水
    private double CementOne;//水泥1
    private double CementTwo;//水泥2

    public String getTotalCode() {
        return totalCode;
    }

    public void setTotalCode(String totalCode) {
        this.totalCode = totalCode;
    }

    public String getStationID() {
        return StationID;
    }
    public void setStationID(String stationNum) {
        StationID = stationNum;
    }
    public String getProjectID() {
        return ProjectID;
    }
    public void setProjectID(String projectNum) {
        ProjectID = projectNum;
    }
    public String getRecordID() {
        return RecordID;
    }
    public void setRecordID(String recordNum) {
        RecordID = recordNum;
    }
    public String getWater() {
        return Water;
    }
    public void setWater(String water) {
        Water = water;
    }
    public double getCementOne() {
        return CementOne;
    }
    public void setCementOne(double cementOne) {
        CementOne = cementOne;
    }
    public double getCementTwo() {
        return CementTwo;
    }
    public void setCementTwo(double cementTwo) {
        CementTwo = cementTwo;
    }

    /**
     * 设置所有参数
     * @param object
     * @return
     */
    public boolean setAll(Object[] object) {
        int i = 0;

        this.StationID = (String) object[i++];
        if (i >= object.length) {
            return false;
        }
        this.ProjectID = (String) object[i++];
        if (i >= object.length) {
            return false;
        }
        this.RecordID = (String) object[i++];
        if (i >= object.length) {
            return false;
        }
        this.Water = (String) object[i++];
        if (i >= object.length) {
            return false;
        }
        this.CementOne = (double) object[i++];
        if (i >= object.length) {
            return false;
        }
        this.CementTwo = (double) object[i++];
        if (i >= object.length) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "MixingPlant{" +
                "StationNum='" + StationID + '\'' +
                ", ProjectNum='" + ProjectID + '\'' +
                ", RecordNum='" + RecordID + '\'' +
                ", Water='" + Water + '\'' +
                ", CementOne=" + CementOne +
                ", CementTwo=" + CementTwo +
                '}';
    }
}
