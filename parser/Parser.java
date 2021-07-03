package parser;

import parser.*;
import lexer.*;
import java.util.*;

public class Parser {

	public Parser(lexer.Lexer lex) {
		this.lex = lex;
		current_token = lex.GetNextToken();
		GetNextToken = lex.GetNextToken();
	}

	public Parser(lexer.Lexer lex, int tokens) {
		this.lex = lex;
		GetNextToken = lex.GetNextToken();
		for (int i = 0; i < tokens; i++) {
			consume_token();
		}
	}

	public final ASTProgramNode parse_program() {

		var statements = new ArrayList<ASTNode>();

		while (current_token.type != lexer.TOKENS.TOK_EOF) {

			statements.add(parse_statement());
			consume_token();
		}

		return new ASTProgramNode(statements);
	}

	private lexer.Lexer lex;
	private lexer.Token current_token = new lexer.Token();
	private lexer.Token GetNextToken = new lexer.Token();

	private void consume_token() {
		current_token = GetNextToken;
		GetNextToken = lex.GetNextToken();
	}

	// Statement Nodes
	private ASTStatementNode parse_statement() {

		switch (current_token.type) {

			case TOK_LET:
				return parse_declaration_statement();

			case TOK_IDENTIFIER:
				return parse_assignment_statement();

			case TOK_PRINT:
				return parse_print_statement();

			case TOK_IF:
				return parse_if_statement();

			case TOK_WHILE:
				return parse_while_statement();

			case TOK_FOR:
				return parse_for_statement();

			case TOK_RETURN:
				return parse_return_statement();

			case TOK_BOOL_TYPE:
			case TOK_FLOAT_TYPE:
			case TOK_INT_TYPE:
			case TOK_STRING_TYPE:
				return parse_function_definition();

			case TOK_LEFT_CURLY:
				return parse_block();

			default:
				throw new RuntimeException("Invalid statement starting with '" + current_token.value
						+ "' encountered on line " + String.valueOf(current_token.line_number) + ".");
		}
	}

	public final ASTExprNode parse_expression() {
		ASTExprNode simple_expr = parse_simple_expression();
		int line_number = current_token.line_number;

		if (GetNextToken.type == lexer.TOKENS.TOK_RELATIONAL_OP) {
			consume_token();
			return new ASTBinaryExprNode(current_token.value, simple_expr, parse_expression(), line_number);
		}

		return simple_expr;
	}

	private ASTDeclarationNode parse_declaration_statement() {

		// Node attributes
		TYPE type;
		String identifier;
		ASTExprNode expr;
		int line_number;

		// Determine line number
		line_number = current_token.line_number;

		// Consume identifier
		consume_token();
		if (current_token.type != lexer.TOKENS.TOK_IDENTIFIER) {
			throw new RuntimeException(
					"Expected variable name after 'let' on line " + String.valueOf(current_token.line_number) + ".");
		}
		identifier = current_token.value;

		consume_token();
		if (current_token.type != lexer.TOKENS.TOK_COLON) {
			throw new RuntimeException(
					"Expected ':' after " + identifier + " on line " + String.valueOf(current_token.line_number) + ".");
		}

		consume_token();
		type = parse_type(identifier);

		consume_token();
		if (current_token.type != lexer.TOKENS.TOK_EQUALS) {
			throw new RuntimeException("Expected assignment operator '=' for " + identifier + " on line "
					+ String.valueOf(current_token.line_number) + ".");
		}

		// Parse the right hand side
		expr = parse_expression();

		consume_token();
		if (current_token.type != lexer.TOKENS.TOK_SEMICOLON) {
			throw new RuntimeException("Expected ';' after assignment of " + identifier + " on line "
					+ String.valueOf(current_token.line_number) + ".");
		}

		// Create ASTExpressionNode to return
		return new ASTDeclarationNode(type, identifier, expr, line_number);
	}

	private ASTAssignmentNode parse_assignment_statement() {

		// Node attributes
		String identifier;
		ASTExprNode expr;

		// Determine line number
		int line_number = current_token.line_number;

		// consume_token();
		if (current_token.type != lexer.TOKENS.TOK_IDENTIFIER) {
			throw new RuntimeException(
					"Expected variable name on line " + String.valueOf(current_token.line_number) + ".");
		}
		identifier = current_token.value;

		consume_token();
		if (current_token.type != lexer.TOKENS.TOK_EQUALS) {
			throw new RuntimeException("Expected assignment operator '=' after " + identifier + " on line "
					+ String.valueOf(current_token.line_number) + ".");
		}

		// Parse the right hand side
		expr = parse_expression();

		// lookahaead if next statment is end of for loop
		if (GetNextToken.type == lexer.TOKENS.TOK_RIGHT_BRACKET) {
			return new ASTAssignmentNode(identifier, expr, line_number);
		}

		consume_token();
		if (current_token.type != lexer.TOKENS.TOK_SEMICOLON) {
			throw new RuntimeException("Expected ';' after assignment of " + identifier + " on line "
					+ String.valueOf(current_token.line_number) + ".");
		}

		return new ASTAssignmentNode(identifier, expr, line_number);
	}

	private ASTPrintNode parse_print_statement() {

		// Determine line number
		int line_number = current_token.line_number;

		// Get expression to print
		ASTExprNode expr = parse_expression();

		// Consume ';' token
		consume_token();

		// Make sure it's a ';'
		if (current_token.type != lexer.TOKENS.TOK_SEMICOLON) {
			throw new RuntimeException(
					"Expected ';' after print statement on line " + String.valueOf(current_token.line_number) + ".");
		}

		// Return return node
		return new ASTPrintNode(expr, line_number);
	}

	private ASTReturnNode parse_return_statement() {

		// Determine line number
		int line_number = current_token.line_number;

		// Get expression to return
		ASTExprNode expr = parse_expression();

		// Consume ';' token
		consume_token();

		// Make sure it's a ';'
		if (current_token.type != lexer.TOKENS.TOK_SEMICOLON) {
			throw new RuntimeException(
					"Expected ';' after return statement on line " + String.valueOf(current_token.line_number) + ".");
		}

		// Return return node
		return new ASTReturnNode(expr, line_number);
	}

	public ASTBlockNode parse_block() {

		var statements = new ArrayList<ASTStatementNode>();

		// Determine line number

		int line_number = current_token.line_number;

		// Current token is '{', consume first token of statement
		consume_token();

		// While not reached end of block or end of file
		while (current_token.type != lexer.TOKENS.TOK_RIGHT_CURLY && current_token.type != lexer.TOKENS.TOK_ERROR
				&& current_token.type != lexer.TOKENS.TOK_EOF) {

			// Parse the statement
			statements.add(parse_statement());

			// Consume first token of next statement
			consume_token();
		}

		// If block ended by '}', return block
		if (current_token.type == lexer.TOKENS.TOK_RIGHT_CURLY) {
			return new ASTBlockNode(statements, line_number);
		}

		// Otherwise the user left the block open
		else {
			throw new RuntimeException("Reached end of file while parsing." + " Mismatched scopes.");
		}
	}

	public ASTIfNode parse_if_statement() {

		// Node attributes
		ASTExprNode condition;
		ASTBlockNode if_block;

		int line_number = current_token.line_number;

		// Consume '('
		consume_token();
		if (current_token.type != lexer.TOKENS.TOK_LEFT_BRACKET) {
			throw new RuntimeException(
					"Expected '(' after 'if' on line " + String.valueOf(current_token.line_number) + ".");
		}

		// Parse the expression
		condition = parse_expression();

		// Consume ')'
		consume_token();
		if (current_token.type != lexer.TOKENS.TOK_RIGHT_BRACKET) {
			throw new RuntimeException(
					"Expected ')' after if-condition on line " + String.valueOf(current_token.line_number) + ".");
		}

		// Consume '{'
		consume_token();
		if (current_token.type != lexer.TOKENS.TOK_LEFT_CURLY) {
			throw new RuntimeException(
					"Expected '{' after if-condition on line " + String.valueOf(current_token.line_number) + ".");
		}

		// Consume if-block and '}'
		if_block = parse_block();

		// Lookahead whether there is an else
		if (GetNextToken.type != lexer.TOKENS.TOK_ELSE) {
			return new ASTIfNode(condition, if_block, line_number);
		}

		// Otherwise, consume the else
		consume_token();

		// Consume '{' after else
		consume_token();
		if (current_token.type != lexer.TOKENS.TOK_LEFT_CURLY) {
			throw new RuntimeException(
					"Expected '{' after else on line " + String.valueOf(current_token.line_number) + ".");
		}

		// Parse else-block and '}'
		ASTBlockNode else_block = parse_block();

		// Return if node
		return new ASTIfNode(condition, if_block, line_number, else_block);
	}

	public ASTWhileNode parse_while_statement() {

		// Node attributes
		ASTExprNode condition;
		ASTBlockNode block;

		int line_number = current_token.line_number;

		// Consume '('
		consume_token();
		if (current_token.type != lexer.TOKENS.TOK_LEFT_BRACKET) {
			throw new RuntimeException(
					"Expected '(' after 'while' on line " + String.valueOf(current_token.line_number) + ".");
		}

		// Parse the expression
		condition = parse_expression();

		// Consume ')'
		consume_token();
		if (current_token.type != lexer.TOKENS.TOK_RIGHT_BRACKET) {
			throw new RuntimeException(
					"Expected ')' after while-condition on line " + String.valueOf(current_token.line_number) + ".");
		}

		// Consume '{'
		consume_token();
		if (current_token.type != lexer.TOKENS.TOK_LEFT_CURLY) {
			throw new RuntimeException(
					"Expected '{' after while-condition on line " + String.valueOf(current_token.line_number) + ".");
		}

		// Consume while-block and '}'
		block = parse_block();

		// Return while node
		return new ASTWhileNode(condition, block, line_number);
	}

	// bookmark
	public ASTForNode parse_for_statement() {

		// Node attributes
		ASTDeclarationNode variable = null;
		ASTExprNode condition;
		ASTAssignmentNode assignment;
		ASTBlockNode block;

		int line_number = current_token.line_number;

		// Consume 'for'
		consume_token();

		// Consume '('
		if (current_token.type != lexer.TOKENS.TOK_LEFT_BRACKET) {
			throw new RuntimeException(
					"Expected '(' after 'for' on line " + String.valueOf(current_token.line_number) + ".");
		}
		consume_token();

		// Check if need to declare variable
		if (current_token.type == lexer.TOKENS.TOK_LET) {
			variable = parse_declaration_statement();
		}

		// Consume the ';'
		if (current_token.type != lexer.TOKENS.TOK_SEMICOLON) {
			throw new RuntimeException(
					"Expected ';' in for statement on line " + String.valueOf(current_token.line_number) + ".");
		}
		consume_token();

		// Parse the expression
		consume_token();
		condition = parse_expression();

		// Consume ';'
		consume_token();
		if (current_token.type != lexer.TOKENS.TOK_SEMICOLON) {
			throw new RuntimeException("Expected ';' after expression in for statemenet on line "
					+ String.valueOf(current_token.line_number) + ".");
		}

		// Parse the assignment
		consume_token();
		assignment = parse_assignment_statement();

		// Consume the ')'
		consume_token();
		if (current_token.type != lexer.TOKENS.TOK_RIGHT_BRACKET) {
			throw new RuntimeException("Expected ')' at the end of for statement on line "
					+ String.valueOf(current_token.line_number) + ".");
		}

		// Consume '{'
		consume_token();
		if (current_token.type != lexer.TOKENS.TOK_LEFT_CURLY) {
			throw new RuntimeException(
					"Expected '{' after while-condition on line " + String.valueOf(current_token.line_number) + ".");
		}

		// Consume while-block and '}'
		block = parse_block();

		if (variable != null) {
			return new ASTForNode(variable, condition, assignment, block, line_number);
		} else {
			return new ASTForNode(condition, assignment, block, line_number);
		}
	}

	public ASTFunctionDefinitionNode parse_function_definition() {

		// Node attributes
		String identifier;
		ArrayList<tangible.Pair<String, TYPE>> parameters = new ArrayList<tangible.Pair<String, TYPE>>();
		TYPE type;
		ASTBlockNode block;
		int line_number = current_token.line_number;

		// Consume type
		identifier = current_token.value;
		type = parse_type(identifier);
		consume_token();

		// Make sure it is an identifier
		if (current_token.type != lexer.TOKENS.TOK_IDENTIFIER) {
			throw new RuntimeException(
					"Expected function identifier on line " + String.valueOf(current_token.line_number) + ".");
		}

		identifier = current_token.value;

		// Consume '('
		consume_token();
		if (current_token.type != lexer.TOKENS.TOK_LEFT_BRACKET) {
			throw new RuntimeException("Expected '(' after '" + identifier + "' on line "
					+ String.valueOf(current_token.line_number) + ".");
		}

		// Consume ')' or parameters
		consume_token();

		if (current_token.type != lexer.TOKENS.TOK_RIGHT_BRACKET) {

			// Parse first parameter
			parameters.add(parse_formal_param());

			// Consume ',' or ')'
			consume_token();

			while (current_token.type == lexer.TOKENS.TOK_COMMA) {

				// Consume identifier
				consume_token();

				// Parse parameter
				parameters.add(parse_formal_param());

				// Consume ',' or ')'
				consume_token();
			}

			// Exited while-loop, so token must be ')'
			if (current_token.type != lexer.TOKENS.TOK_RIGHT_BRACKET) {
				throw new RuntimeException(
						"Expected ')' or more parameters on line " + String.valueOf(current_token.line_number) + ".");
			}
		}

		// Consume '{'
		consume_token();

		if (current_token.type != lexer.TOKENS.TOK_LEFT_CURLY) {
			throw new RuntimeException("Expected '{' after function '" + identifier + "' definition on line "
					+ String.valueOf(current_token.line_number) + ".");
		}

		// Parse block
		block = parse_block();

		// Return function definition node
		return new ASTFunctionDefinitionNode(identifier, parameters, type, block, line_number);

	}

	public tangible.Pair<String, TYPE> parse_formal_param() {

		String identifier;
		TYPE type;

		// Make sure current token is identifier
		if (current_token.type != lexer.TOKENS.TOK_IDENTIFIER) {
			throw new RuntimeException("Expected variable name in function definition on line "
					+ String.valueOf(current_token.line_number) + ".");
		}
		identifier = current_token.value;

		// Consume ':'
		consume_token();

		if (current_token.type != lexer.TOKENS.TOK_COLON) {
			throw new RuntimeException("Expected ':' after '" + identifier + "' on line "
					+ String.valueOf(current_token.line_number) + ".");
		}

		// Consume type
		consume_token();
		type = parse_type(identifier);

		return new tangible.Pair<String, TYPE>(identifier, type);

	}

	public ASTExprNode parse_simple_expression() {

		ASTExprNode term = parse_term();
		int line_number = current_token.line_number;

		if (GetNextToken.type == lexer.TOKENS.TOK_ADDITIVE_OP) {
			consume_token();
			return new ASTBinaryExprNode(current_token.value, term, parse_simple_expression(), line_number);
		}

		return term;
	}

	public ASTExprNode parse_term() {

		ASTExprNode factor = parse_factor();
		int line_number = current_token.line_number;

		if (GetNextToken.type == lexer.TOKENS.TOK_MULTIPLICATIVE_OP) {
			consume_token();
			return new ASTBinaryExprNode(current_token.value, factor, parse_term(), line_number);
		}

		return factor;
	}

	public ASTExprNode parse_factor() {

		consume_token();

		// Determine line number
		int line_number = current_token.line_number;

		switch (current_token.type) {

			// Literal Cases
			case TOK_INT:
				return new ASTLiteralNode<Integer>(Integer.parseInt(current_token.value), line_number);

			case TOK_FLOAT:
				return new ASTLiteralNode<Float>(Float.parseFloat(current_token.value), line_number);

			case TOK_BOOL:
				return new ASTLiteralNode<Boolean>(current_token.value.equals("true"), line_number);

			case TOK_STRING: {
				// Remove " character from front and end of lexeme
				String str = current_token.value.substring(1, current_token.value.length() - 2);

				// Replace \" with quote
				int pos = str.indexOf("\\\"");
				while (pos != -1) {
					// Replace
					str = (str.substring(0, pos) + "\"" + str.substring(pos + 2));
					// Get next occurrence from current position
					pos = str.indexOf("\\\"", pos + 2);
				}

				// Replace \n with newline
				pos = str.indexOf("\\n");
				while (pos != -1) {
					// Replace
					str = (str.substring(0, pos) + "\n" + str.substring(pos + 2));
					// Get next occurrence from current position
					pos = str.indexOf("\\n", pos + 2);
				}

				// Replace \t with tab
				pos = str.indexOf("\\t");
				while (pos != -1) {
					// Replace
					str = (str.substring(0, pos) + "\t" + str.substring(pos + 2));
					// Get next occurrence from current position
					pos = str.indexOf("\\t", pos + 2);
				}

				// Replace \b with backslash
				pos = str.indexOf("\\b");
				while (pos != -1) {
					// Replace
					str = (str.substring(0, pos) + "\\" + str.substring(pos + 2));
					// Get next occurrence from current position
					pos = str.indexOf("\\b", pos + 2);
				}

				return new ASTLiteralNode<String>(str, line_number);
			}

			// Identifier or function call case
			case TOK_IDENTIFIER:
				if (GetNextToken.type == lexer.TOKENS.TOK_LEFT_BRACKET) {
					return parse_function_call();
				} else {
					return new ASTIdentifierNode(current_token.value, line_number);
				}

				// Subexpression case
			case TOK_LEFT_BRACKET: {
				ASTExprNode sub_expr = parse_expression();
				consume_token();
				if (current_token.type != lexer.TOKENS.TOK_RIGHT_BRACKET) {
					throw new RuntimeException(
							"Expected ')' after expression on line " + String.valueOf(current_token.line_number) + ".");
				}
				return sub_expr;
			}

			// Unary expression case
			case TOK_ADDITIVE_OP:
			case TOK_NOT:
				return new ASTUnaryExprNode(current_token.value, parse_expression(), line_number);

			default:
				throw new RuntimeException(
						"Expected expression on line " + String.valueOf(current_token.line_number) + ".");

		}

	}

	public ASTFunctionCallNode parse_function_call() {
		// current token is the function identifier
		String identifier = current_token.value;
		var parameters = new ArrayList<ASTExprNode>();
		int line_number = current_token.line_number;

		consume_token();
		if (current_token.type != lexer.TOKENS.TOK_LEFT_BRACKET) {
			throw new RuntimeException("Expected '(' on line " + String.valueOf(current_token.line_number) + ".");
		}

		// If next token is not right bracket, we have parameters
		if (GetNextToken.type != lexer.TOKENS.TOK_RIGHT_BRACKET) {
			parameters = parse_actual_params();
		} else {
			// Consume ')'
			consume_token();
		}

		// Ensure right close bracket after fetching parameters
		if (current_token.type != lexer.TOKENS.TOK_RIGHT_BRACKET) {
			throw new RuntimeException("Expected ')' on line " + String.valueOf(current_token.line_number)
					+ " after function parameters.");
		}

		return new ASTFunctionCallNode(identifier, parameters, line_number);
	}

	public ArrayList<ASTExprNode> parse_actual_params() {

		var parameters = new ArrayList<ASTExprNode>();

		parameters.add(parse_expression());
		consume_token();

		// If there are more
		while (current_token.type == lexer.TOKENS.TOK_COMMA) {
			parameters.add(parse_expression());
			consume_token();
		}

		return new ArrayList<ASTExprNode>(parameters);
	}

	public TYPE parse_type(String identifier) {
		switch (current_token.type) {
			case TOK_INT_TYPE:
				return TYPE.INT;

			case TOK_FLOAT_TYPE:
				return TYPE.FLOAT;

			case TOK_BOOL_TYPE:
				return TYPE.BOOLEAN;

			case TOK_STRING_TYPE:
				return TYPE.STRING;

			default:
				throw new RuntimeException("Expected type for " + identifier + " after ':' on line "
						+ String.valueOf(current_token.line_number) + ".");
		}
	}
}