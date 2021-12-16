package com.example.services.dto;

import java.time.LocalTime;

public class ReqCozinha {

	private Long id;

	private String nome;

	private int quantidade;

	private String observacao;

	private String status;

	private LocalTime horaPedido;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalTime getHoraPedido() {
		return horaPedido;
	}

	public void setHoraPedido(LocalTime horaPedido) {
		this.horaPedido = horaPedido;
	}

	public int verificaTempo() {

		LocalTime horaPedido = this.horaPedido;

		LocalTime horaAgora = LocalTime.now();

		LocalTime diferenca = null;

		double minutos = 0;

		// adiciona somente os minutos caso não tenha passado uma hora
		if (horaAgora.getHour() > horaPedido.getHour() && horaAgora.getMinute() < horaPedido.getMinute()) { 

			diferenca = horaPedido.minusMinutes(horaPedido.getMinute());

			minutos += diferenca.getMinute();

		} else { // adiciona as horas e os minutos à diferença

			diferenca = horaAgora.minusHours(horaPedido.getHour()).minusMinutes(horaPedido.getMinute());

			minutos = diferenca.getHour() * 60;

			minutos += diferenca.getMinute();

		}

		if (minutos <= 30) {

			return 1;

		} else if (minutos <= 60) {

			return 2;
		}

		return 3;
	}

}
