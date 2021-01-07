package life.majiang.community.controller;

import life.majiang.community.dto.CommentCreateDTO;
import life.majiang.community.dto.CommentDTO;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.enums.CommentTypeEnum;
import life.majiang.community.service.CommentService;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String quetion(@PathVariable(name="id") Long id,
                          Model model) {
        //向前端传question
        questionService.incView(id);
        QuestionDTO questionDTO=questionService.getById(id);

        model.addAttribute("question",questionDTO);

        //向前端传comment
        List<CommentDTO> commentDTOList=commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
        model.addAttribute("comments",commentDTOList);

        //向前端传related Question
        List<QuestionDTO> questionDTOS=questionService.selectRelated(questionDTO);
        model.addAttribute("relatedQuestions",questionDTOS);
        return "question";
    }
}
