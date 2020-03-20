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
    private ArrayList<Variaveis> localvar = new ArrayList<Variaveis>();

    public FunctionsProcedures(String nome, String retorno) {
        this.nome = nome;
        this.retorno = retorno;
    }
    public FunctionsProcedures(String nome) {
        this.nome = nome;
    }

    public String getRetorno() {
        return retorno;
    }

    public void setRetorno(String retorno) {
        this.retorno = retorno;
    }
    
    
    public void addParametro(Variaveis var){
        parametro.add(var);
    }
    public void addVar(Variaveis var){
        localvar.add(var);
    }
    
    public boolean compareTo(FunctionsProcedures obj){
        if(this.nome.equals(obj.getNome())){
            if(this.parametro.equals(obj.getParametro()))
                return true;
        }
        return false;
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

    public ArrayList<Variaveis> getLocalvar() {
        return localvar;
    }

    public void setLocalvar(ArrayList<Variaveis> localvar) {
        this.localvar = localvar;
    }

    @Override
    public String toString() {
        return "FunctionsProcedures{" + "nome=" + nome + ", retorno=" + retorno + ", parametro=" + parametro + ", localvar=" + localvar + '}';
    }
    
    
}
