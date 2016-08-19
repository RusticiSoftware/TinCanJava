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
package com.rusticisoftware.tincan.http;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.HashMap;
import java.util.List;

/**
 * HTTPResponse Class Description
 */
@Data
@NoArgsConstructor
public class HTTPResponse {
    private int status;
    private String statusMsg;
    private final HashMap<String, String> headers = new HashMap<String, String>();
    private byte[] contentBytes;
    private List<HTTPPart> partList;
    private HashMap<String, byte[]> attachments = new HashMap<String, byte[]>();

    public String getHeader(String key) {
        return this.headers.get(key);
    }
    public void setHeader(String key, String val) {
        this.headers.put(key, val);
    }

    public byte[] getAttachment(String key) { return this.attachments.get(key); }
    public void setAttachment(String key, byte[] val) { this.attachments.put(key, val); }

    public String getContent() {
        return new String(this.getContentBytes());
    }

    public String getContentType() { return this.getHeader("Content-Type"); }
    public String getEtag() {
        String etag = this.getHeader("ETag");
        if (etag == null) {
            return etag;
        }

        return etag.toLowerCase();
    }
    public DateTime getLastModified() {
        DateTimeFormatter RFC1123_DATE_TIME_FORMATTER =
                DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'").withZoneUTC();
        try {
            return DateTime.parse(this.getHeader("Last-Modified"), RFC1123_DATE_TIME_FORMATTER);
        }
        catch (Exception parseException) {
            return null;
        }
    }

    public Boolean isBinary() {
        String contentType = this.getContentType();
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
