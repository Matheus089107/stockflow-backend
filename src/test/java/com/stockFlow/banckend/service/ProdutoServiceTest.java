package com.stockFlow.banckend.service;

import com.stockFlow.banckend.exception.ValidacaoException;
import com.stockFlow.banckend.model.Produto;
import com.stockFlow.banckend.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    private Produto produtoValido;

    @BeforeEach
    void setUp() {
        produtoValido = Produto.builder()
                .id(1L)
                .nome("Teclado Mecânico")
                .quantidade(10)
                .precoUnitario(250.0)
                .build();
    }

    @Test
    void buscarTodos_DeveRetornarListaDeProdutos() {
        // Arrange
        List<Produto> produtos = Arrays.asList(produtoValido);
        when(produtoRepository.findAll()).thenReturn(produtos);

        // Act
        List<Produto> resultado = produtoService.buscarTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Teclado Mecânico", resultado.get(0).getNome());
        verify(produtoRepository, times(1)).findAll();
    }

    @Test
    void cadastrar_ComProdutoValido_DeveSalvarERetornar() {
        // Arrange
        when(produtoRepository.save(produtoValido)).thenReturn(produtoValido);

        // Act
        Produto resultado = produtoService.cadastrar(produtoValido);

        // Assert
        assertNotNull(resultado);
        assertEquals(produtoValido.getNome(), resultado.getNome());
        verify(produtoRepository, times(1)).save(produtoValido);
    }

    @Test
    void cadastrar_ComProdutoNulo_DeveLancarValidacaoException() {
        // Act & Assert
        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> {
            produtoService.cadastrar(null);
        });
        assertEquals("Os dados do produto não podem ser nulos", exception.getMessage());
        verify(produtoRepository, never()).save(any());
    }

    @Test
    void cadastrar_ComNomeNuloOuVazio_DeveLancarValidacaoException() {
        // Teste nulo
        Produto semNome = Produto.builder().quantidade(5).precoUnitario(10.0).build();
        ValidacaoException ex1 = assertThrows(ValidacaoException.class, () -> produtoService.cadastrar(semNome));
        assertEquals("O nome do produto não pode ser vazio ou nulo", ex1.getMessage());

        // Teste vazio
        Produto nomeVazio = Produto.builder().nome("   ").quantidade(5).precoUnitario(10.0).build();
        ValidacaoException ex2 = assertThrows(ValidacaoException.class, () -> produtoService.cadastrar(nomeVazio));
        assertEquals("O nome do produto não pode ser vazio ou nulo", ex2.getMessage());

        verify(produtoRepository, never()).save(any());
    }

    @Test
    void cadastrar_ComQuantidadeNegativa_DeveLancarValidacaoException() {
        Produto quantNegativa = Produto.builder().nome("Mouse").quantidade(-1).precoUnitario(50.0).build();

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> {
            produtoService.cadastrar(quantNegativa);
        });

        assertEquals("A quantidade do produto não pode ser menor que 0", exception.getMessage());
        verify(produtoRepository, never()).save(any());
    }

    @Test
    void cadastrar_ComPrecoMenorOuIgualAZero_DeveLancarValidacaoException() {
        // Teste menor que zero
        Produto precoNegativo = Produto.builder().nome("Monitor").quantidade(2).precoUnitario(-5.0).build();
        ValidacaoException ex1 = assertThrows(ValidacaoException.class, () -> produtoService.cadastrar(precoNegativo));
        assertEquals("O preço unitário do produto deve ser maior que 0", ex1.getMessage());

        // Teste igual a zero
        Produto precoZero = Produto.builder().nome("Monitor").quantidade(2).precoUnitario(0.0).build();
        ValidacaoException ex2 = assertThrows(ValidacaoException.class, () -> produtoService.cadastrar(precoZero));
        assertEquals("O preço unitário do produto deve ser maior que 0", ex2.getMessage());

        verify(produtoRepository, never()).save(any());
    }

    @Test
    void atualizar_ComIdExistenteEProdutoValido_DeveAtualizarERetornar() {
        // Arrange
        Long id = 1L;
        when(produtoRepository.existsById(id)).thenReturn(true);
        when(produtoRepository.save(produtoValido)).thenReturn(produtoValido);

        // Act
        Produto resultado = produtoService.atualizar(id, produtoValido);

        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        verify(produtoRepository, times(1)).existsById(id);
        verify(produtoRepository, times(1)).save(produtoValido);
    }

    @Test
    void atualizar_ComIdNaoExistente_DeveLancarValidacaoException() {
        // Arrange
        Long id = 99L;
        when(produtoRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> {
            produtoService.atualizar(id, produtoValido);
        });

        assertEquals("Produto não encontrado", exception.getMessage());
        verify(produtoRepository, times(1)).existsById(id);
        verify(produtoRepository, never()).save(any());
    }

    @Test
    void remover_ComIdExistente_DeveRemover() {
        // Arrange
        Long id = 1L;
        when(produtoRepository.existsById(id)).thenReturn(true);
        doNothing().when(produtoRepository).deleteById(id);

        // Act & Assert
        assertDoesNotThrow(() -> produtoService.remover(id));

        verify(produtoRepository, times(1)).existsById(id);
        verify(produtoRepository, times(1)).deleteById(id);
    }

    @Test
    void remover_ComIdNaoExistente_DeveLancarValidacaoException() {
        // Arrange
        Long id = 99L;
        when(produtoRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> {
            produtoService.remover(id);
        });

        assertEquals("Produto não encontrado", exception.getMessage());
        verify(produtoRepository, times(1)).existsById(id);
        verify(produtoRepository, never()).deleteById(any());
    }
}
