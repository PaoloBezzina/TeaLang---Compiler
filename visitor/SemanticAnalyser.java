package visitor;

import visitor.*;
import java.io.*;
import java.util.*;
import parser.*;
import parser.visitor.*;

public class SemanticAnalyser implements Visitor {

	private ArrayList<SemanticScope> scopes = new ArrayList<SemanticScope>();
	private Stack<parser.TYPE> functions = new Stack<parser.TYPE>();
	private ArrayList<tangible.Pair<String, TYPE>> current_function_parameters = new ArrayList<tangible.Pair<String, TYPE>>();
	private TYPE current_expression_type;

	public SemanticAnalyser() {
		// Add global scope
		scopes.add(new SemanticScope());
	}

	public SemanticAnalyser(SemanticScope global_scope) {
		// Add global scope
		scopes.add(global_scope);
	}


	@Override
	public void visit(parser.ASTProgramNode prog) {

		// For each statement, accept
		for (var statement : prog.statements) {
			statement.accept(this);
		}
	}

	@Override
	public void visit(parser.ASTDeclarationNode decl) {

		// Current scope is the scope at the back
		SemanticScope current_scope = scopes.get(scopes.size() - 1);

		// If variable already declared, throw error
		if (current_scope.already_declared(decl.identifier)) {
			throw new RuntimeException("Variable redeclaration on line " + String.valueOf(decl.line_number) + ". '"
					+ decl.identifier + "' was already declared in this scope on line "
					+ String.valueOf(current_scope.declaration_line(decl.identifier)) + ".");
		}

		// Visit the expression to update current type
		decl.expr.accept(this);

		// allow mismatched type in the case of declaration of int to real
		if (decl.type == parser.TYPE.FLOAT && current_expression_type == parser.TYPE.INT) {
			current_scope.declare(decl.identifier, parser.TYPE.FLOAT, decl.line_number);
		}

		// types match
		else if (decl.type == current_expression_type) {
			current_scope.declare(decl.identifier, decl.type, decl.line_number);
		}

		// types don't match
		else {
			throw new RuntimeException(
					"Found " + type_str(current_expression_type) + " on line " + String.valueOf(decl.line_number)
							+ " in definition of '" + decl.identifier + "', expected " + type_str(decl.type) + ".");
		}
	}

	@Override
	public void visit(parser.ASTAssignmentNode assign) {

		// Determine the inner-most scope in which the value is declared
		int i;
		for (i = scopes.size() - 1; !scopes.get(i).already_declared(assign.identifier); i--) {
			if (i <= 0) {
				throw new RuntimeException("Identifier '" + assign.identifier + "' being reassigned on line "
						+ String.valueOf(assign.line_number) + " was never declared "
						+ ((scopes.size() == 1) ? "globally." : "in this scope."));
			}
		}

		// Get the type of the originally declared variable
		parser.TYPE type = scopes.get(i).type(assign.identifier);

		// Visit the expression to update current type
		assign.expr.accept(this);

		// allow mismatched type in the case of declaration of int to real
		if (type == parser.TYPE.FLOAT && current_expression_type == parser.TYPE.INT) {
		}

		// otherwise throw error
		else if (current_expression_type != type) {
			throw new RuntimeException(
					"Mismatched type for '" + assign.identifier + "' on line " + String.valueOf(assign.line_number)
							+ ". Expected " + type_str(type) + ", found " + type_str(current_expression_type) + ".");
		}
	}

	@Override
	public void visit(parser.ASTPrintNode print) {

		// Update current expression
		print.expr.accept(this);
	}

	@Override
	public void visit(parser.ASTReturnNode ret) {

		// Update current expression
		ret.expr.accept(this);

		// If we are not global, check that we return current function return type
		if (!functions.empty() && current_expression_type != functions.peek()) {
			throw new RuntimeException("Invalid return type on line " + String.valueOf(ret.line_number) + ". Expected "
					+ type_str(functions.peek()) + ", found " + type_str(current_expression_type) + ".");
		}
	}

	public void visit(parser.ASTBlockNode block) {

		// Create new scope
		scopes.add(new SemanticScope());

		// Check whether this is a function block by seeing if we have any current
		// function parameters. If we do, then add them to the current scope.
		for (var param : current_function_parameters) {
			int index = (scopes.size() - 1);
			// Access last element by passing index
			scopes.get(index).declare(param.first, param.second, block.line_number);
		}

		// Clear the global function parameters vector
		current_function_parameters.clear();

		// Visit each statement in the block
		for (var stmt : block.statements) {
			stmt.accept(this);
		}

		// Close scope
		scopes.remove(scopes.size()-1);
		//scopes = pop_back(scopes);
	}

	public void visit(parser.ASTIfNode ifnode) {

		// Set current type to while expression
		ifnode.condition.accept(this);

		// Make sure it is boolean
		if (current_expression_type != parser.TYPE.BOOLEAN) {
			throw new RuntimeException("Invalid if-condition on line " + String.valueOf(ifnode.line_number)
					+ ", expected boolean expression.");
		}

		// Check the if block
		ifnode.if_block.accept(this);

		// If there is an else block, check it too
		if (ifnode.else_block != null) {
			ifnode.else_block.accept(this);
		}

	}

	public void visit(parser.ASTWhileNode whilenode) {

		// Set current type to while expression
		whilenode.condition.accept(this);

		// Make sure it is boolean
		if (current_expression_type != parser.TYPE.BOOLEAN) {
			throw new RuntimeException("Invalid while-condition on line " + String.valueOf(whilenode.line_number)
					+ ", expected boolean expression.");
		}

		// Check the while block
		whilenode.block.accept(this);
	}

	public void visit(parser.ASTForNode fornode) {
		
		if (fornode.variable != null){
			fornode.variable.accept(this);
		}

		// Set current type to for expression
		fornode.expression.accept(this);
		
		// Make sure it is boolean
		if (current_expression_type != parser.TYPE.BOOLEAN) {
			throw new RuntimeException("Invalid for-condition on line " + String.valueOf(fornode.line_number)
					+ ", expected boolean expression.");
		}

		// Check the for block
		fornode.block.accept(this);
	}

	public void visit(parser.ASTFunctionDefinitionNode func) {

		// First check that all enclosing scopes have not already defined the function
		for (var scope : scopes) {
			if (scope.already_declared(func.identifier, func.signature)) {

				// Determine line number of error and the corresponding function signature
				int line = scope.declaration_line(func.identifier, func.signature);
				String signature = "(";
				for (var param : func.signature) {
					signature += type_str(param) + ", ";
				}
				// remove last whitespace
				signature = pop_back(signature);
				// remove last comma
				signature = pop_back(signature);
				signature += ")";

				throw new RuntimeException("Error on line " + String.valueOf(func.line_number) + ". Function "
						+ func.identifier + signature + " already defined on line " + String.valueOf(line) + ".");
			}
		}

		// Add function to symbol table
		int index = scopes.size() - 1;
		// Access last element by passing index
		scopes.get(index).declare(func.identifier, func.type, func.signature, func.line_number);

		// Push current function type onto function stack
		functions.add(func.type);

		// Empty and update current function parameters vector
		current_function_parameters.clear();
		current_function_parameters = func.parameters;

		// Check semantics of function block by visiting nodes
		func.block.accept(this);

		// Check that the function body returns
		if (!returns(func.block)) {
			throw new RuntimeException("Function " + func.identifier + " defined on line "
					+ String.valueOf(func.line_number) + " is not guaranteed to " + "return a value.");
		}

		// End the current function
		// bookmark
		//functions.pop();
	}

	public void visit(parser.ASTLiteralNode lit) {
		String type = lit.getType();

		if (type.equals("Integer")) {
			current_expression_type = parser.TYPE.INT;
		} else if (type.equals("Float")) {
			current_expression_type = parser.TYPE.FLOAT;
		} else if (type.equals("Boolean")) {
			current_expression_type = parser.TYPE.BOOLEAN;
		} else if (type.equals("String")) {
			current_expression_type = parser.TYPE.STRING;
		} else {
			throw new RuntimeException("Invalid type found");
		}
	}

	public void visit(parser.ASTBinaryExprNode bin) {

		// Operator
		String op = bin.identifier;

		// Visit left node first
		bin.left.accept(this);
		parser.TYPE l_type = current_expression_type;

		// Then right node
		bin.right.accept(this);
		parser.TYPE r_type = current_expression_type;

		// These only work for int/real
		if (op.equals("*") || op.equals("/") || op.equals("-")) {
			if ((l_type != parser.TYPE.INT && l_type != parser.TYPE.FLOAT)
					|| (r_type != parser.TYPE.INT && r_type != parser.TYPE.FLOAT)) {
				throw new RuntimeException("Expected numerical operands for '" + op + "' operator on line "
						+ String.valueOf(bin.line_number) + ".");
			}

			// If both int, then expression is int, otherwise real
			current_expression_type = (l_type == parser.TYPE.INT && r_type == parser.TYPE.INT) ? parser.TYPE.INT
					: parser.TYPE.FLOAT;
		}

		// + works for all types except bool
		else if (op.equals("+")) {
			if (l_type == parser.TYPE.BOOLEAN || r_type == parser.TYPE.BOOLEAN) {
				throw new RuntimeException("Invalid operand for '+' operator, expected numerical or string"
						+ " operand on line " + String.valueOf(bin.line_number) + ".");
			}

			// If both string, no error
			if (l_type == parser.TYPE.STRING && r_type == parser.TYPE.STRING) {
				current_expression_type = parser.TYPE.STRING;
			}

			// only one is string, error
			else if (l_type == parser.TYPE.STRING || r_type == parser.TYPE.STRING) {
				throw new RuntimeException(
						"Mismatched operands for '+' operator, found " + type_str(l_type) + " on the left, but "
								+ type_str(r_type) + " on the right (line " + String.valueOf(bin.line_number) + ").");
			}

			// real/int possibilities remain. If both int, then result is int, otherwise
			// result is real
			else {
				current_expression_type = (l_type == parser.TYPE.INT && r_type == parser.TYPE.INT) ? parser.TYPE.INT
						: parser.TYPE.FLOAT;
			}
		}

		// and/or only work for bool
		else if (op.equals("and") || op.equals("or")) {
			if (l_type == parser.TYPE.BOOLEAN && r_type == parser.TYPE.BOOLEAN) {
				current_expression_type = parser.TYPE.BOOLEAN;
			} else {
				throw new RuntimeException("Expected two boolean-type operands for '" + op + "' operator " + "on line "
						+ String.valueOf(bin.line_number) + ".");
			}
		}

		// rel-ops only work for numeric types
		else if (op.equals("<") || op.equals(">") || op.equals("<=") || op.equals(">=")) {
			if ((l_type != parser.TYPE.FLOAT && l_type != parser.TYPE.INT)
					|| (r_type != parser.TYPE.FLOAT && r_type != parser.TYPE.INT)) {
				throw new RuntimeException("Expected two numerical operands for '" + op + "' operator " + "on line "
						+ String.valueOf(bin.line_number) + ".");
			}
			current_expression_type = parser.TYPE.BOOLEAN;
		}

		// == and != only work for like types
		else if (op.equals("==") || op.equals("!=")) {
			if (l_type != r_type && (l_type != parser.TYPE.FLOAT || r_type != parser.TYPE.INT)
					&& (l_type != parser.TYPE.INT || r_type != parser.TYPE.FLOAT)) {
				throw new RuntimeException("Expected arguments of the same type '" + op + "' operator " + "on line "
						+ String.valueOf(bin.line_number) + ".");
			}
			current_expression_type = parser.TYPE.BOOLEAN;
		}

		else {
			throw new RuntimeException("Unhandled semantic error in binary operator.");
		}
	}

	public void visit(parser.ASTIdentifierNode id) {

		// Determine the inner-most scope in which the value is declared
		int i;
		for (i = scopes.size() - 1; !scopes.get(i).already_declared(id.identifier); i--) {
			if (i <= 0) {
				throw new RuntimeException(
						"Identifier '" + id.identifier + "' appearing on line " + String.valueOf(id.line_number)
								+ " was never declared " + ((scopes.size() == 1) ? "globally." : "in this scope."));
			}
		}

		// Update current expression type
		current_expression_type = scopes.get(i).type(id.identifier);
	}

	public void visit(parser.ASTUnaryExprNode un) {

		// Determine expression type
		un.expr.accept(this);

		// Handle different cases
		switch (current_expression_type) {
			case INT:
			case FLOAT:
				if (!un.unary_op.equals("+") && !un.unary_op.equals("-")) {
					throw new RuntimeException("Operator '" + un.unary_op + "' in front of numerical "
							+ "expression on line " + String.valueOf(un.line_number) + ".");
				}
				break;
			case BOOLEAN:
				if (!un.unary_op.equals("not")) {
					throw new RuntimeException("Operator '" + un.unary_op + "' in front of boolean "
							+ "expression on line " + String.valueOf(un.line_number) + ".");
				}
				break;
			default:
				throw new RuntimeException("Incompatible unary operator '" + un.unary_op + "' in front of "
						+ "expression on line " + String.valueOf(un.line_number) + ".");
		}
	}

	public void visit(parser.ASTFunctionCallNode func) {

		// Determine the signature of the function
		ArrayList<parser.TYPE> signature = new ArrayList<parser.TYPE>();

		// For each parameter,
		for (var param : func.statements) {

			// visit to update current expr type
			param.accept(this);

			// add the type of current expr to signature
			signature.add(current_expression_type);
		}

		// Make sure the function exists in some scope i
		int i;
		for (i = scopes.size() - 1; !scopes.get(i).already_declared(func.identifier, signature); i--) {
			if (i <= 0) {
				String func_name = func.identifier + "(";
				for (var param : signature) {
					func_name += type_str(param) + ", ";
				}
				// remove last whitespace
				func_name = pop_back(func_name);
				// remove last comma
				func_name = pop_back(func_name);
				func_name += ")";
				throw new RuntimeException(
						"Function '" + func_name + "' appearing on line " + String.valueOf(func.line_number)
								+ " was never declared " + ((scopes.size() == 1) ? "globally." : "in this scope."));
			}
		}

		// Set current expression type to the return value of the function
		current_expression_type = scopes.get(i).type(func.identifier, signature);
	}

	public boolean returns(parser.ASTStatementNode stmt) {

		// Base case: if the statement is a return statement, then it definitely returns
		if (stmt instanceof parser.ASTReturnNode) {
			return true;
		}

		// For a block, if at least one statement returns, then the block returns
		if (stmt instanceof parser.ASTBlockNode) {
			var block = (parser.ASTBlockNode) stmt;
			for (var blk_stmt : block.statements) {
				if (returns(blk_stmt)) {
					return true;
				}
			}
		}

		// An if-(else) block returns only if both the if and the else statement return.
		if (stmt instanceof parser.ASTIfNode) {
			var ifstmt = (parser.ASTIfNode) stmt;
			if (ifstmt.else_block != null) {
				return (returns(ifstmt.if_block) && returns(ifstmt.else_block));
			}
		}

		// A while block returns if its block returns
		if (stmt instanceof parser.ASTWhileNode) {
			var whilestmt = (parser.ASTWhileNode) stmt;
			return returns(whilestmt.block);
		}

		if(stmt instanceof ASTForNode){
            ASTForNode forStatement = (ASTForNode) stmt;
            return returns(forStatement.block);
        }

		// Other statements do not return
		else {
			return false;
		}

	}

	public static SemanticScope back(ArrayList<SemanticScope> scope) {
		int index = (scope.size() - 1);
		// Access last element by passing index
		return scope.get(index);
	}

	public static ArrayList<SemanticScope> pop_back(ArrayList<SemanticScope> scope) {
		int index = (scope.size() - 1);
		// Delete last element by passing index
		scope.remove(index);
		return scope;
	}

	public static String pop_back(String str) {
		return removeLastChars(str, 1);
	}

	public static String removeLastChars(String str, int chars) {
		if (str.length() > 0) {
			return str.substring(0, str.length() - chars);
		}
		return str;
	}

	public String type_str(parser.TYPE t) {

		switch (t) {
			case INT:
				return "int";
			case FLOAT:
				return "float";
			case BOOLEAN:
				return "bool";
			case STRING:
				return "string";
			default:
				throw new RuntimeException("Invalid type encountered in syntax tree when generating XML.");
		}
	}
}