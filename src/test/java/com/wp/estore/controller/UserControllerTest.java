package com.wp.estore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wp.estore.dtos.ApiResponseMessage;
import com.wp.estore.dtos.PageableResponse;
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

import java.util.List;

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
                .userId("ABC")
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

    @Test
    public void testGetAllUsers(){
        //creating user DTOs
        UserDto userDto = UserDto.builder().userId("111").name("ABC").password("abc2001@").email("abc123@gmail.com").about("This is about ABC").gender("Male").imageName("abc.png").build();
        UserDto userDto1 = UserDto.builder().userId("112").name("PQR").password("pqr2001@").email("pqr123@gmail.com").about("This is about PQR").gender("Female").imageName("pqr.png").build();
        UserDto userDto2 = UserDto.builder().userId("113").name("XYZ").password("xyz2001@").email("xyz123@gmail.com").about("This is about XYZ").gender("Male").imageName("xyz.png").build();

        PageableResponse <UserDto> pageableResponse = new PageableResponse<>();
        pageableResponse.setContent(List.of(userDto,userDto1,userDto2));
        pageableResponse.setPageNumber(1);
        pageableResponse.setPageSize(10);
        pageableResponse.setTotalElements(3);
        pageableResponse.setTotalPages(1);
        pageableResponse.setLastPage(false);

        Mockito.when(userService.getAllUser(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pageableResponse);

        try{
            this.mockMvc.perform(MockMvcRequestBuilders.get("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content.length()").value(3))
                    .andExpect(jsonPath("$.pageNumber").value(1))
                    .andExpect(jsonPath("$.pageSize").value(10))
                    .andExpect(jsonPath("$.totalElements").value(3))
                    .andExpect(jsonPath("$.totalPages").value(1))
                    .andExpect(jsonPath("$.lastPage").value(false));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testDeleteUser(){
        String userId = "ABC";

        Mockito.doNothing().when(userService).deleteUser(Mockito.anyString());

        try{
            this.mockMvc.perform(MockMvcRequestBuilders.delete("/users/{userId}",userId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("User deleted Successfully!!!"))
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.status").value("OK"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetUserById(){
        String userId = "ABC";
        Mockito.when(userService.getUserById(Mockito.anyString())).thenReturn(mapper.map(user,UserDto.class));
        try{
            this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}",userId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Piyush"))
                    .andExpect(jsonPath("$.email").value("piyush123@gmail.com"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void uploadUserImage(){
        // This test can be implemented when the image upload functionality is added.
        // PENDING
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
