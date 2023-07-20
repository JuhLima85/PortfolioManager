package br.com.projetoPortFolio.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.portfolioManager.Entity.Pessoa;
import br.com.portfolioManager.Repository.PessoaRepository;
import br.com.portfolioManager.Service.PessoaService;

@ExtendWith(SpringExtension.class)
class PessoaServiceTest {

	@InjectMocks
	private PessoaService pessoaService;

	@Mock
	private PessoaRepository pessoaRepository;

	Date dataNascimento = new Date(1960 - 10 - 10);
	Pessoa pessoa = new Pessoa("Maria José", dataNascimento, "01439869103", true);

	@Test
	@DisplayName("Deve salvar pessoa e retornar 'Pessoa salva'")
	void gravarComSucesso() {
		Mockito.when(pessoaRepository.existsByCpf(pessoa.getCpf())).thenReturn(false);
		String resultado = pessoaService.gravar(pessoa);
		assertEquals("Pessoa salva", resultado);
		Mockito.verify(pessoaRepository, Mockito.times(1)).existsByCpf(pessoa.getCpf());
		Mockito.verify(pessoaRepository, Mockito.times(1)).save(pessoa);
	}

	@Test
	@DisplayName("Deve retornar mensagem de CPF inválido")
	void gravarComCPFInvalido() {
		Pessoa pessoa = new Pessoa("Maria José", dataNascimento, "222222222222", true);
		String resultado = pessoaService.gravar(pessoa);
		assertEquals("CPF invalido", resultado);
		Mockito.verify(pessoaRepository, Mockito.never()).existsByCpf(pessoa.getCpf());
		Mockito.verify(pessoaRepository, Mockito.never()).save(Mockito.any());
	}

	@Test
	@DisplayName("Deve retornar mensagem de CPF existente")
	void gravarComCPFExistente() {
		Mockito.when(pessoaRepository.existsByCpf(pessoa.getCpf())).thenReturn(true);
		String resultado = pessoaService.gravar(pessoa);
		assertEquals("CPF existente", resultado);
		Mockito.verify(pessoaRepository, Mockito.never()).save(Mockito.any());
	}

	@Test
	@DisplayName("Deve retornar mensagem de erro ao salvar")
	void gravarComExcecaoAoSalvar() {
		Mockito.when(pessoaRepository.existsByCpf(pessoa.getCpf())).thenReturn(false);
		Mockito.when(pessoaRepository.save(Mockito.any())).thenThrow(new RuntimeException("Erro ao salvar pessoa"));
		assertThrows(IllegalArgumentException.class, () -> {
			pessoaService.gravar(pessoa);
		});
	}

	@Test
	@DisplayName("Deve atualizar pessoa com sucesso")
	void atualizarPessoaComSucesso() {
		Long pessoaId = 1L;
		String novoNome = "Fernanda Souza";
		String novoCpf = "01439869103";
		Date novaDataNascimento = new Date(1980 - 7 - 11);
		boolean novoFuncionario = true;
		Pessoa pessoaExistente = new Pessoa("Maria José", dataNascimento, "01439869103", true);
		pessoaExistente.setId(pessoaId);
		Pessoa pessoaAtualizada = new Pessoa(novoNome, novaDataNascimento, novoCpf, novoFuncionario);
		pessoaAtualizada.setId(pessoaId);
		PessoaService pessoaServiceSpy = Mockito.spy(pessoaService);
		Mockito.doReturn(false).when(pessoaServiceSpy).isGerenteResponsavel(pessoaId);
		Mockito.when(pessoaRepository.findById(pessoaId)).thenReturn(Optional.of(pessoaExistente));
		String resultado = pessoaServiceSpy.atualizarPessoa(pessoaAtualizada);
		assertEquals("Pessoa Atualizada", resultado);
		assertEquals(novoNome, pessoaExistente.getNome());
		assertEquals(novoCpf, pessoaExistente.getCpf());
		assertEquals(novaDataNascimento, pessoaExistente.getDataNascimento());
		assertEquals(novoFuncionario, pessoaExistente.isFuncionario());
		Mockito.verify(pessoaRepository).save(pessoaExistente);
	}

	@Test
	@DisplayName("Deve retornar mensagem de CPF inválido ao atualizar")
	void atualizarPessoaComCPFInvalido() {
		Long pessoaId = 1L;
		String novoNome = "Fernanda Souza";
		String novoCpf = "22222222222";
		Date novaDataNascimento = new Date(1980 - 7 - 11);
		boolean novoFuncionario = true;
		Pessoa pessoaAtualizada = new Pessoa(novoNome, novaDataNascimento, novoCpf, novoFuncionario);
		pessoaAtualizada.setId(pessoaId);
		PessoaService pessoaServiceSpy = Mockito.spy(pessoaService);
		String resultado = pessoaService.atualizarPessoa(pessoaAtualizada);
		assertEquals("CPF invalido", resultado);
		Mockito.verify(pessoaServiceSpy, Mockito.never()).isGerenteResponsavel(Mockito.anyLong());
		Mockito.verify(pessoaRepository, Mockito.never()).findById(Mockito.anyLong());
		Mockito.verify(pessoaRepository, Mockito.never()).save(Mockito.any());
	}

	@Test
	@DisplayName("Deve retornar mensagem de erro ao atualizar pessoa que é gerente responsável por algum projeto")
	void atualizarPessoaGerenteResponsavel() {
		Long pessoaId = 1L;
		String novoNome = "Fernanda Souza";
		String novoCpf = "01439869103";
		Date novaDataNascimento = new Date(1980 - 7 - 11);
		boolean novoFuncionario = true;
		Pessoa pessoaExistente = new Pessoa("Maria José", dataNascimento, "01439869103", true);
		pessoaExistente.setId(pessoaId);
		Pessoa pessoaAtualizada = new Pessoa(novoNome, novaDataNascimento, novoCpf, novoFuncionario);
		pessoaAtualizada.setId(pessoaId);
		PessoaService pessoaServiceSpy = Mockito.spy(pessoaService);
		Mockito.doReturn(true).when(pessoaServiceSpy).isGerenteResponsavel(pessoaId);
		String resultado = pessoaServiceSpy.atualizarPessoa(pessoaAtualizada);
		assertEquals("nao pode atualizar", resultado);
		Mockito.verify(pessoaServiceSpy).isGerenteResponsavel(pessoaId);
		Mockito.verify(pessoaRepository, Mockito.never()).findById(Mockito.anyLong());
		Mockito.verify(pessoaRepository, Mockito.never()).save(Mockito.any());
	}

	@Test
	@DisplayName("Deve remover Pessoa com Sucesso")
	void removerPessoaComSucesso() {
		Long pessoaId = 1L;
		pessoa.setId(pessoaId);
		Mockito.when(pessoaRepository.findById(pessoaId)).thenReturn(Optional.of(pessoa));
		pessoaService.removerPessoa(pessoaId);
		Mockito.verify(pessoaRepository).deleteById(pessoaId);
	}

	@Test
	@DisplayName("Deve retornar sem ação ao tentar remover pessoa com ID nulo")
	void removerPessoaComIdNulo() {
		Long pessoaId = null;
		pessoaService.removerPessoa(pessoaId);
		Mockito.verify(pessoaRepository, Mockito.never()).deleteById(Mockito.anyLong());
	}

	@Test
	@DisplayName("Deve retornar a lista de pessoas vazia")
	void deveRetornarListaVazia() {
		PessoaRepository pessoaRepository = mock(PessoaRepository.class);
		when(pessoaRepository.findAll()).thenReturn(new ArrayList<>());
		PessoaService pessoaService = new PessoaService(pessoaRepository);
		List<Pessoa> pessoas = pessoaService.listarTodasPessoas();
		assertEquals(0, pessoas.size());
		verify(pessoaRepository, times(1)).findAll();
	}

	@Test
	@DisplayName("Deve retornar a lista de pessoas correta")
	void deveRetornarListaCorreta() {
		List<Pessoa> listaPessoas = new ArrayList<>();
		listaPessoas.add(new Pessoa("João", new Date(1990 - 5 - 15), "11111111111", true));
		listaPessoas.add(new Pessoa("Maria", new Date(1985 - 8 - 25), "22222222222", false));
		PessoaRepository pessoaRepository = mock(PessoaRepository.class);
		when(pessoaRepository.findAll()).thenReturn(listaPessoas);
		PessoaService pessoaService = new PessoaService(pessoaRepository);
		List<Pessoa> pessoas = pessoaService.listarTodasPessoas();
		assertEquals(listaPessoas, pessoas);
		verify(pessoaRepository, times(1)).findAll();
	}

	@Test
	@DisplayName("Deve lançar exceção quando ocorrer um erro ao listar pessoas")
	void deveLancarExcecaoAoListarPessoas() {
		PessoaRepository pessoaRepository = mock(PessoaRepository.class);
		when(pessoaRepository.findAll()).thenThrow(new RuntimeException("Erro ao listar pessoas"));
		PessoaService pessoaService = new PessoaService(pessoaRepository);
		assertThrows(RuntimeException.class, () -> {
			pessoaService.listarTodasPessoas();
		});
		verify(pessoaRepository, times(1)).findAll();
	}

	@Test
	@DisplayName("Deve retornar apenas funcionários quando existem funcionários no banco de dados")
	void deveRetornarApenasFuncionariosQuandoExistemFuncionarios() {
		// Criação de dados de exemplo
		Pessoa funcionario1 = new Pessoa("João", new Date(), "12345678900", true);
		Pessoa funcionario2 = new Pessoa("Maria", new Date(), "98765432100", true);
		List<Pessoa> pessoas = Arrays.asList(funcionario1, funcionario2);
		Mockito.when(pessoaRepository.findAll()).thenReturn(pessoas);
		PessoaService pessoaService = new PessoaService(pessoaRepository);
		List<Pessoa> funcionarios = pessoaService.listarFuncionario();
		assertEquals(2, funcionarios.size());
		assertTrue(funcionarios.contains(funcionario1));
		assertTrue(funcionarios.contains(funcionario2));
	}

	@Test
	@DisplayName("Deve buscar pessoa por ID com sucesso")
	void buscarPessoaPorIdSucesso() {
		Long pessoaId = 1L;
		Date dataNascimento = new Date(1980 - 7 - 11);
		Pessoa pessoaExistente = new Pessoa("Maria José", dataNascimento, "01439869103", true);
		pessoaExistente.setId(pessoaId);
		Mockito.when(pessoaRepository.findById(pessoaId)).thenReturn(Optional.of(pessoaExistente));
		Pessoa pessoaEncontrada = pessoaService.buscarPessoaPorId(pessoaId);
		assertEquals(pessoaExistente, pessoaEncontrada);
		assertEquals(pessoaId, pessoaEncontrada.getId());
		assertEquals("Maria José", pessoaEncontrada.getNome());
		assertEquals("01439869103", pessoaEncontrada.getCpf());
		assertEquals(dataNascimento, pessoaEncontrada.getDataNascimento());
		assertTrue(pessoaEncontrada.isFuncionario());
		verify(pessoaRepository, times(1)).findById(pessoaId);

	}

	@Test
	@DisplayName("Deve lançar NoSuchElementException ao buscar pessoa por ID inexistente")
	void buscarPessoaPorIdInexistente() {
		Long pessoaId = 2L;
		when(pessoaRepository.findById(pessoaId)).thenReturn(Optional.empty());
		assertThrows(NoSuchElementException.class, () -> {
			pessoaService.buscarPessoaPorId(pessoaId);
		});
		verify(pessoaRepository, times(1)).findById(pessoaId);
	}

	@Test
	@DisplayName("Deve retornar verdadeiro para um CPF válido")
	void isCpfValidoCPFValido() {
		String cpfValido = "01439869103";
		assertTrue(pessoaService.isCpfValido(cpfValido));
	}

	@Test
	@DisplayName("Deve retornar falso para um CPF inválido")
	void isCpfValidoCPFInvalido() {
		String cpfInvalido = "11122233344";
		assertFalse(pessoaService.isCpfValido(cpfInvalido));
	}

	@Test
	@DisplayName("Deve lidar corretamente com CPFs formatados")
	void isCpfValidoCPFFormatado() {
		String cpfFormatado = "123.456.789-09";
		assertTrue(pessoaService.isCpfValido(cpfFormatado));
	}

	@Test
	@DisplayName("Deve retornar falso para um CPF inválido com caracteres não numéricos")
	void isCpfValidoCPFComCaracteresNaoNumericos() {
		String cpfComCaracteresNaoNumericos = "123.abc.789-09";
		assertFalse(pessoaService.isCpfValido(cpfComCaracteresNaoNumericos));
	}
}
