package woko.mock;

import woko.users.UsernameResolutionStrategy;

import javax.servlet.http.HttpServletRequest;

public class MockUsernameResolutionStrategy implements UsernameResolutionStrategy {

    private String username;

    public MockUsernameResolutionStrategy(String username) {
        this.username = username;
    }

    public MockUsernameResolutionStrategy setUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public String getUsername(HttpServletRequest request) {
        return username;
    }
}
