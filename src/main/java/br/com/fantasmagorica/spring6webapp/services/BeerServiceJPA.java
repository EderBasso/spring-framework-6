package br.com.fantasmagorica.spring6webapp.services;

import br.com.fantasmagorica.spring6webapp.entities.Beer;
import br.com.fantasmagorica.spring6webapp.mappers.BeerMapper;
import br.com.fantasmagorica.spring6webapp.models.BeerDTO;
import br.com.fantasmagorica.spring6webapp.models.BeerStyle;
import br.com.fantasmagorica.spring6webapp.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

    private final BeerRepository beerRepository;

    private final BeerMapper beerMapper;

    private final static Integer DEFAULT_PAGE = 0;
    private final static Integer DEFAULT_PAGE_SIZE = 25;

    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory,
                                   Integer pageNumber, Integer pageSize) {

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        Page<Beer> beerPage;

        if(StringUtils.hasText(beerName) && beerStyle == null){
            beerPage = listBeersByName(beerName, pageRequest);
        }else if(!StringUtils.hasText(beerName) && beerStyle != null){
            beerPage = listBeersByStyle(beerStyle, pageRequest);
        }else if(StringUtils.hasText(beerName) && beerStyle != null){
            beerPage = listBeersByNameAndStyle(beerName, beerStyle, pageRequest);
        }else{
            beerPage = beerRepository.findAll(pageRequest);
        }

        if(showInventory != null && !showInventory) {
            beerPage.forEach(beer -> beer.setQuantityOnHand(null));
        }

        return beerPage.map(beerMapper::beerToBeerDto);

    }

    PageRequest buildPageRequest(Integer pageNumber, Integer pageSize){
        int queryPageNumber;
        int queryPageSize;

        if(pageNumber != null && pageNumber > 0){
            queryPageNumber = pageNumber - 1;
        }else {
            queryPageNumber = DEFAULT_PAGE;
        }

        if(pageSize != null && pageSize>0){
            queryPageSize = pageSize <= 1000 ? pageSize : 1000;
        }else {
            queryPageSize = DEFAULT_PAGE_SIZE;
        }

        Sort sort = Sort.by(Sort.Order.asc("beerName"));

        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }

    Page<Beer> listBeersByNameAndStyle(String beerName, BeerStyle beerStyle, PageRequest pageRequest){
        return beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%"+beerName+"%", beerStyle, pageRequest);
    }

    Page<Beer> listBeersByName(String beerName, PageRequest pageRequest){
        return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%"+beerName+"%", pageRequest);
    }

    Page<Beer> listBeersByStyle(BeerStyle beerStyle, PageRequest pageRequest){
        return beerRepository.findAllByBeerStyle(beerStyle, pageRequest);
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.ofNullable(beerMapper.beerToBeerDto(beerRepository.findById(id).orElse(null)));
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {
        return beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beer)));
    }

    @Override
    public Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer) {
        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();

        beerRepository.findById(beerId).ifPresentOrElse(foundBeer -> {
            foundBeer.setBeerName(beer.getBeerName());
            foundBeer.setBeerStyle(beer.getBeerStyle());
            foundBeer.setUpc(beer.getUpc());
            foundBeer.setPrice(beer.getPrice());
            atomicReference.set(Optional.ofNullable(beerMapper
                    .beerToBeerDto(beerRepository.save(foundBeer))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public Boolean deleteBeerById(UUID beerId) {
        if (beerRepository.existsById(beerId)){
            beerRepository.deleteById(beerId);
            return true;
        }
        return false;
    }

    @Override
    public Optional<BeerDTO> patchBeerById(UUID beerId, BeerDTO beer) {
        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();

        beerRepository.findById(beerId).ifPresentOrElse(foundBeer -> {
            if(beer.getBeerName() != null){
                foundBeer.setBeerName(beer.getBeerName());
            }
            if(beer.getBeerStyle() != null){
                foundBeer.setBeerStyle(beer.getBeerStyle());
            }
            if(beer.getUpc() != null){
                foundBeer.setUpc(beer.getUpc());
            }
            if(beer.getPrice() != null){
                foundBeer.setPrice(beer.getPrice());
            }
            atomicReference.set(Optional.ofNullable(beerMapper
                    .beerToBeerDto(beerRepository.save(foundBeer))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }
}
