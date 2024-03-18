package br.com.fantasmagorica.spring6webapp.repositories;

import br.com.fantasmagorica.spring6webapp.entities.Beer;
import br.com.fantasmagorica.spring6webapp.models.BeerStyle;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

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