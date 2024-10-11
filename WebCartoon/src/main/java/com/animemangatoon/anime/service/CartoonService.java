package com.animemangatoon.anime.service;

import com.animemangatoon.anime.entity.Cartoon;
import com.animemangatoon.anime.repository.CartoonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class CartoonService {

    private final CartoonRepository cartoonRepository;

    public CartoonService(CartoonRepository cartoonRepository) {
        this.cartoonRepository = cartoonRepository;
    }

    public Cartoon addCartoon(Cartoon cartoon) {
        log.info("saving a new cartoon");
        Cartoon savedCartoon = this.cartoonRepository.save(cartoon);
        log.info("saved a new cartoon with id: {}", savedCartoon.getId());
        return savedCartoon;
    }

    public List<Cartoon> getAllCartoons() {
        return this.cartoonRepository.findAll();
    }

    public Cartoon getCartoonsById(UUID CartoonId) {
        Optional<Cartoon> cartoonOptional = this.cartoonRepository.findById(CartoonId);
        return cartoonOptional.orElse(null);
    }

    public void deleteCartoon(UUID id) {
        Optional<Cartoon> cartoon = cartoonRepository.findById(id);
        if (cartoon.isPresent()) {
            log.info("Deleting cartoon with id: {}", id);
            cartoonRepository.deleteById(id);
        }
    }
}
