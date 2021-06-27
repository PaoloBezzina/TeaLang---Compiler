package lexer;

public enum TRANSITION_TYPE {
    DIGIT(0),
    PERIOD(1),
    ADDITIVE_OP(2),
    ASTERISK(3),
    EXCL_MARK(4),
    ORDER_REL(5),
    EQUALS(6),
    UNDERSCORE(7),
    FORWARDSLASH(8),
    BACKSLASH(9),
    QUOTATION_MARK(10),
    PUNCTUATION(11),
    NEWLINE(12),
    ENDOFFILE(13),
    LETTER(14),
    PRINTABLE(15),
    OTHER(16);

    public static final int SIZE = java.lang.Integer.SIZE;
    private static java.util.HashMap<Integer, TRANSITION_TYPE> mappings;
    private int intValue;

    private TRANSITION_TYPE(int value) {
        intValue = value;
        getMappings().put(value, this);
    }

    private static java.util.HashMap<Integer, TRANSITION_TYPE> getMappings() {
        if (mappings == null) {
            synchronized (TRANSITION_TYPE.class) {
                if (mappings == null) {
                    mappings = new java.util.HashMap<Integer, TRANSITION_TYPE>();
                }
            }
        }
        return mappings;
    }

    public static TRANSITION_TYPE forValue(int value) {
        return getMappings().get(value);
    }

    public int getValue() {
        return intValue;
    }
}