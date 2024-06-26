package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.NotFoundException;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.service.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

import static guru.springframework.spring6restmvc.constants.RESTConstants.*;

@Slf4j
@RequiredArgsConstructor
@RestController
//@RequestMapping(BEER_URL)
public class BeerController {
    public static final String BEER_PATH = "/api/v1/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

    private final BeerService beerService;
    @PatchMapping(value = BEER_URL_ID)
    public ResponseEntity updateBeerPatchById( @PathVariable(BEER_ID) UUID id, @RequestBody BeerDTO beer){
        beerService.patchBeerById(id,beer);
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity(headers,HttpStatus.NO_CONTENT);
    }


    @DeleteMapping(value = BEER_URL_ID)
    public ResponseEntity deleteById(@PathVariable(BEER_ID)UUID id){
        if(beerService.deleteBeerById(id)) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity(headers, HttpStatus.NO_CONTENT);
        }
        else
            throw new NotFoundException("Beer with id " + id + " not found");

    }

    @PutMapping(value = BEER_URL_ID)
    public ResponseEntity updateById(@PathVariable(BEER_ID) UUID id, @RequestBody BeerDTO beer){
        if(beerService.updateBear(id,beer).isEmpty())
            throw new NotFoundException("Beer not found");

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    @RequestMapping(value = BEER_URL_ID,method = RequestMethod.GET)
    public BeerDTO getBeerById(@PathVariable(BEER_ID) UUID beerId){
        System.out.println("KOK\t"+beerId+"\tKOK");
        System.out.println("KOKOKO");
        log.debug("Get Beer by Id - controller");
        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }
    @RequestMapping(value = BEER_URL,method = RequestMethod.GET)
    public Page<BeerDTO> beerList(@RequestParam(required = false) String beerName,
                                  @RequestParam(required = false) BeerStyle beerStyle,
                                  @RequestParam(required = false) Boolean showInventory,
                                  @RequestParam(required = false) Integer pageNumber,
                                  @RequestParam(required = false) Integer pageSize){
        log.debug("Get Beer by Id - controller");
        return beerService.listBeers(beerName, beerStyle, showInventory, pageNumber, pageSize);
       // return beerService.listBeers().stream().filter(beer -> beer.getBeerName().equals(beerName)).collect(Collectors.toList());
    }
    @PostMapping(value = BEER_URL)
    //@RequestMapping(method = RequestMethod.POST)
    public ResponseEntity handlePost(@Validated @RequestBody BeerDTO beer){
        System.out.println(beer);
        beer.setUpdateDate(LocalDateTime.now());
        beer.setCreatedDate(LocalDateTime.now());
        BeerDTO savedBeed = beerService.saveNewBear(beer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location",BEER_URL+"/"+savedBeed.getId());
        return new ResponseEntity(headers,HttpStatus.CREATED);
    }


}
