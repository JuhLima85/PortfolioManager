package br.com.projetoPortFolio;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.portfolioManager.Entity.Pessoa;
import br.com.portfolioManager.Entity.Projeto;
import br.com.portfolioManager.Repository.ProjetoRepository;
import br.com.portfolioManager.Service.PessoaService;
import br.com.portfolioManager.Service.ProjetoService;

@SpringBootTest
class ProjetoPortFolioApplicationTests {

	@Autowired
	private PessoaService service;

	@Autowired
	private ProjetoRepository teste;

	@Autowired
	private ProjetoService serviceP;

//	@Test
//	void contextLoads() {
//		try {
//			LocalDate nasc = LocalDate.of(1978, 11, 13);
//			Pessoa p = new Pessoa();
//			p.setCpf("69779848134");
//			p.setNome("Ernilson");
//			p.setDataNascimento(nasc);
//			p.setFuncionario(false);
//			Projeto p2 = new Projeto();
//			Double b = 7.25;
//			LocalDate inicio = LocalDate.of(2023, 6, 1);
//			LocalDate fim = LocalDate.of(2023, 8, 1);
//			LocalDate previsto = LocalDate.of(2023, 7, 1);
//			p2.setDataInicio(inicio);
//			p2.setDataFim(fim);
//			p2.setDataPrevisaoFim(previsto);
//			p2.setDescricao("Funciona");
//			p2.setNome("teste");
//			p2.setOrcamento(b);
//			p2.setRisco(ClassificacaoRisco.ALTO_RISCO);
//			p2.setStatus(StatusProjeto.ANALISE_APROVADA);
//			p2.setGerenteResponsavel(p);
//
//			serviceP.salvarProjeto(p2);
//		} catch (Exception e) {
//			e.getMessage();
//		}
//
//	}

}
