package com.example.services.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.services.Model.Categoria;
import com.example.services.repositories.CategoriaRepository;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@GetMapping
	public ModelAndView listar() {
		ModelAndView modelView = new ModelAndView("administracao/categoria/listarCategorias");
		modelView.addObject("categorias", categoriaRepository.findAll());
		modelView.addObject(new Categoria());
		return modelView;
		
	}
	
	@GetMapping("delete/{id}")
	public String deleteCustomerForm(@PathVariable Long id) {
	    this.categoriaRepository.deleteById(id);
	    return "redirect:/categorias";       
	}
	
	@PostMapping
	public String salvar(Categoria categoria) {
		this.categoriaRepository.save(categoria);
		return "redirect:/categorias";
	}
	
}
