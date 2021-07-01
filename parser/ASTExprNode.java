package parser;

import parser.*;
import parser.visitor.Visitor;
import java.util.*;

public interface ASTExprNode extends ASTNode {
	@Override
	void accept(Visitor UnnamedParameter);
}