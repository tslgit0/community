package life.majiang.community.controller;

import life.majiang.community.dto.PageinationDTO;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import life.majiang.community.service.QuestionService;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;
    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name="action") String action,
                          HttpServletRequest request,
                          Model model,
                          @RequestParam (name="page",defaultValue = "1") Integer page,
                          @RequestParam(name="size",defaultValue = "5") Integer size){


        User user=(User)request.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/";
        }
        if(action.equals("question")){
            model.addAttribute("section","question");
            model.addAttribute("sectionName","我的提问");
        }
        if(action.equals("replies")){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最近回复");
        }
        PageinationDTO pagenation=questionService.list(user.getId(),page,size);

        model.addAttribute("pagenation",pagenation);
        return "profile";


    }
}
