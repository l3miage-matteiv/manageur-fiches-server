package com.example.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class FicheRenseignement implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    Long id;
    Long idEtudiant;
    Long idServiceRH;
    Long idTuteur;
    Long idEnseignent;
    String mailServiceRH;
    String mailTuteur;
    String mailEnseignent;
    Long idFicheAccueilStagiaire;
    Long idFicheTuteur;
    String raisonSociale;
    String representantLegal;
    String progres;

    public FicheRenseignement() { }

    public FicheRenseignement(Long id, Long idEtudiant, Long idServiceRH, Long idTuteur, Long idEnseignent, String mailServiceRH, String mailTuteur, String mailEnseignent, Long idFicheAccueilStagiaire, Long idFicheTuteur, String raisonSociale, String representantLegal, String progres) {
        this.id = id;
        this.idEtudiant = idEtudiant;
        this.idServiceRH = idServiceRH;
        this.idTuteur = idTuteur;
        this.idEnseignent = idEnseignent;
        this.mailServiceRH = mailServiceRH;
        this.mailTuteur = mailTuteur;
        this.mailEnseignent = mailEnseignent;
        this.idFicheAccueilStagiaire = idFicheAccueilStagiaire;
        this.idFicheTuteur = idFicheTuteur;
        this.raisonSociale = raisonSociale;
        this.representantLegal = representantLegal;
        this.progres = progres;
    }

    public void setID(Long id) {
        this.id = id;
    }

    public Long getID() {
        return this.id;
    }

    public void setIDEtudiant(Long idEtudiant) {
        this.idEtudiant = idEtudiant;
    }

    public Long getIDEtudiant() {
        return this.idEtudiant;
    }

    public void setIDServiceRH(Long idServiceRH) {
        this.idServiceRH = idServiceRH;
    }

    public Long getIDServiceRH() {
        return this.idServiceRH;
    }

    public void setIDTuteur(Long idTuteur) {
        this.idTuteur = idTuteur;
    }

    public Long getIDTuteur() {
        return this.idTuteur;
    }

    public void setIDEnseignent(Long idEnseignent) {
        this.idEnseignent = idEnseignent;
    }

    public Long getIDEnseignent(){
        return this.idEnseignent;
    }

    public void setMailServiceRH(String mailServiceRH) {
        this.mailServiceRH = mailServiceRH;
    }

    public String getMailServiceRH() {
        return this.mailServiceRH;
    }

    public void setMailTuteur(String mailTuteur) {
        this.mailTuteur = mailTuteur;
    }

    public String getMailTuteur() {
        return this.mailTuteur;
    }

    public void setMailEnseignent(String mailEnseignent) {
        this.mailEnseignent = mailEnseignent;
    }

    public String getMailEnseignent() {
        return this.mailEnseignent;
    }

    public void setIDFicheAccueilStagiaire(Long id) {
        this.idFicheAccueilStagiaire = id;
    }

    public Long getIDFicheAccueilStagiaire() {
        return this.idFicheAccueilStagiaire;
    }

    public void setIDFicheTuteur(Long idFicheTuteur) {
        this.idFicheTuteur = idFicheTuteur;
    }

    public Long getIDFicheTuteur() {
        return this.idFicheTuteur;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public String getRaisonSociale() {
        return this.raisonSociale;
    }

    public void setRepresentantLegal(String representantLegal) {
        this.representantLegal = representantLegal;
    }

    public String getRepresentantLegal() {
        return this.representantLegal;
    }

    public void setProgres(String progres) {
        this.progres = progres;
    }

    public String getProgres() {
        return this.progres;
    }
}
