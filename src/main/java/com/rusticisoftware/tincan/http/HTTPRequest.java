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

import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.io.Buffer;
import org.eclipse.jetty.io.ByteArrayBuffer;

import java.io.IOException;
import java.util.Map;

/**
 * HTTPRequest Class Description
 */
public class HTTPRequest {
    private String method;
    private String resource;
    private Map<String, String> queryParams;
    private Map<String, String> headers;
    private String contentType;
    private ByteArrayBuffer contentBuffer;
    private byte[] content;
    private HTTPResponse response;

	public HTTPRequest() {
	}
	

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public Map<String, String> getQueryParams() {
		return queryParams;
	}

	public void setQueryParams(Map<String, String> queryParams) {
		this.queryParams = queryParams;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public ByteArrayBuffer getContentBuffer() {
		return contentBuffer;
	}

	public void setContentBuffer(ByteArrayBuffer contentBuffer) {
		this.contentBuffer = contentBuffer;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public HTTPResponse getResponse() {
		return response;
	}

	public void setResponse(HTTPResponse response) {
		this.response = response;
	}
}
