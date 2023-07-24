package br.com.projetoPortFolio.Service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.portfolioManager.Entity.Pessoa;
import br.com.portfolioManager.Entity.Projeto;
import br.com.portfolioManager.Repository.ProjetoRepository;
import br.com.portfolioManager.Service.PessoaService;
import br.com.portfolioManager.Service.ProjetoService;

@ExtendWith(SpringExtension.class)
class ProjetoServiceTest {

	@InjectMocks
	private ProjetoService projetoService;

	@Mock
	private ProjetoRepository projetoRepository;

	@Mock
	private PessoaService pessoaService;

	private Projeto criarProjeto(long id, String nome, Date dataInicio, Date dataPrevisaoFim, Date dataFim,
			String descricao, String status, double orcamento, Pessoa gerenteResponsavel) {
		Projeto projeto1 = new Projeto();
		projeto1.setId(id);
		projeto1.setNome(nome);
		projeto1.setDataInicio(dataInicio);
		projeto1.setDataPrevisaoFim(dataPrevisaoFim);
		projeto1.setDataFim(dataFim);
		projeto1.setDescricao(descricao);
		projeto1.setStatus(status);
		projeto1.setOrcamento(orcamento);
		projeto1.setGerenteResponsavel(gerenteResponsavel);
		return projeto1;
	}

	private Pessoa criarPessoa(String nome, Date dataNascimento, String cpf, boolean funcionario) {
		Pessoa pessoa = new Pessoa();
		pessoa.setNome(nome);
		pessoa.setDataNascimento(dataNascimento);
		pessoa.setCpf(cpf);
		pessoa.setFuncionario(funcionario);
		return pessoa;
	}

	@Test
	@DisplayName("Deve retornar lista vazia ao listar projetos quando não há projetos cadastrados")
	void listarProjetosSemProjetosCadastrados() {
		when(projetoRepository.findAll()).thenReturn(new ArrayList<>());
		List<Projeto> projetos = projetoService.listarProjetos();
		assertTrue(projetos.isEmpty(), "A lista de projetos deve estar vazia");
	}

	@Test
	@DisplayName("Deve retornar lista com projetos e classificação de risco ao listar projetos quando há projetos cadastrados")
	void listarProjetosComProjetosCadastrados() {
		Projeto projeto1 = criarProjeto(1L, "Projeto 1", new Date(2022 - 5 - 15), new Date(2023 - 1 - 15),
				new Date(2022 - 12 - 20), "Descrição do Projeto 1", "Encerrado", 10000.0,
				criarPessoa("Fernando Silva", new Date(1980 - 1 - 25), "01439869103", true));

		Projeto projeto2 = new Projeto();
		projeto2.setId(2L);
		projeto2.setNome("Projeto 2");
		projeto2.setDataInicio(new Date(2023 - 1 - 21));
		projeto2.setDataPrevisaoFim(new Date(2023 - 06 - 21));
		projeto2.setDescricao("Descrição do Projeto 2");
		projeto2.setStatus("Planejado");
		projeto2.setOrcamento(20000.0);
		projeto2.setRisco("Alto Risco");
		projeto2.setGerenteResponsavel(new Pessoa("Maria José", new Date(1990 - 5 - 15), "01439869103", true));
		when(projetoRepository.findAll()).thenReturn(Arrays.asList(projeto1, projeto2));
		List<Projeto> projetos = projetoService.listarProjetos();
		assertFalse(projetos.isEmpty(), "A lista de projetos não deve estar vazia");
		assertEquals("Baixo Risco", projetos.get(0).getRisco(),
				"A classificação de risco do projeto 1 deve ser 'Baixo Risco'");
		assertEquals("Alto Risco", projetos.get(1).getRisco(),
				"A classificação de risco do projeto 2 deve ser 'Alto Risco'");
	}

	@Test
	@DisplayName("Deve lançar NoSuchElementException ao listar projetos e ocorrer um erro ao acessar o repositório")
	void deveLancarExcecaoAoListarProjetosComErroNoRepositorio() {
		when(projetoRepository.findAll()).thenThrow(new NoSuchElementException("Erro ao acessar o repositório"));
		assertThrows(NoSuchElementException.class, () -> {
			projetoService.listarProjetos();
		});
	}

	@Test
	@DisplayName("Deve retornar o projeto ao buscar por ID válido")
	void buscarProjetoPorIdValido() {
		Long projetoId = 1L;
		Projeto projeto1 = criarProjeto(1L, "Projeto 1", new Date(2022 - 5 - 15), new Date(2023 - 1 - 15),
				new Date(2022 - 12 - 20), "Descrição do Projeto 1", "Encerrado", 10000.0,
				criarPessoa("Fernando Silva", new Date(1980 - 1 - 25), "01439869103", true));

		when(projetoRepository.findById(projetoId)).thenReturn(Optional.of(projeto1));
		Projeto projetoRetornado = projetoService.buscarProjeto(projetoId);
		assertNotNull(projetoRetornado, "O projeto retornado não deve ser nulo");
		assertEquals(projetoId, projetoRetornado.getId(), "O ID do projeto retornado deve ser igual ao ID buscado");
	}

	@Test
	@DisplayName("Deve lançar NoSuchElementException ao buscar projeto por ID inválido")
	void buscarProjetoPorIdInvalido() {
		Long projetoId = 100L;
		when(projetoRepository.findById(projetoId)).thenReturn(Optional.empty());
		assertThrows(NoSuchElementException.class, () -> {
			projetoService.buscarProjeto(projetoId);
		});
	}

	@Test
	@DisplayName("Deve salvar o projeto corretamente")
	void salvarProjetoComSucesso() {
		Projeto projeto1 = criarProjeto(1L, "Projeto 1", new Date(2022 - 5 - 15), new Date(2023 - 1 - 15),
				new Date(2022 - 12 - 20), "Descrição do Projeto 1", "Encerrado", 10000.0,
				criarPessoa("Fernando Silva", new Date(1980 - 1 - 25), "01439869103", true));
		String resultado = projetoService.salvarProjeto(projeto1);
		assertEquals("Projeto salvo", resultado, "O resultado deve ser 'Projeto salvo'");
		verify(projetoRepository, times(1)).save(projeto1);
	}

	@Test
	@DisplayName("Deve retornar mensagem diferente de 'sucesso' ao tentar salvar projeto com datas não validadas")
	void salvarProjetoComDatasNaovalidadas() {
		Projeto projeto1 = criarProjeto(1L, "Projeto 1", new Date(2022, 5, 15), new Date(2022, 4, 15),
				new Date(2022 - 12 - 20), "Descrição do Projeto 1", "Encerrado", 10000.0,
				criarPessoa("Fernando Silva", new Date(1980 - 1 - 25), "01439869103", true));
		String resultado = projetoService.salvarProjeto(projeto1);
		assertNotEquals("sucesso", resultado, "O resultado deve ser diferente de 'sucesso'");

		verify(projetoRepository, never()).save(projeto1);
	}

	@Test
	@DisplayName("Deve lançar IllegalArgumentException ao ocorrer um erro ao salvar o projeto no repositório")
	void salvarProjetoComErroNoRepositorio() {
		Projeto projeto1 = criarProjeto(1L, "Projeto 1", new Date(2022 - 5 - 15), new Date(2023 - 1 - 15),
				new Date(2022 - 12 - 20), "Descrição do Projeto 1", "Encerrado", 10000.0,
				criarPessoa("Fernando Silva", new Date(1980 - 1 - 25), "01439869103", true));
		doThrow(new RuntimeException("Erro ao salvar projeto")).when(projetoRepository).save(projeto1);
		assertThrows(IllegalArgumentException.class, () -> {
			projetoService.salvarProjeto(projeto1);
		});
	}

	@Test
	@DisplayName("Deve retornar 'cadastre' ao verificar se há funcionários e não houver funcionários cadastrados")
	void verificaSetemFuncionarioSemFuncionariosCadastrados() {
		when(pessoaService.listarFuncionario()).thenReturn(new ArrayList<>());
		String resultado = projetoService.verificaSetemFuncionario();
		assertEquals("cadastre", resultado, "Deve retornar 'cadastre' quando não há funcionários cadastrados");
	}

	@Test
	@DisplayName("Deve retornar 'funcionario' ao verificar que há funcionários cadastrados")
	void verificaSetemFuncionarioComFuncionariosCadastrados() {
		List<Pessoa> funcionarios = new ArrayList<>();
		funcionarios.add(new Pessoa());
		when(pessoaService.listarFuncionario()).thenReturn(funcionarios);
		String resultado = projetoService.verificaSetemFuncionario();
		assertEquals("funcionario", resultado, "Deve retornar 'funcionario' quando há funcionários cadastrados");
	}

	@Test
	@DisplayName("Deve lançar IllegalArgumentException ao ocorrer um erro ao acessar o serviço de pessoa")
	void LancaExcecaoAoVerificarFuncionario() {
		when(pessoaService.listarFuncionario())
				.thenThrow(new IllegalArgumentException("Erro ao acessar o serviço de pessoa"));
		assertThrows(IllegalArgumentException.class, () -> {
			projetoService.verificaSetemFuncionario();
		});
	}

	@Test
	@DisplayName("Deve retornar 'sucesso' ao validar as datas corretamente")
	void deveRetornarSucessoAoValidarDatasCorretamente() {
		Projeto projeto1 = criarProjeto(1L, "Projeto 1", new Date(2022 - 5 - 15), new Date(2023 - 1 - 15),
				new Date(2022 - 12 - 20), "Descrição do Projeto 1", "Encerrado", 10000.0,
				criarPessoa("Fernando Silva", new Date(1980 - 1 - 25), "01439869103", true));		
		String resultado = projetoService.validarDatasProjeto(projeto1);
		assertEquals("sucesso", resultado, "O resultado deve ser 'sucesso'");
	}

	@Test
	@DisplayName("Deve retornar 'previsao' ao validar data de previsão de fim anterior à data de início")
	void deveRetornarPrevisaoAoValidarDataPrevisaoAnteriorADataInicio() {
		Projeto projeto1 = criarProjeto(1L, "Projeto 1", new Date(2022, 5, 15), new Date(2022, 4, 15),
				new Date(2022 - 12 - 20), "Descrição do Projeto 1", "Encerrado", 10000.0,
				criarPessoa("Fernando Silva", new Date(1980 - 1 - 25), "01439869103", true));		
		String resultado = projetoService.validarDatasProjeto(projeto1);
		assertEquals("previsao", resultado, "O resultado deve ser 'previsao'");
	}

	@Test
	@DisplayName("Deve retornar 'fim' ao validar data de fim anterior à data de início")
	void deveRetornarFimAoValidarDataFimAnteriorADataInicioOuPosteriorDataAtual() {
		Projeto projeto1 = criarProjeto(1L, "Projeto 1", new Date(2023, 5, 15), new Date(2023, 8, 15),
				new Date(2023, 4, 20), "Descrição do Projeto 1", "Encerrado", 10000.0,
				criarPessoa("Fernando Silva", new Date(1980 - 1 - 25), "01439869103", true));			
		String resultado1 = projetoService.validarDatasProjeto(projeto1);
		assertEquals("fim", resultado1, "O resultado deve ser 'fim'");
	}

	@Test
	@DisplayName("Deve excluir projeto com ID válido")
	void excluirProjetoComIdValido() {
		Long projetoId = 1L;
		doNothing().when(projetoRepository).deleteById(projetoId);
		assertDoesNotThrow(() -> projetoService.excluirProjeto(projetoId));
		verify(projetoRepository, times(1)).deleteById(projetoId);
	}

	@Test
	@DisplayName("Deve ignorar a exclusão de projeto com ID nulo")
	void excluirProjetoComIdNulo() {
		Long projetoId = null;
		projetoService.excluirProjeto(projetoId);
		verify(projetoRepository, never()).deleteById(anyLong());
	}

	@Test
	@DisplayName("Deve lançar IllegalArgumentException ao tentar excluir projeto inexistente")
	void excluirProjetoInexistente() {
		Long projetoId = 1L;
		doThrow(EmptyResultDataAccessException.class).when(projetoRepository).deleteById(projetoId);
		assertThrows(IllegalArgumentException.class, () -> projetoService.excluirProjeto(projetoId));
		verify(projetoRepository, times(1)).deleteById(projetoId);
	}

	@DisplayName("Deve atualizar o projeto corretamente quando os dados são válidos e o projeto existe no repositório")
	void atualizarProjetoExistente() {
		Projeto projetoExistente = criarProjeto(1L, "Projeto Existente", new Date(2022 - 5 - 15), new Date(2023 - 1 - 15),
				new Date(2022 - 12 - 20), "Descrição do Projeto existente", "Encerrado", 10000.0,
				criarPessoa("Fernando Silva", new Date(1980 - 1 - 25), "01439869103", true));
		Pessoa gerenteResponsavel = new Pessoa("Fernando Silva", new Date(), "01439869103", true);
		when(pessoaService.buscarPessoaPorId(anyLong())).thenReturn(gerenteResponsavel);
		when(projetoRepository.findById(1L)).thenReturn(Optional.of(projetoExistente));
		
		Projeto projetoAtualizado = criarProjeto(1L, "Projeto Atualizado", new Date(2022 - 9 - 15), new Date(2023 - 1 - 15),
				new Date(2023 - 1 - 10), "Descrição do Projeto existente", "Encerrado", 10000.0,
				criarPessoa("Maria Antonieta", new Date(1980 - 1 - 25), "01439869103", true));
		String resultado = projetoService.atualizarProjeto(projetoAtualizado, 1L, "Encerrado");
		verify(projetoRepository).save(projetoExistente);
		assertEquals("atualizado", resultado, "O resultado deve ser 'atualizado'");
	}

	@DisplayName("Deve retornar mensagem diferente de 'sucesso' ao tentar atualizar projeto com datas não validadas")
	void atualizarProjetoDatasInvalidas() {
		Projeto projetoExistente = criarProjeto(1L, "Projeto Existente", new Date(2022 - 5 - 15), new Date(2023 - 1 - 15),
				new Date(2022 - 12 - 20), "Descrição do Projeto existente", "Encerrado", 10000.0,
				criarPessoa("Fernando Silva", new Date(1980 - 1 - 25), "01439869103", true));
		
		Pessoa gerenteResponsavel = new Pessoa("Fernando Silva", new Date(1980 - 1 - 25), "01439869103", true);
		when(pessoaService.buscarPessoaPorId(anyLong())).thenReturn(gerenteResponsavel);
		when(projetoRepository.findById(1L)).thenReturn(Optional.of(projetoExistente));

		Projeto projetoAtualizado = criarProjeto(1L, "Projeto Atualizado", new Date(2022, 5, 15), new Date(2022, 4, 15),
				new Date(2023 - 1 - 10), "Descrição do Projeto Atualizado", "Encerrado", 10000.0,
				criarPessoa("Maria Antonieta", new Date(1980 - 1 - 25), "01439869103", true));		
		String resultado = projetoService.atualizarProjeto(projetoAtualizado, 1L, "Encerrado");
		assertNotEquals("sucesso", resultado, "O resultado deve ser diferente de 'sucesso'");
		verify(projetoRepository, never()).save(any());
	}

	@Test
	@DisplayName("Deve lançar IllegalArgumentException ao tentar atualizar projeto inexistente")
	void atualizarProjetoInexistente() {
		Projeto projetoAtualizado = new Projeto();
		projetoAtualizado.setId(999L);
		when(projetoRepository.findById(999L)).thenReturn(Optional.empty());
		assertThrows(IllegalArgumentException.class, () -> {
			projetoService.atualizarProjeto(projetoAtualizado, 1L, "Encerrado");
		});
		verify(projetoRepository, never()).save(any());
	}

	@Test
	@DisplayName("Deve retornar a lista de gerentes responsáveis corretamente")
	void listaGerenteResponsavelTest() {
		Pessoa gerente1 = new Pessoa("Gerente 1", null, null, true);
		Pessoa gerente2 = new Pessoa("Gerente 2", null, null, true);
		Pessoa gerente3 = new Pessoa("Gerente 3", null, null, true);

		Projeto projeto1 = new Projeto();
		projeto1.setNome("Projeto 1");
		projeto1.setGerenteResponsavel(gerente1);

		Projeto projeto2 = new Projeto();
		projeto2.setNome("Projeto 2");
		projeto2.setGerenteResponsavel(gerente2);

		Projeto projeto3 = new Projeto();
		projeto3.setNome("Projeto 3");
		projeto3.setGerenteResponsavel(gerente3);

		List<Projeto> projetos = new ArrayList<>();
		projetos.add(projeto1);
		projetos.add(projeto2);
		projetos.add(projeto3);

		ProjetoService projetoServiceSpy = spy(projetoService);
		doReturn(projetos).when(projetoServiceSpy).listarProjetos();

		List<Pessoa> gerentesResponsaveis = projetoServiceSpy.listaGerenteResponsavel();

		assertEquals(3, gerentesResponsaveis.size());
		assertTrue(gerentesResponsaveis.contains(gerente1));
		assertTrue(gerentesResponsaveis.contains(gerente2));
		assertTrue(gerentesResponsaveis.contains(gerente3));
	}

	@Test
	@DisplayName("Deve retornar 'Baixo Risco' para status 'Encerrado' e data de término antes da data de previsão de término")
	void determinarClassificacaoRiscoEncerradoComDataAntesDaPrevisao() {
		Projeto projeto = new Projeto();
		projeto.setStatus("Encerrado");
		projeto.setDataFim(new Date(2023 - 1 - 15));
		projeto.setDataPrevisaoFim(new Date(2023 - 2 - 1));

		String result = projetoService.determinarClassificacaoRisco(projeto);
		assertEquals("Baixo Risco", result);
	}	
}
