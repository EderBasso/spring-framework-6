package br.com.fantasmagorica.spring6webapp.services;

import br.com.fantasmagorica.spring6webapp.domain.Author;

public interface AuthorService {

    Iterable<Author> findAll();
}
