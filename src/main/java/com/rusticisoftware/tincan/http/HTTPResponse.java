package com.rusticisoftware.tincan.http;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashMap;

/**
 * HTTPResponse Class Description
 */
@Data
@NoArgsConstructor
public class HTTPResponse {
    private int status;
    private String statusMsg;
    private final HashMap<String,String> headers = new HashMap<String, String>();
    private byte[] contentBytes;

    public String getHeader(String key) {
        return this.headers.get(key);
    }
    public void setHeader(String key, String val) {
        this.headers.put(key, val);
    }

    public String getContent() {
        return new String(this.getContentBytes());
    }
    public Boolean isBinary() {
        String contentType = this.getHeader("Content-Type");
        if (contentType == null) {
            return false;
        }
        contentType = contentType.toLowerCase();

        if (contentType.contains("json") || contentType.contains("xml") || contentType.contains("text")) {
            return false;
        }

        return true;
    }
}
