package az.unibank.unitechapp.exception;

public enum ErrorCodeEnum {

    UNKNOWN_ERROR(1000," unkown error"),
    ALREADY_REGISTERED(1001,"pin already registered"),
    WRONG_CREDENTIALS(1002,"pin or password is wrong"),
    BALANCE_LOW(1003,"no enough money in my account balance"),
    TRANSFER_SAME_ACCOUNT(1004,"transfer to same account"),
    TRANSFER_DEACTIVE_ACCOUNT(1005,"transfer to deactive account"),
    TRANSFER_NON_EXISTING_ACCOUNT(1006,"transfer to non existing account"),
    UNAUTHORIZED(1008,"invalid token"),
    NOT_FOUND_ACCOUNT(1007,"not found account"),
    CURRENCY_CODE_NOT_FOUND(1009,"currency code is not right");

    private final int code;
    private final String message;

    ErrorCodeEnum(int code,String message) {
        this.code=code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    public int getCode() { return code; }



}
