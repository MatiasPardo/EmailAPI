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
public class Deposito {
    @Id
    private String id;
    @OneToMany(mappedBy="deposito", cascade=CascadeType.REMOVE, fetch=FetchType.LAZY)
    private Collection<Inventario> items;
}