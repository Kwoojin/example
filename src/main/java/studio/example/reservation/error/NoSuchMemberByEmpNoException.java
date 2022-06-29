package studio.example.reservation.error;

public class NoSuchMemberByEmpNoException extends  RuntimeException{
    public NoSuchMemberByEmpNoException(String message) {
        super(message);
    }

    public NoSuchMemberByEmpNoException(String message, Throwable cause) {
        super(message, cause);
    }
}
