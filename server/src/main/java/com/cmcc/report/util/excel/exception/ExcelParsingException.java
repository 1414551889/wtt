package com.cmcc.report.util.excel.exception;

public class ExcelParsingException extends RuntimeException {

    public ExcelParsingException(String message) {
        super(message);
    }

    public ExcelParsingException(String message, Exception exception) {
        super(message, exception);
    }

}
