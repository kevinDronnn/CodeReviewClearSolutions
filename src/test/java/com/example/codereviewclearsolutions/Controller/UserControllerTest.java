package com.example.codereviewclearsolutions.Controller;

import com.example.codereviewclearsolutions.Dto.UserDto;
import com.example.codereviewclearsolutions.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    private final int id = 5;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Validator validator;

    private UserDto userDto = null;

    @BeforeEach
    public void setUp(){
        userDto = new UserDto();
        userDto.setFirstName("alex");
        userDto.setLastName("sanchez");
        userDto.setEmail("sanchez@gmail.com");
        userDto.setPhoneNumber("098432535");
        userDto.setAddress("Some address");
        userDto.setBirthDate(LocalDate.of(2000, Month.APRIL , 1));
    }

    @Test
    public void shouldSaveUser() throws Exception {
        doNothing().when(service).save(userDto);


            mockMvc.perform(post("/api/user/saveUser")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userDto)))
                    .andExpect(status().isCreated());

        verify(service, times(1)).save(userDto);
    }

    @Test
    public void shouldNotSaveUserByAge() throws Exception {
        userDto.setBirthDate(LocalDate.of(2023, Month.APRIL , 1));
        doThrow(new ArithmeticException("User must be 18 years or older")).when(service).save(userDto);


        mockMvc.perform(post("/api/user/saveUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User must be 18 years or older"));


        verify(service, times(1)).save(userDto);
    }

    @Test
    public void shouldNotSaveUserByEmptyRequiredField() throws Exception {
        userDto.setEmail("");
        doThrow(new IllegalArgumentException("You must fill all required fields")).when(service).save(userDto);


        mockMvc.perform(post("/api/user/saveUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("You must fill all required fields"));


        verify(service, times(1)).save(userDto);
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        doNothing().when(service).deleteUser(id);

        mockMvc.perform(delete("/api/user/deleteUser/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service,times(1)).deleteUser(id);
    }

    @Test
    public void shouldNotDeleteUserWhenUserNotFound() throws Exception {

        doThrow(new NoSuchElementException("User with id "+99+" not found")).when(service).deleteUser(99);

        mockMvc.perform(delete("/api/user/deleteUser/{id}", 99)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User with id "+99+" not found"));

        verify(service, times(1)).deleteUser(99);
    }

    @Test
    public void shouldGetUsersListInDateRange() throws Exception {
        when(service.findUserEntitiesByBirthDateBetween
                (LocalDate.of(2000, Month.APRIL , 1),
                        LocalDate.of(2000, Month.APRIL , 2))).thenReturn(List.of(userDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/findUsersInDateRange")
                        .param("from", "2000-04-01")
                        .param("to", "2000-04-02")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(userDto))));


        verify(service, times(1))
                .findUserEntitiesByBirthDateBetween(LocalDate.of(2000, Month.APRIL , 1),
                LocalDate.of(2000, Month.APRIL , 2));
    }

    @Test
    public void shouldNotGetUsersListInWrongDateRange() throws Exception {
        when(service.findUserEntitiesByBirthDateBetween(
                LocalDate.of(2001, Month.APRIL , 1),
                LocalDate.of(2000, Month.APRIL , 1)))
                .thenThrow(new IllegalArgumentException("First date must be < then second"));

        mockMvc.perform(get("/api/user/findUsersInDateRange")
                        .param("from", "2001-04-01")
                        .param("to", "2000-04-01")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());


        verify(service, times(1))
                .findUserEntitiesByBirthDateBetween(LocalDate.of(2001, Month.APRIL , 1),
                        LocalDate.of(2000, Month.APRIL , 1));
    }

    @Test
    public void shouldUpdateAllFieldsOfUser() throws Exception {
        userDto.setFirstName("Mike");
        doNothing().when(service).updateUser(id,userDto);

        mockMvc.perform(post("/api/user/updateAllFieldsOfUser/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());

        verify(service, times(1)).updateUser(id, userDto);
    }

    @Test
    public void shouldUpdateUserFields() throws Exception {
        mockMvc.perform(post("/api/user/updateUserFields/{id}", id)
                        .param("email", "mike@gmail.com")
                        .param("firstName", "")
                        .param("lastName", "")
                        .param("birthDate", "")
                        .param("address", "")
                        .param("phoneNumber", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).updateUserFields(id, "mike@gmail.com", "", "",
               null, "", "");
    }




}