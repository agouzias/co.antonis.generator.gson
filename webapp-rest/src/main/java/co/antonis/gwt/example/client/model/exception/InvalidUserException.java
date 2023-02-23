package co.antonis.gwt.example.client.model.exception;

import java.io.Serializable;

public class InvalidUserException extends RuntimeException implements Serializable {

    public enum UserExceptionType {
        Type_Unknown("Unknown", -1), /*The date in database is newer than the issue date of the token*/
        Type_token_invalid("Token Invalid", 0), /*The date-issue of token is old*/
        Type_token_expired("Expired Token", 1), /*The date-issue of token is old*/
        Type_invalid_username("Invalid Credentials", 2),  /*Invalid username or password*/
        Type_no_rights("Not permitted", 3), /*The user has no rights to execute the requested task*/
        Type_login_revalidate("Re-validation needed (forced)", 4), /*The date in database is newer than the issue date of the token*/
        Type_invalid_totp("Invalid or No TOTP (google-authenticator) is provided. A valid TOTP is required", 5), /*The user should provide totp*/
        Type_invalid_server_config("Invalid Server Config or no client ip", 6), /*Invalid server config*/
        Type_token_sso_email_not_configured_in_any_user("User SSO email is not registered", 7); /*Invalid server config*/

        String title;
        int type;

        UserExceptionType(String title, int type) {
            this.title = title;
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public String getTitle() {
            return title;
        }
    }

    public UserExceptionType type;

    public InvalidUserException() {
        type = UserExceptionType.Type_Unknown;
    }

    public InvalidUserException(String message, UserExceptionType type) {
        super(message);
        this.type = type;
    }

    public InvalidUserException(UserExceptionType type) {
        super(type.title);
        this.type = type;
    }

    public UserExceptionType getType() {
        return type;
    }

    public void setType(UserExceptionType type) {
        this.type = type;
    }


}
