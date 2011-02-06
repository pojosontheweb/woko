package woko.util.pexpressions;

public class ExpressionNodeWalker {

    public void walk(ExpressionNode node) {
        onNode(node);
        for (ExpressionNode en : node.getChildren()) {
            walk(en);
        }
    }

    public void onNode(ExpressionNode node) { }    

}
