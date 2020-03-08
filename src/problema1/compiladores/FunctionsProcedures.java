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
public class FunctionsProcedures {
    private String nome;
    private String retorno;
    private ArrayList<Variaveis> parametro = new ArrayList<Variaveis>();
    private ArrayList<String> localvar = new ArrayList<String>();

    public FunctionsProcedures(String nome, String retorno) {
        this.nome = nome;
        this.retorno = retorno;
    }
    public FunctionsProcedures(String nome) {
        this.nome = nome;
    }
    
    public void addParametro(Variaveis var){
        parametro.add(var);
    }
    public void addVar(String var){
        localvar.add(var);
    }
    
    public boolean compareTo(FunctionsProcedures obj){
        if(this.nome.equals(obj.getNome())){
            if(this.parametro == obj.getParametro())
                return false;
        }
        return true;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<Variaveis> getParametro() {
        return parametro;
    }

    public void setParametro(ArrayList<Variaveis> parametro) {
        this.parametro = parametro;
    }

    public ArrayList<String> getLocalvar() {
        return localvar;
    }

    public void setLocalvar(ArrayList<String> localvar) {
        this.localvar = localvar;
    }
    
}
