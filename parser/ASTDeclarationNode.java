package parser;

import parser.*;
import java.util.*;

public class ASTDeclarationNode implements ASTStatementNode {
	// C++ TO JAVA CONVERTER WARNING: Unsigned integer types have no direct
	// equivalent in Java:
	// ORIGINAL LINE: ASTDeclarationNode(TYPE, std::string, ASTExprNode*, unsigned
	// int);

	// Statement Nodes
	public ASTDeclarationNode(TYPE type, String identifier, ASTExprNode expr, int line_number)
	{
		this.type = new parser.TYPE(type);
		this.identifier = identifier;
		this.expr = expr;
		this.line_number = line_number;
	}

	public TYPE type;
	public String identifier;
	public ASTExprNode expr;
	// C++ TO JAVA CONVERTER WARNING: Unsigned integer types have no direct
	// equivalent in Java:
	// ORIGINAL LINE: unsigned int line_number;
	public int line_number;

	@Override
	public void accept(visitor.Visitor v) {
		v.visit(this);
	}
}