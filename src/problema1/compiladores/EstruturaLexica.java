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
public class EstruturaLexica {
    
    private final ArrayList<String> palavrasReservadas = new ArrayList<>();
    private final ArrayList<String> identificadores = new ArrayList<>();
    private final ArrayList<String> numeros = new ArrayList<>();
    private final ArrayList<String> digitos = new ArrayList<>();
    private final ArrayList<String> letras = new ArrayList<>();
    private final ArrayList<Character> operadoresAritmeticos = new ArrayList<>();
    private final ArrayList<Character> operadoresRelacionais = new ArrayList<>();
    private final ArrayList<Character> operadoresLogicos = new ArrayList<>();
    private final ArrayList<String> comentarios = new ArrayList<>();
    private final ArrayList<Character> delimitadores = new ArrayList<>();
    private final ArrayList<String> cadeiaDeCaracteres = new ArrayList<>();
    private final ArrayList<String> simbolos = new ArrayList<>();
    private final ArrayList<String> simboloInvalido = new ArrayList<>();
    private final ArrayList<String> espaco = new ArrayList<>();
    
    public EstruturaLexica(){
        palavrasReservadas.add("var");
        palavrasReservadas.add("const");
        palavrasReservadas.add("typedef");
        palavrasReservadas.add("struct");
        palavrasReservadas.add("extends");
        palavrasReservadas.add("procedure");
        palavrasReservadas.add("function");
        palavrasReservadas.add("start");
        palavrasReservadas.add("return");
        palavrasReservadas.add("if");
        palavrasReservadas.add("else");
        palavrasReservadas.add("then");
        palavrasReservadas.add("while");
        palavrasReservadas.add("read");
        palavrasReservadas.add("print");
        palavrasReservadas.add("int");
        palavrasReservadas.add("real");
        palavrasReservadas.add("boolean");
        palavrasReservadas.add("string");
        palavrasReservadas.add("true");
        palavrasReservadas.add("false");
        palavrasReservadas.add("global");
        palavrasReservadas.add("local");
        
        for(int i=1; i<10; i++)
            digitos.add(String.valueOf(i));
       
        for(char j= 'a'; j<='z'; j++)
            letras.add(String.valueOf(j));
       
        for(char k= 'A'; k<='Z'; k++)
            letras.add(String.valueOf(k));
        
        operadoresAritmeticos.add('+');
        operadoresAritmeticos.add('-');
        operadoresAritmeticos.add('*');
        operadoresAritmeticos.add('/');
  /*      operadoresAritmeticos.add('++');
        operadoresAritmeticos.add('--');*/
        
        operadoresRelacionais.add('!');
        operadoresRelacionais.add('=');
        operadoresRelacionais.add('<');
        operadoresRelacionais.add('>');
        
        operadoresLogicos.add('!');
        operadoresLogicos.add('&');
        operadoresLogicos.add('|');
        
        delimitadores.add(';');
        delimitadores.add(',');
        delimitadores.add('(');
        delimitadores.add(')');
        delimitadores.add('[');
        delimitadores.add(']');
        delimitadores.add('{');
        delimitadores.add('}');
        delimitadores.add('.');
    }
    
    public boolean verificaPalavraReservada(String string){
        return this.palavrasReservadas.contains(string);
    }
    
    public boolean verificaOperadorAritmetico(char caractere){
        return this.operadoresAritmeticos.contains(caractere);
    }
    
    public boolean verificaOperadorRelacional(char caractere){
        return this.operadoresRelacionais.contains(caractere);
    }
    
    public boolean verificaOperadorLogico(char caractere){
        return this.operadoresLogicos.contains(caractere);
    }
    
      public boolean verificaDelimitador(char caractere){
        return this.delimitadores.contains(caractere);
    }

    public boolean verificaLetra(char caractere){
        return this.letras.contains(caractere);
    }

    public boolean verificaSimbolo(char caractere){
        return this.simbolos.contains(caractere);
    }    

    public boolean verificaSimboloInvalido(char caractere){
        return this.simboloInvalido.contains(caractere);
    }

    public boolean verificaEspaco(char caractere){
        return (Character.isSpaceChar(caractere) || caractere == 9);
    }    

    

    boolean verificaNumero(char[] linha) {
        for(int i=0;i< linha.length; i++){
            if(Character.isDigit( linha[i]))
                return true;
        }
        return false;
    }
}
