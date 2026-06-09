package com.stockFlow.banckend.controller;

import com.stockFlow.banckend.exception.ValidacaoException;
import com.stockFlow.banckend.model.Produto;
import com.stockFlow.banckend.model.RespostaDTO;
import com.stockFlow.banckend.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @GetMapping("/health")
    public ResponseEntity<RespostaDTO> health() {
        try {
            // Tenta consultar o banco para verificar a saúde do sistema
            produtoService.buscarTodos();
            RespostaDTO resposta = RespostaDTO.builder()
                    .sucesso(true)
                    .mensagem("Sistema e MySQL estão online.")
                    .dados("Healthcheck OK")
                    .erro(null)
                    .build();
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            RespostaDTO resposta = RespostaDTO.builder()
                    .sucesso(false)
                    .mensagem("O sistema está online, mas o MySQL está inacessível ou offline.")
                    .dados(null)
                    .erro(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(resposta);
        }
    }

    @GetMapping("/produtos")
    public ResponseEntity<RespostaDTO> listar() {
        List<Produto> produtos = produtoService.buscarTodos();
        RespostaDTO resposta = RespostaDTO.builder()
                .sucesso(true)
                .mensagem("Lista de produtos recuperada com sucesso.")
                .dados(produtos)
                .erro(null)
                .build();
        return ResponseEntity.ok(resposta);
    }

    @PostMapping("/produtos")
    public ResponseEntity<RespostaDTO> cadastrar(@RequestBody Produto produto) {
        Produto criado = produtoService.cadastrar(produto);
        RespostaDTO resposta = RespostaDTO.builder()
                .sucesso(true)
                .mensagem("Produto cadastrado com sucesso.")
                .dados(criado)
                .erro(null)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @PutMapping("/produtos/{id}")
    public ResponseEntity<RespostaDTO> atualizar(@PathVariable Long id, @RequestBody Produto produto) {
        Produto atualizado = produtoService.atualizar(id, produto);
        RespostaDTO resposta = RespostaDTO.builder()
                .sucesso(true)
                .mensagem("Produto atualizado com sucesso.")
                .dados(atualizado)
                .erro(null)
                .build();
        return ResponseEntity.ok(resposta);
    }

    @DeleteMapping("/produtos/{id}")
    public ResponseEntity<RespostaDTO> remover(@PathVariable Long id) {
        produtoService.remover(id);
        RespostaDTO resposta = RespostaDTO.builder()
                .sucesso(true)
                .mensagem("Produto removido com sucesso.")
                .dados(null)
                .erro(null)
                .build();
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/fornecedor/cep/{cep}")
    public ResponseEntity<RespostaDTO> buscarFornecedorPorCep(@PathVariable String cep) {
        if ("99999999".equals(cep)) {
            // Lança uma ValidacaoException customizada com HTTP Status 504 (Gateway Timeout)
            throw new ValidacaoException(
                    "Erro de timeout ao realizar integração externa com o fornecedor.",
                    HttpStatus.GATEWAY_TIMEOUT
            );
        }

        // Simulação de retorno estático de endereço correto
        Map<String, String> enderecoSimulado = Map.of(
                "cep", cep,
                "logradouro", "Avenida do Fluxo de Estoque, 777",
                "bairro", "Distrito Industrial",
                "localidade", "São Paulo",
                "uf", "SP",
                "fornecedor", "StockFlow Global Logistics Ltda"
        );

        RespostaDTO resposta = RespostaDTO.builder()
                .sucesso(true)
                .mensagem("Endereço do fornecedor localizado com sucesso.")
                .dados(enderecoSimulado)
                .erro(null)
                .build();
        return ResponseEntity.ok(resposta);
    }
}
