package com.example.services.dto;

import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.example.services.Model.Permissao;
import com.example.services.Model.Usuario;

// é uma classe dto (data transfer object) uma classe auxiliar de segurança para receber o formulario html apenas esses atributos
public class RequisicaoNovoUsuario {
	
	
	private Long codigo;
	
	@NotBlank
	private String nome;
	
	@NotBlank
	@Email
	private String email;
	
	@NotBlank
	private String senha;
	
	@ManyToMany(fetch = FetchType.EAGER)						//toda vez que eu buscar os usuario já traz as permissoes dele
	@JoinTable(name = "usuario_permissao",						//onde vou estar relacionando com a tabela Usuario_Permissao
	joinColumns = @JoinColumn (name = "codigo_usuario"),		//relacionamento principal 
	inverseJoinColumns = @JoinColumn(name = "codigo_permissao"))//relacionamento da classe <permissao>  
	private List<Permissao> permissoes;
	
	

	public List<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<Permissao> permissoes) {
		this.permissoes = permissoes;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		
		this.senha = senha;
	}
	
	
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public Usuario toUsuario() {
		Usuario usuario = new Usuario();
		usuario.setCodigo(this.codigo);
		usuario.setNome(this.nome);
		usuario.setEmail(this.email);
		usuario.setSenha(this.senha);
		usuario.setPermissoes(this.permissoes);
		
		return usuario;
	}
	
	public Usuario toUsuario(Usuario usuario) {		
		usuario.setCodigo(this.codigo);
		usuario.setNome(this.nome);
		usuario.setEmail(this.email);
		usuario.setSenha(this.senha);
		return usuario;
	}
	
	public void fromUsuario(Usuario usuario) {
		this.codigo = usuario.getCodigo();
		this.nome = usuario.getNome();
		this.email = usuario.getEmail();
		this.senha = usuario.getSenha();
	}

	@Override
	public String toString() {
		return "RequisicaoNovoUsuario [codigo=" + codigo + ", nome=" + nome + ", email=" + email + ", senha=" + senha
				+ ", permissoes=" + permissoes + "]";
	}

	
}