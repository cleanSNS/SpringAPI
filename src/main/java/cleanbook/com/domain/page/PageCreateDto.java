package cleanbook.com.domain.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageCreateDto {
    private String title;
    private String content;
    private PageSetting pageSetting;
    private List<String> imgUrlList = new ArrayList<>();
    private List<String> pageHashtagList = new ArrayList<>();

    public PageCreateDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}