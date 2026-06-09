package com.stockFlow.banckend.exception;

import com.stockFlow.banckend.model.RespostaDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<RespostaDTO> handleValidacaoException(ValidacaoException ex) {
        RespostaDTO resposta = RespostaDTO.builder()
                .sucesso(false)
                .mensagem(ex.getMessage())
                .erro("Validação de regra de negócio falhou.")
                .dados(null)
                .build();
        return new ResponseEntity<>(resposta, ex.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RespostaDTO> handleGenericException(Exception ex) {
        RespostaDTO resposta = RespostaDTO.builder()
                .sucesso(false)
                .mensagem("Ocorreu um erro interno no servidor.")
                .erro(ex.getMessage())
                .dados(null)
                .build();
        return new ResponseEntity<>(resposta, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
