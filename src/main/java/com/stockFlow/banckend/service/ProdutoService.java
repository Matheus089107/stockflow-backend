package com.stockFlow.banckend.service;

import com.stockFlow.banckend.exception.ValidacaoException;
import com.stockFlow.banckend.model.Produto;
import com.stockFlow.banckend.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public List<Produto> buscarTodos() {
        return produtoRepository.findAll();
    }

    public Produto cadastrar(Produto p) {
        validarProduto(p);
        return produtoRepository.save(p);
    }

    public Produto atualizar(Long id, Produto p) {
        if (!produtoRepository.existsById(id)) {
            throw new ValidacaoException("Produto não encontrado");
        }
        validarProduto(p);
        p.setId(id);
        return produtoRepository.save(p);
    }

    public void remover(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new ValidacaoException("Produto não encontrado");
        }
        produtoRepository.deleteById(id);
    }

    private void validarProduto(Produto p) {
        if (p == null) {
            throw new ValidacaoException("Os dados do produto não podem ser nulos");
        }
        if (p.getNome() == null || p.getNome().trim().isEmpty()) {
            throw new ValidacaoException("O nome do produto não pode ser vazio ou nulo");
        }
        if (p.getQuantidade() == null || p.getQuantidade() < 0) {
            throw new ValidacaoException("A quantidade do produto não pode ser menor que 0");
        }
        if (p.getPrecoUnitario() == null || p.getPrecoUnitario() <= 0) {
            throw new ValidacaoException("O preço unitário do produto deve ser maior que 0");
        }
    }
}
