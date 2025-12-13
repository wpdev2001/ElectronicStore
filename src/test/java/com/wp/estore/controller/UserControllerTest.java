package com.wp.estore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.wp.estore.dtos.UserDto;
import com.wp.estore.entities.User;
import com.wp.estore.services.UserService;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
	@MockitoBean
	private UserService userService;
	
	private User user;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private MockMvc mockMvc;
	
	@BeforeEach
	public void init() {
		user = User.builder()
				.name("Piyush")
				.email("piyush123@gmail.com")
				.about("This is testing create method")
				.gender("Male")
				.imageName("abc.png")
				.password("abc")
				.build();
	}
	
	@Test
	public void testCreateUser() throws Exception {
		UserDto userDto = mapper.map(user, UserDto.class);

		Mockito.when(userService.createUser(Mockito.any())).thenReturn(userDto);

		//actual request for url

		this.mockMvc.perform(MockMvcRequestBuilders.post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(convertObjectToJsonString(user))
				.accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists());

	}

    @Test
    public void testUpdateUser() throws Exception {
        String userId = "ABC";
        Mockito.when(userService.updateUser(Mockito.any(),Mockito.anyString())).thenReturn(mapper.map(user,UserDto.class));

        this.mockMvc.perform(MockMvcRequestBuilders.put("/users/{userId}}",userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonString(user))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists());
    }

    private String convertObjectToJsonString(Object obj) {
        try{
            return new ObjectMapper().writeValueAsString(obj);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
