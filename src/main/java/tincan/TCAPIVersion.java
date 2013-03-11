package tincan;

/**
 * Description
 */
public enum TCAPIVersion {
    //V100("1.0"),
    V095("0.95"),
    V090("0.90");

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
}
