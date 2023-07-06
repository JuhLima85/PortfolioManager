package br.com.portfolioManager.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import br.com.portfolioManager.Entity.Pessoa;
import br.com.portfolioManager.Repository.PessoaRepository;

@Service
public class PessoaService {

	private PessoaRepository service;

	@Autowired
	public PessoaService(PessoaRepository service) {
		this.service = service;
	}

	public String gravar(Pessoa pessoa) {
		try {
			if (service.existsByCpf(pessoa.getCpf())) {
				return "CPF existente";
			}
			service.save(pessoa);

		} catch (Exception e) {
			throw new IllegalArgumentException("Erro ao cadastrar pessoa: " + e.getMessage());
		}
		return "Pessoa salva";
	}

	public void atualizarPessoa(Pessoa pessoaAtualizada) {
		try {
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
	}

	@Transactional
	public void removerPessoa(Long pessoaId) {
		try {
			service.deleteById(pessoaId);
		} catch (Exception e) {
			throw new IllegalArgumentException("Erro ao remover pessoa! " + e.getMessage());
		}
	}

	@Transactional(readOnly = true)
	public Optional<Pessoa> listarPessoasPorProjeto(Long Id) {
		return service.findById(Id);
	}

	@Transactional(readOnly = true)
	public List<Pessoa> listarTodasPessoas() {
		List<Pessoa> pessoas = service.findAll();
		if (pessoas.isEmpty()) {
		}
		return pessoas;
	}

	@Transactional(readOnly = true)
	public Pessoa buscarPessoaPorId(Long id) {
		Optional<Pessoa> optionalPessoa = service.findById(id);
		if (optionalPessoa.isPresent()) {
			return optionalPessoa.get();
		} else {
			throw new NoSuchElementException("Pessoa não encontrada");
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
