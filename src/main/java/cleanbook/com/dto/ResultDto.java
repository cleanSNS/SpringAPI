package cleanbook.com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResultDto<T> {
    private T data;
    private Long startId;

    public ResultDto(T data) {
        this.data = data;
    }

    public ResultDto(T data, Long startId) {
        this.data = data;
        this.startId = startId;
    }
}
