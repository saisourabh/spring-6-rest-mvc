package guru.springframework.spring6restmvc.service;

import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {
    public final BeerRepository beerRepository;
    public final BeerMapper beerMapper;
    @Override
    public List<BeerDTO> listBeers() {
        return List.of();
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.empty();
    }

    @Override
    public BeerDTO saveNewBear(BeerDTO beer) {
        return null;
    }

    @Override
    public BeerDTO updateBear(UUID id, BeerDTO beer) {
        return null;
    }

    @Override
    public BeerDTO deleteBeerById(UUID id) {
        return null;
    }

    @Override
    public void patchBeerById(UUID id, BeerDTO beer) {

    }
}
