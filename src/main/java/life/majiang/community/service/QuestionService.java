package life.majiang.community.service;

import life.majiang.community.dto.PageinationDTO;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.exception.CustomizeErrorCode;

import life.majiang.community.exception.CustomizeException;
import life.majiang.community.mapper.QuestionExtMapper;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.CustomerExample;
import life.majiang.community.model.Question;
import life.majiang.community.model.QuestionExample;
import life.majiang.community.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class QuestionService {
    @Autowired
    QuestionExtMapper questionExtMapper;
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
        Integer totalCount = (int)questionMapper.countByExample(new QuestionExample());
        page=pageinationDTO.setPagenation(totalCount,page,size);
        Integer offset=size*(page-1);
        List<Question> questions =questionMapper.selectByExampleWithRowbounds(new QuestionExample(),new RowBounds(offset,size));
        if (totalCount%size==0){
            totalPage=totalCount/size;

        }
        else {
            totalPage=totalCount/size+1;
        }
        for (Question q:questions
             ) {
            User user =userMapper.selectByPrimaryKey(q.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        pageinationDTO.setQuestions(questionDTOList);
        return pageinationDTO;
    }
    public PageinationDTO list(Long id,Integer page,Integer size){
        PageinationDTO pageinationDTO = new PageinationDTO();

        List<QuestionDTO> questionDTOList = new ArrayList<>();

        QuestionExample questionExample=new QuestionExample();
        questionExample.createCriteria().andIdEqualTo(id);
        Integer totalCount = (int)questionMapper.countByExample(questionExample);
        page=pageinationDTO.setPagenation(totalCount,page,size);
        Integer offset=size*(page-1);
        List<Question> questions =questionMapper.selectByExampleWithRowbounds(questionExample,new RowBounds(offset,size));
        if (totalCount%size==0){
            totalPage=totalCount/size;

        }
        else {
            totalPage=totalCount/size+1;
        }
        for (Question q:questions
        ) {
            User user =userMapper.selectByPrimaryKey(q.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        pageinationDTO.setQuestions(questionDTOList);
        return pageinationDTO;
    }

    public QuestionDTO getById(Long id) {
       // User user =userMapper.findById(id);
        Question question =questionMapper.selectByPrimaryKey(id);
        QuestionDTO questionDTO = new QuestionDTO();
        if(question==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        BeanUtils.copyProperties(question,questionDTO);
        User user =userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;

    }

    public void createOrUpdate(Question question) {

        if(question.getId()!=null){
            //update

            Question questionUpdate=new Question();
            questionUpdate.setGmtModified(System.currentTimeMillis());
            questionUpdate.setTitle(question.getTitle());
            questionUpdate.setDescription(question.getDescription());
            questionUpdate.setTag(question.getTag());
            QuestionExample questionExample=new QuestionExample();
            questionExample.createCriteria().andIdEqualTo(question.getId());
            int updated=questionMapper.updateByExampleSelective(questionUpdate,questionExample);
            if(updated!=1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
        else{
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setCommentCount(0);
            question.setLikeCount(0);
            question.setViewCount(0);
            //create
            questionMapper.insert(question);
        }
    }

    public void incView(Long id) {
//        QuestionExample questionExample=new QuestionExample();
//        questionExample.createCriteria().andIdEqualTo(id);
//        Question question =new Question();
//
//        Question question1=questionMapper.selectByPrimaryKey(id);
//        //从数据库取值
//        //在取值的基础上 做变化写回去 在并发时会出问题 一般在数据库的基础上加减
//        question.setViewCount(question1.getViewCount()+1);//也就是这个+1操作是在sql语句上执行
//        questionMapper.updateByExampleSelective(question,questionExample);
        Question question=new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }
}
