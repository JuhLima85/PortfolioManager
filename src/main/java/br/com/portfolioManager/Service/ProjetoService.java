package br.com.portfolioManager.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.portfolioManager.Entity.Pessoa;
import br.com.portfolioManager.Entity.Projeto;
import br.com.portfolioManager.Repository.ProjetoRepository;

@Service
public class ProjetoService {

	private final ProjetoRepository projetoRepository;
	private final PessoaService pessoaService;

	@Autowired
	public ProjetoService(ProjetoRepository projetoRepository, PessoaService pessoaService) {
		this.projetoRepository = projetoRepository;
		this.pessoaService = pessoaService;
	}

	@Transactional(readOnly = true)
	public List<Projeto> listarProjetos() {
		List<Projeto> lista = projetoRepository.findAll();
		try {
			if (!lista.isEmpty()) {
				for (Projeto projeto : lista) {
					String classificacaoRisco = determinarClassificacaoRisco(projeto);
					projeto.setRisco(classificacaoRisco);
				}
			}
			return lista;
		} catch (Exception e) {
			throw new NoSuchElementException(e.getMessage());
		}
	}

	@Transactional(readOnly = true)
	public Projeto buscarProjeto(Long id) {
		Optional<Projeto> optionalProjeto = projetoRepository.findById(id);
		if (optionalProjeto.isPresent()) {
			return optionalProjeto.get();
		}
		throw new NoSuchElementException("Projeto não encontrado");
	}

	@Transactional
	public String salvarProjeto(Projeto projeto) {
		try {
			String classificacaoRisco = determinarClassificacaoRisco(projeto);
			projeto.setRisco(classificacaoRisco);
			String dataValidada = validarDatasProjeto(projeto);
			if (!dataValidada.equals("sucesso")) {
				return dataValidada;
			}
			projetoRepository.save(projeto);
		} catch (Exception e) {
			throw new IllegalArgumentException("Erro ao cadastrar projeto: " + e.getMessage());
		}
		return "Projeto salvo";
	}

	public String verificaSetemFuncionario() {
		List<Pessoa> funcionarios = pessoaService.listarFuncionario();
		if (funcionarios.size() > 0) {
			return "funcionario";
		} else if (funcionarios.isEmpty()) {
			return "cadastre";
		}
		throw new IllegalArgumentException("Erro ao verificar se tem funcionário: ");
	}

	public String validarDatasProjeto(Projeto projeto) {
		Date dataInicio = dataSemHora(projeto.getDataInicio());
		Date dataPrevisaoFim = dataSemHora(projeto.getDataPrevisaoFim());

		if (dataPrevisaoFim.before(dataInicio)) {
			return "previsao";
		}
		if (projeto.getDataFim() != null) {
			Date dataFim = dataSemHora(projeto.getDataFim());
			if (dataFim != null && (dataFim.before(dataInicio) || dataFim.after(new Date()))) {
				return "fim";
			}
		}
		return "sucesso";
	}

	public Date dataSemHora(Date data) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	@Transactional
	public void excluirProjeto(Long id) {
		try {
			if (id != null) {
				projetoRepository.deleteById(id);
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("Erro ao remover projeto! " + e.getMessage());
		}
	}

	@Transactional
	public String atualizarProjeto(Projeto projetoAtualizado, @RequestParam("gerentes") Long gerenteId,
			@RequestParam("selectedStatus") String selectedStatus) {
		try {
			if (projetoAtualizado != null) {
				Pessoa gerenteResponsavel = pessoaService.buscarPessoaPorId(gerenteId);
				projetoAtualizado.setGerenteResponsavel(gerenteResponsavel);
				projetoAtualizado.setStatus(selectedStatus);
				String classificacaoRisco = determinarClassificacaoRisco(projetoAtualizado);
				projetoAtualizado.setRisco(classificacaoRisco);
				String dataValidada = validarDatasProjeto(projetoAtualizado);
				if (!dataValidada.equals("sucesso")) {
					return dataValidada;
				}
				Long projetoId = projetoAtualizado.getId();
				Optional<Projeto> projetoExistente = projetoRepository.findById(projetoId);

				if (projetoExistente.isPresent()) {
					Projeto projeto = projetoExistente.get();
					projeto.setNome(projetoAtualizado.getNome());
					projeto.setDataInicio(projetoAtualizado.getDataInicio());
					projeto.setDataPrevisaoFim(projetoAtualizado.getDataPrevisaoFim());
					projeto.setDataFim(projetoAtualizado.getDataFim());
					projeto.setDescricao(projetoAtualizado.getDescricao());
					projeto.setStatus(projetoAtualizado.getStatus());
					projeto.setOrcamento(projetoAtualizado.getOrcamento());
					projeto.setRisco(projetoAtualizado.getRisco());
					projeto.setGerenteResponsavel(projetoAtualizado.getGerenteResponsavel());
					projetoRepository.save(projeto);
					return "atualizado";
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("Erro ao remover projeto! " + e.getMessage());
		}
		return null;
	}

	@Transactional(readOnly = true)
	public List<Pessoa> listaGerenteResponsavel() {
		List<Projeto> projetos = listarProjetos();
		List<Pessoa> gerentes = new ArrayList<>();
		for (Projeto projeto : projetos) {
			Pessoa gerenteResponsavel = projeto.getGerenteResponsavel();
			if (gerenteResponsavel != null && !gerentes.contains(gerenteResponsavel)) {
				gerentes.add(gerenteResponsavel);
			}
		}
		return gerentes;
	}

	public String determinarClassificacaoRisco(Projeto projeto) {
		if ("Encerrado".equals(projeto.getStatus())) {
			if (projeto.getDataFim() != null && projeto.getDataFim().before(projeto.getDataPrevisaoFim())) {
				return "Baixo Risco";
			} else if (projeto.getDataFim() != null || projeto.getDataFim().after(projeto.getDataPrevisaoFim())) {
				return "Alto Risco";
			}
		} else if (projeto.getStatus().equals("Cancelado")) {
			return "Baixo Risco";
		}
		double prazoTotal = calcularDiasEntreDatas(projeto.getDataInicio(), projeto.getDataPrevisaoFim());
		double diasPercorridos = calcularDiasEntreDatas(projeto.getDataInicio(), new Date());
		double diasRestantes = prazoTotal - diasPercorridos;
		double percentualPrazoDisponivelAtual = (diasRestantes / prazoTotal) * 100;

		String status = projeto.getStatus();
		double percentualPrazoDisponivelPrevisto = 0;

		if ("Planejado".equals(status)) {
			percentualPrazoDisponivelPrevisto = 100;
		} else if ("Em análise".equals(status)) {
			percentualPrazoDisponivelPrevisto = 80;
		} else if ("Análise realizada".equals(status)) {
			percentualPrazoDisponivelPrevisto = 70;
		} else if ("Análise aprovada".equals(status)) {
			percentualPrazoDisponivelPrevisto = 60;
		} else if ("Iniciado".equals(status)) {
			percentualPrazoDisponivelPrevisto = 50;
		} else if ("Em andamento".equals(status)) {
			percentualPrazoDisponivelPrevisto = 25;
		}

		if (percentualPrazoDisponivelAtual > percentualPrazoDisponivelPrevisto) {
			return "Baixo Risco";
		} else if (percentualPrazoDisponivelAtual == percentualPrazoDisponivelPrevisto) {
			return "Médio Risco";
		} else {
			return "Alto Risco";
		}
	}

	private double calcularDiasEntreDatas(Date dataInicio, Date dataPrecisaoFim) {
		long diferencaMilissegundos = dataPrecisaoFim.getTime() - dataInicio.getTime();
		long diferencaDias = diferencaMilissegundos / (24 * 60 * 60 * 1000);
		return diferencaDias;
	}
}
