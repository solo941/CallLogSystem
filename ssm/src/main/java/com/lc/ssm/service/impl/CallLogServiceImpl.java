package com.lc.ssm.service.impl;

import com.lc.ssm.domain.CallLog;
import com.lc.ssm.domain.CallLogRange;
import com.lc.ssm.service.CallLogService;
import com.lc.ssm.service.PersonService;
import com.lc.ssm.util.CallLogUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service("callLogService")
public class CallLogServiceImpl implements CallLogService {
    @Resource(name = "personService")
    private PersonService ps;
    private Table table;
    public CallLogServiceImpl(){
        try {
            Configuration conf = HBaseConfiguration.create();
            Connection conn = ConnectionFactory.createConnection(conf);
            TableName name = TableName.valueOf("ns1:calllogs");
            table = conn.getTable(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<CallLog> findAll() {
        List<CallLog> list = new ArrayList<CallLog>();
        try {
            Scan scan = new Scan();
            ResultScanner rs = table.getScanner(scan);
            Iterator<Result> it = rs.iterator();
            List<String> listTemp = new ArrayList<String>();
            byte[] f = Bytes.toBytes("f1");
            byte[] caller = Bytes.toBytes("caller");
            byte[] callee = Bytes.toBytes("callee");
            byte[] callTime = Bytes.toBytes("callTime");
            byte[] callDuration = Bytes.toBytes("callDuration");
            CallLog log = null;
            String rowkey = null;
            while (it.hasNext()){
                log = new CallLog();
                Result r = it.next();
                rowkey = Bytes.toString(r.getRow());
                if (listTemp.contains(rowkey)){
                    continue;
                }
                else {
                    //设置用户名
                    String callerStr = Bytes.toString(r.getValue(f, caller));
                    log.setCaller(callerStr);
                    log.setCallerName(ps.selectNameByPhone(callerStr));

                    String calleeStr = Bytes.toString(r.getValue(f, callee));
                    log.setCallee(calleeStr);
                    log.setCalleeName(ps.selectNameByPhone(calleeStr));

                    log.setCallTime(Bytes.toString(r.getValue(f, callTime)));
                    log.setCallDuration(Bytes.toString(r.getValue(f, callDuration)));
                    list.add(log);
                    listTemp.add(rowkey);
                }
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<CallLog> findCallLogs(String call, List<CallLogRange> ranges) {
        List<CallLog> logs = new ArrayList<CallLog>();
        try {
            for (CallLogRange range : ranges){
                Scan scan = new Scan();
                scan.setStartRow(Bytes.toBytes(CallLogUtil.getStartRowkey(call,range.getStartPoint(),100)));
                scan.setStopRow(Bytes.toBytes(CallLogUtil.getStopRowkey(call,range.getStartPoint(),range.getEndPoint(),100)));

                ResultScanner rs = table.getScanner(scan);
                Iterator<Result> it = rs.iterator();
                byte[] f = Bytes.toBytes("f1");
                byte[] caller = Bytes.toBytes("caller");
                byte[] callee = Bytes.toBytes("callee");
                byte[] callTime = Bytes.toBytes("callTime");
                byte[] callDuration = Bytes.toBytes("callDuration");
                CallLog log = null;
                while (it.hasNext()){
                    log = new CallLog();
                    Result r = it.next();

                    //设置用户名
                    String callerStr = Bytes.toString(r.getValue(f,caller));
                    log.setCaller(callerStr);
                    log.setCallerName(ps.selectNameByPhone(callerStr));

                    String calleeStr = Bytes.toString(r.getValue(f,callee));
                    log.setCallee(calleeStr);
                    log.setCalleeName(ps.selectNameByPhone(calleeStr));
                    log.setCallTime(Bytes.toString(r.getValue(f,callTime)));
                    log.setCallDuration(Bytes.toString(r.getValue(f,callDuration)));
                    logs.add(log);
                }
            }
            return logs;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
