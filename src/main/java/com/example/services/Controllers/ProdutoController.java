package com.example.services.Controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.services.Model.Categoria;
import com.example.services.Model.Produto;
import com.example.services.repositories.CategoriaRepository;
import com.example.services.repositories.ProdutoRepository;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@GetMapping
	public ModelAndView listar() {
		ModelAndView modelView = new ModelAndView("administracao/produto/listarProdutos");
		modelView.addObject("produtos",produtoRepository.findAll());
		return modelView;
	}
	
	@GetMapping("delete/{id}")
	public String deleteProdutoForm(@PathVariable Long id) {
	    this.produtoRepository.deleteById(id);
	    return "redirect:/produtos";       
	}
	
	@GetMapping("/criar")
	public ModelAndView formulario () {
		ModelAndView modelView = new ModelAndView("administracao/produto/produtoForm");
		Produto produto = new Produto();
		modelView.addObject(produto);
		
		// Adiciono minha lista de categorias
		List<Categoria> categorias = this.categoriaRepository.findAll();
		modelView.addObject("categorias",categorias);
		
		return modelView;
	}
	
	@PostMapping
	public String salvar(@Valid Produto produto) {
		this.produtoRepository.save(produto);
		return "redirect:/produtos";
	}
	
}
