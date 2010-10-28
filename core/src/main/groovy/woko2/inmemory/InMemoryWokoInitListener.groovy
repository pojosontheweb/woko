package woko2.inmemory

import woko2.WokoInitListener
import woko2.Woko

class InMemoryWokoInitListener extends WokoInitListener {

  Woko createWoko() {
    return new InMemoryWoko()
  }


}
