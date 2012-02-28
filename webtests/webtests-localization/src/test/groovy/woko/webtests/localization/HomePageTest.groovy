package woko.webtests.localization

class HomePageTest extends WebTestBase {

    void testAuthenticationWithHome() {
        webtest("test home page") {

            // Test Home page UI for Guest user
            goToPage '/home'
            verifyTitle 'Woko - home'
            verifyText 'Guest Home'
            verifyText 'This is guest home !'
            verifyText 'You are not authenticated'

            // Test Home page UI for Admin user
            login()

            goToPage '/home'
            verifyTitle 'Woko - home'

            // Click 'Home" link in navbar
            clickLink href:'/woko-webtests/home'
            verifyTitle 'Woko - home'
            verifyText 'This is developer home !'
        }
    }

}
