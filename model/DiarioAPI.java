// Salve como model/Daily.java
package model;


public class DiarioAPI {

    private String[] tempo;
    private double[] temperaturaMax;
    private double[] temperaturaMin;
    private int[] precipitacaoMax;

    // Getters e Setters
    public String[] getTempo() {
        return tempo;
    }

    public void setTempo(String[] tempo) {
        this.tempo = tempo;
    }

    public double[] getTemperaturaMax() {
        return temperaturaMax;
    }

    public void setTemperaturaMax(double[] temperaturaMax) {
        this.temperaturaMax = temperaturaMax;
    }

    public double[] getTemperaturaMin() {
        return temperaturaMin;
    }

    public void setTemperaturaMin(double[] temperaturaMin) {
        this.temperaturaMin = temperaturaMin;
    }

    public int[] getPrecipitacaoMax() {
        return precipitacaoMax;
    }

    public void setPrecipitacaoMax(int[] precicipitacaoMax) {
        this.precipitacaoMax = precicipitacaoMax;
    }
}