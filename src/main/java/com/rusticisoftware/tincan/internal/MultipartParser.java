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
    private byte[] boundary;
    private byte[] finalBoundary;
    private byte[] content;
    private byte[] responseContent;
    private HashMap<String, String> headers = new HashMap<String, String>();
    private ArrayList<byte[]> sections = new ArrayList<byte[]>();
    private final byte[] CONTENT_DELIMITER = ("\r\n\r\n").getBytes();

    public MultipartParser(byte[] responseContent, String boundary) throws IOException {
        this.responseContent = responseContent;
        this.boundary = ("--" + boundary).getBytes();
        this.finalBoundary = ("--" + boundary + "--").getBytes();
        splitIntoParts();
    }

    /**
     * Code to find the index of a particular byte[] inside a larger byte[]
     * `indexOf` and `computeFailure` taken from StackOverflow user janko. Original response can be found at this link:
     * https://stackoverflow.com/questions/1507780/searching-for-a-sequence-of-bytes-in-a-binary-file-with-java
     */
    private int indexOf(byte[] data, byte[] pattern) {
        int[] failure = computeFailure(pattern);

        int j = 0;
        if (data.length == 0) return -1;

        for (int i = 0; i < data.length; i++) {
            while (j > 0 && pattern[j] != data[i]) {
                j = failure[j - 1];
            }
            if (pattern[j] == data[i]) { j++; }
            if (j == pattern.length) {
                return i - pattern.length + 1;
            }
        }
        return -1;
    }

    /**
     * Computes the failure function using a boot-strapping process,
     * where the pattern is matched against itself.
     */
    private int[] computeFailure(byte[] pattern) {
        int[] failure = new int[pattern.length];

        int j = 0;
        for (int i = 1; i < pattern.length; i++) {
            while (j > 0 && pattern[j] != pattern[i]) {
                j = failure[j - 1];
            }
            if (pattern[j] == pattern[i]) {
                j++;
            }
            failure[i] = j;
        }

        return failure;
    }

    private void splitIntoParts() {
        byte[] beforeFinalBoundary = Arrays.copyOfRange(responseContent, 0, indexOf(responseContent, finalBoundary));

        int index;
        while ((index = indexOf(beforeFinalBoundary, boundary)) >= 0) {
            sections.add(Arrays.copyOfRange(beforeFinalBoundary, 0, index));
            beforeFinalBoundary = Arrays.copyOfRange(beforeFinalBoundary, index + this.boundary.length, beforeFinalBoundary.length);
        }
        while (beforeFinalBoundary[beforeFinalBoundary.length - 1] == (byte) '\n' || beforeFinalBoundary[beforeFinalBoundary.length - 1] == (byte) '\r') {
            beforeFinalBoundary = Arrays.copyOfRange(beforeFinalBoundary, 0, beforeFinalBoundary.length - 1);
        }
        sections.add(beforeFinalBoundary);
    }

    private ArrayList<byte[]> splitDelimited(byte[] original, byte[] delimiter) {
        ArrayList<byte[]> parts = new ArrayList<byte[]>();

        int index;
        while ((index = indexOf(original, delimiter)) >= 0) {
            parts.add(Arrays.copyOfRange(original, 0, index));
            original = Arrays.copyOfRange(original, index + delimiter.length, original.length);
        }
        while (original[original.length - 1] == (byte) '\n' || original[original.length - 1] == (byte) '\r') {
            original = Arrays.copyOfRange(original, 0, original.length - 1);
        }
        parts.add(original);

        return parts;
    }

    public void parsePart(int i) throws IOException {
        ArrayList<byte[]> sectionParts = splitDelimited(sections.get(i), CONTENT_DELIMITER);
        Scanner scanner = new Scanner(new String(sectionParts.get(0)));

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

        if (headers.get("Content-Type").contains("application/json")) {
            scanner = new Scanner(new String(sectionParts.get(1)));

            // Read in the content
            String contentString = "";
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).equals(boundary)) {
                contentString += line;
            }
            content = contentString.getBytes();
        }
        else {
            content = sectionParts.get(1);
        }
    }
}