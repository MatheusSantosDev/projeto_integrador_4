package com.example.services.Controllers;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.services.Model.Comanda;
import com.example.services.Model.Pedido;
import com.example.services.Model.Produto;
import com.example.services.Model.Usuario;
import com.example.services.dto.ReqNovoPedido;
import com.example.services.repositories.ComandaRepository;
import com.example.services.repositories.PedidoRepository;
import com.example.services.repositories.ProdutoRepository;
import com.example.services.repositories.UsuarioRepository;

@Controller
@RequestMapping("/pedidos/comanda")
public class PedidoController {

	@Autowired
	private ComandaRepository comandaRepository;

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@GetMapping("/{id}/listar")
	public ModelAndView listar(@PathVariable Long id) {

		// verifica se a comanda existe

		if (comandaRepository.existsById(id)) {

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			Optional<Usuario> usuarioop = usuarioRepository.findByEmail(auth.getName());

			Usuario usuario = usuarioop.get();

			Long idusu = usuario.getCodigo();

			List<Pedido> listaPedido = pedidoRepository.findPedidosByComandaIdAndUser(id, idusu);
			ModelAndView mv = new ModelAndView("administracao/pedido/comanda/listarPedidos");
			
			Optional<Comanda> comandaop = comandaRepository.findById(id);
			Comanda comanda = comandaop.get();
			
			mv.addObject("comanda", comanda);
			

			mv.addObject("pedidos", listaPedido);
			mv.addObject("id", id);

			return mv;
		} else {
			ModelAndView mv = new ModelAndView("administracao/pedido/comanda/erroComanda");
			return mv;
		}

	}

	@GetMapping("{id}/criar")
	public ModelAndView form(@PathVariable Long id) {
		ModelAndView mv = new ModelAndView("administracao/pedido/comanda/CriarPedido");

		ReqNovoPedido pedido = new ReqNovoPedido();
		mv.addObject(pedido);
		mv.addObject("id", id);

		// lista de produtos
		List<Produto> produtos = this.produtoRepository.findAll();
		mv.addObject("produtos", produtos);

		return mv;
	}

	@PostMapping("/{id}")
	public ModelAndView create(@PathVariable Long id, @Valid ReqNovoPedido reqpedido, BindingResult bindingResult) {
		// binding result é um parametro do validation do spring utilizado em conjunto
		// com o @valid
		if (bindingResult.hasErrors()) {			
			
			return new ModelAndView("redirect:/pedidos/comanda/"+id+"/criar");
			
		} else {

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Optional<Usuario> usuarioop = usuarioRepository.findByEmail(auth.getName());
			Usuario usuario = usuarioop.get();
			
			
			Pedido pedido = new Pedido();

			pedido.setProduto(reqpedido.getProduto());
			pedido.setQuantidade(reqpedido.getQuantidade());
			pedido.setObservacao(reqpedido.getObservacao());

			Optional<Comanda> optional = this.comandaRepository.findById(id);
			Comanda comanda = optional.get();
			pedido.setComanda(comanda);
			pedido.setMesa(comanda.getMesa());
			pedido.setHoraDoPedido(OffsetDateTime.now());
			pedido.setStatus("preparacao");
			pedido.setUsuario(usuario);

			this.pedidoRepository.save(pedido);
			
			return new ModelAndView("redirect:/pedidos/" + "comanda/" + id + "/listar");
		}
	}
}
