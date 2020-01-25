package Model;

public class AuthTokenM {
    private String authToken;
    private String username;

    public AuthTokenM (String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof AuthTokenM) {
            AuthTokenM oAuthToken = (AuthTokenM) o;
            return oAuthToken.getUsername().equals(getUsername()) &&
                    oAuthToken.getAuthToken().equals(getAuthToken());
        } else {
            return false;
        }
    }
}
