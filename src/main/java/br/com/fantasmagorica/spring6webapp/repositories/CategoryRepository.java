package br.com.fantasmagorica.spring6webapp.repositories;

import br.com.fantasmagorica.spring6webapp.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository  extends JpaRepository<Category, UUID> {
}
