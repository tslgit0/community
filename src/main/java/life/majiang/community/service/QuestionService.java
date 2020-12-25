package life.majiang.community.service;

import life.majiang.community.dto.PageinationDTO;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class QuestionService {
    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    UserMapper userMapper;
    private Integer totalPage;

    //page 第几页 offset 当前问题起始
    public PageinationDTO list(Integer page, Integer size){
        // pages.clear();

        PageinationDTO pageinationDTO = new PageinationDTO();

        List<QuestionDTO> questionDTOList = new ArrayList<>();
        Integer totalCount = questionMapper.cout();
        page=pageinationDTO.setPagenation(totalCount,page,size);
        Integer offset=size*(page-1);
        List<Question> questions =questionMapper.list(offset,size);
        if (totalCount%size==0){
            totalPage=totalCount/size;

        }
        else {
            totalPage=totalCount/size+1;
        }
        for (Question q:questions
             ) {
            User user =userMapper.findById(q.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        pageinationDTO.setQuestions(questionDTOList);
        return pageinationDTO;
    }
    public PageinationDTO list(Integer id,Integer page,Integer size){
        PageinationDTO pageinationDTO = new PageinationDTO();

        List<QuestionDTO> questionDTOList = new ArrayList<>();
        Integer totalCount = questionMapper.countById(id);
        page=pageinationDTO.setPagenation(totalCount,page,size);
        Integer offset=size*(page-1);
        List<Question> questions =questionMapper.listByUserId(id,offset,size);
        if (totalCount%size==0){
            totalPage=totalCount/size;

        }
        else {
            totalPage=totalCount/size+1;
        }
        for (Question q:questions
        ) {
            User user =userMapper.findById(q.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        pageinationDTO.setQuestions(questionDTOList);
        return pageinationDTO;
    }

    public QuestionDTO getById(Integer id) {
       // User user =userMapper.findById(id);
        Question question =questionMapper.getById(id);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user =userMapper.findById(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;

    }

    public void createOrUpdate(Question question) {

        if(question.getId()!=null){
            //update
            question.setGmtModified(System.currentTimeMillis());

            questionMapper.update(question);
        }
        else{
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            //create
            questionMapper.create(question);
        }
    }
}
