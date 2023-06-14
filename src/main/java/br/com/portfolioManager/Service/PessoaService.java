package br.com.portfolioManager.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.portfolioManager.Entity.Pessoa;
import br.com.portfolioManager.Repository.PessoaRepository;

@Service
public class PessoaService {

	private PessoaRepository service;

	@Autowired
	public PessoaService(PessoaRepository service) {
		this.service = service;
	}

	@Transactional
	public Pessoa gravar(Pessoa pessoa) {
		return service.save(pessoa);
	}

	@Transactional
	public Pessoa atualizarPessoa(Pessoa pessoaAtualizada) {
		Long pessoaId = pessoaAtualizada.getId();
		Optional<Pessoa> pessoaExistente = service.findById(pessoaId);

		if (pessoaExistente.isPresent()) {
			Pessoa pessoa = pessoaExistente.get();
			pessoa.setNome(pessoaAtualizada.getNome());
			pessoa.setCpf(pessoaAtualizada.getCpf());
			pessoa.setDataNascimento(pessoaAtualizada.getDataNascimento());
			pessoa.setFuncionario(pessoaAtualizada.isFuncionario());
			return service.save(pessoa);
		} else {
			throw new NoSuchElementException("Pessoa não encontrada");
		}
	}

	@Transactional
	public void removerPessoa(Long pessoaId) {
		try {
			service.deleteById(pessoaId);
		} catch (Exception e) {
			throw new RuntimeException("Erro ao remover pessoa! ");
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
	
}
