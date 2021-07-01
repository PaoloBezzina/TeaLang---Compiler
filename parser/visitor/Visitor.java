package parser.visitor;

public interface Visitor <T>{

	void visit(parser.ASTProgramNode UnnamedParameter);

	void visit(parser.ASTDeclarationNode UnnamedParameter);

	void visit(parser.ASTAssignmentNode UnnamedParameter);

	void visit(parser.ASTPrintNode UnnamedParameter);

	void visit(parser.ASTReturnNode UnnamedParameter);

	void visit(parser.ASTBlockNode UnnamedParameter);

	void visit(parser.ASTIfNode UnnamedParameter);

	void visit(parser.ASTWhileNode UnnamedParameter);

	void visit(parser.ASTFunctionDefinitionNode UnnamedParameter);

	void visit(parser.ASTLiteralNode<T> UnnamedParameter);

/* 	void visit(parser.ASTLiteralNode<Float> UnnamedParameter);

	void visit(parser.ASTLiteralNode<Boolean> UnnamedParameter);

	void visit(parser.ASTLiteralNode<String> UnnamedParameter); */

	void visit(parser.ASTBinaryExprNode UnnamedParameter);

	void visit(parser.ASTIdentifierNode UnnamedParameter);

	void visit(parser.ASTUnaryExprNode UnnamedParameter);

	void visit(parser.ASTFunctionCallNode UnnamedParameter);
}