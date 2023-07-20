package br.com.portfolioManager.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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

	private final PessoaService pessoaService;
	private final ProjetoService projetoService;

	@Autowired
	public PessoaController(PessoaService pessoaService, ProjetoService projetoService) {
		this.pessoaService = pessoaService;
		this.projetoService = projetoService;
	}

	@GetMapping("/listar")
	public String listar(Model model) {
		List<Pessoa> lista = pessoaService.listarTodasPessoas();
		List<Pessoa> gerentesResponsaveis = projetoService.listaGerenteResponsavel();
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
	public String criarPessoa(Pessoa pessoa, BindingResult bindingResult, RedirectAttributes attributes) {
		String mensagemRetorno = pessoaService.gravar(pessoa);
		if (mensagemRetorno == null) {
			if (bindingResult.hasErrors()) {
				for (FieldError error : bindingResult.getFieldErrors()) {
					if (error.getField().equals("dataNascimento") && error.getCode().equals("typeMismatch")) {
						attributes.addFlashAttribute("pessoa", pessoa);
						attributes.addFlashAttribute("mensagem_error",
								"Formato de data incorreto. Por favor, insira uma data válida no formato DD/MM/AAAA.");
						return "redirect:/pessoas/novo";
					}
				}
			}
		} else if (mensagemRetorno.equals("CPF invalido")) {
			attributes.addFlashAttribute("pessoa", pessoa);
			attributes.addFlashAttribute("mensagem_error", "CPF inválido.");
			return "redirect:/pessoas/novo";
		} else if (mensagemRetorno.equals("CPF existente")) {
			attributes.addFlashAttribute("pessoa", pessoa);
			attributes.addFlashAttribute("mensagem_error", "CPF já cadastrado na base de dados.");
			return "redirect:/pessoas/novo";
		} else if (mensagemRetorno.equals("Pessoa salva")) {
			attributes.addFlashAttribute("mensagem", "Pessoa salva com sucesso!");
		}
		return "redirect:/pessoas/listar";
	}

	@GetMapping("/editar/{id}")
	public String editarPessoa(@PathVariable Long id, Model model) {
		Pessoa pessoa = pessoaService.buscarPessoaPorId(id);
		model.addAttribute("pessoa", pessoa);
		return "editar-pessoa";
	}

	@PostMapping("/atualizar")
	public String atualizarPessoa(Pessoa pessoa, BindingResult bindingResult, RedirectAttributes attributes) {
		if (bindingResult.hasErrors()) {
			for (FieldError error : bindingResult.getFieldErrors()) {
				if (error.getField().equals("dataNascimento") && error.getCode().equals("typeMismatch")) {
					attributes.addFlashAttribute("pessoa", pessoa);
					attributes.addFlashAttribute("mensagem_error",
							"Formato de data incorreto. Por favor, insira uma data válida no formato DD/MM/AAAA.");
					return "redirect:/pessoas/novo?acao=atualizar";
				}
			}
		}
		String mensagemRetorno = pessoaService.atualizarPessoa(pessoa);
		if (mensagemRetorno.equals("CPF invalido")) {
			attributes.addFlashAttribute("pessoa", pessoa);
			attributes.addFlashAttribute("mensagem_error", "CPF inválido");
			return "redirect:/pessoas/novo?acao=atualizar";
		}
		if (mensagemRetorno.equals("nao pode atualizar")) {
			attributes.addFlashAttribute("pessoa", pessoa);
			attributes.addFlashAttribute("mensagem_error",
					"Essa pessoa é gerente em algum projeto. Por favor, remova-a do projeto antes de fazer a edição.");
			return "redirect:/pessoas/novo?acao=atualizar";
		}
		attributes.addFlashAttribute("mensagem", "Pessoa atualizada com sucesso!");
		return "redirect:/pessoas/listar";
	}

	@PostMapping("/remover/{id}")
	public String excluirPessoa(@PathVariable Long id, RedirectAttributes attributes) {
		this.pessoaService.removerPessoa(id);
		List<Pessoa> lista = this.pessoaService.listarTodasPessoas();
		attributes.addFlashAttribute("pessoas", lista);
		attributes.addFlashAttribute("mensagem", "Pessoa excluída com sucesso!");
		return "redirect:/pessoas/listar";

	}
}