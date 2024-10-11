package com.animemangatoon.anime.controller;


import com.animemangatoon.anime.entity.Cartoon;
import com.animemangatoon.anime.service.CartoonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/webcartoon")
public class CartoonController {

    private final CartoonService cartoonService;

    @Autowired
    public CartoonController (CartoonService cartoonService){
        this.cartoonService= cartoonService;
    }

    @PostMapping("/add")
    public ResponseEntity<Cartoon> addCartoon(@Valid @RequestBody Cartoon cartoon){
        Cartoon savedCartoon = this.cartoonService.addCartoon(cartoon);
        return new ResponseEntity<>(savedCartoon,HttpStatus.CREATED);
    }

    @GetMapping("/List")
    public ResponseEntity<List<Cartoon>> getAllCartoons(){
        List<Cartoon> CartoonList = this.cartoonService.getAllCartoons();
        return new ResponseEntity<>(CartoonList,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cartoon> getCartoonById(@PathVariable UUID id) {
        Cartoon cartoon = this.cartoonService.getCartoonsById(id);
        return new ResponseEntity<>(cartoon, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCartoonById(@PathVariable UUID id) {
        try {
            this.cartoonService.deleteCartoon(id);
            return new ResponseEntity<>("Cartoon deleted successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
