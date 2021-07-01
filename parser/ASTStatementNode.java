package parser;

import parser.*;
import parser.visitor.Visitor;
import java.util.*;

public interface ASTStatementNode extends ASTNode{
	@Override
	void accept(Visitor UnnamedParameter);
}