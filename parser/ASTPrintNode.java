package parser;

import parser.visitor.Visitor;

public class ASTPrintNode implements ASTStatementNode {
	public ASTPrintNode(ASTExprNode expr, int line_number) {
		this.expr = expr;
		this.line_number = line_number;
	}

	public ASTExprNode expr;
	public int line_number;

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}