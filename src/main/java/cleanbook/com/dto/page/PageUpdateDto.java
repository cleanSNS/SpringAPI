package cleanbook.com.dto.page;

import cleanbook.com.entity.page.PageSetting;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageUpdateDto {

    @NotBlank
    private String content;
    private PageSetting pageSetting;
    private List<String> imgUrlList = new ArrayList<>();
    private List<String> pageHashtagList = new ArrayList<>();

    public PageUpdateDto(String content) {
        this.content = content;
    }
}
