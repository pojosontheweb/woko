package woko2.inmemory

import woko2.users.User

class InMemoryUser implements User {

  String username
  String password
  List<String> roles

}
