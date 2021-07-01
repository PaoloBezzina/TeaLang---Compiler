package parser;

import parser.*;
import parser.visitor.Visitor;
import java.util.*;

public class ASTIfNode implements ASTStatementNode {
	public ASTIfNode(ASTExprNode condition, ASTBlockNode if_block, int line_number) {
		this(condition, if_block, line_number, null);
	}

	public ASTIfNode(ASTExprNode condition, ASTBlockNode if_block, int line_number, ASTBlockNode else_block) {
		this.condition = condition;
		this.if_block = if_block;
		this.line_number = line_number;
		this.else_block = else_block;
	}

	public ASTExprNode condition;
	public ASTBlockNode if_block;
	public ASTBlockNode else_block;
	public int line_number;

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}