package com.example.services.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class BasicSecurity extends WebSecurityConfigurerAdapter {
	
	private CustomAuthenticationSuccessHandler authenticationSuccessHandler;
	
	@Autowired
    public BasicSecurity (CustomAuthenticationSuccessHandler authenticationSuccessHandler) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		.and()
//		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) verificar

		http.authorizeRequests().antMatchers("/","/css/**", "/js/**", "/img/**").permitAll()//concede acesso a home para todos.
		//restrição de acessos
		.antMatchers("/usuarios").hasAnyRole("USUARIO")
		.antMatchers("/categorias").hasAnyRole("USUARIO")
		.antMatchers("/produtos").hasAnyRole("USUARIO")
		.antMatchers("/comandas/criar").hasAnyRole("USUARIO")
		.antMatchers("/dashAdm").hasAnyRole("USUARIO")
		.antMatchers("/dashAdm/comandas").hasAnyRole("USUARIO")
		.antMatchers("/garcons").hasAnyRole("USUARIO")
		.antMatchers("/cozinha/listar/adm").hasAnyRole("USUARIO")
		.antMatchers("/garcons").hasAnyRole("GARCOM")
		.antMatchers("/cozinha/listar").hasAnyRole("COZINHA")
	        .anyRequest().authenticated()// Para qualquer requisição (anyRequest) é preciso estar autenticado (authenticated).
	    .and()
	    .exceptionHandling()
        .accessDeniedPage("/acessoNegado")//pagina de acesso negado
        .and()
	    	.formLogin().loginPage("/entrar")// Aqui passamos a página customizada.
	    	.successHandler(authenticationSuccessHandler)//redirecionamento dependendo do tipo de usuario.
	    	.permitAll() //avisa o spring pra liberar o acesso a pagina customizada para todos.
		.and()
			.logout().logoutSuccessUrl("/entrar")//customização da url de logout, a url padrão causa bugs no login.
		.and()
			.httpBasic()
			.and().csrf().disable();			
		
		
	}
	
	
}

