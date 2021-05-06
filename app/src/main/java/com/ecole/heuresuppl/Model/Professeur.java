package com.ecole.heuresuppl.Model;

import android.text.Editable;

import java.io.Serializable;

public class Professeur implements Serializable {
    private String matricule;
    private String nom;

    public Professeur(){}

    public Professeur(String matricule, String nom){
        this.matricule = matricule;
        this.nom = nom;
    }

    public String getMatricule() {
        return matricule;
    }

    public String getNom() {
        return nom;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString(){
        return "{"+
                "\"matricule\" : \"" + this.matricule +
                "\",\"nom\" : \"" + this.nom +
                "\"}";
    }

}
