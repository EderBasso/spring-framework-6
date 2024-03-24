package br.com.fantasmagorica.spring6webapp.repositories;

import br.com.fantasmagorica.spring6webapp.bootstrap.BootstrapData;
import br.com.fantasmagorica.spring6webapp.entities.Beer;
import br.com.fantasmagorica.spring6webapp.models.BeerStyle;
import br.com.fantasmagorica.spring6webapp.services.BeerCsvServiceImpl;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import({BootstrapData.class, BeerCsvServiceImpl.class})
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testListBeersByName() {
        List<Beer> list = beerRepository.findAllByBeerNameIsLikeIgnoreCase("%IPA%");

        assertThat(list.size()).isEqualTo(336);
    }

    @Test
    void testSaveBeerTooLong() {

        assertThrows(ConstraintViolationException.class, () ->{
            Beer savedBeer = beerRepository.save(Beer.builder()
                    .beerName("Testing Beer Too Long Name Testing Beer Too Long Name")
                    .beerStyle(BeerStyle.LAGER)
                    .upc("123123")
                    .price(new BigDecimal("12.99"))
                    .build());

            beerRepository.flush();
        });
    }

    @Test
    void testSaveBeer() {
        Beer savedBeer = beerRepository.save(Beer.builder()
                .beerName("Test Beer")
                        .beerStyle(BeerStyle.LAGER)
                        .upc("123123")
                        .price(new BigDecimal("12.99"))
                .build());

        beerRepository.flush();

        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }
}