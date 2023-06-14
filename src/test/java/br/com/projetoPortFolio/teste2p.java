package br.com.projetoPortFolio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.portfolioManager.Service.PessoaService;
import br.com.portfolioManager.Service.ProjetoService;

@SpringBootTest
public class teste2p {

	@Autowired
	private ProjetoService serviceP;

	@Autowired
	private PessoaService service;

//	@Test
//	void testLould() {
//		Pessoa tp = new Pessoa();
//		LocalDate nasc = LocalDate.of(1978, 11, 13);
//		tp.setNome("teste");
//		tp.setCpf("teste2");
//		tp.setFuncionario(false);
//		tp.setDataNascimento(nasc);
//
////		service.gravar(tp);
//		// --------------------------------------------------------------------
//		Double b = 7.25;
//		LocalDate inicio = LocalDate.of(2023, 6, 1);
//		LocalDate fim = LocalDate.of(2023, 8, 1);
//		LocalDate previsto = LocalDate.of(2023, 7, 1);
//		// -----------------------------------------------------
//		Projeto p2 = new Projeto();
//		p2.setDataInicio(inicio);
//		p2.setDataFim(fim);
//		p2.setDataPrevisaoFim(previsto);
//		p2.setDescricao("Funciona");
//		p2.setNome("teste");
//		p2.setOrcamento(b);
//		p2.setRisco(ClassificacaoRisco.ALTO_RISCO);
//		p2.setStatus(StatusProjeto.ANALISE_APROVADA);
//
////		serviceP.salvarProjeto(p2);
//		// -----------------------------------------------------
////        Membros membro = new Membros(p2, tp);
////        membro.setPessoa(tp);
////        membro.setProjeto(p2);
////        membrosService.criarMembro(membro);
//
//		try {
//			List<Pessoa> pessoas = service.listarTodasPessoas();
//
//			for (Pessoa pessoa : pessoas) {
//				System.out.println("CPF: " + pessoa.getCpf() + ", Nome: " + pessoa.getNome());
//			}
//		} catch (Exception e) {
//			System.out.println(e);
//		}
//
//	}
//	public static void main(String[] args) {		
//		PessoaService service = new PessoaService();
//		try {
//			List<Pessoa> pessoas = service.listarTodasPessoas();
//
//			for (Pessoa pessoa : pessoas) {
//				System.out.println("CPF: " + pessoa.getCpf() + ", Nome: " + pessoa.getNome());
//			}
//		} catch (Exception e) {
//			System.out.println(e);
//		}
//	}

}
