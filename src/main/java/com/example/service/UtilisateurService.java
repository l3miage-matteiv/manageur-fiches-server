package com.example.service;

import java.util.List;

import com.example.exception.UserNotFoundException;
import com.example.model.Utilisateur;
import com.example.repo.UtilisateurRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurService {
    private final UtilisateurRepo utilisateurRepo;

    @Autowired
    public UtilisateurService(UtilisateurRepo utilisateurRepo) {
        this.utilisateurRepo = utilisateurRepo;
    }

    public Utilisateur addUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepo.save(utilisateur);
    }

    public List<Utilisateur> findAllUtilisateurs() {
        return utilisateurRepo.findAll();
    }

    public Utilisateur updateUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepo.save(utilisateur);
    }

    public Utilisateur findUtilisateurById(Long id) {
        return utilisateurRepo.findUtilisateurById(id).orElseThrow(() -> new UserNotFoundException("User by id " + id + " was not found"));
    }

    public void deleteUtilisateur(Long id) {
        utilisateurRepo.deleteUtilisateurById(id);
    }
}
