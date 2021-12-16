package com.example.services.Controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.services.Model.Permissao;
import com.example.services.Model.Usuario;
import com.example.services.dto.RequisicaoNovoUsuario;
import com.example.services.repositories.PermissaoRepository;
import com.example.services.repositories.UsuarioRepository;

@Controller
@RequestMapping(value = "/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PermissaoRepository permissaoRepository;

	@GetMapping("")
	public ModelAndView listar() {

		List<Usuario> usuarios = this.usuarioRepository.findAll();
		ModelAndView mv = new ModelAndView("administracao/usuarios/ListaDeUsuarios");
		mv.addObject("usuarios", usuarios);
		return mv;
	}

	@GetMapping("/criar")
	public ModelAndView criar() {
		ModelAndView mv = new ModelAndView("administracao/usuarios/CriarUsuario");
		RequisicaoNovoUsuario requisicao = new RequisicaoNovoUsuario();
		mv.addObject(requisicao);

		// lista de permissoes
		List<Permissao> permissao = this.permissaoRepository.findAll();
		mv.addObject("permissoes", permissao);
		return mv;
	}

	@PostMapping("")
	public ModelAndView create(@Valid RequisicaoNovoUsuario requisicao, BindingResult bindingResult) {
		// binding result é um parametro do validation do spring utilizado em conjunto
		// com o @valid
		if (bindingResult.hasErrors()) {

			ModelAndView mv = new ModelAndView("administracao/usuarios/CriarUsuario");
			return mv;
		} else {
			Usuario usuario = requisicao.toUsuario();
			this.usuarioRepository.save(usuario);			
			return new ModelAndView("redirect:/usuarios");
		}
	}

	@GetMapping("/{id}/editar")
	public ModelAndView edit(@PathVariable Long id, RequisicaoNovoUsuario requisicao) {
		Optional<Usuario> optional = this.usuarioRepository.findById(id);

		if (optional.isPresent()) {
			Usuario usuario = optional.get();
			requisicao.fromUsuario(usuario);
			ModelAndView mv = new ModelAndView("administracao/usuarios/EditarUsuario");
			mv.addObject("usuarioId", usuario.getCodigo());
			return mv;

		} else {

			return new ModelAndView("redirect:/usuarios");

		}

	}

	@PostMapping("/{id}")
	public ModelAndView update(@PathVariable Long id, @Valid RequisicaoNovoUsuario requisicao,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {

			ModelAndView mv = new ModelAndView("administracao/usuarios/editarUsuario");
			mv.addObject("usuarioId", id);
			return mv;

		} else {
			Optional<Usuario> optional = this.usuarioRepository.findById(id);

			if (optional.isPresent()) {
				Usuario usuario = requisicao.toUsuario(optional.get());
				usuario.setCodigo(id);
				this.usuarioRepository.save(usuario);
				return new ModelAndView("redirect:/usuarios");
			} else {
				return new ModelAndView("redirect:/usuarios");
			}
		}

	}

	@GetMapping("{id}/deletar")
	public String delete(@PathVariable Long id) {
		try {
			this.usuarioRepository.deleteById(id);
			return "redirect:/usuarios";
		} catch (EmptyResultDataAccessException e) {
			System.out.println(e);
			return "redirect:/usuarios";
		}

	}
}
