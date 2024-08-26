package cnsa.mergedate.common.exception;

import cnsa.mergedate.common.exception.code.ErrorCode;

public class LoginRequiredException extends BaseException {
    public LoginRequiredException() {
        super(ErrorCode.LOGIN_REQUIRED.getMessage());
    }
}
