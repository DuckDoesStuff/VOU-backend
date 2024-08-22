package MCService.dto.stream;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Question {
    String question;
    String[] answer;
    int correctAnswer;
}
