package com.toy.diary.comn.exception;

import com.toy.diary.comn.code.ErrorCode;
import com.toy.diary.comn.code.Level;
import com.toy.diary.comn.code.Source;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;

import java.util.Locale;

@Log4j2
public class CustomException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final Level level;

    private final Source source;

    private final ErrorCode errorCode;


    private final Object[] paramArray;

    private String sourceErrorCode;

    private String sourceErrorMessage;

    public CustomException(Throwable th, Level level, Source source, ErrorCode errorCode, Object... paramArray) {
        super(th != null ? th.getMessage() : errorCode.message(), th);
        this.level = level;
        this.errorCode = errorCode;
        this.source = source;
        this.paramArray = paramArray;
        this.sourceErrorCode = errorCode.code();
        this.sourceErrorMessage = errorCode.message();
    }

    public CustomException(Throwable th, Level level, ErrorCode errorCode, Object... paramArray) {
        this(th, level, Source.INTERNAL, errorCode, paramArray);
    }

    public CustomException(Throwable th, Source source, ErrorCode errorCode, Object... paramArray) {
        this(th, Level.WARN, source, errorCode, paramArray);
    }

    public CustomException(Throwable th, ErrorCode errorCode, Object... paramArray) {
        this(th, Level.WARN, Source.INTERNAL, errorCode, paramArray);
    }

    public CustomException(Level level, Source source, String errorCode, Object... paramArray) {
        this(null, level, source, errorCode, paramArray);
    }

    public CustomException(Level level, String errorCode, Object... paramArray) {
        this(null, level, Source.INTERNAL, errorCode, paramArray);
    }

    public CustomException(Source source, String errorCode, Object... paramArray) {
        this(null, Level.WARN, source, errorCode, paramArray);
    }

    public CustomException(ErrorCode errorCode, Object... paramArray) {
        this(null, Level.WARN, Source.INTERNAL, errorCode, paramArray);
    }
//	public BaseException(String errorCode) {
//		this(errorCode, new Object());
//	}

    public CustomException(Throwable th, ErrorCode erorCode, String sourceErrorMessage) {
        this(th, Level.WARN, ErrorCode.INTERNAL_SERVER_ERROR, (Object[])null);
        this.sourceErrorCode = erorCode.code();
        this.sourceErrorMessage = errorCode.message();
    }

    public CustomException(ErrorCode sourceErrorCode, String sourceErrorMessage) {
        this(null, sourceErrorCode, sourceErrorMessage);
    }

    @Deprecated
    public CustomException(Throwable th, Level level, ErrorCode errorCode, Source source, Object... paramArray) {
        this(th, level, source, errorCode, paramArray);
    }

    @Deprecated
    public CustomException(Level level, ErrorCode errorCode, Source source, Object... paramArray) {
        this(null, level, source, errorCode, paramArray);
    }

    public String getErrorCode() {
        return errorCode.code();
    }

    public Object[] getParamArray() {
        return paramArray;
    }

    public Level getLevel() {
        return level;
    }

    public Source getSource() {
        return source;
    }

    public String getErrorMessage(MessageSource ms) {
        if (ms == null) {
            return ErrorCode.INTERNAL_SERVER_ERROR.message();
        }
        log.debug("############ getErrorMessage     : {}",ms);
        log.debug("############ getErrorCode        : {}",getErrorCode());
        log.debug("############ getParamArray       : {}",getParamArray());
        log.debug("############ ErrorCode.NOT_REGIST_ERROR_CODE.message() : {}",ErrorCode.NOT_REGIST_ERROR_CODE.message());
        log.debug("############ Locale.getDefault() : {}", Locale.getDefault());
        return ms.getMessage(getErrorCode(), getParamArray(), ErrorCode.NOT_REGIST_ERROR_CODE.message(), Locale.getDefault());
    }

    public String getSourceErrorCode() {
        return sourceErrorCode;
    }

    public void setSourceErrorCode(String sourceErrorCode) {
        this.sourceErrorCode = sourceErrorCode;
    }

    public String getSourceErrorMessage() {
        return sourceErrorMessage;
    }

    public void setSourceErrorMessage(String sourceErrorMessage) {
        this.sourceErrorMessage = sourceErrorMessage;
    }
}
