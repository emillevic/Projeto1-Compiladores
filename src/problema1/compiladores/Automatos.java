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
    private ArrayList<String> codigoSemTratamento;
    private ArrayList<char[]> codigo;
    private char linha[];
    private int posicao;
    private int auxPosicao;
    private int numLinha;
    private ArrayList<Tokens> tokens;
    private ArrayList<String> tabela;
    private EstruturaLexica estruturaLexica;
    
    public Automatos(ArrayList<String> codigoSemTratamento){
        this.tokens = new ArrayList<Tokens>();
        this.codigoSemTratamento = codigoSemTratamento;
        this.codigo = new ArrayList<char[]>();
        this.tabela = new ArrayList<String>();
        this.posicao = 0;
        this.auxPosicao = 0;
        this.numLinha = 0;
        //this.lexema = new char[10];
        estruturaLexica = new EstruturaLexica();
    }
    public ArrayList<String> analisadorLexico(){
        for(int j = 0; j<codigoSemTratamento.size(); j++){
            codigo.add(codigoSemTratamento.get(j).toCharArray());
        }
        linha = codigo.get(0);
        controle();
        System.out.println(tokens);
        for(int i = 0; i<tokens.size(); i++){
            System.out.println("it" +tokens);
            tabela.add(tokens.get(i).toString());
        }
        return tabela;
    }
    
    /*Método para pegar o próximo caractere do código
    
    Ainda está em construção, pois provavelmente eu mudarei a lógica para acessar
    isso nos automatos.
    
    */

    public ArrayList<Tokens> getTokens(){
        return tokens;
    }
    
    /*public void zerarLexema(){
        for(int i = 0; i<lexema.length; i++){
            lexema[i] = '\0';
        }
    }*/
    
    public boolean proximaLinha(){
        if(posicao == linha.length){
            if(numLinha+1 < codigo.size()){
            System.out.println("aqui1" + codigo.size() + numLinha);
                numLinha++;
            System.out.println("aqui1" + codigo.get(numLinha) + numLinha);
                linha = codigo.get(numLinha);
                return true;
            }
        }
        return false;
    }
    
    public int ignorarEspaco(int pos){
        if(pos < linha.length){
            while(estruturaLexica.verificaEspaco(linha[pos]) && pos < linha.length){
                pos++;
            }
        }
        return pos;
    }
    
    public String controle(){
        while(true){
            //zerarLexema();
            if(posicao < linha.length){
                System.out.println(posicao);
                System.out.println(linha[posicao]);
                if(linha[posicao] == '/'){
                    comentarios();
                } else if(estruturaLexica.verificaOperadorAritmetico(linha[posicao])){
                    operadorAritmetico();
                } else if(estruturaLexica.verificaOperadorLogico(linha[posicao])){
                    System.out.println("euuuuuuuuuuuu");
                    operadorRelacional();
                } else if(estruturaLexica.verificaOperadorRelacional(linha[posicao])){
                    operadorLogico();
                } else if(estruturaLexica.verificaDelimitador(linha[posicao])){
                    delimitador();
                } else if(estruturaLexica.verificaNumero(linha)){
                    
                }
            } else if(proximaLinha()){
                posicao = 0;
            } else if(numLinha <= codigo.size()){
                System.out.println(codigo.size());
                System.out.println("final" + numLinha);
                return "FIM";
            }
        }
        
    }

    public boolean operadorAritmetico(){
        char[] lexema;
        while(true){
        auxPosicao = posicao+1;
            if(posicao < linha.length){
                switch(linha[posicao]){
                    case '-':
                        auxPosicao = ignorarEspaco(auxPosicao);
                        proximaLinha();
                        auxPosicao = ignorarEspaco(auxPosicao);
                        if(auxPosicao < linha.length && linha[auxPosicao] == '-'){
                            lexema = new char[2];
                            lexema[0] = linha[posicao];
                            lexema[1] = linha[auxPosicao];
                            Tokens token1 = new Tokens(numLinha, "OPERADOR ARITMETICO", lexema);
                            tokens.add(token1);
                            posicao = auxPosicao+1;
                            return true;
                        } else{
                            lexema = new char[1];
                            lexema[0] = linha[posicao];
                            Tokens token2 = new Tokens(numLinha, "OPERADOR ARITMETICO", lexema);
                            tokens.add(token2);
                            
                            posicao = auxPosicao;
                            return true;
                        }
                        
                    case '+':
                        auxPosicao = ignorarEspaco(auxPosicao);
                        proximaLinha();
                        auxPosicao = ignorarEspaco(auxPosicao);
                        if(auxPosicao < linha.length && linha[auxPosicao] == '+'){
                            lexema = new char[2];
                            lexema[0] = linha[posicao];
                            lexema[1] = linha[auxPosicao];
                            Tokens token3 = new Tokens(numLinha, "OPERADOR ARITMETICO", lexema);
                            tokens.add(token3);
                            posicao = auxPosicao+1;
                            return true;
                        } else{
                            lexema = new char[1];
                            lexema[0] = linha[posicao];
                            Tokens token4 = new Tokens(numLinha, "OPERADOR ARITMETICO", lexema);
                            tokens.add(token4);
                            posicao = auxPosicao;
                            return true;
                        }
                    case '*':
                        lexema = new char[1];
                        lexema[0] = linha[posicao];
                        Tokens token5 = new Tokens(numLinha, "OPERADOR ARITMETICO", lexema);
                        tokens.add(token5);
                        posicao = auxPosicao;
                        return true;
                        
                    case '/':
                        lexema = new char[1];
                        lexema[0] = linha[posicao];
                        Tokens token6 = new Tokens(numLinha, "OPERADOR ARITMETICO", lexema);
                        tokens.add(token6);
                        posicao = auxPosicao;
                        return true;
                    default:
                        lexema = new char[1];
                        Tokens token7 = new Tokens(numLinha, "ERRO OPERADOR ARITMETICO", lexema);
                        tokens.add(token7);
                        posicao = auxPosicao;
                        return true;
                }
            }

        }
    }
    
    public boolean operadorRelacional(){
        char[] lexema;
        while(true){
        auxPosicao = posicao+1;
            if(posicao < linha.length){
                if(linha[posicao]== '!' || linha[posicao] == '=' || linha[posicao] == '>' || linha[posicao] == '<'){
                    auxPosicao = ignorarEspaco(auxPosicao);
                    proximaLinha();
                    auxPosicao = ignorarEspaco(auxPosicao);
                    if(auxPosicao < linha.length && linha[auxPosicao] == '='){
                        lexema = new char[2];
                        lexema[0] = linha[posicao];
                        lexema[1] = linha[auxPosicao];
                        Tokens token = new Tokens(numLinha, "OPERADOR RELACIONAL", lexema);
                        tokens.add(token);
                        posicao = auxPosicao+1;
                        return true;
                    } else if(linha[posicao] != '!'){
                        lexema = new char[1];
                        lexema[0] = linha[posicao];
                        Tokens token = new Tokens(numLinha, "OPERADOR RELACIONAL", lexema);
                        tokens.add(token);
                        posicao = auxPosicao;
                        return true;
                    } else{
                        lexema = new char[1];
                        Tokens token = new Tokens(numLinha, "ERRO OPERARDOR RELACIONAL", lexema);
                        tokens.add(token);
                        posicao = auxPosicao;
                        return true;
                    }
                }
            }
        }
    }
    
    public boolean operadorLogico(){
        char[] lexema;
        while(true){
        auxPosicao = posicao+1;
            if(posicao <linha.length){
                switch(linha[posicao]){
                    case '!':
                                                    lexema = new char[1];

                        lexema[0] = linha[posicao];
                        Tokens token = new Tokens(numLinha, "OPERADOR LOGICO", lexema);
                        tokens.add(token);
                        posicao++;
                        return true;
                    case '&':
                        auxPosicao = ignorarEspaco(auxPosicao);
                        proximaLinha();
                        auxPosicao = ignorarEspaco(auxPosicao);
                        if(auxPosicao < linha.length && linha[auxPosicao] == '&'){
                                                        lexema = new char[2];

                            lexema[0] = linha[posicao];
                            lexema[0] = linha[auxPosicao];
                            Tokens token1 = new Tokens(numLinha, "OPERADOR LOGICO", lexema);
                            tokens.add(token1);
                            posicao = auxPosicao+1;
                            return true;
                        } else{
                                                        lexema = new char[1];

                            Tokens token2 = new Tokens(numLinha, "ERRO OPERADOR LOGICO", lexema);
                            tokens.add(token2);
                            posicao = auxPosicao;
                            return true;
                        }
                    case '|':
                        auxPosicao = ignorarEspaco(auxPosicao);
                        proximaLinha();
                        auxPosicao = ignorarEspaco(auxPosicao);
                        if(auxPosicao < linha.length && linha[auxPosicao] == '|'){
                                                        lexema = new char[2];

                            lexema[0] = linha[posicao];
                            lexema[0] = linha[auxPosicao];
                            Tokens token1 = new Tokens(numLinha, "OPERADOR LOGICO", lexema);
                            tokens.add(token1);
                            posicao = auxPosicao+1;
                            return true;
                        } else{
                                                        lexema = new char[2];

                            Tokens token2 = new Tokens(numLinha, "ERRO OPERADOR LOGICO", lexema);
                            tokens.add(token2);
                            posicao = auxPosicao;
                            return true;
                        }
                }
            }
        }
    }
    
    public boolean comentarios(){ // não salvar no array de tokens, apenas no arquivo
        char[] lexema;
        while(true){
        auxPosicao = posicao+1;
            if(posicao < linha.length && auxPosicao < linha.length ){
                if(linha[auxPosicao] != '/' && linha[auxPosicao] != '*'){
                    operadorAritmetico();
                    return true;
                }else if(linha[posicao]== '/'){
                    if(linha[auxPosicao] == '/'){
                        //colocar pra printar apenas no arquivo
                        lexema = new char[2];
                        lexema[0] = linha[posicao];
                        lexema[1] = linha[auxPosicao];
                        Tokens token = new Tokens(numLinha, "COMENTARIO", lexema);
                        tokens.add(token);
                        posicao = 0;
                        numLinha++;
                        linha = codigo.get(numLinha);
                        return true;
                    }
                    else if(linha[auxPosicao] == '*'){
                        auxPosicao++;
                        for(int b = numLinha; b <codigo.size(); b++){
                            for(int a = auxPosicao; a<linha.length; a++){
                                if(linha[a] == '*' && linha[a+1] == '/'){
                                    //colocar pra printar no arquivo
                                    return true;
                                }
                            }
                        }

                    }
                    else{
                        lexema = new char[2];

                        Tokens token = new Tokens(numLinha, "ERRO COMENTARIO", lexema);
                        tokens.add(token);
                        return true;
                    }
                }
            } else{
                operadorAritmetico();
                    return true;
            }
        }
    }
    
    public boolean delimitador(){
        char[] lexema;
        while(posicao < linha.length){
        auxPosicao = posicao+1;
            if(linha[posicao] == ';' || linha[posicao] == ',' || linha[posicao] == '(' || linha[posicao] == ')' || linha[posicao] == '[' || linha[posicao] == ']' || linha[posicao] == '{' || linha[posicao] == '}' || linha[posicao] == '.'){
                lexema = new char[1];
                lexema[0] = linha[posicao];
                Tokens token = new Tokens(numLinha, "DELIMITADOR", lexema);
                tokens.add(token);
                posicao = auxPosicao;
                
                return true;
            }
            
        }
        return false;
    }
    public boolean negativo(){
        char[] lexema = null;
        
        while(true){
             posicao=auxPosicao;
             auxPosicao++;
             if(posicao < linha.length){
                if(linha[posicao]=='-'){
                        lexema[0]='-';
                        auxPosicao = ignorarEspaco(auxPosicao);
                        
                        if(linha[auxPosicao] == '-'){
                            lexema[1]='-';
                            Tokens token = new Tokens(numLinha, "OPERADOR ARITMETICO", lexema);
                            
                            tokens.add(token);
                            posicao = auxPosicao;
                            return true;
                        }
                        else{
                            return numero();
                                
                        }
                    }
            }
        }
    }

    private boolean numero() {
        char[] lexema =null;
        int tamanho =0;
        if(linha[posicao]=='-'){
            lexema[0]='-';
            tamanho++;
        }
        while (Character.isDigit(linha[posicao])|| auxPosicao<linha.length){
            posicao=auxPosicao;
            auxPosicao= posicao +1;
            lexema[tamanho]=linha[posicao];
            if(!Character.isDigit(linha[posicao])){
                if(linha[posicao]== '.'){
                    while (Character.isDigit(linha[posicao])){
                        posicao=auxPosicao;
                        auxPosicao= posicao +1;
                        lexema[tamanho]=linha[posicao];
                    }
                    Tokens token = new Tokens(numLinha, "Numero", lexema);
                    tokens.add(token);
                    posicao = auxPosicao;
                    return true;
                }
                else{
                    Tokens token = new Tokens(numLinha, "Numero", lexema);
                    tokens.add(token);
                    posicao = auxPosicao;
                    return true;
                }
            }
          tamanho++;  
        }
        return false;
    }
}
