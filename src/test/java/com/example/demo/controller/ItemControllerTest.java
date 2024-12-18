package com.example.demo.controller;

import com.example.demo.dto.ItemRequestDto;
import com.example.demo.filter.CommonAuthFilter;
import com.example.demo.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    ItemService itemService;

    @Test
    void createItem() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto("KMH", "내용", 1L, 1L);

        String requestBody = new ObjectMapper().writeValueAsString(itemRequestDto);

        ResultActions result = mockMvc.perform(post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        result.andExpect(status().isOk());
    }
}