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
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {
    public final BeerRepository beerRepository;
    public final BeerMapper beerMapper;

    @Override
    public List<BeerDTO> listBeers() {
        return beerRepository.findAll().stream()
                .map(beerMapper::beerToBeerDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        System.out.println("in new "+BeerServiceJPA.class+" /");
        return Optional.ofNullable(beerMapper.beerToBeerDTO(beerRepository.findById(id).orElse(null)));
    }

    @Override
    public BeerDTO saveNewBear(BeerDTO beer) {
           return beerMapper.beerToBeerDTO( beerRepository.save(beerMapper.beerDTOToBeer(beer)));
    }

    @Override
    public Optional<BeerDTO> updateBear(UUID id, BeerDTO beer) {
        AtomicReference<Optional<BeerDTO>> automocReference   = new AtomicReference();
        beerRepository.findById(id).ifPresentOrElse( foundBeer -> {
            foundBeer.setBeerName(beer.getBeerName());
            foundBeer.setBeerStyle(beer.getBeerStyle());
            foundBeer.setUpc(beer.getUpc());
            foundBeer.setPrice(beer.getPrice());
           ;
            automocReference.set(Optional.of(beerMapper.beerToBeerDTO( beerRepository.save(foundBeer))));
        }, () -> automocReference.set(Optional.empty()));
    return automocReference.get();
    }

    @Override
    public boolean deleteBeerById(UUID id) {
        if(! beerRepository.findById(id).isEmpty())
        {
            beerRepository.deleteById(id);
            return true;
        }
        return false;


    }

    @Override
    public void patchBeerById(UUID id, BeerDTO beer) {

    }
}
