package woko.webtests.localization

class LocalizationTest extends WebTestBase {

    void testLocalization() {
        webtest("test localization") {

            // Test Home page UI for Guest user
            goToPage '/localization'
            verifyTitle 'Woko - LocalizationTitle'
            verifyText : 'This is an important message'
            verifyText 'Guest Home'
            verifyText 'Woko message (woko.guest.home.content) overridden from application resources'
        }
    }
}
