package com.example.services.dto;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.example.services.Model.Produto;

public class ReqNovoPedido {
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "produto_id")
	private Produto produto;
	
	@Min(1)
	private int quantidade;
	
	private String observacao;

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@Override
	public String toString() {
		return "ReqNovoPedido [produto=" + produto + ", quantidade=" + quantidade + ", observacao=" + observacao + "]";
	}
	
	
}
