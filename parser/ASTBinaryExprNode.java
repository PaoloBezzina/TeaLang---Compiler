package parser;

import parser.*;
import parser.visitor.Visitor;
import java.util.*;

public class ASTBinaryExprNode implements ASTExprNode{

    public ASTBinaryExprNode(String identifier, ASTExprNode left, ASTExprNode right, int line_number) {
        this.identifier = identifier;
		this.left = left;
		this.right = right;
		this.line_number = line_number;
	}

    public String identifier;
	public ASTExprNode left;
	public ASTExprNode right;
	public int line_number;

    @Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}
