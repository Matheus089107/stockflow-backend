package com.stockFlow.banckend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RespostaDTO {

    private boolean sucesso;
    private String mensagem;
    private Object dados;
    private String erro;
}
