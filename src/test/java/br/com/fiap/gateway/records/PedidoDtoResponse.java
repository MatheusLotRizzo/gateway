package br.com.fiap.gateway.records;

import java.time.LocalDateTime;
import java.util.List;

public record PedidoDtoResponse(
        Long id,
        Long idCliente,
        StatusPedidoEnum statusPedido,
        LocalDateTime dataCriacao,
        List<ItemPedidoDtoResponse> itens
) {
}
