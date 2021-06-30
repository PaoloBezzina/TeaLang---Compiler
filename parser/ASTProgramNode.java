package parser;

import parser.*;
import java.util.*;

// Statement Nodes
public class ASTProgramNode implements ASTNode {

	// Program Node
	public ASTProgramNode(ArrayList<ASTNode> statements)
	{
		this.statements = statements;
	}

	public ArrayList<ASTNode> statements = new ArrayList<ASTNode>();

	@Override
	public void accept(visitor.Visitor v) {
		v.visit(this);
	}
}