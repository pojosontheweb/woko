package gitfs

import com.rvkb.gitfs.store.GitEntity

@GitEntity(keyProp="id")
class MyEntity {

  String id
  String prop1
  String prop2

}
