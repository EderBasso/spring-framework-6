package br.com.fantasmagorica.spring6webapp.service;

import br.com.fantasmagorica.spring6webapp.model.Beer;

import java.util.List;
import java.util.UUID;

public interface BeerService {
    List<Beer> listBeers();

    Beer getBeerById(UUID id);
}
