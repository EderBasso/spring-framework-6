package br.com.fantasmagorica.spring6webapp.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class BeerDTO {
    private UUID id;
    private Integer version;

    @NotBlank
    @NotNull
    private String beerName;

    @NotNull
    private BeerStyle beerStyle;

    @NotBlank
    private String upc;

    @NotNull
    private BigDecimal price;

    private Integer quantityOnHand;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}