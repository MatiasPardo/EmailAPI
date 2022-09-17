package com.producteca.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Inventario {
    @Id
    private String id;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    private Deposito deposito;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    private Producto producto;

    private BigDecimal stock = BigDecimal.ZERO;

}