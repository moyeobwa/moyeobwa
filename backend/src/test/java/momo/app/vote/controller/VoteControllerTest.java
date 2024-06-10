package momo.app.vote.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import momo.app.auth.jwt.service.JwtCreateAndUpdateService;
import momo.app.vote.dto.VoteCreateRequest;
import momo.app.vote.dto.VoteRequest;
import momo.app.vote.dto.VoteResponse;
import momo.app.vote.service.VoteCommandService;
import momo.app.vote.service.VoteQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class VoteControllerTest {

    private static final String BEARER = "Bearer ";

    @MockBean
    VoteCommandService voteCommandService;

    @MockBean
    VoteQueryService voteQueryService;

    @Autowired
    JwtCreateAndUpdateService jwtCreateAndUpdateService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    void 투표를_생성한다() throws Exception {
        Long voteId = 1L;
        VoteCreateRequest request = new VoteCreateRequest("투표1", 1L, List.of("옵션1", "옵션2", "옵션3"));

        given(voteCommandService.create(any(), any())).willReturn(voteId);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/votes")
                .header(AUTHORIZATION, BEARER + jwtCreateAndUpdateService.createAccessToken("user"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(print());
    }

    @Test
    void 투표를_진행한다() throws Exception {
        VoteRequest request = new VoteRequest(1L);
        doNothing().when(voteCommandService).vote(any(), any(), any());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/votes/1/vote")
                .header(AUTHORIZATION, BEARER + jwtCreateAndUpdateService.createAccessToken("user"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    void 모임방의_투표_리스트를_조회한다() throws Exception {
        List<VoteResponse> response = List.of(
                new VoteResponse("투표1", 3L),
                new VoteResponse("투표2", 2L),
                new VoteResponse("투표3", 1L)
        );

        given(voteQueryService.findAll(any(), any())).willReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/votes/1")
                .header(AUTHORIZATION, BEARER + jwtCreateAndUpdateService.createAccessToken("user")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[2].id").value(1))
                .andDo(print());
    }
}