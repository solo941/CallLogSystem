package com.lc.callloggen;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class App {
    static Random r = new Random();
    static List<String> phoneNumbers = new ArrayList<String>();
    static Map<String,String> callers = new HashMap<String, String>();
    static {
        callers.put("15810092493", "史玉龙");
        callers.put("18000696806", "赵贺彪");
        callers.put("15151889601", "张倩 ");
        callers.put("13269361119", "王世昌");
        callers.put("15032293356", "张涛");
        callers.put("17731088562", "张阳");
        callers.put("15338595369", "李进全");
        callers.put("15733218050", "杜泽文");
        callers.put("15614201525", "任宗阳");
        callers.put("15778423030", "梁鹏");
        callers.put("18641241020", "郭美彤");
        callers.put("15732648446", "刘飞飞");
        callers.put("13341109505", "段光星");
        callers.put("13560190665", "唐会华");
        callers.put("18301589432", "杨力谋");
        callers.put("13520404983", "温海英");
        callers.put("18332562075", "朱尚宽");
        callers.put("18620192711", "刘能宗");
        phoneNumbers.addAll(callers.keySet());
    }
    public static void main(String[] args) throws InterruptedException, IOException {
        genCallLog();
    }

    private static void genCallLog() throws InterruptedException, IOException {
        FileWriter fw = new FileWriter(PropertiesUtil.getString("log.file"),true);
        while(true) {
            String caller = phoneNumbers.get(r.nextInt(callers.size()));
            String callerName = callers.get(caller);
            String callee = null;
            String calleeName = null;
            while (true) {
                callee = phoneNumbers.get(r.nextInt(callers.size()));
                if (!caller.equals(callee))
                    break;
            }
            calleeName = callers.get(callee);
            int duration = r.nextInt(PropertiesUtil.getInt("call.duration.max")) + 1;
            DecimalFormat df = new DecimalFormat();
            df.applyPattern(PropertiesUtil.getString("call.duration.format"));
            String durStr = df.format(duration);

            int year = PropertiesUtil.getInt("call.year");
            //月份(0~11)
            int month = r.nextInt(12);
            //天,范围(1~31)
            int day = r.nextInt(29) + 1;
            int hour = r.nextInt(24);
            int min = r.nextInt(60);
            int sec = r.nextInt(60);

            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, min);
            c.set(Calendar.SECOND, sec);
            Date date = c.getTime();

            Date now = new Date();
            if (date.compareTo(now) > 0) {
                continue;
            }
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern(PropertiesUtil.getString("call.time.format"));
            String dateStr = sdf.format(date);

            String log = caller + "," + callee + "," + dateStr + "," + durStr;
            System.out.println(log);
            fw.write(log + "\r\n");
            fw.flush();
            Thread.sleep(PropertiesUtil.getInt("gen.data.interval.ms"));
        }



    }
}
