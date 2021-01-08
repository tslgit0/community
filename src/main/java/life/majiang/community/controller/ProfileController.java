package life.majiang.community.controller;

import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import life.majiang.community.service.NotificationService;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private NotificationService notificationService;
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
            PaginationDTO paginationDTO=questionService.list(user.getId(),page,size);
            model.addAttribute("pagination",paginationDTO);
        }
        if(action.equals("replies")){
            PaginationDTO paginationDTO = notificationService.list(user.getId(), page, size);
            Long unreadCount=notificationService.countUnread(user.getId());
            model.addAttribute("section", "replies");
            model.addAttribute("pagination", paginationDTO);
            model.addAttribute("unreadCount",unreadCount);
            model.addAttribute("sectionName", "最新回复");
        }

        return "profile";


    }
}
