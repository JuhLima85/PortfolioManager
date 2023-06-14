package br.com.projetoPortFolio.TestService;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.portfolioManager.Entity.Projeto;
import br.com.portfolioManager.Service.ProjetoService;

@SpringBootTest
public class ProjetoServiceTeste {

	@Autowired
	private ProjetoService serviceP;

//	@Test
//	void testLould() {
//
//		Double b = 7.25;
//		LocalDate inicio = LocalDate.of(2023, 6, 1);
//		LocalDate fim = LocalDate.of(2023, 8, 1);
//		LocalDate previsto = LocalDate.of(2023, 7, 1);
//		// -----------------------------------------------------
//		Projeto p2 = new Projeto();
//		p2.setDataInicio(inicio);
//		p2.setDataFim(fim);
//		p2.setDataPrevisaoFim(previsto);
//		p2.setDescricao("Fabrica de Software");
//		p2.setNome("Solemtec");
//		p2.setOrcamento(b);
//		p2.setRisco(ClassificacaoRisco.ALTO_RISCO);
//		p2.setStatus(StatusProjeto.ANALISE_APROVADA);
//
//		serviceP.salvarProjeto(p2);
//
//		// --------------Lista todos -------------------------------------------------
//		try {
//			List<Projeto> projetos = serviceP.listarProjetos();
//
//			for (Projeto projeto : projetos) {
//				System.out.println("Nome: " + projeto.getNome() + ", Descricao: " + projeto.getDescricao());
//			}
//		} catch (Exception e) {
//			System.out.println(e);
//		}
//
//	}

}
