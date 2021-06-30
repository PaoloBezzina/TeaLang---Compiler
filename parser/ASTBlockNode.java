package parser;

import parser.*;
import java.util.*;

public class ASTBlockNode implements ASTStatementNode {
	// C++ TO JAVA CONVERTER WARNING: Unsigned integer types have no direct
	// equivalent in Java:
	// ORIGINAL LINE: ASTBlockNode(std::vector<ASTStatementNode*>, unsigned int);
	public ASTBlockNode(ArrayList<ASTStatementNode> statements, int line_number)
	{
		this.statements = statements;
		this.line_number = line_number;
	}

	public ArrayList<ASTStatementNode> statements = new ArrayList<ASTStatementNode>();
	// C++ TO JAVA CONVERTER WARNING: Unsigned integer types have no direct
	// equivalent in Java:
	// ORIGINAL LINE: unsigned int line_number;
	public int line_number;

	@Override
	public void accept(visitor.Visitor v) {
		v.visit(this);
	}
}