package lexer;

public enum TOKENS {
    TOK_INT,
    TOK_FLOAT,
    TOK_ADDITIVE_OP,
    TOK_MULTIPLICATIVE_OP,
    TOK_RELATIONAL_OP,
    TOK_EQUALS,
    TOK_LET,
    TOK_SET,
    TOK_DEF,
    TOK_RETURN,
    TOK_IF,
    TOK_ELSE,
    TOK_FOR,
    TOK_WHILE,
    TOK_PRINT,
    TOK_INT_TYPE,
    TOK_FLOAT_TYPE,
    TOK_BOOL_TYPE,
    TOK_STRING_TYPE,
    TOK_BOOL,
    TOK_NOT,
    TOK_IDENTIFIER,
    TOK_COMMENT,
    TOK_STRING,
    TOK_LEFT_CURLY,
    TOK_RIGHT_CURLY,
    TOK_LEFT_BRACKET,
    TOK_RIGHT_BRACKET,
    TOK_COMMA,
    TOK_SEMICOLON,
    TOK_COLON,
    TOK_EOF,
    TOK_ERROR;

    public static final int SIZE = java.lang.Integer.SIZE;

    public static TOKENS forValue(int value) {
        return values()[value];
    }

    public int getValue() {
        return this.ordinal();
    }
}