package br.com.portfolioManager.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.portfolioManager.Entity.Pessoa;
import br.com.portfolioManager.Service.PessoaService;
import br.com.portfolioManager.Service.ProjetoService;

@Controller
@RequestMapping("/pessoas")
public class PessoaController {

	private PessoaService service;

	@Autowired
	private ProjetoService projetoService;

	@SuppressWarnings("unused")
	private String result;

	@Autowired
	public PessoaController(PessoaService service) {
		this.service = service;
	}

	@GetMapping("/listar")
	public String listar(Model model) {
		List<Pessoa> lista = service.listarTodasPessoas();
		List<Pessoa> gerentesResponsaveis = projetoService.isGerenteResponsavel();
		model.addAttribute("pessoas", lista);
		model.addAttribute("gerentesResponsaveis", gerentesResponsaveis);
		return "listar-pessoas";
	}

	@GetMapping("/novo")
	public String novaPessoa(@RequestParam(value = "acao", required = false) String acao) {
		if (acao != null && acao.equals("atualizar")) {
			return "editar-pessoa";
		} else {
			return "cadastrar-pessoa";
		}
	}

	@PostMapping("/salvar")
	public String criarPessoa(Pessoa pessoa, RedirectAttributes attributes) {
		String mensagemRetorno = service.gravar(pessoa);
		if (mensagemRetorno.equals("CPF inválido")) {
			attributes.addFlashAttribute("pessoa", pessoa);
			attributes.addFlashAttribute("mensagem_error", "CPF inválido");
			return "redirect:/pessoas/novo";
		} else if (mensagemRetorno.equals("CPF existente")) {
			attributes.addFlashAttribute("pessoa", pessoa);
			attributes.addFlashAttribute("mensagem_error", "CPF já cadastrado na base de dados!");
			return "redirect:/pessoas/novo";
		} else if (mensagemRetorno.equals("Pessoa salva")) {
			attributes.addFlashAttribute("mensagem", "Pessoa salva com sucesso!");
		}
		return "redirect:/pessoas/listar";
	}

	@GetMapping("/editar/{id}")
	public String editarPessoa(@PathVariable Long id, Model model) {
		Pessoa pessoa = service.buscarPessoaPorId(id);
		model.addAttribute("pessoa", pessoa);
		return "editar-pessoa";
	}

	@PostMapping("/atualizar")
	public String atualizarPessoa(Pessoa pessoa, RedirectAttributes attributes) {
		String mensagemRetorno = service.atualizarPessoa(pessoa);
		if (mensagemRetorno.equals("CPF inválido")) {
			attributes.addFlashAttribute("pessoa", pessoa);
			attributes.addFlashAttribute("mensagem_error", "CPF inválido");
			return "redirect:/pessoas/novo?acao=atualizar";
		}
		attributes.addFlashAttribute("mensagem", "Pessoa atualizada com sucesso!");
		return "redirect:/pessoas/listar";
	}

	@PostMapping("/remover/{id}")
	public String excluirPessoa(@PathVariable Long id, RedirectAttributes attributes) {
		if (id != null) {
			this.service.removerPessoa(id);
			List<Pessoa> lista = this.service.listarTodasPessoas();
			attributes.addFlashAttribute("pessoas", lista);
			attributes.addFlashAttribute("mensagem", "Pessoa excluída com sucesso!");
			return "redirect:/pessoas/listar";
		}
		attributes.addFlashAttribute("mensagem_error", "Erro ao excluir pessoa, id null.");
		return "redirect:/pessoas/listar";
	}
}