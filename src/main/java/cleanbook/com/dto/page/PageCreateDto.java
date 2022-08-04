package cleanbook.com.dto.page;

import cleanbook.com.entity.page.PageSetting;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageCreateDto {
    private String content;
    private PageSetting pageSetting;
    private List<String> imgUrlList = new ArrayList<>();
    private List<String> pageHashtagList = new ArrayList<>();

    public PageCreateDto(String content) {
        this.content = content;
    }

    public PageCreateDto(String content, List<String> imgUrlList) {
        this.content = content;
        this.imgUrlList = imgUrlList;
    }

    public PageCreateDto(String content, List<String> imgUrlList, List<String> pageHashtagList) {
        this.content = content;
        this.imgUrlList = imgUrlList;
        this.pageHashtagList = pageHashtagList;
    }
}