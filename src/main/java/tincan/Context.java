package tincan;

import lombok.Data;

import java.util.HashMap;
import java.util.UUID;

/**
 * Context Class Description
 */
@Data
public class Context {
    private UUID registration;
    private Agent instructor;
    private Agent team;
    private HashMap contextActivities;
    private HashMap revision;
    private String platform;
    private String language;
    private SubStatement statement;
    private Extensions extensions;
}
