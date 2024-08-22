package MCService.dto.socket;

import lombok.Data;

@Data
public class UserAnswer {
    String userID;
    int questionId;
    int answer;
}
