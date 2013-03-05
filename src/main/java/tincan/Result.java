package tincan;

import lombok.Data;
import org.joda.time.Duration;

/**
 * Result Model class
 */
@Data
public class Result {
    private Score score;
    private Boolean success;
    private Boolean completion;
    private Duration duration;
    private String response;
    private Extensions extensions;
}
