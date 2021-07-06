package parser;

import parser.visitor.Visitor;

public class ASTAssignmentNode implements ASTStatementNode {
	public ASTAssignmentNode(String identifier, ASTExprNode expr, int line_number) {
		this.identifier = identifier;
		this.expr = expr;
		this.line_number = line_number;
	}

	public String identifier;
	public ASTExprNode expr;
	public int line_number;

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}