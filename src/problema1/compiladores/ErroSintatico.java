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
public class ErroSintatico {
    private final String erro;
    private final int linha;
    
    
    public ErroSintatico(String erro, int linha){
        this.erro = erro;
        this.linha = linha+1;
    }
    public String toStringLista(){
        return "linha " + linha + " - Erro: " + erro;
    }
    
    public String getErro(){
        return erro;
    }
    
}
