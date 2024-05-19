package br.com.fiap.gateway.records;

public record ProdutoDtoResponse(
        int id,
        String nome,
        String descricao,
        int quantidadeEstoque,
        double preco
) {
}