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

/**
 * Description
 */
public enum TCAPIVersion {
    V101("1.0.1"),
    V100("1.0.0"),
    V095("0.95");

    /**
     * @param text
     */
    private TCAPIVersion(final String text) {
        this.text = text;
    }

    private final String text;

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }

    public static TCAPIVersion latest() {
        return TCAPIVersion.values()[0];
    }
    
    public static TCAPIVersion fromString(String text) {
        if (text != null) {
            for (TCAPIVersion v : TCAPIVersion.values()) {
                if (text.equalsIgnoreCase(v.text)) {
                    return v;
                }
            }
        }
        return null;
    }
}
