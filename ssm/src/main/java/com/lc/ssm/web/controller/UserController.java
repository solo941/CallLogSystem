package com.lc.ssm.web.controller;

import com.lc.ssm.domain.User;
import com.lc.ssm.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;
@Controller
public class UserController {
    @Resource(name = "userService")
    private UserService us;
    /**
     * 查看全部user
     */
    @RequestMapping("/user/findall")
    public String findAll(Model m){
        List<User> list = us.selectAll();
        m.addAttribute("allUsers",list);
        return "user/userList";
    }
    @RequestMapping("/user/deleteuser")
    public String deleteUser(@RequestParam("uid") Integer uid){
        us.delete(uid);
        return "redirect:/user/findall";
    }
    @RequestMapping(value = "user/userSave", method = RequestMethod.POST)
    public String saveUser(User u){
        us.save(u);
        return "redirect:/user/findall";
    }
    @RequestMapping("user/toAddUserPage")
    public String toAddUserPage(){
        return "user/userEdit";
    }

    @RequestMapping("/user/findPage")
    public String findPage(Model m, @RequestParam("pn") int pn){
        //查询总记录数
        int counts = us.selectCount();

        //每页记录数
        int recordPerPage = 5;

        //结算页数
        int pages = 0;
        if (counts % recordPerPage == 0){
            pages = counts / recordPerPage;
        }
        else {
            pages = counts / recordPerPage + 1;
        }

        //偏移量
        int offset = (pn-1) * recordPerPage;
        List<User> list = us.selectPage(offset,recordPerPage);
        m.addAttribute("allUsers",list);
        m.addAttribute("pages", pages);
        return "user/userList";
    }
}
