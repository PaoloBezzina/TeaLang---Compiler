package parser;

import parser.*;
import java.util.*;

public class ASTWhileNode implements ASTStatementNode {
	public ASTWhileNode(ASTExprNode condition, ASTBlockNode block, int line_number) {
		this.condition = condition;
		this.block = block;
		this.line_number = line_number;
	}

	public ASTExprNode condition;
	public ASTBlockNode block;
	public int line_number;

	@Override
	public void accept(visitor.Visitor v) {
		v.visit(this);
	}
}