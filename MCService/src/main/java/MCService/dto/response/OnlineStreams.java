package MCService.dto.response;

import lombok.Builder;
import org.springframework.beans.factory.annotation.Value;

import java.util.Set;

@Builder
public class OnlineStreams {
    @Value("${rtmp.server}")
    private String server;
    private Set<String> rooms;
}
