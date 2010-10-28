package woko2.util.pexpressions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class ExpressionTree {

    private ExpressionNode root = null;
    private List<String> exprs = new ArrayList<String>();

    public ExpressionNode getRoot() {
        return root;
    }

    public ExpressionTree addToTree(String expr) throws JSONException {
        ExpressionNode n = computePath(expr);
        if (n==null) {
            throw new IllegalArgumentException("Unable to compute path for expr " + expr);
        }
        if (root==null) {
            root = n;
        } else {
            merge(root, n);
        }
        exprs.add(expr);
        return this;
    }

    private void merge(ExpressionNode root, ExpressionNode n) {
        ExpressionNode child = n.getFirstChild();
        if (child!=null) {
            ExpressionNode rootChild = root.findChild(child.getName());
            if (rootChild==null) {
                // not yet created, merge
                root.getChildren().add(child);
            } else {
                // continue to dig !
                merge(rootChild, child);
            }
        }
    }

    private ExpressionNode computePath(String expr) throws JSONException {
        int indexOfEq = expr.indexOf("=");
        if (indexOfEq==-1) {
            throw new IllegalArgumentException("Expr " + expr + " is not valid (should be like p[.p]*=v)");
        }
        String left = expr.substring(0, indexOfEq);
        String right = expr.substring(indexOfEq+1, expr.length());
        String[] parts = left.split("\\.");
        ExpressionNode firstNode = null;
        ExpressionNode current = null;
        for (String part : parts) {
            ExpressionNode newNode = new ExpressionNode();
            newNode.setName(part);
            if (current!=null) {
                newNode.setParent(current);
                current.getChildren().add(newNode);
            }
            current = newNode;
            if (firstNode==null) {
                firstNode = newNode;
            }
        }
        if (current!=null) {
            current.setValue(stringToValue(right));
        }
        return firstNode;
    }

    private Object stringToValue(String str) throws JSONException {
        if (str.startsWith("[")) {
            // array
            JSONArray a = new JSONArray(str);
            List al = new ArrayList();
            for (int i=0 ; i<a.length() ; i++) {
                Object elem = a.get(i);
                if (elem!=null) {
                    al.add(stringToValue(elem.toString()));
                }
            }
            return al;
        }
        return JSONObject.stringToValue(str);
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder("Expression Tree ").
                append(super.toString()).
                append("\nExpressions :\n");
        for (String expr : exprs) {
            sb.append(" * ").append(expr).append('\n');
        }
        sb.append("Tree :\n");
        new ExpressionTreeWalker() {
            @Override
            public void onNode(ExpressionNode node) {
                List<ExpressionNode> parentChain = node.getParentChain();
                for (int i=0 ; i<parentChain.size() ; i++) {
                    sb.append("  ");
                }
                sb.append(node.getName());
                Object value = node.getValue();
                if (value!=null) {
                    sb.append("=").append(value.toString());    
                }
                sb.append('\n');
            }
        }.walk(this);
        return sb.toString();
    }

    public ExpressionNode getByXPath(String xpath) {
        StringTokenizer st = new StringTokenizer(xpath, "/");
        ExpressionNode en = root;
        String firstToken = st.nextToken();
        if (en.getName().equals(firstToken)) {
            while (st.hasMoreTokens() && en!=null) {
                String tk = st.nextToken();
                en = en.findChild(tk);
            }
        }
        return en;
    }

    public Map toMap() {
        HashMap res = new HashMap();
        res.put(root.getName(), root.toMap());
        return res;                        
    }
}
