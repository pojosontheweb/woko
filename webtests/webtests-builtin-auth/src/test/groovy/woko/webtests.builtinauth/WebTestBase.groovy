package woko.webtests.builtinauth

import woko.webtests.WokoWebTestBase

abstract class WebTestBase extends WokoWebTestBase {

    WebTestBase() {
        useContainerAuth = false
    }
}
