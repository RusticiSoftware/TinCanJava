package com.rusticisoftware.tincan;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.rusticisoftware.tincan.json.JSONBase;
import com.rusticisoftware.tincan.json.Mapper;
import com.rusticisoftware.tincan.json.StringOfJSON;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Statements result model class, returned by LRS calls to get multiple statements
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class StatementsResult extends JSONBase {
    private ArrayList<Statement> statements = new ArrayList<Statement>();
    private String moreURL;

    public StatementsResult(JsonNode jsonNode) throws MalformedURLException {
        this();

        JsonNode statementsNode = jsonNode.path("statements");
        if (! statementsNode.isMissingNode()) {
            Iterator it = statementsNode.elements();
            while(it.hasNext()) {
                this.statements.add(new Statement((JsonNode) it.next()));
            }
        }

        JsonNode moreURLNode = jsonNode.path("more");
        if (! moreURLNode.isMissingNode()) {
            this.setMoreURL(moreURLNode.textValue());
        }
    }

    public StatementsResult(StringOfJSON json) throws IOException {
        this(json.toJSONNode());
    }

    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = Mapper.getInstance().createObjectNode();
        node.put("more", this.getMoreURL());

        return node;
    }
}
