package life.majiang.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class PaginationDTO<T> {
    private List<T> data;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page;
    private List<Integer> pages=new ArrayList<>();
    private Integer totalPage;
    //page第几页 totolcount 一共几个问题
    public int setPagenation(Integer totalCount, Integer page, Integer size) {
        //一共几页
        if (page<1){
            page=1;
        }

       // pages.clear();
        if (totalCount%size==0){
            totalPage=totalCount/size;

        }
        else {
            totalPage=totalCount/size+1;
        }
        if(page>totalPage){
            page=totalPage;
        }
        this.page=page;

        pages.add(page);
        for(int i =1;i<4;i++){
            if(page-i>0){
                pages.add(0,page-i);
            }
            if(page+i<=totalPage){
                pages.add(page+i);
            }
        }


        //是否显示previous 只有1 和最后一页不显示
        if (page==1){
            showPrevious=false;
        }
        else{
            showPrevious=true;
        }
        if(page==totalPage){
            showNext=false;
        }else{showNext=true;}
        if(pages.contains(1)){
            showFirstPage=false;
        }
        else{
            showFirstPage=true;

        }
        if (pages.contains(totalPage)){
            showEndPage=false;
        }
        else{
            showEndPage=true;
        }
        return page;
    }
}
