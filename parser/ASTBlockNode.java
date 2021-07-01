package parser;

import parser.*;
import parser.visitor.Visitor;
import java.util.*;

public class ASTBlockNode implements ASTStatementNode {
	public ASTBlockNode(ArrayList<ASTStatementNode> statements, int line_number)
	{
		this.statements = statements;
		this.line_number = line_number;
	}

	public ArrayList<ASTStatementNode> statements = new ArrayList<ASTStatementNode>();
	public int line_number;

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}