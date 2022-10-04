package cleanbook.com.dto.page;

import cleanbook.com.entity.page.PageSetting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageCreateDto {

    @NotBlank
    private String content;
    @Builder.Default
    private PageSetting pageSetting = PageSetting.builder().build();
    @Builder.Default
    private List<String> imgUrlList = new ArrayList<>();
    @Builder.Default
    private List<String> pageHashtagList = new ArrayList<>();

    @Builder
    public PageCreateDto(String content, PageSetting pageSetting) {
        this.content = content;
        this.pageSetting = pageSetting;
    }

    @Builder
    public PageCreateDto(String content, List<String> pageHashtagList) {
        this.content = content;
        this.pageHashtagList = pageHashtagList;
    }
}