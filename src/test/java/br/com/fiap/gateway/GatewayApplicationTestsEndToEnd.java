package br.com.fiap.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import br.com.fiap.gateway.records.ClienteDtoResponse;
import br.com.fiap.gateway.records.ProdutoDtoResponse;

@SpringBootTest
@AutoConfigureWebTestClient
class GatewayApplicationTestsEndToEnd {

	@Autowired
	private WebTestClient testClient;

	@Test
	void testClienteCriaNovoPedido() {
		final ClienteDtoResponse cliente = testClient.get().uri("/clientes/1")
			.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody(ClienteDtoResponse.class)
			.returnResult()
		.getResponseBody();
		
		
		final ProdutoDtoResponse produto1 = testClient.get().uri("/produtos/1")
			.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody(ProdutoDtoResponse.class)
			.returnResult()
		.getResponseBody();

		final String jsonPedido = String.format("""
			{
				"idCliente": %d,
				"itens": [
					{
					"idProduto": %d,
					"quantidade": 1
					}
				],
				"formaPagamento": "CARTO_DE_CREDITO",
				"quantidadeParcelas": 1,
				"enderecoPedido": {
					"cep": "%s",
					"logradouro": "%s",
					"numero": "%s",
					"complemento": "%s",
					"bairro": "%s",
					"cidade": "%s",
					"estado": "%s"
				}
				}
		""", 
			cliente.codigoCliente(), 
			produto1.id(),
			cliente.cep(),
			cliente.logradouro(),
			cliente.numero(),
			cliente.complemento(),
			cliente.bairro(),
			cliente.cidade(),
			cliente.estado()
		);

		testClient.post().uri("/pedidos")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(jsonPedido)
				.exchange()
				.expectStatus().isCreated();
	}

	@Test
	void testEntregaPedido(){
		final String json = """
			{
				"codigoPedido": 99,
				"codigoCliente": 88,
				"dataCriacao": "2024-05-19T19:52:47.581Z",
				"statusPedido": "AGUARDANDO_ENTREGA",
				"cep": "12345678",
				"numeroEndereco": "100",
				"complementoEndereco": "Apto 101",
				"produtos": [
				  {
					"codigoProduto": 1,
					"quantidade": 1
				  }
				]
			  }
		""";
			  
		testClient.post().uri("/logistica/processar/pedido")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(json)
				.exchange()
				.expectStatus().isNoContent()
			;

		testClient.put().uri("/atualizarEntrega")
			.body(BodyInserters
			.fromFormData("idEntrega", "4")
				.with("idEntregador", "3"))
				.exchange()
				.expectStatus().isNoContent()
			;
			
		testClient.put().uri("/encerrarEntrega")
			.body(BodyInserters.fromFormData("idEntrega", "4"))
				.exchange()
				.expectStatus().isNoContent()
			;

			System.out.println("uahsuhsa");
	}
}