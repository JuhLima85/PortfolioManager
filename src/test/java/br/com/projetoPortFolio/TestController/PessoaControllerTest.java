package br.com.projetoPortFolio.TestController;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import br.com.portfolioManager.Controller.PessoaController;

@WebMvcTest(PessoaController.class)
public class PessoaControllerTest {

	@Autowired
	private MockMvc mockMvc;

//	@Test
//	public void testListar() throws Exception {
//		mockMvc.perform(MockMvcRequestBuilders.get("/pessoas/listar")).andExpect(MockMvcResultMatchers.status().isOk())
//				.andExpect(MockMvcResultMatchers.view().name("listar-pessoas"));
//	}

}
