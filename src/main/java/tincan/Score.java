package tincan;

import lombok.Data;

/**
 * Score model class
 */
@Data
public class Score {
    private String scaled;
    private String raw;
    private String min;
    private String max;
}
