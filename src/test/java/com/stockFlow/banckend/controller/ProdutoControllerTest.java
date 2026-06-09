package com.stockFlow.banckend.controller;

import tools.jackson.databind.ObjectMapper;
import com.stockFlow.banckend.exception.ValidacaoException;
import com.stockFlow.banckend.model.Produto;
import com.stockFlow.banckend.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProdutoService produtoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Produto produtoValido;

    @BeforeEach
    void setUp() {
        produtoValido = Produto.builder()
                .id(1L)
                .nome("Mouse Gamer")
                .quantidade(25)
                .precoUnitario(120.0)
                .build();
    }

    @Test
    void health_DeveRetornarSucessoSeBancoOnline() throws Exception {
        when(produtoService.buscarTodos()).thenReturn(Arrays.asList(produtoValido));

        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sucesso", is(true)))
                .andExpect(jsonPath("$.mensagem", is("Sistema e MySQL estão online.")))
                .andExpect(jsonPath("$.dados", is("Healthcheck OK")));
    }

    @Test
    void health_DeveRetornarServiceUnavailableSeBancoOffline() throws Exception {
        when(produtoService.buscarTodos()).thenThrow(new RuntimeException("Conexão recusada"));

        mockMvc.perform(get("/api/health"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.sucesso", is(false)))
                .andExpect(jsonPath("$.mensagem", is("O sistema está online, mas o MySQL está inacessível ou offline.")))
                .andExpect(jsonPath("$.erro", is("Conexão recusada")));
    }

    @Test
    void listar_DeveRetornarListaDeProdutos() throws Exception {
        when(produtoService.buscarTodos()).thenReturn(Arrays.asList(produtoValido));

        mockMvc.perform(get("/api/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sucesso", is(true)))
                .andExpect(jsonPath("$.mensagem", is("Lista de produtos recuperada com sucesso.")))
                .andExpect(jsonPath("$.dados", hasSize(1)))
                .andExpect(jsonPath("$.dados[0].nome", is("Mouse Gamer")));
    }

    @Test
    void cadastrar_ComDadosValidos_DeveRetornarCreated() throws Exception {
        when(produtoService.cadastrar(any(Produto.class))).thenReturn(produtoValido);

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoValido)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sucesso", is(true)))
                .andExpect(jsonPath("$.mensagem", is("Produto cadastrado com sucesso.")))
                .andExpect(jsonPath("$.dados.nome", is("Mouse Gamer")));
    }

    @Test
    void cadastrar_ComErroDeValidacao_DeveRetornarBadRequest() throws Exception {
        when(produtoService.cadastrar(any(Produto.class)))
                .thenThrow(new ValidacaoException("O nome do produto não pode ser vazio ou nulo"));

        Produto produtoInvalido = Produto.builder().quantidade(10).precoUnitario(5.0).build();

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.sucesso", is(false)))
                .andExpect(jsonPath("$.mensagem", is("O nome do produto não pode ser vazio ou nulo")))
                .andExpect(jsonPath("$.erro", is("Validação de regra de negócio falhou.")));
    }

    @Test
    void atualizar_ComIdExistenteEDadosValidos_DeveRetornarOk() throws Exception {
        when(produtoService.atualizar(eq(1L), any(Produto.class))).thenReturn(produtoValido);

        mockMvc.perform(put("/api/produtos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoValido)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sucesso", is(true)))
                .andExpect(jsonPath("$.mensagem", is("Produto atualizado com sucesso.")))
                .andExpect(jsonPath("$.dados.id", is(1)));
    }

    @Test
    void remover_ComIdExistente_DeveRetornarOk() throws Exception {
        doNothing().when(produtoService).remover(1L);

        mockMvc.perform(delete("/api/produtos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sucesso", is(true)))
                .andExpect(jsonPath("$.mensagem", is("Produto removido com sucesso.")));
    }

    @Test
    void buscarFornecedorPorCep_ComCepValido_DeveRetornarOk() throws Exception {
        mockMvc.perform(get("/api/fornecedor/cep/01001000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sucesso", is(true)))
                .andExpect(jsonPath("$.mensagem", is("Endereço do fornecedor localizado com sucesso.")))
                .andExpect(jsonPath("$.dados.cep", is("01001000")))
                .andExpect(jsonPath("$.dados.fornecedor", is("StockFlow Global Logistics Ltda")));
    }

    @Test
    void buscarFornecedorPorCep_ComCepDeTimeout_DeveRetornarGatewayTimeout() throws Exception {
        mockMvc.perform(get("/api/fornecedor/cep/99999999"))
                .andExpect(status().isGatewayTimeout())
                .andExpect(jsonPath("$.sucesso", is(false)))
                .andExpect(jsonPath("$.mensagem", is("Erro de timeout ao realizar integração externa com o fornecedor.")))
                .andExpect(jsonPath("$.erro", is("Validação de regra de negócio falhou.")));
    }
}
