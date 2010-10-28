package woko2.inmemory

import woko2.WokoInitServlet
import woko2.Woko

class InMemoryWokoInitServlet extends WokoInitServlet {

  Woko createWoko() {
    return new InMemoryWoko()
  }


}
