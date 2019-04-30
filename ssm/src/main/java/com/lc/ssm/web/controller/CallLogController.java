package com.lc.ssm.web.controller;


import com.alibaba.fastjson.JSON;
import com.lc.ssm.domain.CallLog;
import com.lc.ssm.domain.CallLogRange;
import com.lc.ssm.domain.CallLogStat;
import com.lc.ssm.hive.HiveCallLogService;
import com.lc.ssm.service.CallLogService;
import com.lc.ssm.service.PersonService;
import com.lc.ssm.util.CallLogUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Controller
public class CallLogController {


    @Resource(name = "callLogService")
    private CallLogService cs ;

    @Resource(name = "hiveCallLogService")
    private HiveCallLogService hcs;

    @Resource(name = "personService")
    private PersonService ps;

    @RequestMapping("/callLog/findAll")
    public String findAll(Model m){
        List<CallLog> list = cs.findAll();
        m.addAttribute("callLogs",list);
        return "callLog/callLogListAll";
    }

    @RequestMapping("/callLog/json/findAll")
    public String findAllJSON(HttpServletResponse response){
        List<CallLog> list = cs.findAll();
        String json = JSON.toJSONString(list);
        //通知浏览器内容类型
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        try {
            //json文件
            OutputStream out = response.getOutputStream();
            out.write(json.getBytes("utf-8"));
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 进入查询通话的页面
     * @return
     */
    @RequestMapping("/callLog/toFindCallLogPage")
    public String toFindCallLogPage(){
        return "callLog/findCallLog";
    }


    @RequestMapping(value = "/callLog/findCallLog",method = RequestMethod.POST)
    public String findCallLog(Model m, @RequestParam("caller") String caller, @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime){
        List<CallLogRange> list = CallLogUtil.getCallLogRanges(startTime,endTime);
        List<CallLog> logs = cs.findCallLogs(caller,list);
        m.addAttribute("callLogs",logs);
        return "callLog/callLogList";
    }
    @RequestMapping("/callLog/toFindLatestCallLogPage")
    public String toFindLatestCallLogPage(){
        return "callLog/findLatestCallLog";
    }
    @RequestMapping(value = "/callLog/findLatestCallLog",method = RequestMethod.POST)
    public String findLatestCallLog(Model m , @RequestParam("caller") String caller){
        CallLog log = hcs.findLatestCallLog(caller);
        if(log != null){
            m.addAttribute("log", log);
        }
        return "callLog/latestCallLog" ;
    }

    @RequestMapping("/callLog/toStatCallLog")
    public String toStatCallLog(){
        return "callLog/statCallLog";
    }

    @RequestMapping("/callLog/statCallLog")
    public String statCallLog(Model m,@RequestParam("caller") String caller, @RequestParam("year") String year){
        List<CallLogStat> list = hcs.statCallLogsCount(caller,year);
        if (list!=null && !list.isEmpty()) {
            List<String> months = new ArrayList<String>();
            List<Integer> counts = new ArrayList<Integer>();
            for (CallLogStat cls : list) {
                months.add(cls.getYearMonth());
                counts.add(cls.getCount());
            }
            m.addAttribute("title", caller + "在" + year + "年各月份通话次数统计");
            m.addAttribute("list",list);
        }

        return "callLog/statCallLog";
    }
}
