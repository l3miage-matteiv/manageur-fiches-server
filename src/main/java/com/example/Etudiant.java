package com.example;

public class Etudiant extends Utilisateur {
    private String numeroEtudiant;
    private String typeAffiliation;
    private String caisseAssuranceMaladie;
    private String inscription;
    private String enseignantReferent;

    public String getNumeroEtudiant() {
        return numeroEtudiant;
    }

    public void setNumeroEtudiant(String numeroEtudiant) {
        this.numeroEtudiant = numeroEtudiant;
    }

    public String getTypeAffiliation() {
        return typeAffiliation;
    }

    public void setTypeAffiliation(String typeAffiliation) {
        this.typeAffiliation = typeAffiliation;
    }

    public String getCaisseAssuranceMaladie() {
        return caisseAssuranceMaladie;
    }

    public void setCaisseAssuranceMaladie(String caisseAssuranceMaladie) {
        this.caisseAssuranceMaladie = caisseAssuranceMaladie;
    }

    public String getInscription() {
        return inscription;
    }

    public void setInscription(String inscription) {
        this.inscription = inscription;
    }

    public String getEnseignantReferent() {
        return enseignantReferent;
    }

    public void setEnseignantReferent(String enseignantReferent) {
        this.enseignantReferent = enseignantReferent;
    }
}
