package br.com.fantasmagorica.spring6webapp.models.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BeerCSVRecord {

    @CsvBindByName
    private Integer row;
    @CsvBindByName
    private Integer count;
    @CsvBindByName
    private String abv;
    @CsvBindByName
    private String ibu;
    @CsvBindByName
    private Integer id;
    @CsvBindByName
    private String beer;
    @CsvBindByName
    private String style;
    @CsvBindByName
    private Integer breweryId;
    @CsvBindByName
    private Float ounces;
    @CsvBindByName
    private String style2;
    @CsvBindByName
    private String count_y;
    @CsvBindByName
    private String brewery;
    @CsvBindByName
    private String city;
    @CsvBindByName
    private String state;
    @CsvBindByName
    private String label;
}
