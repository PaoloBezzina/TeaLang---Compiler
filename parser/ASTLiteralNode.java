package parser;

import parser.*;
import parser.visitor.Visitor;
import java.util.*;

/* 
This might still no be correct
 */

public class ASTLiteralNode<T> implements ASTExprNode{

    public ASTLiteralNode(T value, int line_number)
	{
        this.value = value;
        this.type = value.getClass().getSimpleName();
		this.line_number = line_number;
	}

    public T value;
    public String type;
	public int line_number;

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}