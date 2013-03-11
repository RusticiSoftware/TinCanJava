package tincan;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * JSONMapper Class provides access to a Jackson ObjectMapper singleton
 */
public class JSONMapper {
    private static class LazyHolder {
        private static final ObjectMapper INSTANCE = new ObjectMapper();
    }

    public static ObjectMapper getInstance() {
        return LazyHolder.INSTANCE;
    }
}
