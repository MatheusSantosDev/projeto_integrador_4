package com.example.services.Security.Util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeradorDeSenha {
	
	/*public static void main(String[] args) {
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String senha = encoder.encode("admin");
		
		System.out.println("Senha Encodada : { " + senha + " }");
	}*/
	
	public GeradorDeSenha () {}
	
	public String EncryptaSenhaUser(String senha) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encrypta = encoder.encode(senha);
		return encrypta;
	}

}
