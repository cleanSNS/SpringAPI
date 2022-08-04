package cleanbook.com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultDto<T> {
    private T data;
    private int pageNumber;
    private Long startId;

    public ResultDto(T data) {
        this.data = data;
    }

    public ResultDto(T data, int pageNumber) {
        this.data = data;
        this.pageNumber = pageNumber;
    }

    public ResultDto(T data, Long startId) {
        this.data = data;
        this.startId = startId;
    }
}
