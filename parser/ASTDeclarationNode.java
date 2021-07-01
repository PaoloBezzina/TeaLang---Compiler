package parser;

import parser.*;
import parser.visitor.Visitor;
import java.util.*;

public class ASTDeclarationNode implements ASTStatementNode {
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
	public int line_number;

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}