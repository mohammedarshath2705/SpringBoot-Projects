package com.animemangatoon.anime.repository;


import com.animemangatoon.anime.entity.Cartoon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CartoonRepository extends JpaRepository<Cartoon, UUID> {
}
