package tincan;

import tincan.exceptions.UnrecognizedInteractionType;

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
