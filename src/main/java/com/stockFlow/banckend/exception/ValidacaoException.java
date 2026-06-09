package com.stockFlow.banckend.exception;

import org.springframework.http.HttpStatus;

public class ValidacaoException extends RuntimeException {

    private final HttpStatus status;

    public ValidacaoException(String mensagem) {
        super(mensagem);
        this.status = HttpStatus.BAD_REQUEST;
    }

    public ValidacaoException(String mensagem, HttpStatus status) {
        super(mensagem);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return this.status;
    }
}
