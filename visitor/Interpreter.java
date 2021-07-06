package visitor;

import visitor.*;
import java.io.*;
import java.util.*;
import parser.*;
import parser.visitor.*;

public class Interpreter implements Visitor {

	private ArrayList<InterpreterScope> scopes = new ArrayList<InterpreterScope>();
	private parser.TYPE current_expression_type;
	private value_t current_expression_value = new value_t();
	private ArrayList<String> current_function_parameters = new ArrayList<String>();
	private ArrayList<value_t> current_function_arguments = new ArrayList<value_t>();

	public Interpreter() {
		// Add global scope
		scopes.add(new InterpreterScope());
	}

	public Interpreter(InterpreterScope global_scope) {
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

		// Visit expression to update current value/type
		decl.expr.accept(this);

		// Declare variable, depending on type
		switch (decl.type) {
			case INT:
				scopes.get(scopes.size() - 1).declare(decl.identifier, current_expression_value.i);
				break;
			case FLOAT:
				if (current_expression_type == parser.TYPE.INT) {
					scopes.get(scopes.size() - 1).declare(decl.identifier, (float) current_expression_value.i);
				} else {
					scopes.get(scopes.size() - 1).declare(decl.identifier, current_expression_value.f);
				}
				break;
			case BOOLEAN:
				scopes.get(scopes.size() - 1).declare(decl.identifier, current_expression_value.b);
				break;
			case STRING:
				scopes.get(scopes.size() - 1).declare(decl.identifier, current_expression_value.s);
				break;
		}
	}

	@Override
	public void visit(parser.ASTAssignmentNode assign) {

		// Determine innermost scope in which variable is declared
		int i;
		for (i = scopes.size() - 1; !scopes.get(i).already_declared(assign.identifier); i--) {
			;
		}

		// Visit expression node to update current value/type
		assign.expr.accept(this);

		// Redeclare variable, depending on type
		switch (scopes.get(i).type_of(assign.identifier)) {
			case INT:
				scopes.get(i).declare(assign.identifier, current_expression_value.i);
				break;

			case FLOAT:
				if (current_expression_type == parser.TYPE.INT) {
					scopes.get(i).declare(assign.identifier, (float) current_expression_value.i);
				} else {
					scopes.get(i).declare(assign.identifier, current_expression_value.f);
				}
				break;

			case BOOLEAN:
				scopes.get(i).declare(assign.identifier, current_expression_value.b);
				break;

			case STRING:
				scopes.get(i).declare(assign.identifier, current_expression_value.s);
				break;
		}
	}

	@Override
	public void visit(parser.ASTPrintNode print) {

		// Visit expression node to update current value/type
		print.expr.accept(this);

		// Print, depending on type
		switch (current_expression_type) {
			case INT:
				System.out.println(current_expression_value.i);
				break;
			case FLOAT:
				System.out.println(current_expression_value.f);
				break;
			case BOOLEAN:
				System.out.println(((current_expression_value.b) ? "true" : "false"));
				break;
			case STRING:
				System.out.println(current_expression_value.s);
				break;
		}
	}

	@Override
	public void visit(parser.ASTReturnNode ret) {
		// Update current expression
		ret.expr.accept(this);
	}

	@Override
	public void visit(parser.ASTBlockNode block) {

		// Create new scope
		scopes.add(new InterpreterScope());

		// Check whether this is a function block by seeing if we have any current
		// function parameters. If we do, then add them to the current scope.
		for (int i = 0; i < current_function_arguments.size(); i++) {
			switch (current_function_arguments.get(i).type) {
				case INT:
					scopes.get(scopes.size() - 1).declare(current_function_parameters.get(i),
							current_function_arguments.get(i).i);
					break;
				case FLOAT:
					scopes.get(scopes.size() - 1).declare(current_function_parameters.get(i),(float) current_function_arguments.get(i).f);
					break;
				case BOOLEAN:
					scopes.get(scopes.size() - 1).declare(current_function_parameters.get(i),
							current_function_arguments.get(i).b);
					break;
				case STRING:
					scopes.get(scopes.size() - 1).declare(current_function_parameters.get(i),
							current_function_arguments.get(i).s);
					break;
				default:
					throw new RuntimeException("Invalid Type for " + current_function_arguments.get(i).type);
			}
		}

		// Clear the global function parameter/argument vectors
		current_function_parameters.clear();
		current_function_arguments.clear();

		// Visit each statement in the block
		for (var stmt : block.statements) {
			stmt.accept(this);
		}

		// Close scope
		scopes.remove(scopes.size() - 1);
	}

	@Override
	public void visit(parser.ASTIfNode ifNode) {

		// Evaluate if condition
		ifNode.condition.accept(this);

		// Execute appropriate blocks
		if (current_expression_value.b) {
			ifNode.if_block.accept(this);
		} else {
			if (ifNode.else_block != null) {
				ifNode.else_block.accept(this);
			}
		}

	}

	@Override
	public void visit(parser.ASTWhileNode whileNode) {

		// Evaluate while condition
		whileNode.condition.accept(this);

		while (current_expression_value.b) {
			// Execute block
			whileNode.block.accept(this);

			// Re-evaluate while condition
			whileNode.condition.accept(this);
		}
	}

	@Override
	public void visit(ASTForNode forNode) {
		forNode.variable.accept(this);
		forNode.expression.accept(this);

		while (current_expression_value.b) {
			forNode.block.accept(this);
			forNode.assignment.accept(this);
			forNode.expression.accept(this);
		}
		forNode.expression.accept(this);
	}

	@Override
	public void visit(parser.ASTFunctionDefinitionNode func) {

		// Add function to symbol table
		scopes.get(scopes.size() - 1).declare(func.identifier, func.signature, func.variable_names, func.block);

	}

	@Override
	public void visit(parser.ASTLiteralNode lit) {
		String type = lit.getType();

		if (type.equals("Integer")) {

			value_t v = new value_t();
			v.i = (int) lit.value;
			v.type = parser.TYPE.INT;
			current_expression_type = parser.TYPE.INT;
			current_expression_value = v;

		} else if (type.equals("Float")) {
			value_t v = new value_t();
			v.f = (float) lit.value;
			v.type = parser.TYPE.FLOAT;
			current_expression_type = parser.TYPE.FLOAT;
			current_expression_value = v;

		} else if (type.equals("Boolean")) {
			value_t v = new value_t();
			v.b = (boolean) lit.value;
			v.type = parser.TYPE.BOOLEAN;
			current_expression_type = parser.TYPE.BOOLEAN;
			current_expression_value = v;

		} else if (type.equals("String")) {
			value_t v = new value_t();
			v.s = (String) lit.value;
			v.type = parser.TYPE.STRING;
			current_expression_type = parser.TYPE.STRING;
			current_expression_value = v;
		} else {
			throw new RuntimeException("Invalid type found");
		}
	}

	@Override
	public void visit(parser.ASTBinaryExprNode bin) {

		// Operator
		String op = bin.identifier;

		// Visit left node first
		bin.left.accept(this);
		parser.TYPE l_type = current_expression_type;
		value_t l_value = current_expression_value;

		// Then right node
		bin.right.accept(this);
		parser.TYPE r_type = current_expression_type;
		value_t r_value = current_expression_value;

		// Expression struct
		value_t v = new value_t();

		// Arithmetic operators for now
		if (op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/")) {
			// Two ints
			if (l_type == parser.TYPE.INT && r_type == parser.TYPE.INT) {
				current_expression_type = parser.TYPE.INT;
				if (op.equals("+")) {
					v.i = l_value.i + r_value.i;
					v.type = current_expression_type;
				} else if (op.equals("-")) {
					v.i = l_value.i - r_value.i;
					v.type = current_expression_type;
				} else if (op.equals("*")) {
					v.i = l_value.i * r_value.i;
					v.type = current_expression_type;
				} else if (op.equals("/")) {
					if (r_value.i == 0) {
						throw new RuntimeException(
								"Division by zero encountered on line " + String.valueOf(bin.line_number) + ".");
					}
					v.i = l_value.i / r_value.i;
					v.type = current_expression_type;
				}
			}
			// At least one real
			else if (l_type == parser.TYPE.FLOAT || r_type == parser.TYPE.FLOAT) {
				current_expression_type = parser.TYPE.FLOAT;
				float l = l_value.f;
				float r = r_value.f;
				if (l_type == parser.TYPE.INT) {
					l = l_value.i;
				}
				if (r_type == parser.TYPE.INT) {
					r = r_value.i;
				}
				if (op.equals("+")) {
					v.f = l + r;
					v.type = current_expression_type;
				} else if (op.equals("-")) {
					v.f = l - r;
					v.type = current_expression_type;
				} else if (op.equals("*")) {
					v.f = l * r;
					v.type = current_expression_type;
				} else if (op.equals("/")) {
					if (r == 0F) {
						throw new RuntimeException(
								"Division by zero encountered on line " + String.valueOf(bin.line_number) + ".");
					}
					v.f = l / r;
					v.type = current_expression_type;
				}
			}
			// Remaining case is for strings
			else {
				current_expression_type = parser.TYPE.STRING;
				v.s = l_value.s + r_value.s;
				v.type = current_expression_type;
			}
		}
		// Now bool
		else if (op.equals("and") || op.equals("or")) {
			current_expression_type = parser.TYPE.BOOLEAN;
			if (op.equals("and")) {
				v.b = l_value.b && r_value.b;
			} else if (op.equals("or")) {
				v.b = l_value.b || r_value.b;
			}
		}

		// Now Comparator Operators
		else {
			current_expression_type = parser.TYPE.BOOLEAN;
			v.type = current_expression_type;
			if (l_type == parser.TYPE.BOOLEAN) {
				v.b = (op.equals("==")) ? l_value.b == r_value.b : l_value.b != r_value.b;
			}

			else if (l_type == parser.TYPE.STRING) {
				v.b = (op.equals("==")) ? l_value.s == r_value.s : l_value.s != r_value.s;
			}

			else {
				float l = l_value.f;
				float r = r_value.f;
				if (l_type == parser.TYPE.INT) {
					l = l_value.i;
				}
				if (r_type == parser.TYPE.INT) {
					r = r_value.i;
				}
				if (op.equals("==")) {
					v.b = l == r;
				} else if (op.equals("!=")) {
					v.b = l != r;
				} else if (op.equals("<")) {
					v.b = l < r;
				} else if (op.equals(">")) {
					v.b = l > r;
				} else if (op.equals(">=")) {
					v.b = l >= r;
				} else if (op.equals("<=")) {
					v.b = l <= r;
				}
			}
		}

		// Update current expression
		current_expression_value = v;

	}

	@Override
	public void visit(parser.ASTIdentifierNode id) {

		// Determine innermost scope in which variable is declared
		int i;
		for (i = scopes.size() - 1; !scopes.get(i).already_declared(id.identifier); i--) {
			;
		}

		// Update current expression
		current_expression_type = scopes.get(i).type_of(id.identifier);
		current_expression_value = scopes.get(i).value_of(id.identifier);

	}

	@Override
	public void visit(parser.ASTUnaryExprNode un) {

		// Update current expression
		un.expr.accept(this);

		switch (current_expression_type) {
			case INT:
				if (un.unary_op.equals("-")) {
					current_expression_value.i *= -1;
				}
				break;
			case FLOAT:
				if (un.unary_op.equals("-")) {
					current_expression_value.f *= -1;
				}
				break;
			case BOOLEAN:
				current_expression_value.b ^= true;
				break;
			default:
				break;
		}
	}

	@Override
	public void visit(parser.ASTFunctionCallNode func) {

		// Determine the signature of the function
		ArrayList<parser.TYPE> signature = new ArrayList<parser.TYPE>();
		ArrayList<value_t> current_function_arguments = new ArrayList<value_t>();

		// For each parameter,
		for (var param : func.statements) {

			// visit to update current expr type
			param.accept(this);

			// add the type of current expr to signature
			signature.add(current_expression_type);

			// add the current expr to the local vector of function arguments, to be used in
			// the creation of the function scope
			current_expression_value.type = current_expression_type;
			current_function_arguments.add(current_expression_value);
		}

		// Update the global vector current_function_arguments
		for (var arg : current_function_arguments) {
			this.current_function_arguments.add(arg);
		}

		// Determine in which scope the function is declared
		int i;
		for (i = scopes.size() - 1; !scopes.get(i).already_declared(func.identifier, signature); i--) {
			;
		}

		// Populate the global vector of function parameter names, to be used in
		// creation of function scope
		current_function_parameters = scopes.get(i).variable_names_of(func.identifier, signature);

		// Visit the corresponding function block
		scopes.get(i).block_of(func.identifier, signature).accept(this);

	}

}