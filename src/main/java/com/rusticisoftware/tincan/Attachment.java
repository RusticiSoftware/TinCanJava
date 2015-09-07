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

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rusticisoftware.tincan.json.JSONBase;
import com.rusticisoftware.tincan.json.Mapper;


/**
 * Attachment Class
 */
public class Attachment extends JSONBase {
    private URI usageType;
    private LanguageMap display;
    private LanguageMap description;
    private String contentType;
    private Integer length;
    private String sha2;
    private URL fileUrl;

	public Attachment() {
	}
	
    
    public Attachment(JsonNode jsonNode) throws URISyntaxException, MalformedURLException {
        JsonNode usageTypeNode = jsonNode.path("usageType");
        if (! usageTypeNode.isMissingNode()) {
            this.setUsageType(new URI(usageTypeNode.textValue()));
        }
        
        JsonNode displayNode = jsonNode.path("display");
        if (! displayNode.isMissingNode()) {
            this.setDisplay(new LanguageMap(displayNode));
        }
        
        JsonNode descriptionNode = jsonNode.path("description");
        if (! descriptionNode.isMissingNode()) {
            this.setDescription(new LanguageMap(descriptionNode));
        }
        
        JsonNode contentTypeNode = jsonNode.path("contentType");
        if (! contentTypeNode.isMissingNode()) {
            this.setContentType(contentTypeNode.textValue());
        }
        
        JsonNode lengthNode = jsonNode.path("length");
        if (! lengthNode.isMissingNode()) {
            this.setLength(lengthNode.intValue());
        }
        
        JsonNode sha2Node = jsonNode.path("sha2");
        if (! sha2Node.isMissingNode()) {
            this.setSha2(sha2Node.textValue());
        }
        
        JsonNode fileUrlNode = jsonNode.path("fileUrl");
        if (! fileUrlNode.isMissingNode()) {
            this.setFileUrl(new URL(fileUrlNode.textValue()));
        }
    }
    
    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = Mapper.getInstance().createObjectNode();
        if (this.getUsageType() != null) {
            node.put("usageType", this.getUsageType().toString());
        }
        if (this.getDisplay() != null) {
            node.put("display", this.getDisplay().toJSONNode(version));
        }
        if (this.getDescription() != null) {
            node.put("description", this.getDescription().toJSONNode(version));
        }
        if (this.getContentType() != null) {
            node.put("contentType", this.getContentType());
        }
        if (this.getLength() != null) {
            node.put("length", this.getLength());
        }
        if (this.getSha2() != null) {
            node.put("sha2", this.getSha2());
        }
        if (this.getFileUrl() != null) {
            node.put("fileUrl", this.getFileUrl().toString());
        }
        return node;
    }

	public URI getUsageType() {
		return usageType;
	}

	public void setUsageType(URI usageType) {
		this.usageType = usageType;
	}

	public LanguageMap getDisplay() {
		return display;
	}

	public void setDisplay(LanguageMap display) {
		this.display = display;
	}

	public LanguageMap getDescription() {
		return description;
	}

	public void setDescription(LanguageMap description) {
		this.description = description;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public String getSha2() {
		return sha2;
	}

	public void setSha2(String sha2) {
		this.sha2 = sha2;
	}

	public URL getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(URL fileUrl) {
		this.fileUrl = fileUrl;
	}
}
