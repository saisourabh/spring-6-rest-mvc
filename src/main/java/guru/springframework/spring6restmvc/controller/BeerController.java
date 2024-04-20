package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.NotFoundException;
import guru.springframework.spring6restmvc.annotations.SomePostMapping;
import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.service.BeerService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.springframework.spring6restmvc.constants.RESTConstants.*;

@Slf4j
@RequiredArgsConstructor
@RestController
//@RequestMapping(BEER_URL)
public class BeerController {
    private final BeerService beerService;
    @PatchMapping(value = BEER_URL_ID)
    public ResponseEntity updateBeerPatchById(@PathVariable(BEER_ID) UUID id, @RequestBody Beer beer){
        beerService.patchBeerById(id,beer);
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity(headers,HttpStatus.NO_CONTENT);
    }


    @DeleteMapping(value = BEER_URL_ID)
    public ResponseEntity deleteById(@PathVariable(BEER_ID)UUID id){
        Beer deletedBeer = beerService.deleteBeerById(id);
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity(headers,HttpStatus.NO_CONTENT);

    }

    @PutMapping(value = BEER_URL_ID)
    public ResponseEntity updateById(@PathVariable(BEER_ID) UUID id, @RequestBody Beer beer){
        Beer savedBeer = beerService.updateBear(id,beer);
        System.out.println("SAVEDBEAR:"+savedBeer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location",BEER_URL+beer.getId());
        headers.add("2nd",BEER_URL+beer.getId());
        headers.add("3rd",BEER_URL+beer.getId());
        headers.add("Ocation",BEER_URL+beer.getId());
        return new ResponseEntity(headers,HttpStatus.NO_CONTENT);
    }
    @RequestMapping(value = BEER_URL_ID,method = RequestMethod.GET)
    public Beer getBeerById(@PathVariable(BEER_ID) UUID beerId){
        System.out.println("KOK\t"+beerId+"\tKOK");
        System.out.println("KOKOKO");
        log.debug("Get Beer by Id - controller");
        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }
    @RequestMapping(value = BEER_URL,method = RequestMethod.GET)
    public List<Beer> beerList(){
        log.debug("Get Beer by Id - controller");
        return beerService.listBeers();
    }
    @PostMapping(value = BEER_URL)
    //@RequestMapping(method = RequestMethod.POST)
    public ResponseEntity handlePost(@RequestBody Beer beer){
        Beer savedBeed = beerService.saveNewBear(beer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location",BEER_URL+savedBeed.getId());
        return new ResponseEntity(headers,HttpStatus.CREATED);
    }


}
