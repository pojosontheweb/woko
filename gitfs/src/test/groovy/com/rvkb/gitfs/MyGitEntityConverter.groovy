package com.rvkb.gitfs

class MyGitEntityConverter implements GitEntityConverter {

  def fromStream(InputStream is) {
    MyGitEntity result = null
    is.withReader { r ->
      def line = r.readLine()
      result = new MyGitEntity([prop: line])
    }
    return result
  }

  void toStream(def entity, OutputStream os) {
    os.withWriter { w ->
      w.write(entity.prop)
    }
  }


}
