package com.lc.ssm.domain;

public class CallLogRange {
    private String startPoint;
    private String endPoint;

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startTime) {
        this.startPoint = startTime;
    }

    @Override
    public String toString() {
        return  startPoint + "-" + endPoint ;
    }

    public void setEndPoint(String endTime) {
        this.endPoint = endTime;
    }

    public String getEndPoint() {
        return endPoint;
    }
}
