package woko.util.pexpressions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpressionNode {

    private String name;
    private ExpressionNode parent;
    private List<ExpressionNode> children = new ArrayList<ExpressionNode>();
    private Object value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public ExpressionNode getParent() {
        return parent;
    }

    public void setParent(ExpressionNode parent) {
        this.parent = parent;
    }

    public List<ExpressionNode> getChildren() {
        return children;
    }

    public void setChildren(List<ExpressionNode> children) {
        this.children = children;
    }

    public ExpressionNode getFirstChild() {
        if (children.size()>0) {
            return children.get(0);
        }
        return null;
    }

    public ExpressionNode findChild(String name) {
        for (ExpressionNode en : children) {
            if (en.getName().equals(name)) {
                return en;
            }
        }
        return null;
    }

    public List<ExpressionNode> getParentChain() {
        ArrayList<ExpressionNode> res = new ArrayList<ExpressionNode>();
        ExpressionNode p = parent;
        while (p!=null) {
            res.add(p);
            p = p.getParent();
        }
        return res;        
    }

    public Object toMap() {
        if (value==null) {
            HashMap res = new HashMap();
            for (ExpressionNode child : children) {
                String name = child.getName();
                int i = name.indexOf("[");
                if (i!=-1) {
                    // array handling
                    String propName = name.substring(0, i);
                    String indexPart = name.substring(i, name.length());
                    indexPart = indexPart.substring(1, indexPart.length()-1);
                    int index = Integer.parseInt(indexPart);
                    List l = (List)res.get(propName);
                    if (l==null) {
                        l = new ArrayList();
                        res.put(propName, l);
                    }
                    while (l.size()<index+1) {
                        l.add(null);
                    }                    
                    l.set(index, child.toMap());
                } else {
                    res.put(child.getName(), child.toMap());
                }
            }
            return res;
        } else {
            return value;
        }
    }
}
