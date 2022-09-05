package cleanbook.com.dto.user;

import cleanbook.com.entity.enums.ReportType;
import lombok.Data;

@Data
public class ReportDto {
    private Long targetId;
    private ReportType type;
}
