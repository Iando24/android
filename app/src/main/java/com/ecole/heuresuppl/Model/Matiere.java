package com.ecole.heuresuppl.Model;

import java.io.Serializable;

public class Matiere implements Serializable {
    private String nummat;
    private String designation;
    private int nbheure;

    public Matiere(){}

    public Matiere(String nummat, String designation, int nbheure){
        this.nummat = nummat;
        this.designation = designation;
        this.nbheure = nbheure;
    }

    public String getNummat() {
        return nummat;
    }

    public String getDesignation() {
        return designation;
    }

    public int getNbheure() {
        return nbheure;
    }

    public void setNummat(String nummat) {
        this.nummat = nummat;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setNbheure(int nbheure) {
        this.nbheure = nbheure;
    }

    @Override
    public String toString(){
        return "{"+
                "\"nummat\" : \"" + this.nummat +
                "\",\"designation\" : \"" + this.designation +
                "\",\"nbheure\" : " + this.nbheure +
                "}";
    }
}
