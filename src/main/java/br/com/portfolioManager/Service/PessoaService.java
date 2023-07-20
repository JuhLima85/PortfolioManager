package br.com.portfolioManager.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import br.com.portfolioManager.Entity.Pessoa;
import br.com.portfolioManager.Entity.Projeto;
import br.com.portfolioManager.Repository.PessoaRepository;

@Service
public class PessoaService {

	private PessoaRepository service;
	private ApplicationContext applicationContext;

	@Autowired
	public PessoaService(PessoaRepository service) {
		this.service = service;
	}

	@Autowired
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Transactional(readOnly = true)
	public boolean isGerenteResponsavel(Long idPessoa) {
		boolean isGerente = false;
		ProjetoService projetoService = applicationContext.getBean(ProjetoService.class);
		List<Projeto> projetos = projetoService.listarProjetos();
		for (Projeto projeto : projetos) {
			Pessoa gerenteResponsavel = projeto.getGerenteResponsavel();
			if (gerenteResponsavel != null && gerenteResponsavel.getId().equals(idPessoa)) {
				isGerente = true;
				break;
			}
		}
		return isGerente;
	}

	@Transactional
	public String gravar(Pessoa pessoa) {
		try {			
			if (!isCpfValido(pessoa.getCpf())) {
				return "CPF invalido";
			}
			if (service.existsByCpf(pessoa.getCpf())) {
				return "CPF existente";
			}
			if(pessoa.getDataNascimento() == null) {
				return null;
			}
			service.save(pessoa);
		} catch (Exception e) {
			throw new IllegalArgumentException("Erro ao cadastrar pessoa: " + e.getMessage());
		}
		return "Pessoa salva";
	}

	@Transactional
	public String atualizarPessoa(Pessoa pessoaAtualizada) {
		try {
			if (!isCpfValido(pessoaAtualizada.getCpf())) {
				return "CPF invalido";
			}

			if (isGerenteResponsavel(pessoaAtualizada.getId())) {
				return "gerente";
			}
			Long pessoaId = pessoaAtualizada.getId();
			Optional<Pessoa> pessoaExistente = service.findById(pessoaId);
			if (pessoaExistente.isPresent()) {
				Pessoa pessoa = pessoaExistente.get();
				pessoa.setNome(pessoaAtualizada.getNome());
				pessoa.setCpf(pessoaAtualizada.getCpf());
				pessoa.setDataNascimento(pessoaAtualizada.getDataNascimento());
				pessoa.setFuncionario(pessoaAtualizada.isFuncionario());
				service.save(pessoa);
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("Erro ao atualizar pessoa: " + e.getMessage());
		}
		return "Pessoa Atualizada";
	}

	@Transactional
	public void removerPessoa(Long pessoaId) {
		try {
			if (pessoaId != null) {
				service.deleteById(pessoaId);
			}

		} catch (Exception e) {
			throw new IllegalArgumentException("Erro ao remover pessoa: " + e.getMessage());
		}
	}

	@Transactional(readOnly = true)
	public List<Pessoa> listarTodasPessoas() {
		List<Pessoa> pessoas = service.findAll();
		try {
			return pessoas;
		} catch (Exception e) {
			throw new NoSuchElementException(e.getMessage());
		}
	}

	@Transactional(readOnly = true)
	public List<Pessoa> listarFuncionario() {
		List<Pessoa> pessoas = listarTodasPessoas();
		List<Pessoa> funcionarios = new ArrayList<>();
		for (Pessoa pessoa : pessoas) {
			if (pessoa.isFuncionario()) {
				funcionarios.add(pessoa);
			}
		}
		return funcionarios;
	}

	@Transactional(readOnly = true)
	public Pessoa buscarPessoaPorId(Long id) {
		Optional<Pessoa> optionalPessoa = service.findById(id);
		if (optionalPessoa.isPresent()) {
			return optionalPessoa.get();
		} else {
			throw new NoSuchElementException("Pessoa não encontrada.");
		}
	}

	public boolean isCpfValido(String cpf) {
		cpf = cpf.replaceAll("[^0-9]", "");
		CPFValidator validator = new CPFValidator();
		try {
			validator.assertValid(cpf);
			return true;
		} catch (InvalidStateException e) {
			return false;
		}
	}
}
