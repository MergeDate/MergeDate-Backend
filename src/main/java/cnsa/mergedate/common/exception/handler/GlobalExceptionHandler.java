package cnsa.mergedate.common.exception.handler;

import cnsa.mergedate.common.exception.BaseException;
import cnsa.mergedate.common.exception.LoginRequiredException;
import cnsa.mergedate.common.exception.RefreshTokenNotValidatedException;
import cnsa.mergedate.common.response.ResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BaseException.class)
    public ResponseBody<Void> handleBaseException(BaseException e) {
        log.error("[BaseException] Message = {}", e.getMessage());
        return ResponseBody.fail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = RefreshTokenNotValidatedException.class)
    public ResponseBody<Void> handlerRefreshTokenNotValidatedException(
        RefreshTokenNotValidatedException e
    ) {
        log.error("[RefreshTokenNotValidatedException] Message = {}", e.getMessage());
        return ResponseBody.fail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = LoginRequiredException.class)
    public ResponseBody<Void> handlerLoginRequiredException(
        LoginRequiredException e
    ) {
        log.error("[LoginRequiredException] Message = {}", e.getMessage());
        return ResponseBody.fail(e.getMessage());
    }
}
