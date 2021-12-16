package com.example.services.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.services.Model.Produto;
import com.example.services.repositories.ProdutoRepository;

@Controller
public class HomeController {
	
	@GetMapping("/")
	public ModelAndView home() {
		
		ModelAndView mv = new ModelAndView("home");
		
		return mv;
	}
	
	@GetMapping("/entrar")
    public String entrar() {
        return "entrar";
    }
	
	@GetMapping("/acessoNegado")
    public String negar() {
        return "acessoNegado";
    }
	
}
