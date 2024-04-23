package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.NotFoundException;
import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class BeerControlleriT {
    @Autowired
    BeerController  beerController;;
    @Autowired
    BeerRepository beerRepository;
    @Autowired
    private BeerMapper beerMapper;

    @Test
    void ListBeers(){
      List<BeerDTO> beerList = beerController.beerList();
        beerList.stream().forEach(System.out::println);
      assert(beerList.size() == 3);
    }
    @Transactional
    @Test
    void DeleteBeers(){
        beerRepository.deleteAll();
        List<BeerDTO> beerList = beerController.beerList();

        beerList.stream().forEach(System.out::println);
        assert(beerList.size() == 0);
    }

    @Test
    void testBeerById(){
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO dto = beerController.getBeerById(beer.getId());
        assertThat(dto).isNotNull();
    }
    @Test
    void testBeerByIdException(){
        assertThrows(NotFoundException.class, () -> {
            beerController.getBeerById(UUID.randomUUID());
        });
    }

    @Test
    void testSavedBeer(){
        System.out.println("Sourabhj");
        BeerDTO beerDTO = BeerDTO.builder().beerName("Sourabh").build();
        ResponseEntity responseEntity= beerController.handlePost(beerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
        System.out.println(responseEntity.getHeaders().getLocation().getPath());
        String[] locations = responseEntity.getHeaders().getLocation().getPath().toString().split("/");
        Arrays.stream(locations).forEach(System.out::println);
        UUID id = UUID.fromString(locations[4]);
        Beer beer = beerRepository.findById(id).get();
        assertThat(beer).isNotNull();

    }

    @Transactional
    @Test
    void updateBeerTest(){
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO beerDTO = beerMapper.beerToBeerDTO(beer);
        beerDTO.setId(null);
        beerDTO.setBeerStyle(null);
        final String beerName = "UPDATED BEER";
        beerDTO.setBeerName(beerName);
        ResponseEntity responseEntity = beerController.updateById(beer.getId(),beerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(204));
        Beer saved = beerRepository.findById(beer.getId()).get();
        assertThat(saved).isNotNull();
        assertThat(saved.getBeerName()).isEqualTo(beerName);

       // assertThat(saved.getBeerName()).isEqualTo(beerName);
    }
    @Transactional
    @Test
    void updateBeerTestNegative(){
        assertThrows(NotFoundException.class, () -> {
            beerController.updateById(UUID.randomUUID(), BeerDTO.builder().build());
        });
    }

    @Transactional
    @Test
    void deleteBeerTest(){
        Beer beer = beerRepository.findAll().get(0);
        ResponseEntity responseEntity = beerController.deleteById(beer.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(204));
        assertThat(beerRepository.findById(beer.getId()).isEmpty());
    }
    @Transactional
    @Test
    void deleteBeerTestNegative(){
        assertThrows(NotFoundException.class, () -> {
            beerController.deleteById(UUID.randomUUID());
        });
    }
}