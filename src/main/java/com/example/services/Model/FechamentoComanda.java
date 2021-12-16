package com.example.services.Model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "fechamento_comanda")
public class FechamentoComanda {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idfechamento_comanda")
	private Long id;
	
	@NotNull
	@ManyToOne 						// N para 1, mesa pode ter um pedido ou mais
	@JoinColumn(name = "cod_pedido")
	private Pedido pedido;
	
	@Column(name = "hora_de_fechamento")
	private OffsetDateTime horaFechamento;
	
	@NotNull
	@Column(name = "valor_Total")
	private BigDecimal valorTotal;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public OffsetDateTime getHoraFechamento() {
		return horaFechamento;
	}

	public void setHoraFechamento(OffsetDateTime horaFechamento) {
		this.horaFechamento = horaFechamento;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((horaFechamento == null) ? 0 : horaFechamento.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((pedido == null) ? 0 : pedido.hashCode());
		result = prime * result + ((valorTotal == null) ? 0 : valorTotal.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FechamentoComanda other = (FechamentoComanda) obj;
		if (horaFechamento == null) {
			if (other.horaFechamento != null)
				return false;
		} else if (!horaFechamento.equals(other.horaFechamento))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (pedido == null) {
			if (other.pedido != null)
				return false;
		} else if (!pedido.equals(other.pedido))
			return false;
		if (valorTotal == null) {
			if (other.valorTotal != null)
				return false;
		} else if (!valorTotal.equals(other.valorTotal))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FechamentoComanda [id=" + id + ", pedido=" + pedido + ", horaFechamento=" + horaFechamento
				+ ", valorTotal=" + valorTotal + "]";
	}
	
}
