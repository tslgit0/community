package life.majiang.community.enums;

import life.majiang.community.model.Question;

public enum CommentTypeEnum {
    QUESTION(1),
    COMMENT(2);
    private Integer type;

    public static boolean isExist(Integer type) {
        for (CommentTypeEnum c:CommentTypeEnum.values()
             ) {
            // == 有疑问 --没疑问了 type是Int 还以为是String
            if(type==c.getType()) return true;

        }
        return false;
    }

    public Integer getType() {
        return type;
    }
    CommentTypeEnum(Integer type){
        this.type=type;
    }
}
