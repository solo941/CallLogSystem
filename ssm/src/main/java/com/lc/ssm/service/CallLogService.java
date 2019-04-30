package com.lc.ssm.service;

import com.lc.ssm.domain.CallLog;
import com.lc.ssm.domain.CallLogRange;

import java.util.List;

public interface CallLogService {
    public List<CallLog> findAll();
    /*
    按照范围查询通话记录
     */
    List<CallLog> findCallLogs(String call, List<CallLogRange> list);
}
