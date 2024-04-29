package guru.springframework.spring6restmvc.service;

import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImp implements BeerService{

    private Map<UUID, BeerDTO> beerMap;
    public BeerServiceImp(){
        this.beerMap = new HashMap<>();
        BeerDTO beer1 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Galaxy Kitty")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        BeerDTO beer2 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("1234567")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        BeerDTO beer3 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.IPA)
                .upc("123456")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        beerMap.put(beer1.getId(),beer1);
        beerMap.put(beer2.getId(),beer2);
        beerMap.put(beer3.getId(),beer3);
    }

    @Override
    public List<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory){
        return new ArrayList<>(beerMap.values());
    }
    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        log.debug("Get Beer by Id - in service, id:"+id.toString());
        return Optional.of(beerMap.get(id));
    }

    @Override
    public BeerDTO saveNewBear(BeerDTO beer) {
        BeerDTO savedBeer = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(beer.getVersion())
                .beerName(beer.getBeerName())
                .beerStyle(beer.getBeerStyle())
                .upc(beer.getUpc())
                .price(beer.getPrice())
                .quantityOnHand(beer.getQuantityOnHand())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
         beerMap.put(savedBeer.getId(),savedBeer);
         return savedBeer;
    }

    @Override
    public Optional<BeerDTO> updateBear(UUID id, BeerDTO beer) {
        System.out.println("IIJIJIJIJIJIJIJIJIJI");
        System.out.println("beer: /3"+beer);
        BeerDTO exisitng = beerMap.get(id);
        if(exisitng != null) {
            exisitng.setBeerName(beer.getBeerName());
            exisitng.setBeerStyle(beer.getBeerStyle());
            exisitng.setPrice(beer.getPrice());
            exisitng.setVersion(beer.getVersion());
            exisitng.setUpc(beer.getUpc());
           // return beerMap.put(exisitng.getId(), exisitng);
        }
        return Optional.of(exisitng);
       //return beerMap.put(id,beer);
    }

    @Override
    public boolean deleteBeerById(UUID id) {
          beerMap.remove(id);
          return true;
    }

    @Override
    public Optional<BeerDTO> patchBeerById(UUID id, BeerDTO beer) {

        BeerDTO existing = beerMap.get(id);
        if(StringUtils.hasText(beer.getBeerName()))
            existing.setBeerName(beer.getBeerName());
        if(beer.getBeerStyle() != null)
            existing.setBeerStyle(beer.getBeerStyle());
        if(beer.getPrice() != null)
            existing.setPrice(beer.getPrice());
        if(beer.getVersion() != null)
            existing.setVersion(beer.getVersion());
        if(beer.getQuantityOnHand() !=null)
            existing.setQuantityOnHand(beer.getQuantityOnHand());
        if(StringUtils.hasText(beer.getUpc()))
            existing.setUpc(beer.getUpc());
        return Optional.of(existing);

    }

}
