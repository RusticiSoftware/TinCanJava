package tincan;

import lombok.Data;
import java.util.UUID;

/**
 * StatementRef Class used when referencing another statement from a statement's
 * object property
 */
@Data
public class StatementRef implements StatementTarget {
    private final String objectType = "StatementRef";
    private UUID id;
}
