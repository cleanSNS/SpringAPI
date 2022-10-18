package cleanbook.com.dto.chat;

import cleanbook.com.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatroomNameAndUserDto {
    private String name;
    private List<UserDto> userDto;
}
