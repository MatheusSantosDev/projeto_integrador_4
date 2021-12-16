package com.example.services.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.services.repositories.ComandaRepository;
import com.example.services.repositories.MesaRepository;
import com.example.services.repositories.PedidoRepository;
import com.example.services.repositories.ProdutoRepository;
import com.example.services.repositories.UsuarioRepository;

@Controller
@RequestMapping("/comandas")
public class ComandaController {
	
	@Autowired
	private MesaRepository mesaRepository;
	
	@Autowired
	private ComandaRepository comandaRepository;
	
	@Autowired 
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@GetMapping
	public String listar(Model model) {
		List<Comanda> listaComandas = comandaRepository.findAll();
		model.addAttribute("listaComandas", listaComandas);
		return "/administracao/comanda/listarComanda";
	}
	
	@GetMapping("/comanda/{id}")
	public ModelAndView listarComanda(@PathVariable Long id) {

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
	
	@GetMapping("/criar")
	public ModelAndView form() {
		ModelAndView mv = new ModelAndView("/administracao/comanda/criaComanda");

		Comanda comanda = new Comanda();
		mv.addObject(comanda);
						
		// lista de mesa
		List<Mesa> mesas = this.mesaRepository.findAll();
		mv.addObject("mesa", mesas);
		
		return mv;
	}
	
	@GetMapping("delete/{id}")
	public String deleteComandaForm(@PathVariable Long id) {
	    this.comandaRepository.deleteById(id);
	    return "redirect:/comandas";       
	}
	
	
	@PostMapping("")	
	public ModelAndView create(@Valid ReqComanda reqcomanda, BindingResult bindingResult) {
		
		Comanda comanda = new Comanda();
		
		if (bindingResult.hasErrors()) {
			ModelAndView mv = new ModelAndView("/administracao/comanda/criaComanda");				
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
			// chamada do metodo que redireciona apos criar a comanda
		return redirecionar(comanda, bindingResult);
	}
	
	//metodo para redirecionar para tela de pedido assim que criar a comanda
	@RequestMapping(value = "/pedidos/comanda/listar", method = RequestMethod.GET)			
	public ModelAndView redirecionar(@Valid Comanda comanda, BindingResult bindingResult) {
		Long id = this.comandaRepository.findComanda();		
		ModelAndView mv = new ModelAndView("redirect:/pedidos/"+"comanda/"+id+"/listar");		
		return mv;
	}
	
	@GetMapping("/{id}/editar")
	public ModelAndView edit(@PathVariable Long id, Comanda requisicao) {
		Optional<Comanda> optional = this.comandaRepository.findById(id);

		if (optional.isPresent()) {
			Comanda comanda = optional.get();			
			ModelAndView mv = new ModelAndView("administracao/comanda/EditarComanda");			
			mv.addObject("comandaId", comanda.getId());
			return mv;

		} else {

			return new ModelAndView("redirect:/comandas");

		}

	}
	
	@PostMapping("/{id}")
	public ModelAndView update(@PathVariable Long id, @Valid Comanda requisicao,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {

			ModelAndView mv = new ModelAndView("administracao/comanda/editarComanda");
			mv.addObject("comandaId", id);
			return mv;
			
		} else {
			Optional<Comanda> optional = this.comandaRepository.findById(id);
			
			if(optional.isPresent()) {				
				Comanda comanda = optional.get();
				comanda.setId(id);
				this.comandaRepository.save(comanda);
				return new ModelAndView("redirect:/comandas");
			}else {
				return new ModelAndView("redirect:/comandas");
			}
		}

	}
	
	@GetMapping("/{id}/caixa")
	public ModelAndView visualizarCaixa(@PathVariable Long id) {
		if (!pedidoRepository.findPedidosByComandaId(id).isEmpty()) {

			ModelAndView mv = new ModelAndView("administracao/caixa/Caixa");

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

			ModelAndView mv = new ModelAndView("redirect:/pedidos/comanda/"+id+"/listar");
			return mv;

		}

	}
	
	
	@PostMapping("/{id}/finalizar")
	public ModelAndView finalizar(@PathVariable Long id) {
		if (!pedidoRepository.findPedidosByComandaId(id).isEmpty()) {			

			comandaRepository.fecharComanda(id);
			
			comandaRepository.pagarPedido(id);
			
			return  new ModelAndView("redirect:/dashAdm");
		} else {
			
			return new ModelAndView("redirect:/pedidos/comanda/"+id+"/listar");

		}

	}
		

}
