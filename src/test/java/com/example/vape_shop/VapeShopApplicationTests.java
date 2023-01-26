package com.example.vape_shop;

import com.example.vape_shop.controllers.ItemController;
import com.example.vape_shop.models.Comment;
import com.example.vape_shop.models.Item;
import com.example.vape_shop.models.Man;
import com.example.vape_shop.services.CommentService;
import com.example.vape_shop.services.ItemService;
import com.example.vape_shop.services.ManService;
import com.example.vape_shop.services.PurchaseRequestService;
import com.example.vape_shop.util.AccessRights;
import com.example.vape_shop.util.ImageWorker;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ConcurrentModel;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
//@WithUserDetails("nik.porhennikov@mail.ru")
class VapeShopApplicationTests {

//	@Autowired
//	private WebApplicationContext wac;
//
//	private MockMvc mvc;
//
//	private MockMvc mvc2;
//
//	private MockMvc mockMvc;
//
//	@MockBean
//	private ItemService itemService;
//	@MockBean
//	private ManService manService;
//	@MockBean
//	private AccessRights accessRights;
//	@MockBean
//	private CommentService commentService;
//	@MockBean
//	private PurchaseRequestService purchaseRequestService;
//	@MockBean
//	private ImageWorker imageWorker;
//
//	@Autowired
//	private ItemController itemController;
//
//
//	@BeforeEach
//	public void setup() {
////		mvc2 = MockMvcBuilders.webAppContextSetup(wac).build();
//		mvc = MockMvcBuilders.standaloneSetup(itemController).build();
//	}
//
//	@Test
//	void getItemPage() throws Exception {
//		mvc.perform(get("/items/{item_id}", 4))
//				.andDo(print())
//				.andExpect(view().name("card"))
//				.andExpect(status().isOk());
//	}
//
//	@Test
//	void loginPageTest() throws Exception {
//		Man man = new Man();
//		man.setUserRole("");
//		Mockito.doReturn(man).when(manService).getEnteredMan();
//		Mockito.doReturn(new Item()).when(itemService).findById(1);
//		this.mvc.perform(post("/items/{item_id}/create_message", 1))
//				.andDo(print())
//				.andExpect(status().is3xxRedirection())
//				.andExpect(redirectedUrl("/items/1"));
//	}
//
//	@Test
//	void correctLoginPage() throws Exception {
//		this.mvc2.perform(formLogin().loginProcessingUrl("/auth/login").user("nik.porshennikov@mail.ru").password(""))
//				.andDo(print())
//				.andExpect(status().is3xxRedirection())
//				.andExpect(redirectedUrl("/main"));
//	}
//
//	@Test
//	void babCredentials() throws Exception{
//		this.mvc2.perform(post("/auth/login").param("user", "Alfred"))
//				.andDo(print())
//				.andExpect(status().isForbidden());
//	}
//
//	@Test
//	void mainPage() throws Exception {
//		mvc2.perform(get("main"))
//				.andDo(print())
//				.andExpect(authenticated());
//	}

}
