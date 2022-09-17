package com.producteca.model;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ListaPrecio {
    
    @Id
    private String id;
    @OneToMany(mappedBy="listaprecio", cascade=CascadeType.REMOVE, fetch=FetchType.LAZY)
    private Collection<Precio> precios;
}
