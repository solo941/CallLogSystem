package com.lc.ssm.domain;

import com.lc.ssm.util.CallLogUtil;

public class CallLog {
    private String caller;

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public void setCallee(String callee) {
        this.callee = callee;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }

    public String getCaller() {
        return caller;
    }

    public String getCallee() {
        return callee;
    }

    public String getCallTime() {
        if (callTime != null){
            return CallLogUtil.formatDate(callTime);
        }
        return null;
    }

    public String getCallDuration() {
        return callDuration;
    }


    private String callee;
    private String callTime;

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }

    public void setCalleeName(String calleeName) {
        this.calleeName = calleeName;
    }

    public String getCallerName() {
        return callerName;
    }

    public String getCalleeName() {
        return calleeName;
    }

    private String callDuration;
    //主被叫
    private String callerName;
    private String calleeName;
}
