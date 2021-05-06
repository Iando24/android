package com.ecole.heuresuppl.Model;

import java.io.Serializable;

public class Volumehoraire implements Serializable {
    private int id;
    private String matricule;
    private String nummat;
    private int tauxhoraire;

    public Volumehoraire(){}

    public Volumehoraire(String matricule, String nummat, int tauxhoraire){
        this.matricule = matricule;
        this.nummat = nummat;
        this.tauxhoraire = tauxhoraire;
    }

    public int getId() { return id;}

    public String getMatricule() {
        return matricule;
    }

    public String getNummat() {
        return nummat;
    }

    public int getVolumeHoraire() {
        return tauxhoraire;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public void setNummat(String nummat) {
        this.nummat = nummat;
    }

    public void setVolumeHoraire(int tauxhoraire) {
        this.tauxhoraire = tauxhoraire;
    }

    @Override
    public String toString(){
        return "{"+
                "\"id\" : \"" + this.id +
                "\"matricule\" : \"" + this.matricule +
                "\"nummat\" : \"" + this.nummat +
                "\"tauxhoraire\" : " + this.tauxhoraire +
                "}";
    }
}
