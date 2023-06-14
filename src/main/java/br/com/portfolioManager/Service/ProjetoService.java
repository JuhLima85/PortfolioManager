package br.com.portfolioManager.Service;

import java.util.ArrayList;
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
	public Projeto salvarProjeto(Projeto projeto) {
		try {
			projetoRepository.save(projeto);
		} catch (Exception e) {
			e.getMessage();
		}
		return projeto;
	}

	@Transactional
	public void excluirProjeto(Long id) {
		Optional<Projeto> optionalProjeto = projetoRepository.findById(id);

		if (optionalProjeto.isPresent()) {
			Projeto projeto = optionalProjeto.get();

			if ("Iniciado".equalsIgnoreCase(projeto.getStatus()) || "Em andamento".equalsIgnoreCase(projeto.getStatus())
					|| "Encerrado".equalsIgnoreCase(projeto.getStatus())) {
				throw new RuntimeException(
						"Não é possível excluir um projeto com o status " + projeto.getStatus() + ".");
			}
			projetoRepository.deleteById(id);
		} else {
			throw new RuntimeException("Projeto não encontrado");
		}
	}

	@Transactional
	public Projeto atualizarProjeto(Projeto projetoAtualizado) {
		Long projetoId = projetoAtualizado.getId();
		Optional<Projeto> projetoExistente = projetoRepository.findById(projetoId);

		if (projetoExistente.isPresent()) {
			Projeto projeto = projetoExistente.get();
			projeto.setNome(projetoAtualizado.getNome());
			projeto.setDescricao(projetoAtualizado.getDescricao());
			projeto.setDataInicio(projetoAtualizado.getDataInicio());
			projeto.setDataPrevisaoFim(projetoAtualizado.getDataPrevisaoFim());
			projeto.setDataFim(projetoAtualizado.getDataFim());
			projeto.setOrcamento(projetoAtualizado.getOrcamento());
			projeto.setStatus(projetoAtualizado.getStatus());
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
}
