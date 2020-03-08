/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problema1.compiladores;

import java.util.ArrayList;

/**
 *
 * @author Mille
 */
public class Structs {
    private String nome;
    private ArrayList<Variaveis> localvar = new ArrayList<Variaveis>();
    
    public Structs(String nome){
        this.nome = nome;
    }
    
    public void addVar(Variaveis var){
        localvar.add(var);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<Variaveis> getLocalvar() {
        return localvar;
    }

    public void setLocalvar(ArrayList<Variaveis> localvar) {
        this.localvar = localvar;
    }
    
    
}
