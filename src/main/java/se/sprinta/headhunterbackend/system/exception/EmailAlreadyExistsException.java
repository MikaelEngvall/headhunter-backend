package se.sprinta.headhunterbackend.system.exception;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String email) {
        super("Email '" + email + "' already exists.");
    }
}
