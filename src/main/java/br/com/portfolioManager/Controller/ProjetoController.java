package br.com.portfolioManager.Controller;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

	private final ProjetoService projetoService;
	private final PessoaService pessoaService;

	@Autowired
	public ProjetoController(ProjetoService projetoService, PessoaService pessoaService) {
		this.projetoService = projetoService;
		this.pessoaService = pessoaService;
	}	

	@GetMapping("/listar")
	public String listar(Model model) {
		List<Projeto> lista = projetoService.listarProjetos();
		model.addAttribute("projetos", lista);
		return "listar-projetos";
	}

	@GetMapping("/novo")
	public String novoProjeto(Model model, @RequestParam(value = "acao", required = false) String acao) {
		List<Pessoa> pessoas = pessoaService.listarTodasPessoas();
		model.addAttribute("pessoas", pessoas);

		if (acao != null && acao.equals("atualizar")) {
			return "editar-projeto";
		} else {
			return "cadastrar-projeto";
		}
	}

	@GetMapping("/editar/{id}")
	public String editarProjeto(@PathVariable Long id, Model model) {
		Projeto projeto = projetoService.buscarProjeto(id);
		List<Pessoa> pessoas = pessoaService.listarTodasPessoas();
		NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
		String orcamentoFormatado = format.format(projeto.getOrcamento());
		model.addAttribute("pessoas", pessoas);
		model.addAttribute("projeto", projeto);
		model.addAttribute("orcamentoFormatado", orcamentoFormatado);
		return "editar-projeto";
	}

	@PostMapping("/atualizar")
	public String atualizarProjeto(Projeto projeto, BindingResult bindingResult,
			@RequestParam("gerentes") Long gerenteId, @RequestParam("selectedStatus") String selectedStatus,
			RedirectAttributes attributes) {
		NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
		String orcamentoFormatado = format.format(projeto.getOrcamento());
		if (gerenteId != null && selectedStatus != null) {
			Pessoa gerenteResponsavel = pessoaService.buscarPessoaPorId(gerenteId);
			projeto.setGerenteResponsavel(gerenteResponsavel);
			projeto.setStatus(selectedStatus);
			if (bindingResult.hasErrors()) {
				attributes.addFlashAttribute("projeto", projeto);
				attributes.addFlashAttribute("orcamentoFormatado", orcamentoFormatado);
				attributes.addFlashAttribute("mensagem_error",
						"Formato de data incorreto. Por favor, insira uma data válida no formato DD/MM/AAAA.");
				return "redirect:/projetos/novo?acao=atualizar";
			}
			String mensagemRetorno = projetoService.atualizarProjeto(projeto, gerenteId, selectedStatus);
			if (mensagemRetorno.equals("previsao")) {
				attributes.addFlashAttribute("projeto", projeto);
				attributes.addFlashAttribute("orcamentoFormatado", orcamentoFormatado);
				attributes.addFlashAttribute("mensagem_error",
						"A data de previsão de término não pode ser anterior à data de início.");
				return "redirect:/projetos/novo?acao=atualizar";
			} else if (mensagemRetorno.equals("fim")) {
				attributes.addFlashAttribute("projeto", projeto);
				attributes.addFlashAttribute("orcamentoFormatado", orcamentoFormatado);
				attributes.addFlashAttribute("mensagem_error",
						"Inconsistência entre as datas informadas. Verifique se a data de término é anterior à data de início ou posterior à data atual.");
				return "redirect:/projetos/novo?acao=atualizar";
			} else if (mensagemRetorno.equals("atualizado")) {
				attributes.addFlashAttribute("mensagem", "Projeto atualizado com sucesso!");
			}
			return "redirect:/projetos/listar";

		} else {
			throw new IllegalArgumentException("Erro ao remover projeto!");
		}
	}

	@PostMapping("/salvar")
	public String criarProjeto(@ModelAttribute("projeto") Projeto projeto, BindingResult bindingResult,
			@RequestParam(name = "gerentes") Optional<Long> gerenteIdOptional, RedirectAttributes attributes) {
		NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
		String orcamentoFormatado = format.format(projeto.getOrcamento());
		Long gerenteId = null;
		if (gerenteIdOptional.isPresent()) {
			gerenteId = gerenteIdOptional.get();
			Pessoa gerente = pessoaService.buscarPessoaPorId(gerenteId);
			projeto.setGerenteResponsavel(gerente);
		} else if (projetoService.verificaSetemFuncionario() != null) {
			String retorno = projetoService.verificaSetemFuncionario();
			if (retorno.equals("funcionario")) {
				attributes.addFlashAttribute("projeto", projeto);
				attributes.addFlashAttribute("orcamentoFormatado", orcamentoFormatado);
				attributes.addFlashAttribute("mensagem_error", "Selecione um gerente da lista.");
				return "redirect:/projetos/novo";
			} else if (retorno.equals("cadastre")) {
				attributes.addFlashAttribute("mensagem_error",
						"Cadastre primeiro uma pessoa que seja funcionário e depois o projeto.");
				return "redirect:/projetos/novo";
			}
		}
		if (bindingResult.hasErrors()) {
			attributes.addFlashAttribute("projeto", projeto);
			attributes.addFlashAttribute("orcamentoFormatado", orcamentoFormatado);
			attributes.addFlashAttribute("mensagem_error",
					"Formato de data incorreto. Por favor, insira uma data válida no formato DD/MM/AAAA.");
			return "redirect:/projetos/novo";
		}
		String mensagemRetorno = projetoService.salvarProjeto(projeto);
		if (mensagemRetorno.equals("previsao")) {
			attributes.addFlashAttribute("projeto", projeto);
			attributes.addFlashAttribute("orcamentoFormatado", orcamentoFormatado);
			attributes.addFlashAttribute("mensagem_error",
					"A data de previsão de término não pode ser anterior à data de início.");
			return "redirect:/projetos/novo";
		} else if (mensagemRetorno.equals("fim")) {
			attributes.addFlashAttribute("projeto", projeto);
			attributes.addFlashAttribute("orcamentoFormatado", orcamentoFormatado);
			attributes.addFlashAttribute("mensagem_error",
					"Inconsistência entre as datas informadas. Verifique se a data final é anterior à data de início ou posterior à data atual.");
			return "redirect:/projetos/novo";
		} else if (mensagemRetorno.equals("Projeto salvo")) {
			attributes.addFlashAttribute("mensagem", "Projeto salvo com sucesso!");
		}
		return "redirect:/projetos/novo";
	}

	@PostMapping("/remover/{id}")
	public String excluirProjeto(@PathVariable Long id, RedirectAttributes attributes) {
		projetoService.excluirProjeto(id);
		List<Projeto> lista = projetoService.listarProjetos();
		attributes.addFlashAttribute("projetos", lista);
		attributes.addFlashAttribute("mensagem", "Projeto excluído com sucesso!");
		return "redirect:/projetos/listar";
	}
}