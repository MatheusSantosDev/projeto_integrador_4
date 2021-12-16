package com.example.services.Controllers;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.services.Model.Comanda;
import com.example.services.Model.Mesa;
import com.example.services.Model.Pedido;
import com.example.services.Model.Produto;
import com.example.services.Model.Usuario;
import com.example.services.dto.ReqCaixa;
import com.example.services.dto.ReqComanda;
import com.example.services.dto.ReqNovoPedido;
import com.example.services.repositories.ComandaRepository;
import com.example.services.repositories.MesaRepository;
import com.example.services.repositories.PedidoRepository;
import com.example.services.repositories.ProdutoRepository;
import com.example.services.repositories.UsuarioRepository;

@Controller
@RequestMapping("/garcons")
public class GarconController {

	@Autowired
	private MesaRepository mesaRepository;

	@Autowired
	private ComandaRepository comandaRepository;

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ProdutoRepository produtoRepository;

	@GetMapping
	public String listarMesas(Model model) {
		// lista de mesa
		List<Mesa> mesas = this.mesaRepository.findAll();
		model.addAttribute("mesas", mesas);
		return "/garcon/dashboardG";
	}

	@GetMapping("/mesa/{id}")
	public ModelAndView listarPorMesa(@PathVariable Long id) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Optional<Usuario> usuarioop = usuarioRepository.findByEmail(auth.getName());
		Usuario usuario = usuarioop.get();
		Long idusu = usuario.getCodigo();

		List<Comanda> comandas = comandaRepository.comandasPorMesa(id, idusu);

		if (comandas.isEmpty()) {

			return new ModelAndView("redirect:/garcons");
		}

		ModelAndView mv = new ModelAndView("garcon/mesa");

		mv.addObject("comandas", comandas);
		mv.addObject("id", id);

		return mv;
	}

	@GetMapping("/comanda/{id}")
	public ModelAndView listarComanda(@PathVariable Long id) {

		if (comandaRepository.existsById(id)) {

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Optional<Usuario> usuarioop = usuarioRepository.findByEmail(auth.getName());
			Usuario usuario = usuarioop.get();
			Long idusu = usuario.getCodigo();

			List<Pedido> listaPedido = pedidoRepository.findPedidosByComandaIdAndUser(id, idusu);
			ModelAndView mv = new ModelAndView("garcon/pedido/comanda/listarPedidos");
			
			Optional<Comanda> comandaop = comandaRepository.findById(id);
			Comanda comanda = comandaop.get();
			
			mv.addObject("comanda", comanda);

			mv.addObject("pedidos", listaPedido);
			mv.addObject("id", id);

			return mv;
		} else {
			ModelAndView mv = new ModelAndView("garcon/pedido/comanda/erroComanda");
			return mv;
		}
	}

	// mapeamento de comandas para os garçons

	@GetMapping("/comandas/criar")
	public ModelAndView form() {
		ModelAndView mv = new ModelAndView("/garcon/CriarComanda");

		Comanda comanda = new Comanda();
		mv.addObject(comanda);

		// lista de mesa
		List<Mesa> mesas = this.mesaRepository.findAll();
		mv.addObject("mesa", mesas);

		return mv;
	}

	@GetMapping("/comandas/delete/{id}")
	public String deleteComandaForm(@PathVariable Long id) {
		this.comandaRepository.deleteById(id);
		return "redirect:/garcons/comandas";
	}

	@PostMapping("/comanda")
	public ModelAndView createComandas(@Valid ReqComanda reqcomanda, BindingResult bindingResult) {

		Comanda comanda = new Comanda();

		if (bindingResult.hasErrors()) {
			ModelAndView mv = new ModelAndView("/garcon/CriarComanda");
			return mv;
		} else {

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Optional<Usuario> usuarioop = usuarioRepository.findByEmail(auth.getName());
			Usuario usuario = usuarioop.get();

			comanda.setId(reqcomanda.getId());
			comanda.setMesa(reqcomanda.getMesa());
			comanda.setStatus("aberta");
			comanda.setUsuario(usuario);
			this.comandaRepository.save(comanda);
		}
				
		return new ModelAndView("redirect:/garcons/pedidos/comanda/" + comanda.getId() + "/listar");
	}

	

	@GetMapping("/comandas/{id}/editar")
	public ModelAndView edit(@PathVariable Long id, Comanda requisicao) {
		Optional<Comanda> optional = this.comandaRepository.findById(id);

		if (optional.isPresent()) {
			Comanda comanda = optional.get();
			ModelAndView mv = new ModelAndView("/garcon/CriarComanda");
			mv.addObject("comandaId", comanda.getId());
			return mv;

		} else {

			return new ModelAndView("redirect:/garcons/comandas/criar");

		}

	}

	@PostMapping("comandas/comanda/{id}")
	public ModelAndView update(@PathVariable Long id, @Valid Comanda requisicao, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {

			ModelAndView mv = new ModelAndView("/garcon/CriarComanda");
			mv.addObject("comandaId", id);
			return mv;

		} else {
			Optional<Comanda> optional = this.comandaRepository.findById(id);

			if (optional.isPresent()) {
				Comanda comanda = optional.get();
				comanda.setId(id);
				this.comandaRepository.save(comanda);
				return new ModelAndView("redirect:/garcons/comandas");
			} else {
				return new ModelAndView("redirect:/garcons/comandas");
			}
		}

	}

	@GetMapping("comandas/{id}/deletar")
	public String delete(@PathVariable Long id) {
		try {
			this.comandaRepository.deleteById(id);
			return "redirect:/usuarios";
		} catch (EmptyResultDataAccessException e) {
			return "redirect:/usuarios";
		}

	}

	// gerenciamento dos pedidos dos garçons

	@GetMapping("/pedidos/comanda/{id}/listar")
	public ModelAndView listar(@PathVariable Long id) {

		// verifica se a comanda existe

		if (comandaRepository.existsById(id)) {

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			Optional<Usuario> usuarioop = usuarioRepository.findByEmail(auth.getName());

			Usuario usuario = usuarioop.get();

			Long idusu = usuario.getCodigo();

			List<Pedido> listaPedido = pedidoRepository.findPedidosByComandaIdAndUser(id, idusu);
			ModelAndView mv = new ModelAndView("garcon/pedido/comanda/listarPedidos");
			
			Optional<Comanda> comandaop = comandaRepository.findById(id);
			Comanda comanda = comandaop.get();
			
			mv.addObject("comanda", comanda);

			mv.addObject("pedidos", listaPedido);
			mv.addObject("id", id);

			return mv;
		} else {
			ModelAndView mv = new ModelAndView("garcon/pedido/comanda/erroComanda");
			return mv;
		}

	}

	@GetMapping("/pedidos/comanda/{id}/criar")
	public ModelAndView form(@PathVariable Long id) {
		ModelAndView mv = new ModelAndView("garcon/pedido/comanda/CriarPedido");

		ReqNovoPedido pedido = new ReqNovoPedido();
		mv.addObject(pedido);
		mv.addObject("id", id);

		// lista de produtos
		List<Produto> produtos = this.produtoRepository.findAll();
		mv.addObject("produtos", produtos);

		return mv;
	}

	@PostMapping("pedidos/comanda/{id}")
	public ModelAndView create(@PathVariable Long id, @Valid ReqNovoPedido reqpedido, BindingResult bindingResult) {
		// binding result é um parametro do validation do spring utilizado em conjunto
		// com o @valid
		if (bindingResult.hasErrors()) {
			System.out.println(reqpedido);
			System.out.println(bindingResult.toString());
			ModelAndView mv = new ModelAndView("garcon/pedido/comanda/CriarPedido");
			return mv;
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
			return redirecionar(comanda.getId(), bindingResult);
		}

	}

	@RequestMapping(value = "criandoPedido/pedidos/comanda/listar", method = RequestMethod.GET)
	public ModelAndView redirecionar(Long id, BindingResult bindingResult) {
		ModelAndView mv = new ModelAndView("redirect:/garcons/pedidos/comanda/" + id + "/listar");
		return mv;
	}

	@GetMapping("/comanda/{id}/caixa")
	public ModelAndView visualizarCaixa(@PathVariable Long id) {
		if (!pedidoRepository.findPedidosByComandaId(id).isEmpty()) {

			ModelAndView mv = new ModelAndView("garcon/caixa/Caixa");

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			Optional<Usuario> usuarioop = usuarioRepository.findByEmail(auth.getName());

			Usuario usuario = usuarioop.get();

			Long idusu = usuario.getCodigo();

			List<Pedido> listaPedido = pedidoRepository.findPedidosByComandaIdAndUser(id, idusu);
			
			List<ReqCaixa> listaCaixa = new ArrayList<ReqCaixa>();

			for (int j = 0; j < listaPedido.size(); j++) {

				Optional<Produto> p = produtoRepository.findById(listaPedido.get(j).getProduto().getId());
				Produto pp = p.get();
				
				ReqCaixa caixa = new ReqCaixa();

				caixa.setNome(pp.getNome());
				caixa.setValor(pp.getValor());
				caixa.setMesa(listaPedido.get(j).getMesa().getNumero());
				caixa.setQuantidade(listaPedido.get(j).getQuantidade());			

				listaCaixa.add(caixa);
			}			
			
			mv.addObject("valorTotal",comandaRepository.findValorComanda(id));
			mv.addObject("listaCaixa",listaCaixa);
			mv.addObject("id",id);
			
			return mv;
		} else {

			ModelAndView mv = new ModelAndView("redirect:/garcons/comanda/"+id);
			return mv;

		}

	}
	
	@PostMapping("/comanda/{id}/finalizar")
	public ModelAndView finalizar(@PathVariable Long id) {
		if (!pedidoRepository.findPedidosByComandaId(id).isEmpty()) {			

			comandaRepository.fecharComanda(id);
			
			comandaRepository.pagarPedido(id);
			
			ModelAndView mv = new ModelAndView("redirect:/garcons");
			
			return mv;
		} else {

			ModelAndView mv = new ModelAndView("redirect:/garcons/comanda/"+id);
			return mv;

		}

	}
	
	
}
