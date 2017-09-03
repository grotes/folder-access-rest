/*package movie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTest {
	
	@Autowired
	private MockMvc mockMvc;


	@Test
	public void shouldFailGetIndex() throws Exception {

		mockMvc.perform(get("/indexAll")).andExpect(
						status().isMethodNotAllowed()).andDo(print()).andReturn();

	}
	@Test
	public void shouldOkPostIndex() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/indexAll")).andExpect(
						status().isOk()).andDo(print()).andReturn();
		String body = mvcResult.getResponse().getContentAsString();
		if(!body.contains("totalSize") || !body.contains("size")){
			throw new Exception();
		}

	}
	@Test
	public void shouldFailListMovieByName() throws Exception{
		mockMvc.perform(post("/listMovieByName")).andExpect(status().isBadRequest()).andDo(print()).andReturn();
	}
	@Test
	public void shouldOkListMovieByName() throws Exception{
		MvcResult mvcResult = mockMvc.perform(post("/listMovieByName").param("name", "")).andExpect(status().isOk()).andDo(print()).andReturn();
		String body = mvcResult.getResponse().getContentAsString();
		if(!body.contains("totalSize") || !body.contains("size")){
			throw new Exception();
		}
	}
	@Test
	public void shouldFailParamListMovieByName() throws Exception{
		mockMvc.perform(post("/listMovieByName").param("page", "a")).andExpect(status().isBadRequest()).andDo(print()).andReturn();
	}
}*/
