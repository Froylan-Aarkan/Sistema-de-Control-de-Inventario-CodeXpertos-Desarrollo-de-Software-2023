/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.POJO;

import java.util.Objects;

/**
 *
 * @author froyl
 */
public class Periferico {
    private int idPeriferico;
    private String tipo;
    private String marca;
    private String modelo;
    private String estado;
    private String numeroSerie;
    private boolean inalambrico;
    private int idCentroComputo;

    public Periferico() {
    }

    public int getIdPeriferico() {
        return idPeriferico;
    }

    public String getTipo() {
        return tipo;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public String getEstado() {
        return estado;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public boolean isInalambrico() {
        return inalambrico;
    }

    public int getIdCentroComputo() {
        return idCentroComputo;
    }

    public void setIdPeriferico(int idPeriferico) {
        this.idPeriferico = idPeriferico;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public void setInalambrico(boolean inalambrico) {
        this.inalambrico = inalambrico;
    }

    public void setIdCentroComputo(int idCentroComputo) {
        this.idCentroComputo = idCentroComputo;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + this.idPeriferico;
        hash = 53 * hash + Objects.hashCode(this.tipo);
        hash = 53 * hash + Objects.hashCode(this.marca);
        hash = 53 * hash + Objects.hashCode(this.modelo);
        hash = 53 * hash + Objects.hashCode(this.estado);
        hash = 53 * hash + Objects.hashCode(this.numeroSerie);
        hash = 53 * hash + (this.inalambrico ? 1 : 0);
        hash = 53 * hash + this.idCentroComputo;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Periferico other = (Periferico) obj;
        if (this.idPeriferico != other.idPeriferico) {
            return false;
        }
        if (this.inalambrico != other.inalambrico) {
            return false;
        }
        if (this.idCentroComputo != other.idCentroComputo) {
            return false;
        }
        if (!Objects.equals(this.tipo, other.tipo)) {
            return false;
        }
        if (!Objects.equals(this.marca, other.marca)) {
            return false;
        }
        if (!Objects.equals(this.modelo, other.modelo)) {
            return false;
        }
        if (!Objects.equals(this.estado, other.estado)) {
            return false;
        }
        return Objects.equals(this.numeroSerie, other.numeroSerie);
    }
    
}
