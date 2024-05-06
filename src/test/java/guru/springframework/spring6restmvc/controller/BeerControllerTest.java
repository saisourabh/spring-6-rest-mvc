package guru.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.config.SpringSecConfig;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.service.BeerService;
import guru.springframework.spring6restmvc.service.BeerServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.*;
import java.util.stream.Collectors;

import static guru.springframework.spring6restmvc.constants.RESTConstants.BEER_URL;
import static guru.springframework.spring6restmvc.constants.RESTConstants.BEER_URL_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//@SpringBootTest
@WebMvcTest(BeerController.class)
@Import(SpringSecConfig.class)
class BeerControllerTest {
    @Autowired
    ObjectMapper objectMapper;
    //@Autowired
    //BeerController beerController;
    @Autowired
    MockMvc mockMvc;
    @MockBean
    BeerService beerService;

    BeerServiceImp beerServiceImp;
    static int itr = 0;

    @Captor
    ArgumentCaptor<BeerDTO> beerArgumentCaptor;
    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @BeforeEach
    void setUp() {
        beerServiceImp = new BeerServiceImp();
        System.out.println("itr:" + itr);
        itr += 1;
    }

    @Test
    void getBeerById() throws Exception {
        BeerDTO testBeer = beerServiceImp.listBeers(null, null, false, 1, 25).stream().findFirst().orElse(null);
        given(beerService.getBeerById(testBeer.getId())).willReturn(Optional.of(testBeer));
        ResultActions resultActions = mockMvc.perform(get(BEER_URL_ID, testBeer.getId())
                        .with(httpBasic("user1","password"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
        System.out.println(resultActions);
        System.out.println(resultActions);

        // System.out.println(beerController.getBeerById(UUID.randomUUID()));
    }

    @Test
    void getBeerById1() throws Exception {
        mockMvc.perform(get(BEER_URL_ID, UUID.randomUUID())
                        .with(httpBasic("user1","password"))

                        .accept(MediaType.APPLICATION_JSON))
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        // System.out.println(beerController.getBeerById(UUID.randomUUID()));
    }

    @Test
    void testListBeers() throws Exception {
        given(beerService.listBeers(any(), any() , any(), any(), any())).willReturn(beerServiceImp.listBeers(null, null, false, 1, 25));

        mockMvc.perform(get(BEER_URL)
                        .with(httpBasic("user1","password"))

                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.size()", is(3)));
    }

    @Test
    void testCreatedNewBeer() throws Exception {

        BeerDTO beer = beerServiceImp.listBeers(null, null, false, 1, 25).stream().findFirst().orElse(null);
        System.out.println(objectMapper.writeValueAsString(beer));
        //beer.setVersion(null);
        // beer.setId(null);
        System.out.println(beer);
        given(beerService.saveNewBear(any(BeerDTO.class))).willReturn(beerServiceImp.listBeers(null, null, false, 1, 25).stream().findFirst().orElse(null));

        mockMvc.perform(post(BEER_URL)
                        .with(httpBasic("user1","password"))

                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

    }

    @Test
    void testCreatedNullBeer() throws Exception {

        BeerDTO beer = BeerDTO.builder().build();
        System.out.println(objectMapper.writeValueAsString(beer));
        //beer.setVersion(null);
        // beer.setId(null);
        given(beerService.saveNewBear(any(BeerDTO.class))).willReturn(beerServiceImp.listBeers(null, null, false, 1, 25).stream().findFirst().orElse(null));

        MvcResult mvcResult = mockMvc.perform(post(BEER_URL)
                        .with(httpBasic("user1","password"))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()",is(6))).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());

    }

    @Test
    void testUpdateBeer() throws Exception {
        BeerDTO beer = beerServiceImp.listBeers(null, null, false, 1, 25).stream().findFirst().orElse(null);
        System.out.println(beer);
        given(beerService.updateBear(any(), any())).willReturn(Optional.of(beer));
        ResultActions resultActions = mockMvc.perform(put(BEER_URL_ID, beer.getId())
                        .with(httpBasic("user1","password"))

                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isNoContent());

        List<String> collect = resultActions.andReturn().getResponse().getHeaderNames().stream().collect(Collectors.toList());
        collect.forEach(System.out::println);
        collect.stream().map(str -> str + ":").map(str -> str.split("")).flatMap(Arrays::stream).forEach(System.out::print);
        verify(beerService, times(1)).updateBear(any(UUID.class), any(BeerDTO.class));

    }

    @Test
    void testDeleteBeer() throws Exception {
        BeerDTO beer = beerServiceImp.listBeers(null, null, false, 1, 25).stream().findFirst().orElse(null);
        given(beerService.deleteBeerById(any())).willReturn(true);
        mockMvc.perform(delete(BEER_URL_ID, beer.getId())
                        .with(httpBasic("user1","password"))

                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(beerService).deleteBeerById(uuidArgumentCaptor.capture());
        UUID uuid = uuidArgumentCaptor.getValue();
        System.out.println(uuid);
        verify(beerService, times(1)).deleteBeerById(any(UUID.class));
        assert (beer.getId().equals(uuid));
    }

    @Test
    void testPatchBeer() throws Exception {
        BeerDTO beer = beerServiceImp.listBeers(null, null, false, 1, 25).getContent().get(0);
        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "New Name");

        mockMvc.perform(patch(BEER_URL_ID, beer.getId())
                        .with(httpBasic("user1","password"))

                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isNoContent());
        verify(beerService).patchBeerById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());
        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(beer).isEqualTo(beerArgumentCaptor.getValue());


    }

    @Test
    void NotFoundException() throws Exception {
        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());
        mockMvc.perform(get(BEER_URL_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}