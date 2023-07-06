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

import br.com.portfolioManager.Entity.Pessoa;
import br.com.portfolioManager.Entity.Projeto;
import br.com.portfolioManager.Repository.ProjetoRepository;

@Service
public class ProjetoService {

	@Autowired
	private ProjetoRepository projetoRepository;

	@Transactional(readOnly = true)
	public List<Projeto> listarProjetos() {
		return projetoRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<Projeto> buscarProjeto(Long id) {
		return projetoRepository.findById(id);
	}

	@Transactional
	public String salvarProjeto(Projeto projeto) {
		Date dataAtualSemHora = dataSemHora(new Date());
		Date dataInicio = dataSemHora(projeto.getDataInicio());
		Date dataPrevisaoFim = dataSemHora(projeto.getDataPrevisaoFim());
		if (dataInicio.before(dataAtualSemHora)) {
			return "início";
		}
		if (dataPrevisaoFim.before(dataInicio)) {
			return "previsão";
		}
		if (projeto.getDataFim() != null) {
			Date dataFim = dataSemHora(projeto.getDataFim());
			if (dataFim != null && dataFim.before(dataInicio)) {
				return "fim";
			}
		}
		try {
			projetoRepository.save(projeto);
		} catch (Exception e) {
			throw new IllegalArgumentException("Erro ao cadastrar projeto: " + e.getMessage());
		}
		return "Projeto salvo";
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
			projetoRepository.deleteById(id);
		} catch (Exception e) {
			throw new IllegalArgumentException("Erro ao remover projeto! " + e.getMessage());
		}
	}

	@Transactional
	public Projeto atualizarProjeto(Projeto projetoAtualizado) {
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
			return projetoRepository.save(projeto);
		} else {
			throw new NoSuchElementException("Projeto não encontrado");
		}
	}

	@Transactional
	public List<Pessoa> isGerenteResponsavel() {
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
