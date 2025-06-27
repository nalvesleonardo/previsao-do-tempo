package model;

import java.sql.Date;

public class DadosHorarios {
    private int id;
    private Date hora;
    private Double temperatura;
    private Double sensacaoTermica;
    private Double precipitacao;
    private Double umidade;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public Double getSensacaoTermica() {
        return sensacaoTermica;
    }

    public void setSensacaoTermica(Double sensacaoTermica) {
        this.sensacaoTermica = sensacaoTermica;
    }

    public Double getPrecipitacao() {
        return precipitacao;
    }

    public void setPrecipitacao(Double precipitacao) {
        this.precipitacao = precipitacao;
    }

    public Double getUmidade() {
        return umidade;
    }

    public void setUmidade(Double umidade) {
        this.umidade = umidade;
    }

    public DadosHorarios(int id, Date hora, Double temperatura, Double sensacaoTermica, Double precipitacao, Double umidade) {
        this.id = id;
        this.hora = hora;
        this.temperatura = temperatura;
        this.sensacaoTermica = sensacaoTermica;
        this.precipitacao = precipitacao;
        this.umidade = umidade;
    }

    @Override
    public String toString() {
        return "DadosHorarios{" +
                "id=" + id +
                ", hora=" + hora +
                ", temperatura=" + temperatura +
                ", sensacaoTermica=" + sensacaoTermica +
                ", precipitacao=" + precipitacao +
                ", umidade=" + umidade +
                '}';
    }
}
