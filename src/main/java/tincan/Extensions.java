package tincan;

import com.fasterxml.jackson.databind.JsonNode;
import java.net.MalformedURLException;
import tincan.json.JSONHashMapURLToObject;
import tincan.json.Mapper;
import tincan.json.StringOfJSON;

/**
 * Extensions model class
 */
public class Extensions extends JSONHashMapURLToObject {
    public Extensions() {
        super();
    }

    public Extensions(JsonNode jsonNode) throws MalformedURLException {
        super(jsonNode);
    }
}
