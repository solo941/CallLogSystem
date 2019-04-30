package com.lc.ssm.hive;

import com.lc.ssm.domain.CallLog;
import com.lc.ssm.domain.CallLogStat;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service("hiveCallLogService")
public class HiveCallLogService {
    /*
    注册驱动
     */
    private static String driverClass = "org.apache.hive.jdbc.HiveDriver";
    private static String url = "jdbc:hive2://s201:10000/mydb2";
    static {
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询最近通话，使用hive进行mr
     */

    public CallLog findLatestCallLog(String phoneNum){
        try {
            Connection conn = DriverManager.getConnection(url,"zkpk","zkpk");
            Statement st = conn.createStatement();
            String sql = "select * from ext_calllogs_in_hbase where id like '%"+phoneNum+"%' order by callTime desc limit 1";
            ResultSet rs = st.executeQuery(sql);
            CallLog log = null;
            if (rs.next()){
                log = new CallLog();
                log.setCaller(rs.getString("caller"));
                log.setCallee(rs.getString("callee"));
                log.setCallTime(rs.getString("callTime"));
                log.setCallDuration(rs.getString("callDuration"));
            }
            rs.close();
            return log;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 查找制定年份各个月份通话次数
     */
    public List<CallLogStat> statCallLogsCount(String caller, String year){
        List<CallLogStat> list = new ArrayList<CallLogStat>();
        try {
            Connection conn = DriverManager.getConnection(url,"zkpk","zkpk");
            Statement st = conn.createStatement();
            String sql = "select count(*), substr(calltime,1,6) from ext_calllogs_in_hbase" +
                    " where caller = '"+caller+"' and substr(calltime,1,4)=='"+year+"' " +
                    "group by substr(calltime,1,6)";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()){
                CallLogStat callLogStat = new CallLogStat();
                callLogStat.setCount(rs.getInt(1));
                callLogStat.setYearMonth(rs.getString(2));
                list.add(callLogStat);
            }
            rs.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
