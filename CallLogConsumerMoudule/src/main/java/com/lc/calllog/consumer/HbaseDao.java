package com.lc.calllog.consumer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Hbase 数据访问对象
 */

public class HbaseDao {
    private DecimalFormat df = new DecimalFormat();
    private Table table = null;
    private String flag = null;
    private  int partitions;
    public HbaseDao(){
        try {
            df.applyPattern(PropertiesUtil.getProp("hashcode.pattern"));
            Configuration conf = HBaseConfiguration.create();
            Connection conn =ConnectionFactory.createConnection(conf);
            TableName name = TableName.valueOf(PropertiesUtil.getProp("table.name"));
            table = conn.getTable(name);
            partitions = Integer.parseInt(PropertiesUtil.getProp("partition.number"));
            flag = PropertiesUtil.getProp("caller.flag");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * put数据到Hbase
     */
    public void put(String log){

            try {
                if (log == null || log.equals("")){
                    return;
                }
                String[] arr = log.split(",");
                if (arr!= null && arr.length == 4) {
                    //解析日志
                    String caller = arr[0];
                    String callee = arr[1];
                    String callTime = arr[2];
                    callTime = callTime.replace("/","");
                    callTime = callTime.replace(" ","");
                    callTime = callTime.replace(":","");
                    String callDuration = arr[3];
                    //结算区域号
                    String rowkey = genRowkey(getHashCode(caller, callTime), caller, callTime, flag, callee, callDuration);
                    Put put = new Put(Bytes.toBytes(rowkey));
                    put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("caller"), Bytes.toBytes(caller));
                    put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("callee"), Bytes.toBytes(callee));
                    put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("callTime"), Bytes.toBytes(callTime));
                    put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("callDuration"), Bytes.toBytes(callDuration));
                    table.put(put);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    public String getHashCode(String caller, String callTime){
        int len = caller.length();
        //后四位电话，年份+月份
        String  last4Code = caller.substring(len - 4);
        String mon = callTime.substring(0,6);

        int hashcode = (Integer.parseInt(mon) ^ Integer.parseInt(last4Code)) % partitions;
        return df.format(hashcode);
    }

    /**
     * 生成rowkey
     * @param hash
     * @param caller
     * @param time
     * @param flag
     * @param callee
     * @param duration
     * @return
     */
    public String genRowkey(String hash, String caller, String time, String flag, String callee, String duration){
        return hash + "," + caller + "," + time + "," + flag +"," + callee +"," +duration;
    }
}
