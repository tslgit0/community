package life.majiang.community.dto;

import lombok.Data;

import java.util.List;
@Data
public class TagDTO {
    private List<String> tags;
    private String CategoryName;
}
