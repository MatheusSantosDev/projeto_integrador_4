package com.example.services.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.services.Model.Cozinha;

public interface CozinhaRepository extends JpaRepository<Cozinha, Long>{
	
	@Query(value ="select pedido.id , produto.nome, pedido.quantidade, pedido.observacao, cozinha.finalizado from pedido, produto, cozinha"
			+ "  where pedido.produto_id = produto.id   and pedido.id = cozinha.cod_pedido and finalizado = 'nao'", nativeQuery = true)
	List<String> findPedidos();
	
	@Modifying
	@Transactional
	@Query(value = "update cozinha set finalizado = 'sim' where cod_pedido = :id_pedido", nativeQuery = true)
	void atualizaPedido(@Param("id_pedido") Long id_pedido);
}
