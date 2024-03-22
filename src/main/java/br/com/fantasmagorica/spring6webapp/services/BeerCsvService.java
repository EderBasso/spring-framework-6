package br.com.fantasmagorica.spring6webapp.services;

import br.com.fantasmagorica.spring6webapp.models.csv.BeerCSVRecord;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public interface BeerCsvService {
    List<BeerCSVRecord> convertCSV(File csvFile);
}
