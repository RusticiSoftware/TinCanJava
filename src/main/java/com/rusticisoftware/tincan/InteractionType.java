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

import com.rusticisoftware.tincan.exceptions.UnrecognizedInteractionType;

/**
 * Possible interaction types
 */
public enum InteractionType {
    CHOICE("choice"),
    SEQUENCING("sequencing"),
    LIKERT("likert"),
    MATCHING("matching"),
    PERFORMANCE("performance"),
    TRUE_FALSE("true-false"),
    FILL_IN("fill-in"),
    LONG_FILL_IN("long-fill-in"),
    NUMERIC("numeric"),
    OTHER("other");


    /**
     * @param text
     */
    private InteractionType(final String text) {
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

    public static InteractionType getByString(String type) throws UnrecognizedInteractionType {
        // TODO: cache this in a map to only do it once?
        for(InteractionType it : InteractionType.values()) {
            if (type.equals(it.toString())) {
                return it;
            }
        }
        throw new UnrecognizedInteractionType(type);
    }
}
