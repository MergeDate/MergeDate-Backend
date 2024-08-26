package cnsa.mergedate.common.exception;

import cnsa.mergedate.common.exception.code.ErrorCode;

public class MemberNotFoundException extends BaseException {
    public MemberNotFoundException() {
        super(ErrorCode.MEMBER_NOT_FOUND.getMessage());
    }
}
