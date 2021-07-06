package parser;

import parser.visitor.Visitor;

public class ASTDeclarationNode implements ASTStatementNode {
	public ASTDeclarationNode(TYPE type, String identifier, ASTExprNode expr, int line_number)
	{
		//this.type = new parser.TYPE(type);
		this.type = type;
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