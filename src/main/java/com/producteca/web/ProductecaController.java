package com.producteca.web;

import javax.validation.Valid;

import java.util.logging.Logger;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.connection.DataProducteca;
import com.producteca.service.ProductsService;


@RestController
@RequestMapping(path = "api/producteca")
public class ProductecaController {
    
    @Autowired
    private ProductsService productsService;

    @PostMapping(path="/update")
    public ResponseEntity<DataProducteca> update(@RequestBody @Valid DataProducteca producteca) throws Exception{
        Logger logger = Logger.getLogger("================== UPDATING ==================");
        logger.setLevel(Level.INFO);
		logger.info("INITIALIZE");
        productsService.update(producteca);
        return new ResponseEntity<DataProducteca>(producteca,HttpStatus.ACCEPTED);
    }

}
