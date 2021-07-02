package parser;

import parser.*;
import parser.visitor.Visitor;
import java.util.*;

public class ASTForNode implements ASTStatementNode {
    public ASTForNode(ASTExprNode expression, ASTAssignmentNode assignment, ASTBlockNode block, int line_number) {
		this(null, expression, assignment, block, line_number);
	}

	public ASTForNode(ASTDeclarationNode variable, ASTExprNode expression, ASTAssignmentNode assignment, ASTBlockNode block, int line_number) {
		this.variable = variable;
        this.expression = expression;
        this.assignment = assignment;
		this.block = block;
		this.line_number = line_number;
	}

	public ASTDeclarationNode variable;
	public ASTExprNode expression;
    public ASTAssignmentNode assignment;
    public ASTBlockNode block;
	public int line_number;

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}