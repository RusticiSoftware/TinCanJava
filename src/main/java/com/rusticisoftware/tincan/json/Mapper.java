package com.rusticisoftware.tincan.json;

import com.fasterxml.jackson.databind.ObjectMapper;

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
}
