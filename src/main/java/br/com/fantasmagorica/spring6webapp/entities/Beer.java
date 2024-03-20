package br.com.fantasmagorica.spring6webapp.entities;

import br.com.fantasmagorica.spring6webapp.models.BeerStyle;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor@NoArgsConstructor
public class Beer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, updatable = false, nullable = false)
    private UUID id;
    @Version
    private Integer version;
    @NotBlank
    @NotNull
    @Size(max = 50)
    @Column(length = 50)
    private String beerName;
    @NotNull
    private BeerStyle beerStyle;
    @NotBlank
    @Size(max = 255)
    private String upc;
    @NotNull
    private BigDecimal price;
    private Integer quantityOnHand;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
