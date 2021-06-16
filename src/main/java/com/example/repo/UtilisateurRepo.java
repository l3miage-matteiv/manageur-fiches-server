package com.example.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.model.Utilisateur;

import java.util.Optional;

public interface UtilisateurRepo extends JpaRepository<Utilisateur, Long> {
    void deleteUtilisateurById(Long id);

    Optional<Utilisateur> findUtilisateurById(Long id);
}
