package parser;

import parser.*;
import java.util.*;

public interface ASTStatementNode extends ASTNode {
	@Override
	void accept(visitor.Visitor UnnamedParameter);
}