package life.majiang.community.controller;

import life.majiang.community.dto.PageinationDTO;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.reflect.GenericArrayType;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @GetMapping("/question/{id}")
    public String quetion(@PathVariable(name="id") Integer id,
                          Model model) {
        questionService.incView(id);
        QuestionDTO questionDTO=questionService.getById(id);

        model.addAttribute("question",questionDTO);
        return "question";
    }
}
