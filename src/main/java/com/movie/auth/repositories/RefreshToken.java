package com.movie.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshToken extends JpaRepository<com.movie.auth.entities.RefreshToken, Integer> {
}
