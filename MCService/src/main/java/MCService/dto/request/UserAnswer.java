package MCService.dto.request;

import lombok.Data;

@Data
public class UserAnswer {
    String roomID;
    String userID;
    int questionId;
    int answer;
}
