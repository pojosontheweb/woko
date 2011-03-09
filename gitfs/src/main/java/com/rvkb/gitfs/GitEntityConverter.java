package com.rvkb.gitfs;

import java.io.InputStream;
import java.io.OutputStream;

public interface GitEntityConverter<T> {

  T fromStream(InputStream is);

  void toStream(T entity, OutputStream os);

}