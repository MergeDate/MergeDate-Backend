package cnsa.mergedate.common.exception;

import cnsa.mergedate.common.exception.code.ErrorCode;

public class RefreshTokenNotValidatedException extends BaseException {
    public RefreshTokenNotValidatedException() {
        super(ErrorCode.REFRESH_TOKEN_NOT_VALIDATED.getMessage());
    }
}
