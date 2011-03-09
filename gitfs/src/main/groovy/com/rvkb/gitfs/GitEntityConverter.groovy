package com.rvkb.gitfs

abstract class GitEntityConverter<T> {

  abstract T fromStream(InputStream is)

  abstract void toStream(T entity, OutputStream os)

}