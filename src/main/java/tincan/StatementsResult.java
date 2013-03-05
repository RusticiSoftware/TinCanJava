package tincan;

import lombok.Data;

/**
 * Statements result model class, returned by LRS calls to get multiple statements
 */
@Data
public class StatementsResult {
    private Statement[] statements;
    private String moreURL;
}
