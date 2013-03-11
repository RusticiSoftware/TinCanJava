package tincan;

import org.codehaus.jackson.JsonNode;

/**
 * Statement target interface for instances stored in the "object" property of a Statement
 */
public interface StatementTarget {
    String getObjectType();

    JsonNode toJSONNode(TCAPIVersion version);
}
