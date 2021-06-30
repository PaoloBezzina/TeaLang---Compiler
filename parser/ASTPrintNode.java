package parser;

import parser.*;
import java.util.*;

public class ASTPrintNode implements ASTStatementNode {
	public ASTPrintNode(ASTExprNode expr, int line_number) {
		this.expr = expr;
		this.line_number = line_number;
	}

	public ASTExprNode expr;
	public int line_number;

	@Override
	public void accept(visitor.Visitor v) {
		v.visit(this);
	}
}