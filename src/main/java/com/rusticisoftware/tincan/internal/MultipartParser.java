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
import org.eclipse.jetty.util.IO;

@Data
public class MultipartParser {
    private Scanner scanner;
    private String boundary = "";
    private HashMap<String, String> headers = new HashMap<String, String>();
    private byte[] content;
    private String contentString;
    public boolean noMoreParts = false;

    public MultipartParser(String contentString, String boundary) throws IOException {
        this.contentString = contentString;
        this.scanner = new Scanner(contentString);
        this.boundary = "--" + boundary;
    }

    public void nextPart() throws IOException {
        String line = scanner.nextLine();

        // Check if the first line is "\r\n" or one of the headers
        if (line.trim().length() > 0) {
            if (line.contains(":")) {
                String[] parts = line.split(":");
                headers.put(parts[0].trim(), parts[1].trim());
            }
        }

        // Populate the headers
        while (scanner.hasNextLine() && (line = scanner.nextLine()).trim().length() > 0) {
            if (line.contains(":")) {
                String[] parts = line.split(":");

                if (! parts[1].equals(null)) {
                    headers.put(parts[0].trim(), parts[1].trim());
                }
                else {
                    if ((line = scanner.nextLine()).charAt(0) == '\t') {
                        headers.put(parts[0].trim(), line.trim());
                    }
                }
            }
        }

        // Read in the content
        String contentString = "";
        while (scanner.hasNextLine() && !(line = scanner.nextLine()).equals(boundary)) {
            if (line.equals(boundary + "--")) {
                noMoreParts = true;
                break;
            }
            contentString += line;
        }

        content = contentString.getBytes();
    }
}