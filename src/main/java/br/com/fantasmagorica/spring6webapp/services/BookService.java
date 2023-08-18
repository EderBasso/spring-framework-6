package br.com.fantasmagorica.spring6webapp.services;

import br.com.fantasmagorica.spring6webapp.domain.Book;

public interface BookService {

    Iterable<Book> findAll();
}
