package cnsa.mergedate.common.exception;

import cnsa.mergedate.common.exception.code.ErrorCode;

public class TokenExpiredException extends BaseException {
    public TokenExpiredException() {
        super(ErrorCode.TOKEN_EXPIRED.getMessage());
    }
}
