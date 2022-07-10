package cleanbook.com.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultDto<T> {
    private T data;
    private int pageNumber;
    private Long startPageId;

    public ResultDto(T data, int pageNumber) {
        this.data = data;
        this.pageNumber = pageNumber;
    }

    public ResultDto(T data, Long startPageId) {
        this.data = data;
        this.startPageId = startPageId;
    }
}
