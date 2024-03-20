package br.com.fantasmagorica.spring6webapp.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, updatable = false, nullable = false)
    private UUID id;
    @Version
    private Integer version;
    private String customerName;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
