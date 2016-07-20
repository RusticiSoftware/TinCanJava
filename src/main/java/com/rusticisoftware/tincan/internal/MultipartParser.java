/*
    Copyright 2016 Rustici Software

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
package com.rusticisoftware.tincan.internal;

import java.io.*;
import java.util.*;
import lombok.Data;

@Data
public class MultipartParser {
    private InputStream in;
    private BufferedReader reader;
    private String boundary = "";
    private String finalBoundary = "";
    private HashMap<String, String> headers = new HashMap<String, String>();
    private byte[] content;
    public boolean noMoreParts = false;

    public MultipartParser(InputStream _in) throws IOException {
        this.in = _in;
        this.reader = new BufferedReader(new InputStreamReader(this.in));
        this.boundary = this.findBoundary();
        this.finalBoundary = boundary + "--";
    }

    public void nextPart() throws IOException {
        String line;
        while ((line = reader.readLine().trim()).length() > 0) {
            String[] parts = line.split(":");
            headers.put(parts[0].trim(), parts[1].trim());
        }

        String contentString = "";
        while (! (line = reader.readLine()).equals(boundary)) {
            if (line.equals(finalBoundary)) {
                noMoreParts = true;
                break;
            }
            contentString += line;
        }

        content = contentString.getBytes();
    }

    private String findBoundary() throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("--")) {
                return line;
            }
        }

        return "";
    }
}