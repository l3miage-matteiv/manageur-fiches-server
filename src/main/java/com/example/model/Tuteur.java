package com.example.model;

public class Tuteur extends Utilisateur {
    private String fonction;
    private String service;

    public String getFonction() {
        return this.fonction;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    public String getService() {
        return this.service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
