package com.rvkb.gitfs

class MyGitEntityConverter extends GitEntityConverter<MyGitEntity> {

  @Override
  MyGitEntity fromStream(InputStream is) {
    MyGitEntity result = null
    is.withReader { r ->
      def line = r.readLine()
      result = new MyGitEntity([prop: line])
    }
    return result
  }

  @Override
  void toStream(MyGitEntity entity, OutputStream os) {
    os.withWriter { w ->
      w.write(entity.prop)
    }
  }


}
