package guru.springframework.spring6restmvc.service;

import guru.springframework.spring6restmvc.model.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    List<BeerDTO> listBeers();

    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO saveNewBear(BeerDTO beer);

    Optional<BeerDTO> updateBear(UUID id, BeerDTO beer);
    boolean deleteBeerById(UUID id);

    void patchBeerById(UUID id, BeerDTO beer);
}
