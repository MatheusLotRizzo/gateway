package br.com.fiap.gateway.records;

import java.time.LocalDateTime;

public record ClienteDtoResponse (
        Long codigoCliente,
        String nome,
        String cpf,
        String email,
        String cep,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado,
        String telefone,
        LocalDateTime dataCriacao
) {
}
