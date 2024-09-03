package cnsa.mergedate.common.exception;

import cnsa.mergedate.common.exception.code.ErrorCode;

public class EventNotFoundException extends BaseException {
    public EventNotFoundException() {
        super(ErrorCode.EVENT_NOT_FOUND.getMessage());
    }
}
