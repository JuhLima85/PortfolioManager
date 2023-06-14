package br.com.projetoPortFolio.TestController;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import br.com.portfolioManager.Controller.ProjetoController;


@WebMvcTest(ProjetoController.class)
public class ProjetoControllerTest {
	
	 @Autowired
	    private MockMvc mockMvc;

//	    @Test
//		public void testListar() throws Exception {
//			mockMvc.perform(MockMvcRequestBuilders.get("/projeto/listar"))
//					.andExpect(MockMvcResultMatchers.status().isOk())
//					.andExpect(MockMvcResultMatchers.view().name("listar-projeto"));
//		}
}
