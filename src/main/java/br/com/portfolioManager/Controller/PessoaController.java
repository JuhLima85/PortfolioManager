package br.com.portfolioManager.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

	@GetMapping("/{id}")
	public String buscarPessoa(@PathVariable Long id, Model model) {
		List<Pessoa> lista = new ArrayList<>();
		Optional<Pessoa> Pessoa = service.listarPessoasPorProjeto(id);
		if (Pessoa.isPresent()) {
			lista.add(Pessoa.get());
			model.addAttribute("Pessoas", lista);
			return "listar-pessoas";
		} else {
			model.addAttribute("Pessoas", lista);
			return "listar-pessoas";
		}
	}

	@GetMapping("/novo")
	public String novaPessoa() {
		return "cadastrar-pessoa";
	}

	@PostMapping("/salvar")
	public String criarPessoa(Pessoa pessoa, Model model, RedirectAttributes attributes) {		
				
		Pessoa novoPessoa = service.gravar(pessoa);
		if (Objects.nonNull(novoPessoa)) {
			this.result = "sucesso";
			attributes.addFlashAttribute("mensagem", "Pessoa salva com sucesso!");

			return "redirect:/pessoas/novo";
		}
		attributes.addFlashAttribute("mensagem-error", "Erro ao cadastrar Pessoa.");
		return "redirect:/pessoas/novo";
	}

	@GetMapping("/editar/{id}")
	public String editarPessoa(@PathVariable Long id, Model model) {
		Optional<Pessoa> pessoa = service.listarPessoasPorProjeto(id);
		if (pessoa.isPresent()) {
			model.addAttribute("pessoa", pessoa.get());
			return "editar-pessoa";
		} else {
			return "editar-pessoa";
		}
	}

	@PostMapping("/atualizar")
	public String atualizarPessoa(Pessoa pessoa, Model model, RedirectAttributes attributes) {
		Pessoa pessoaExistente = service.atualizarPessoa(pessoa);
		if (Objects.nonNull(pessoaExistente)) {
			this.result = "sucesso";
			attributes.addFlashAttribute("mensagem", "Pessoa atualizada com sucesso!");
			return "redirect:/pessoas/listar";
		}
		attributes.addFlashAttribute("mensagem-error", "Erro ao atualizar Pessoa.");
		return "redirect:/pessoas/listar";
	}

	@PostMapping("/remover/{id}")
	public String excluirPessoa(@PathVariable Long id, RedirectAttributes attributes) {
		this.service.removerPessoa(id);
		List<Pessoa> lista = this.service.listarTodasPessoas();
		attributes.addFlashAttribute("pessoas", lista);
		return "redirect:/pessoas/listar";
	}

}
