/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problema1.compiladores;

/**
 *
 * @author Mille
 */
public class Variaveis {
    public String nome;
    public String tipo;
    public String vetMat;

    public Variaveis(String nome, String tipo) {
        this.nome = nome;
        this.tipo = tipo;
        this.vetMat = null;
    }

    public String getVetMat() {
        return vetMat;
    }

    public void setVetMat(String vetMat) {
        this.vetMat = vetMat;
    }
    

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Variaveis{" + "nome=" + nome + ", tipo=" + tipo + ", vetMat=" + vetMat + '}';
    }
     
}
