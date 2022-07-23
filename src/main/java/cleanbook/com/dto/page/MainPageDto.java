package cleanbook.com.dto.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MainPageDto {
    private PageDto pageDto;
    private List<String> imgUrlList;
}
