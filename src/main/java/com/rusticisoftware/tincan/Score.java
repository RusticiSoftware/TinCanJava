/*
    Copyright 2013 Rustici Software

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package com.rusticisoftware.tincan;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rusticisoftware.tincan.json.JSONBase;
import com.rusticisoftware.tincan.json.Mapper;

/**
 * Score model class
 */
public class Score extends JSONBase {
    private Double scaled;
    private Double raw;
    private Double min;
    private Double max;

	public Score() {
	}
	

    public Score(JsonNode jsonNode) {
        this();

        JsonNode scaledNode = jsonNode.path("scaled");
        if (! scaledNode.isMissingNode()) {
            this.setScaled(scaledNode.doubleValue());
        }

        JsonNode rawNode = jsonNode.path("raw");
        if (! rawNode.isMissingNode()) {
            this.setRaw(rawNode.doubleValue());
        }

        JsonNode minNode = jsonNode.path("min");
        if (! minNode.isMissingNode()) {
            this.setMin(minNode.doubleValue());
        }

        JsonNode maxNode = jsonNode.path("max");
        if (! maxNode.isMissingNode()) {
            this.setMax(maxNode.doubleValue());
        }
    }

    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = Mapper.getInstance().createObjectNode();

        if (this.getScaled() != null) {
            node.put("scaled", this.getScaled());
        }
        if (this.getRaw() != null) {
            node.put("raw", this.getRaw());
        }
        if (this.getMin() != null) {
            node.put("min", this.getMin());
        }
        if (this.getMax() != null) {
            node.put("max", this.getMax());
        }

        return node;
    }

	public Double getScaled() {
		return scaled;
	}

	public void setScaled(Double scaled) {
		this.scaled = scaled;
	}

	public Double getRaw() {
		return raw;
	}

	public void setRaw(Double raw) {
		this.raw = raw;
	}

	public Double getMin() {
		return min;
	}

	public void setMin(Double min) {
		this.min = min;
	}

	public Double getMax() {
		return max;
	}

	public void setMax(Double max) {
		this.max = max;
	}
}
