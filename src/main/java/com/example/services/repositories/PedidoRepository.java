package com.example.services.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.services.Model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long>{

	@Query(value ="select * from pedido where comanda_id = :comanda_id and cod_usuario = :id_usuario", nativeQuery = true)
	List<Pedido> findPedidosByComandaIdAndUser(@Param("comanda_id") Long comanda_id, @Param("id_usuario") Long id_usuario);
	
	@Query(value ="select * from pedido where comanda_id = :comanda_id", nativeQuery = true)
	List<Pedido> findPedidosByComandaId(@Param("comanda_id") Long comanda_id);
	
	@Query(value ="select * from pedido, comanda where pedido.comanda_id = comanda.id and comanda.status = 'fechada' and pedido.cod_mesa = :mesa_id", nativeQuery = true)
	List<Pedido> findPedidosByMesa(@Param("mesa_id") Long mesa_id);
	
	@Query(value ="select SUM(valor_Total)  from fechamento_comanda, comanda where fechamento_comanda.comanda_id = comanda.id and comanda.cod_mesa = :mesa_id and comanda.status = 'fechada'", nativeQuery = true)
	double findValorTotalMesa(@Param("mesa_id") Long mesa_id);
}
