package guru.springframework.spring6restmvc.service;

import guru.springframework.spring6restmvc.model.Beer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    List<Beer> listBeers();

    Optional<Beer> getBeerById(UUID id);

    Beer saveNewBear(Beer beer);

    Beer updateBear(UUID id, Beer beer);
    Beer deleteBeerById(UUID id);

    void patchBeerById(UUID id, Beer beer);
}
