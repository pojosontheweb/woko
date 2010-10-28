package woko2.util.pexpressions;

public class ExpressionTreeWalker extends ExpressionNodeWalker {

    public void walk(ExpressionTree tree) {
        onTree(tree);
        walk(tree.getRoot());
    }

    public void onTree(ExpressionTree tree) { }

}
