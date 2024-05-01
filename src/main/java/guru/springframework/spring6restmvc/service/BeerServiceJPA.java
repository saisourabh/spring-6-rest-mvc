package guru.springframework.spring6restmvc.service;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import jakarta.persistence.OrderBy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;
    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize) {

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);
        Page<Beer> beerList;

        if(StringUtils.hasText(beerName) && beerStyle == null) {
            beerList = listBeersByName(beerName,pageRequest);
        } else if (!StringUtils.hasText(beerName) && beerStyle != null){
            beerList = listBeersByStyle(beerStyle,pageRequest);
        } else if (StringUtils.hasText(beerName) && beerStyle != null){
            beerList = listBeersByNameAndStyle(beerName, beerStyle,pageRequest);
        } else {
            beerList = beerRepository.findAll(pageRequest);
        }

        if (showInventory != null && !showInventory) {
            beerList.forEach(beer -> beer.setQuantityOnHand(null));
        }

        return beerList.map(beerMapper::beerToBeerDTO);
//        return beerList.stream()
//                .map(beerMapper::beerToBeerDTO)
//                .collect(Collectors.toList());
    }
    public PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber;
        int queryPageSize;

        if (pageNumber != null && pageNumber > 0) {
            queryPageNumber = pageNumber - 1;
        } else {
            queryPageNumber = DEFAULT_PAGE;
        }

        if (pageSize == null) {
            queryPageSize = DEFAULT_PAGE_SIZE;
        } else {
            if (pageSize > 1000) {
                queryPageSize = 1000;
            } else {
                queryPageSize = pageSize;
            }
        }
        Sort sort = Sort.by(Sort.Order.asc("beerName"));
        System.out.println("pageSize:"+pageSize);
        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }

    private Page<Beer> listBeersByNameAndStyle(String beerName, BeerStyle beerStyle,PageRequest pageRequest) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%"+beerName+"%",beerStyle, pageRequest);
    }

    private Page<Beer> listBeersByStyle(BeerStyle beerStyle,PageRequest pageRequest) {
        return beerRepository.findAllByBeerStyle(beerStyle, pageRequest);
    }

    public Page<Beer> listBeersByName(String beerName,PageRequest pageRequest) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%"+beerName+"%", pageRequest);
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        System.out.println("in new " + BeerServiceJPA.class + " /");
        return Optional.ofNullable(beerMapper.beerToBeerDTO(beerRepository.findById(id).orElse(null)));
    }

    @Override
    public BeerDTO saveNewBear(BeerDTO beer) {
        return beerMapper.beerToBeerDTO(beerRepository.save(beerMapper.beerDTOToBeer(beer)));
    }

    @Override
    public Optional<BeerDTO> updateBear(UUID id, BeerDTO beer) {
//        System.out.println("found "+beerRepository.findById(beer.getId()).get().getBeerName()+" ");
//        return Optional.of(beerMapper.beerToBeerDTO(beerRepository.saveAndFlush(beerMapper.beerDTOToBeer(beer))));
        AtomicReference<Optional<BeerDTO>> automocReference = new AtomicReference();
        beerRepository.findById(id).ifPresentOrElse(foundBeer -> {
            foundBeer.setBeerName(beer.getBeerName());
            foundBeer.setBeerStyle(beer.getBeerStyle());
            foundBeer.setUpc(beer.getUpc());
            foundBeer.setPrice(beer.getPrice());
            ;
            automocReference.set(Optional.of(beerMapper.beerToBeerDTO(beerRepository.save(foundBeer))));
        }, () -> automocReference.set(Optional.empty()));
        return automocReference.get();
    }

    @Override
    public boolean deleteBeerById(UUID id) {
        if (!beerRepository.findById(id).isEmpty()) {
            beerRepository.deleteById(id);
            return true;
        }
        return false;


    }

    @Override
    public Optional<BeerDTO> patchBeerById(UUID beerId, BeerDTO beer) {
        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();

        beerRepository.findById(beerId).ifPresentOrElse(foundBeer -> {
            if (StringUtils.hasText(beer.getBeerName())) {
                foundBeer.setBeerName(beer.getBeerName());
            }
            if (beer.getBeerStyle() != null) {
                foundBeer.setBeerStyle(beer.getBeerStyle());
            }
            if (StringUtils.hasText(beer.getUpc())) {
                foundBeer.setUpc(beer.getUpc());
            }
            if (beer.getPrice() != null) {
                foundBeer.setPrice(beer.getPrice());
            }
            if (beer.getQuantityOnHand() != null) {
                foundBeer.setQuantityOnHand(beer.getQuantityOnHand());
            }
            atomicReference.set(Optional.of(beerMapper
                    .beerToBeerDTO(beerRepository.save(foundBeer))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }
}
