package br.com.projetoPortFolio.TestService;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.portfolioManager.Entity.Pessoa;
import br.com.portfolioManager.Service.PessoaService;

@SpringBootTest
public class PessoaServiceTeste {

	@Autowired
	private PessoaService service;

//	@Test
//	void testLould() {
//		Pessoa tp = new Pessoa();
//		LocalDate nasc = LocalDate.of(1978, 11, 13);
//		try {
//			tp.setNome("Visitante");
//			tp.setCpf("69779848100");
//			tp.setFuncionario(true);
//			tp.setDataNascimento(nasc);
//			service.gravar(tp);
//		} catch (Exception e) {
//			e.getMessage();
//		}
////-----------------------------Lista Todos------------------------------------------------
//		try {
//			List<Pessoa> pessoas = service.listarTodasPessoas();
//
//			for (Pessoa pessoa : pessoas) {
//				System.out.println("CPF: " + pessoa.getCpf() + ", Nome: " + pessoa.getNome());
//			}
//		} catch (Exception e) {
//			System.out.println(e);
//		}

//	public static void main(String[] args) {	
//		
//		try {
//			
//			ProjetoService service = new ProjetoService();
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
//			p2.setGerente(p);
//			
//			service.salvarProjeto(p2);
//		} catch (Exception e) {
//			e.getMessage();
//		}
//		
//
//	}

//	}
}
