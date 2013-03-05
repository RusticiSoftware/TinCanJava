package tincan;

import lombok.Data;

/**
 * Activity model class
 */
@Data
public class Activity {
    private final String objectType = "Activity";

    private String id;
    private ActivityDefinition definition;
}
