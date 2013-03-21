package com.rusticisoftware.tincan;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Statement target interface for instances stored in the "object" property of a Statement
 */
public interface StatementTarget {
    String getObjectType();

    JsonNode toJSONNode(TCAPIVersion version);
}
