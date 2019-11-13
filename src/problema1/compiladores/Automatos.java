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
public class Automatos {
    private static final char FIM = '\0';
    private ArrayList<String> codigo;
    private char linha[];
    private int posicao;
    private int auxPosicao;
    private int numLinha;
    private ArrayList<Tokens> tokens;
    private EstruturaLexica estruturaLexica;
    
    public Automatos(ArrayList<String> codigo){
        this.codigo = codigo;
        this.posicao = 0;
        this.auxPosicao = 0;
        this.numLinha = 0;
        estruturaLexica = new EstruturaLexica();
    }
    public void analisadorLexico(){
        
    }
    
    /*Método para pegar o próximo caractere do código
    
    Ainda está em construção, pois provavelmente eu mudarei a lógica para acessar
    isso nos automatos.
    
    */

    public boolean proximaLinha(){
        if(codigo.size() > numLinha){
            numLinha++;
            return true;
        }
        return false;
    }
    
    public boolean ignorarEspaco(){
        if(estruturaLexica.verificaEspaco(linha[posicao])){
            posicao++;
            return true;
        }
        return false;
    }
    
    public String controle(){
        if(posicao > linha.length){
            if(linha[posicao] == '/'){
                    comentarios();
                } else if(estruturaLexica.verificaOperadorAritmetico(linha[posicao])){
                    operadorAritmetico();
                } else if(estruturaLexica.verificaOperadorLogico(linha[posicao])){
                    operadorLogico();
                } else if(estruturaLexica.verificaOperadorRelacional(linha[posicao])){
                    operadorRelacional();
                } else if(estruturaLexica.verificaDelimitador(linha[posicao])){
                    delimitador();
                }
        } else if(posicao-1 == linha.length){
            if(proximaLinha()){
                controle();
            }
        } else if(numLinha-1 == codigo.size()){
            return "FIM";
        }
        return null;
    }

    
    public void operadorAritmetico(){
        auxPosicao = posicao;
        while(posicao < linha.length){
            if(linha[posicao] == '-'){
                auxPosicao++;
                if(linha[auxPosicao] == '-'){
                    Tokens token = new Tokens(posicao, "OPERADOR ARITMETICO '--' ", linha);
                    tokens.add(token);
                    posicao = auxPosicao+1;
                    break;
                }else{
                    Tokens token = new Tokens(posicao, "OPERADOR ARITMETICO '-' ", linha);
                    tokens.add(token);
                    posicao++;
                    break;
                }
            } else if(linha[posicao] == '+'){
                auxPosicao++;
                if(linha[auxPosicao] == '+'){
                    Tokens token = new Tokens(posicao, "OPERADOR ARITMETICO '++' ", linha);
                    tokens.add(token);
                    posicao = auxPosicao+1;
                    break;
                }else{
                    Tokens token = new Tokens(posicao, "OPERADOR ARITMETICO '+' ", linha);
                    tokens.add(token);
                    posicao++;
                    break;
                }
            } else if(linha[posicao] == '*' || linha[posicao] == '/'){
                Tokens token = new Tokens(posicao, "OPERADOR ARITMETICO", linha);
                tokens.add(token);
                posicao++;
                break;
            }
            else{
                Tokens token = new Tokens(posicao, "ERRO OPERADOR ARITMETICO", linha);
                tokens.add(token);
                break;
            }
        }
    }
    
    public void operadorRelacional(){
        while(posicao < linha.length){
            if(linha[posicao]== '!' || linha[posicao] == '=' || linha[posicao] == '>' || linha[posicao] == '>'){
                auxPosicao = posicao+1;
                if(linha[auxPosicao] == '='){
                    Tokens token = new Tokens(posicao, "OPERADOR RELACIONAL", linha);
                    tokens.add(token);
                    posicao = auxPosicao+1;
                    break;
                }else if(linha[posicao] != '!'){
                    Tokens token = new Tokens(posicao, "OPERADOR RELACIONAL", linha);
                    tokens.add(token);
                    posicao++;
                    break;
                } else{
                    Tokens token = new Tokens(posicao, "ERRO OPERADOR RELACIONAL", linha);
                    tokens.add(token);
                }
                
            }
        }
    }
    
    public void operadorLogico(){
        boolean erro = false;
        while(posicao < linha.length){
            if(linha[posicao] == '!'){
                Tokens token = new Tokens(posicao, "OPERADOR ARITMETICO", linha);
                tokens.add(token);
                posicao++;
                break;
            }else if(linha[posicao] == '&'){
                auxPosicao = posicao+1;
                if(linha[auxPosicao] == '&'){
                    Tokens token = new Tokens(posicao, "OPERADOR ARITMETICO", linha);
                    tokens.add(token);
                    posicao = auxPosicao+1;
                    break;
                }
                else{
                    posicao++;
                    erro = true;
                }
            }else if(linha[posicao] == '|'){
                auxPosicao = posicao+1;
                if(linha[auxPosicao] == '|'){
                    Tokens token = new Tokens(posicao, "OPERADOR ARITMETICO", linha);
                    tokens.add(token);
                    posicao = auxPosicao+1;
                    break;
                }else{
                    posicao++;
                    erro = true;
                }
            }
            if(erro == true){
                Tokens token = new Tokens(posicao, "ERRO OPERADOR LOGICO", linha);
                tokens.add(token);
                posicao++;
            }
        }
    }
    
    
    
    public void comentarios(){ // não salvar no array de tokens, apenas no arquivo
        while(posicao < linha.length){
            if(linha[posicao]== '/'){
                auxPosicao = posicao+1;
                if(linha[auxPosicao] == '/'){
                    //colocar pra printar apenas no arquivo
                    Tokens token = new Tokens(posicao, "COMENTARIO", linha);
                    tokens.add(token);
                    posicao = auxPosicao+1;
                    break;
                }
                else if(linha[auxPosicao] == '*'){
                    auxPosicao++;
                    for(int b = numLinha; b <codigo.size(); b++){
                        for(int a = auxPosicao; a<linha.length; a++){
                            if(linha[a] == '*' && linha[a+1] == '/'){
                                //colocar pra printar no arquivo
                            }
                        }
                    }
                    
                }
                else{
                    Tokens token = new Tokens(posicao, "ERRO COMENTARIO", linha);
                }
            }

        }
    }
    
    public void delimitador(){
        int i = 0;
        while(i < linha.length){
            if(linha[i] == ';' || linha[i] == ',' || linha[i] == '(' || linha[i] == ')' || linha[i] == '[' || linha[i] == ']' || linha[i] == '{' || linha[i] == '}' || linha[i] == '.'){
                Tokens token = new Tokens(posicao, "DELIMITADOR", linha);
                break;
            }
            
        }
    }
}
