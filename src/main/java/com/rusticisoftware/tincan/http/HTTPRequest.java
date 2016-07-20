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

import com.rusticisoftware.tincan.Attachment;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.servlet.http.Part;

import java.util.List;
import java.util.Map;

/**
 * HTTPRequest Class Description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class HTTPRequest {
    private String method;
    private String resource;
    private Map<String, String> queryParams;
    private Map<String, String> headers;
    private String contentType;
    private byte[] content;
    private HTTPResponse response;
    private List<HTTPPart> partList;
}
