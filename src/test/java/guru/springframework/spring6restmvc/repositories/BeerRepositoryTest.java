package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BeerRepositoryTest {
    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSavedBeer(){
        Beer savedBeer = beerRepository.save(Beer.builder()
                .beerName("Beer1")
                .beerStyle(BeerStyle.PALE_ALE)
                .price(new BigDecimal("11.0"))
                .upc("KOL").build());
        beerRepository.flush();
        beerRepository.findAll().stream().forEach(System.out::println);
        assertNotNull(savedBeer);
        assertNotNull(savedBeer.getId());
        System.out.println(savedBeer.getBeerName()+"\t"+savedBeer.getId());
    }
    @Test
    void testSavedBeerLength(){
        assertThrows(ConstraintViolationException.class ,()-> {
            Beer savedBeer = beerRepository.save(Beer.builder()
                    .beerName("Beer11111111111111111111111111111111111111111111111111111111111111111111111111111111")
                    .beerStyle(BeerStyle.PALE_ALE)
                    .price(new BigDecimal("11.0"))
                    .upc("KOL").build());
            beerRepository.flush();
            beerRepository.findAll().stream().forEach(System.out::println);
        });

    }
}