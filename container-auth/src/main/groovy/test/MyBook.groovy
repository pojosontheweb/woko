package test

class MyBook {

  String name
  String _id
  int nbPages
  Date creationTime = new Date()
  List<MyBook> listOfMe = [this, this, this]
  MyBook me = this
  List<String> listOfStrings = ["abd", "ddd"]
  
}
