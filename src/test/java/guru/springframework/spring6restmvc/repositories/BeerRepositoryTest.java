package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.entities.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BeerRepositoryTest {
    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSavedBeer(){
        Beer savedBeer = beerRepository.save(Beer.builder().beerName("Beer1").build());
        assertNotNull(savedBeer);
        assertNotNull(savedBeer.getId());
        System.out.println(savedBeer.getBeerName()+"\t"+savedBeer.getId());
    }
}