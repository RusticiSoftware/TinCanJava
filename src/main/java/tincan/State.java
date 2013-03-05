package tincan;

import lombok.Data;
import org.joda.time.DateTime;

/**
 * State Class
 */
@Data
public class State {
    private String id;
    private DateTime updated;
    private String contents;
}
