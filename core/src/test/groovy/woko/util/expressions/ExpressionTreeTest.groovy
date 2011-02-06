package woko.util.expressions

import junit.framework.TestCase
import woko.util.pexpressions.ExpressionTree
import woko.util.pexpressions.ExpressionNode

class ExpressionTreeTest extends TestCase {

  private void doForXPath(ExpressionTree et, String xPath, Closure c) {
    ExpressionNode en = et.getByXPath(xPath)
    assert en
    c.call(en)
  }

  public void testOneExpr() {
    ExpressionTree et = new ExpressionTree().
            addToTree('this.is.a.test=foo')

    println et

    // make sure all nodes have at most 1 child
    // and we have a total of 4 nodes
    int total = 0
    ExpressionNode en = et.root
    while (en) {
      assert en.children.size() <= 1
      en = en.children[0]
      total++
    }
    assert 4 == total

    doForXPath(et, '/this') { node ->
      assert node.name == 'this'
      assert node.children.size() == 1
    }

    doForXPath(et, '/this/is') { node ->
      assert node.name == 'is'
      assert node.children.size() == 1
    }

    doForXPath(et, '/this/is/a') { node ->
      assert node.name == 'a'
      assert node.children.size() == 1
    }

    doForXPath(et, '/this/is/a/test') { ExpressionNode node ->
      assert node.name == 'test'
      assert node.value == 'foo'
      assert node.children.size() == 0
      def pc = node.getParentChain()
      assert pc.size() == 3
    }

  }

  void testTwoExpr() {
    ExpressionTree et = new ExpressionTree().
      addToTree('this.is.a.test=foo').
      addToTree('this.is.a.schmuck=bar').
      addToTree('this.is.another.test=baz')

    println et

    doForXPath(et, '/this/is/a/test') { n->
      assert n.name == 'test'
    }

    doForXPath(et, '/this/is/a') { n->
      assert n.name == 'a'
      assert n.children[0].name == 'test'
      assert n.children[0].value == 'foo'
      assert n.children[1].name == 'schmuck'
      assert n.children[1].value == 'bar'
    }

    doForXPath(et, '/this/is') { n->
      assert n.children.size() == 2
    }

    doForXPath(et, '/this/is/another/test') { n->
      assert n.children.size() == 0
      assert n.value == "baz"
    }

  }

  void testTreeToMap() {
    ExpressionTree et = new ExpressionTree().
      addToTree('this.is.a.test=foo').
      addToTree('this.is.a.schmuck=1').
      addToTree('this.is.another.test=true')

    println et

    def m = et.toMap()

    println m

    assert m['this'].is.a.test == 'foo'
    assert m['this'].is.a.schmuck == 1
    assert m['this'].is.another.test == true    
  }

  void testIndexedProps1() {
    ExpressionTree et = new ExpressionTree().
            addToTree('baz.foo[0]=bar').
            addToTree('baz.foo[1]=12').
            addToTree('baz.foo[2]=true')
    println et
    def m = et.toMap()
    println m

    assert m.baz.foo[0] == 'bar'
    assert m.baz.foo[1] == 12
    assert m.baz.foo[2]
  }

  void testIndexedProps2() {
    ExpressionTree et = new ExpressionTree().
            addToTree('baz.foo[0].bar=1').
            addToTree('baz.foo[1].baz=schmuck')
    println et
    def m = et.toMap()
    println m

    assert m.baz.foo[0].bar == 1
    assert m.baz.foo[1].baz == 'schmuck'
  }

  void testIndexedPropsWrongOrder() {
    ExpressionTree et = new ExpressionTree().
            addToTree('baz.foo[1].bar=1').
            addToTree('baz.foo[0].baz=schmuck')
    println et
    def m = et.toMap()
    println m

    assert m.baz.foo[0].baz == 'schmuck'
    assert m.baz.foo[1].bar == 1
  }

  void testArrayValue() {
    ExpressionTree et = new ExpressionTree().
            addToTree('baz.foo.bar=[1,2,3]')
    println et
    def m = et.toMap()
    println m

    assert m.baz.foo.bar == [1,2,3]    
  }

  void testNestedArrayValue() {
    ExpressionTree et = new ExpressionTree().
            addToTree('baz.foo.bar=[1,[2,3]]')
    println et
    def m = et.toMap()
    println m

    assert m.baz.foo.bar == [1,[2,3]]    
  }

}
