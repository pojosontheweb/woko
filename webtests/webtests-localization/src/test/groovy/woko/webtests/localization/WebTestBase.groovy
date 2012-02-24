package woko.webtests.localization

import woko.webtests.WokoWebTestBase

abstract class WebTestBase extends WokoWebTestBase {

    WebTestBase() {
        useContainerAuth = false
    }

}
