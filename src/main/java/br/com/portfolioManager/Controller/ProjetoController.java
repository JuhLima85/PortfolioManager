package br.com.portfolioManager.Controller;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.portfolioManager.Entity.Pessoa;
import br.com.portfolioManager.Entity.Projeto;
import br.com.portfolioManager.Service.PessoaService;
import br.com.portfolioManager.Service.ProjetoService;

@Controller
@RequestMapping("/projetos")
public class ProjetoController {

	private List<Pessoa> pessoas;

	@Autowired
	private ProjetoService projetoService;

	@Autowired
	private PessoaService pessoaService;

	@GetMapping("/listar")
	public String listar(Model model) {
		List<Projeto> lista = projetoService.listarProjetos();
		for (Projeto projeto : lista) {
			String classificacaoRisco = projetoService.determinarClassificacaoRisco(projeto);
			projeto.setRisco(classificacaoRisco);
		}
		model.addAttribute("projetos", lista);
		return "listar-projetos";
	}

	@GetMapping(value = "/{id}", produces = { MediaType.ALL_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Projeto> buscarProjeto(@PathVariable Long id) {
		Optional<Projeto> optionalProjeto = projetoService.buscarProjeto(id);
		if (optionalProjeto.isPresent()) {
			return ResponseEntity.ok(optionalProjeto.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/novo")
	public String novoProjeto(Model model) {
		this.pessoas = this.pessoaService.listarTodasPessoas();
		model.addAttribute("pessoas", this.pessoas);
		return "cadastrar-projeto";
	}

	@GetMapping("/editar/{id}")
	public String editarProjeto(@PathVariable Long id, Model model) {
		Optional<Projeto> projeto = projetoService.buscarProjeto(id);
		this.pessoas = this.pessoaService.listarTodasPessoas();
		if (projeto.isPresent()) {
			NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
			String orcamentoFormatado = format.format(projeto.get().getOrcamento());
			model.addAttribute("pessoas", this.pessoas);
			model.addAttribute("projeto", projeto.get());
			model.addAttribute("orcamentoFormatado", orcamentoFormatado);
			return "editar-projeto";
		} else {
			return "editar-projeto";
		}
	}

	@PostMapping("/atualizar")
	public String atualizarProjeto(Projeto projeto, @RequestParam("gerentes") Long gerenteId,
			@RequestParam("selectedStatus") String selectedStatus, Model model, RedirectAttributes attributes) {
		Pessoa gerenteResponsavel = pessoaService.buscarPessoaPorId(gerenteId);
		projeto.setGerenteResponsavel(gerenteResponsavel);
		projeto.setStatus(selectedStatus);
		String classificacaoRisco = projetoService.determinarClassificacaoRisco(projeto);
		projeto.setRisco(classificacaoRisco);
		Projeto projetoExistente = projetoService.atualizarProjeto(projeto);
		if (Objects.nonNull(projetoExistente)) {
			attributes.addFlashAttribute("mensagem", "Pessoa atualizada com sucesso!");
			return "redirect:/projetos/listar";
		}
		attributes.addFlashAttribute("mensagem-error", "Erro ao atualizar Pessoa.");
		return "redirect:/projetos/listar";
	}

	@PostMapping("/salvar")
	public String criarProjeto(Projeto projeto, @RequestParam(name = "gerentes") Optional<Long> gerenteIdOptional,
			RedirectAttributes attributes) {
		Long gerenteId = null;
		if (gerenteIdOptional.isPresent()) {
			gerenteId = gerenteIdOptional.get();
			Pessoa gerente = pessoaService.buscarPessoaPorId(gerenteId);
			projeto.setGerenteResponsavel(gerente);
		} else {
			attributes.addFlashAttribute("projetos", projeto);
			attributes.addFlashAttribute("mensagem_error", "Cadastre primeiro um funcionário e depois o projeto.");
			return "redirect:/pessoas/novo";
		}
		String classificacaoRisco = projetoService.determinarClassificacaoRisco(projeto);
		projeto.setRisco(classificacaoRisco);
		NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
		String orcamentoFormatado = format.format(projeto.getOrcamento());
		String mensagemRetorno = projetoService.salvarProjeto(projeto);
		if (mensagemRetorno.equals("início")) {
			attributes.addFlashAttribute("projeto", projeto);
			attributes.addFlashAttribute("orcamentoFormatado", orcamentoFormatado);
			attributes.addFlashAttribute("mensagem_error", "A data de início não pode ser anterior à data atual.");
			return "redirect:/projetos/novo";
		} else if (mensagemRetorno.equals("previsão")) {
			attributes.addFlashAttribute("projeto", projeto);
			attributes.addFlashAttribute("orcamentoFormatado", orcamentoFormatado);
			attributes.addFlashAttribute("mensagem_error",
					"A data de previsão de término não pode ser anterior à data de início.");
			return "redirect:/projetos/novo";
		} else if (mensagemRetorno.equals("fim")) {
			attributes.addFlashAttribute("projeto", projeto);
			attributes.addFlashAttribute("orcamentoFormatado", orcamentoFormatado);
			attributes.addFlashAttribute("mensagem_error", "A data de fim não pode ser anterior à data de início.");
			return "redirect:/projetos/novo";
		} else if (mensagemRetorno.equals("Projeto salvo")) {
			attributes.addFlashAttribute("mensagem", "Projeto salvo com sucesso!");
		}
		return "redirect:/projetos/novo";
	}

	@PostMapping("/remover/{id}")
	public String excluirProjeto(@PathVariable Long id, RedirectAttributes attributes) {
		if (id != null) {
			projetoService.excluirProjeto(id);
			List<Projeto> lista = projetoService.listarProjetos();
			attributes.addFlashAttribute("projetos", lista);
			attributes.addFlashAttribute("mensagem", "Projeto excluído com sucesso!");
			return "redirect:/projetos/listar";
		}
		attributes.addFlashAttribute("mensagem_error", "Erro ao excluir projeto, id null.");
		return "redirect:/projetos/listar";
	}
}