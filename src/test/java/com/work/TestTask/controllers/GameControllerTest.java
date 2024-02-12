package com.work.TestTask.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GameControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testNew_ValidResponse() throws Exception {
        String result = mockMvc.perform(post("/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "width": 10,
                                  "height": 10,
                                  "mines_count": 10
                                }
                                """))
                .andExpectAll(status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        assertNotNull(result);
        assertThat(result).contains("game_id",
                "field",
                "\"width\":10",
                "height\":10",
                "mines_count\":10",
                "field\":[[",
                "completed");
    }

    @Test
    void testNew_CORS() throws Exception {
        mockMvc.perform(post("/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "width": 10,
                                  "height": 10,
                                  "mines_count": 10
                                }
                                """))
                .andExpectAll(status().isOk(), header().string("Vary", "Origin"),
                        content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testNew_WrongSize() throws Exception {
        String result = mockMvc.perform(post("/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "width": 100,
                                  "height": 30,
                                  "mines_count": 10
                                }
                                """))
                .andExpectAll(status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        assertNotNull(result);
        assertThat(result).contains("error", "2", "30");
    }

    @Test
    void testTurn_Game_idNotFound() throws Exception {
        String result = mockMvc.perform(post("/turn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                   "game_id": "01234567-89AB-CDEF-0123-456789ABCDEF",
                                   "col": 5,
                                   "row": 5
                                 }
                                """))
                .andExpectAll(status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        assertNotNull(result);
        assertThat(result).contains("error", "01234567-89AB-CDEF-0123-456789ABCDEF".toLowerCase());
    }

    @Test
    void testTurn_ValidResponse() throws Exception {
        String result = mockMvc.perform(post("/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "width": 15,
                                  "height": 17,
                                  "mines_count": 12
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(result).contains("game_id");
        String game_id = result.substring(result.indexOf("{game_id:\":\"") + "{game_id:\":\"".length() + 1, result.indexOf("{game_id:\":\"") + "{game_id:\":\"".length() + 1 + "01234567-89AB-CDEF-0123-456789ABCDEF".length());
        result = mockMvc.perform(post("/turn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"game_id\": \"" + game_id + "\"," +
                                """
                                    "col": 5,
                                    "row": 5
                                }
                                """))
                .andExpectAll(status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        assertNotNull(result);
        assertThat(result).contains("game_id",
                "field",
                "width",
                "height",
                "mines_count",
                "field\":[[",
                "completed");
    }
}
