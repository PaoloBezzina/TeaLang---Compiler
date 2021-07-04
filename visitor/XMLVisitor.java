package visitor;

import visitor.*;
import java.io.*;
import parser.*;
import parser.visitor.*;

public class XMLVisitor implements Visitor {

	String filePath = "program.xml";
	FileWriter xmlfile;
	private int indentation_level;
	private String TAB = String.valueOf('\t');

	public XMLVisitor() {
		this.indentation_level = 0;
		try {
			xmlfile = new FileWriter(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public final void close() {
		try {
			xmlfile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public final String indentation() {

		String tabs = "";

		// Tab character for each indentation level
		for (int i = 0; i < indentation_level; i++)
			tabs += TAB;

		return tabs;
	}

	@Override
	public void visit(parser.ASTProgramNode program) {
		// Add initial <program> tag
		try {
			xmlfile.write(indentation() + "<Program>" + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Indent
		indentation_level++;

		// For each statement, accept
		for (var statement : program.statements) {
			statement.accept(this);
		}

		// Unindent
		indentation_level--;

		// Add closing tag
		try {
			xmlfile.write(indentation() + "</Program>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void visit(parser.ASTDeclarationNode decl) {

		// Add initial <decl> tag
		try {
			xmlfile.write(indentation() + "<Decl>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}

		// Indent
		indentation_level++;

		// Add identifier
		try {
			xmlfile.write(
					indentation() + "<Identifier type=\"" + type_str(decl.type) + "\">" + decl.identifier + "</Identifier>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}

		// Expression tags
		decl.expr.accept(this);

		// Unindent
		indentation_level--;

		// Add closing tag
		try {
			xmlfile.write(indentation() + "</Decl>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void visit(parser.ASTAssignmentNode assign) {

		// Add initial <assign> tag
		try {
			xmlfile.write(indentation() + "<Assign>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}

		// Indent
		indentation_level++;

		// Add identifier
		try {
			xmlfile.write(indentation() + "<Identifier>" + assign.identifier + "</Identifier>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}

		// Expression tags
		assign.expr.accept(this);

		// Unindent
		indentation_level--;

		// Add closing tag
		try {
			xmlfile.write(indentation() + "</Assign>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void visit(parser.ASTPrintNode print) {

		// Add initial <print> tag
		try {
			xmlfile.write(indentation() + "<Print>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}

		// Indent
		indentation_level++;

		// Expression tags
		print.expr.accept(this);

		// Unindent
		indentation_level--;

		// Add closing tag
		try {
			xmlfile.write(indentation() + "</Print>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void visit(parser.ASTReturnNode ret) {

		// Add initial <return> tag
		try {
			xmlfile.write(indentation() + "<Return>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}

		// Indent
		indentation_level++;

		// Expression tags
		ret.expr.accept(this);

		// Unindent
		indentation_level--;

		// Add closing tag
		try {
			xmlfile.write(indentation() + "</Return>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void visit(parser.ASTBlockNode block) {

		// Add initial <block> tag
		try {
			xmlfile.write(indentation() + "<Block>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}

		// Indent
		indentation_level++;

		// For each statement, accept
		for (var statement : block.statements) {
			statement.accept(this);
		}

		// Unindent
		indentation_level--;

		// Add closing tag
		try {
			xmlfile.write(indentation() + "</Block>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void visit(parser.ASTIfNode ifnode) {

		// Add initial <if> tag
		try {
			xmlfile.write(indentation() + "<If>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}

		// Indent
		indentation_level++;

		// Add <condition> tag
		try {
			xmlfile.write(indentation() + "<Condition>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}

		// Indent
		indentation_level++;

		// Expression
		ifnode.condition.accept(this);

		// Unindent
		indentation_level--;

		// Add closing tag
		try {
			xmlfile.write(indentation() + "</Condition>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}

		// Add <if-block> tag
		try {
			xmlfile.write(indentation() + "<IfBlock>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}

		// Indent
		indentation_level++;

		// If-block
		ifnode.if_block.accept(this);

		// Unindent
		indentation_level--;

		// Add closing tag
		try {
			xmlfile.write(indentation() + "</IfBlock>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}

		// If there is an else-block
		if (ifnode.else_block != null) {

			// Add <else-block> tag
			try {
				xmlfile.write(indentation() + "<ElseBlock>" + "\n");
			} catch (IOException e) {

				e.printStackTrace();
			}

			// Indent
			indentation_level++;

			// Else-block
			ifnode.else_block.accept(this);

			// Unindent
			indentation_level--;

			// Add closing tag
			try {
				xmlfile.write(indentation() + "</ElseBlock>" + "\n");
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		// Unindent
		indentation_level--;

		// Add closing tag
		try {
			xmlfile.write(indentation() + "</If>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void visit(parser.ASTWhileNode whilenode) {

		// Add initial <while> tag
		try {
			xmlfile.write(indentation() + "<While>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}

		// Indent
		indentation_level++;

		// Add <condition> tag
		try {
			xmlfile.write(indentation() + "<Condition>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}

		// Indent
		indentation_level++;

		// Expression
		whilenode.condition.accept(this);

		// Unindent
		indentation_level--;

		// Add closing tag
		try {
			xmlfile.write(indentation() + "</Condition>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}

		// while-block
		whilenode.block.accept(this);

		// Unindent
		indentation_level--;

		// Add closing tag
		try {
			xmlfile.write(indentation() + "</While>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	//bookmark
	@Override
	public void visit(parser.ASTForNode fornode) {

		// Add initial <for> tag
		try {
			xmlfile.write(indentation() + "<For>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}

		// Indent
		indentation_level++;

		if (fornode.variable != null){
			// Add <variable> tag if necessary
			try {
				xmlfile.write(indentation() + "<Variable>" + "\n");
			} catch (IOException e) {

				e.printStackTrace();
			}

			// Indent
			indentation_level++;

			// Expression
			fornode.variable.accept(this);

			// Unindent
			indentation_level--;

			// Add closing tag
			try {
				xmlfile.write(indentation() + "</Variable>" + "\n");
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		// Add <condition> tag
		try {
			xmlfile.write(indentation() + "<Condition>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}

		// Indent
		indentation_level++;

		// Expression
		fornode.expression.accept(this);

		// Unindent
		indentation_level--;

		// Add closing tag
		try {
			xmlfile.write(indentation() + "</Condition>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}

		// Add <assignment> tag
		try {
			xmlfile.write(indentation() + "<Assignment>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}

		// Indent
		indentation_level++;

		// Expression
		fornode.assignment.accept(this);

		// Unindent
		indentation_level--;

		// Add closing tag
		try {
			xmlfile.write(indentation() + "</Assignment>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}

		// For-block
		fornode.block.accept(this);

		// Unindent
		indentation_level--;

		// Add closing tag
		try {
			xmlfile.write(indentation() + "</For>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void visit(parser.ASTFunctionDefinitionNode func) {

		// Add initial <func-def> tag
		try {
			xmlfile.write(indentation() + "<FuncDef type=\"" + type_str(func.type) + "\">" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}

		// Indent
		indentation_level++;

		// Function identifier
		try {
			xmlfile.write(indentation() + "<Identifier>" + func.identifier + "</Identifier>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}

		// For each parameter
		for (var param : func.parameters) {
			try {
				xmlfile.write(indentation() + "<Parameter type=\"" + type_str(param.second) + "\">" + param.first
						+ "</Parameter>" + "\n");
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		// Function body
		func.block.accept(this);

		// Unindent
		indentation_level--;

		// Add closing tag
		try {
			xmlfile.write(indentation() + "</FuncDef>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void visit(parser.ASTLiteralNode lit) {
		String type = lit.getType();
		if (type.equals("Integer")) {
			// Add initial <int> tag
			try {
				xmlfile.write(indentation() + "<IntConst>");
			} catch (IOException e) {

				e.printStackTrace();
			}

			// Add value
			try {
				xmlfile.write(String.valueOf(lit.value));
			} catch (IOException e) {

				e.printStackTrace();
			}

			// Add closing tag
			try {
				xmlfile.write("</IntConst>" + "\n");
			} catch (IOException e) {

				e.printStackTrace();
			}

		} else if (type.equals("Float")) {
			// Add initial <float> tag
			try {
				xmlfile.write(indentation() + "<FloatConst>");
			} catch (IOException e) {

				e.printStackTrace();
			}

			// Add value
			try {
				xmlfile.write(String.valueOf(lit.value));
			} catch (IOException e) {

				e.printStackTrace();
			}

			// Add closing tag
			try {
				xmlfile.write("</FloatConst>" + "\n");
			} catch (IOException e) {

				e.printStackTrace();
			}

		} else if (type.equals("Boolean")) {
			// Add initial <bool> tag
			try {
				xmlfile.write(indentation() + "<BoolConst>");
			} catch (IOException e) {

				e.printStackTrace();
			}

			// Add value
			if (String.valueOf(lit.value) == "true") {
				try {
					xmlfile.write("true");
				} catch (IOException e) {

					e.printStackTrace();
				}
			} else {
				try {
					xmlfile.write("false");
				} catch (IOException e) {

					e.printStackTrace();
				}
			}

			// Add closing tag
			try {
				xmlfile.write("</BoolConst>" + "\n");
			} catch (IOException e) {

				e.printStackTrace();
			}

		} else if (type.equals("String")) {
			// Add initial <string> tag
			try {
				xmlfile.write(indentation() + "<StringConst>");
			} catch (IOException e) {

				e.printStackTrace();
			}

			// Add value
			try {
				xmlfile.write(String.valueOf(lit.value));
			} catch (IOException e) {

				e.printStackTrace();
			}

			// Add closing tag
			try {
				xmlfile.write("</StringConst>" + "\n");
			} catch (IOException e) {

				e.printStackTrace();
			}

		} else {
			try {
				xmlfile.write(indentation() + "-Invalid Literal Node type-" + "\n");
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	public void visit(parser.ASTBinaryExprNode bin) {

		// Add initial <bin> tag
		try {
			xmlfile.write(indentation() + "<BinExprNode Op=\"" + xml_safe_op(bin.identifier) + "\">" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}

		// Indent
		indentation_level++;

		// Left node
		bin.left.accept(this);

		// Right node
		bin.right.accept(this);

		// Unindent
		indentation_level--;

		// Add closing tag
		try {
			xmlfile.write(indentation() + "</BinExprNode>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void visit(parser.ASTIdentifierNode id) {

		// Add initial <id> tag
		try {
			xmlfile.write(indentation() + "<Identifier>");
		} catch (IOException e) {

			e.printStackTrace();
		}

		// Add value
		try {
			xmlfile.write(id.identifier);
		} catch (IOException e) {

			e.printStackTrace();
		}

		// Add closing tag
		try {
			xmlfile.write("</Identifier>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public void visit(parser.ASTUnaryExprNode un) {

		// Add initial <unary> tag
		try {
			xmlfile.write(indentation() + "<Unary Op=\"" + un.unary_op + "\">" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}

		// Indent
		indentation_level++;

		// Value
		un.expr.accept(this);

		// Unindent
		indentation_level--;

		// Add closing tag
		try {
			xmlfile.write(indentation() + "</Unary>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void visit(parser.ASTFunctionCallNode func) {

		// Add initial <func-call> tag
		try {
			xmlfile.write(indentation() + "<FuncCall>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}

		// Indent
		indentation_level++;

		// Function identifier
		try {
			xmlfile.write(indentation() + "<Identifier>" + func.identifier + "</Identifier>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}

		// For each parameter
		for (var param : func.statements) {
			try {
				xmlfile.write(indentation() + "<Arg>" + "\n");
			} catch (IOException e) {

				e.printStackTrace();
			}

			// Indent
			indentation_level++;

			// Parameter
			param.accept(this);

			// Unindent
			indentation_level--;

			try {
				xmlfile.write(indentation() + "</Arg>" + "\n");
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		// Unindent
		indentation_level--;

		// Add closing tag
		try {
			xmlfile.write(indentation() + "</FuncCall>" + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}
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

	public String xml_safe_op(String op) {

		if (op.equals("<")) {
			return "<";
			//return "&lt;";
		}
		if (op.equals(">")) {
			return ">";
			//return "&gt;";
		}
		if (op.equals("<=")) {
			return "<=";
			//return "&lt;=";
		}
		if (op.equals(">=")) {
			return ">=";
			//return "&gt;=";
		}
		return op;
	}

}