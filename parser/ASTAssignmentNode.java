package parser;

import parser.*;
import java.util.*;

public class ASTAssignmentNode implements ASTStatementNode {
	// C++ TO JAVA CONVERTER WARNING: Unsigned integer types have no direct
	// equivalent in Java:
	// ORIGINAL LINE: ASTAssignmentNode(std::string, ASTExprNode*, unsigned int);
	public ASTAssignmentNode(String identifier, ASTExprNode expr, int line_number)
	{
		this.identifier = identifier;
		this.expr = expr;
		this.line_number = line_number;
	}

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