package az.unibank.unitechapp.exception;


public class CustomErrorException extends RuntimeException {

    private final int code;
    private final String message;

    public CustomErrorException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getMessage());
        this.code=errorCodeEnum.getCode();
        this.message=errorCodeEnum.getMessage();
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
