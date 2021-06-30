package parser;

import parser.*;
import java.util.*;

public interface ASTExprNode extends ASTNode {
	@Override
	void accept(visitor.Visitor UnnamedParameter);
}