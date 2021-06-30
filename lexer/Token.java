package lexer;

//import lexer.*;

public class Token {

	// C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method
	// could not be found:
	// Token();
	
	public Token(){
		
	}

	public Token(int final_state, String value) {
		this(final_state, value, 0);
	}

	public Token(int final_state, String value, int line_number) {
		this.type = determine_token_type(final_state, value);
		this.value = value;
		this.line_number = line_number;
	}

	public TOKENS type;
	public String value;
	public int line_number;

	private TOKENS determine_token_type(int final_state, String value) {
		switch (final_state) {
			case 1:
				return TOKENS.TOK_INT;

			case 3:
				return TOKENS.TOK_FLOAT;

			case 4:
				return TOKENS.TOK_ADDITIVE_OP;

			case 5:
			case 11:
				return TOKENS.TOK_MULTIPLICATIVE_OP;

			case 7:
				return TOKENS.TOK_RELATIONAL_OP;

			case 8:
				return TOKENS.TOK_EQUALS;

			case 9:
				return TOKENS.TOK_RELATIONAL_OP;

			case 10:
				if (value.equals("let")) {
					return TOKENS.TOK_LET;
				}
				if (value.equals("set")) {
					return TOKENS.TOK_SET;
				}

				// TODO: remove this state and replace with tealang function decleration
				if (value.equals("def")) {
					return TOKENS.TOK_DEF;
				}
				if (value.equals("return")) {
					return TOKENS.TOK_RETURN;
				}
				if (value.equals("if")) {
					return TOKENS.TOK_IF;
				}
				if (value.equals("else")) {
					return TOKENS.TOK_ELSE;
				}
				if (value.equals("for")) {
					return TOKENS.TOK_FOR;
				}
				if (value.equals("while")) {
					return TOKENS.TOK_WHILE;
				}
				if (value.equals("print")) {
					return TOKENS.TOK_PRINT;
				}
				if (value.equals("int")) {
					return TOKENS.TOK_INT_TYPE;
				}
				if (value.equals("float")) {
					return TOKENS.TOK_FLOAT_TYPE;
				}
				if (value.equals("bool")) {
					return TOKENS.TOK_BOOL_TYPE;
				}
				if (value.equals("string")) {
					return TOKENS.TOK_STRING_TYPE;
				}
				if (value.equals("true") || value.equals("false")) {
					return TOKENS.TOK_BOOL;
				}
				if (value.equals("and")) {
					return TOKENS.TOK_MULTIPLICATIVE_OP;
				}
				if (value.equals("or")) {
					return TOKENS.TOK_ADDITIVE_OP;
				}
				if (value.equals("not")) {
					return TOKENS.TOK_NOT;
				}
				return TOKENS.TOK_IDENTIFIER;
				
			case 14:
			case 16:
				return TOKENS.TOK_COMMENT;

			case 20:
				return TOKENS.TOK_STRING;

			case 21:
				if (value.equals("{"))
					return TOKENS.TOK_LEFT_CURLY;
				if (value.equals("}"))
					return TOKENS.TOK_RIGHT_CURLY;
				if (value.equals("("))
					return TOKENS.TOK_LEFT_BRACKET;
				if (value.equals(")"))
					return TOKENS.TOK_RIGHT_BRACKET;
				if (value.equals(","))
					return TOKENS.TOK_COMMA;
				if (value.equals(";"))
					return TOKENS.TOK_SEMICOLON;
				if (value.equals(":"))
					return TOKENS.TOK_COLON;

			case 22:
				return TOKENS.TOK_EOF;

			default:
				return TOKENS.TOK_ERROR;
		}
	}
}