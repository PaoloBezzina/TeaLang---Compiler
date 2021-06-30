package parser;

import parser.*;
import java.util.*;

public class ASTIfNode implements ASTStatementNode {
	// ORIGINAL LINE: ASTIfNode(ASTExprNode*, ASTBlockNode*, unsigned int, ASTBlockNode* = nullptr);
	public ASTIfNode(ASTExprNode condition, ASTBlockNode if_block, int line_number) {
		this(condition, if_block, line_number, null);
	}

	// C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above:
	// ORIGINAL LINE: ASTIfNode(ASTExprNode* condition, ASTBlockNode *if_block, uint line_number, ASTBlockNode *else_block = null) : condition(condition), if_block(if_block), line_number(line_number), else_block(else_block)
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
	public void accept(visitor.Visitor v) {
		v.visit(this);
	}
}