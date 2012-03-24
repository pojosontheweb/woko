/*
 * Copyright 2001-2010 Remi Vankeisbelck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
