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
    private ArrayList<Tokens> tokens;
    private EstruturaLexica estruturaLexica;
    
    public Automatos(ArrayList<String> codigo){
        this.codigo = codigo;
        this.auxPosicao = 0;
        estruturaLexica = new EstruturaLexica();
    }
    public void analisadorLexico(){
        
    }
    
    /*Método para pegar o próximo caractere do código
    
    Ainda está em construção, pois provavelmente eu mudarei a lógica para acessar
    isso nos automatos.
    
    */
    public boolean proximoCaractere(){
        if(!codigo.isEmpty()){
            while(auxPosicao < codigo.size()){
                linha = codigo.get(auxPosicao).toCharArray();
                posicao = auxPosicao;
                auxPosicao++;
                return true;
            }
        }
        return false;
    }
    public void controle(){
        int i = 0;
        while(i < linha.length){
            if(linha[i] == '/'){
                comentarios();
            } else if(estruturaLexica.verificaOperadorAritmetico(linha[i])){
                operadorAritmetico();
            }
        }
    }
    
    public void operadorAritmetico(){
        int i = 0;
        while(i < linha.length){
            if(linha[i] == '-'){
                i++;
                if(linha[i] == '-'){
                    Tokens token = new Tokens(posicao, "OPERADOR ARITMETICO '--' ", linha);
                    tokens.add(token);
                    break;
                }else{
                    Tokens token = new Tokens(posicao, "OPERADOR ARITMETICO '-' ", linha);
                    tokens.add(token);
                    break;
                }
            } else if(linha[i] == '+'){
                i++;
                if(linha[i] == '+'){
                    Tokens token = new Tokens(posicao, "OPERADOR ARITMETICO '++' ", linha);
                    tokens.add(token);
                    break;
                }else{
                    Tokens token = new Tokens(posicao, "OPERADOR ARITMETICO '+' ", linha);
                    tokens.add(token);
                    break;
                }
            } else if(linha[i] == '*' || linha[i] == '/'){
                Tokens token = new Tokens(posicao, "OPERADOR ARITMETICO", linha);
                tokens.add(token);
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
        int i = 0;
        while(i < linha.length){
            if(linha[i]== '!' || linha[i] == '=' || linha[i] == '>' || linha[i] == '>'){
                i++;
                if(linha[i] == '='){
                    Tokens token = new Tokens(posicao, "OPERADOR RELACIONAL", linha);
                    tokens.add(token);
                    break;
                }else if(linha[i--] != '!'){
                    Tokens token = new Tokens(posicao, "OPERADOR RELACIONAL", linha);
                    tokens.add(token);
                    break;
                } else{
                    Tokens token = new Tokens(posicao, "ERRO OPERADOR RELACIONAL", linha);
                    tokens.add(token);
                }
                
            }
        }
    }
    
    public void operadorLogico(){
        int i = 0;
        boolean erro = false;
        while(i < linha.length){
            if(linha[i] == '!'){
                Tokens token = new Tokens(posicao, "OPERADOR ARITMETICO", linha);
                tokens.add(token);
                break;
            }else if(linha[i] == '&'){
                i++;
                if(linha[i] == '&'){
                    Tokens token = new Tokens(posicao, "OPERADOR ARITMETICO", linha);
                    tokens.add(token);
                    break;
                }
                else{
                    erro = true;
                }
            }else if(linha[i] == '|'){
                i++;
                if(linha[i] == '|'){
                    Tokens token = new Tokens(posicao, "OPERADOR ARITMETICO", linha);
                    tokens.add(token);
                    break;
                }else{
                    erro = true;
                }
            }
            if(erro == true){
                Tokens token = new Tokens(posicao, "ERRO OPERADOR LOGICO", linha);
                tokens.add(token);
            }
        }
    }
    
    
    
    public void comentarios(){
        int i = 0;
        while(i < linha.length){
            if(linha[i]== '/'){
                i++;
                if(linha[i] == '/'){
                    Tokens token = new Tokens(posicao, "COMENTARIO", linha);
                    tokens.add(token);
                    break;
                }
                else if(linha[i] == '*'){
                    i++;
                    for(int b = posicao; b< codigo.size(); b++){ 
                        for(int a = i; a<linha.length; a++){
                            if(linha[a] == '*' && linha[a+1] == '/'){
                                Tokens token = new Tokens(posicao, "COMENTARIO", linha);
                                break;
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
    
    public void delimitadores(){
        int i = 0;
        while(i < linha.length){
            if(linha[i] == ';' || linha[i] == ',' || linha[i] == '(' || linha[i] == ')' || linha[i] == '[' || linha[i] == ']' || linha[i] == '{' || linha[i] == '}' || linha[i] == '.'){
                Tokens token = new Tokens(posicao, "DELIMITADOR", linha);
                break;
            }
            
        }
    }
}
