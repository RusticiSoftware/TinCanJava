package com.rusticisoftware.tincan.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * Mapper Class provides access to a Jackson ObjectMapper singleton
 */
public class Mapper {
    private static class LazyHolder {
        private static final ObjectMapper INSTANCE = new ObjectMapper();
    }

    public static ObjectMapper getInstance() {
        return LazyHolder.INSTANCE;
    }

    public static ObjectWriter getWriter(Boolean pretty) {
        ObjectMapper mapper = getInstance();

        ObjectWriter writer;
        if (pretty) {
            writer = mapper.writer().withDefaultPrettyPrinter();
        }
        else {
            writer = mapper.writer();
        }

        return writer;
    }
}
