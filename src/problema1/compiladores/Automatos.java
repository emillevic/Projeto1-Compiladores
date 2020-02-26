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
        boolean erro = false;
        for(int i = 0; i<tokens.size(); i++){
            tabela.add(tokens.get(i).toString());
            if(tabela.get(i).contains("ERRO")){
                erro = true;
            }
        }
        if(erro == false){
            tabela.add("CODIGO SEM ERRO!");
        }
        return tabela;
    }
    
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
            numLinha++;
            if(numLinha < codigo.size()){
                linha = codigo.get(numLinha);
                return true;
            } else if(numLinha >= codigo.size()){
                numLinha = codigo.size();
                return false;
            }
        }
        return false;
    }
    
    public int ignorarEspaco(int pos){
        if(pos == linha.length && estruturaLexica.verificaEspaco(linha[pos-1])){
            posicao = pos;
            return pos;
        }else if(pos < linha.length){
            while(pos < linha.length && estruturaLexica.verificaEspaco(linha[pos])){
                pos++;
            }
        }
        return pos;
    }
    
    public String controle(){
        while(true){
            if(posicao < linha.length && numLinha < codigo.size()){
                if(linha[posicao] == '/'){
                    comentarios();
                }else if(estruturaLexica.verificaLetra(linha[posicao])){
                    identificadores();
                }else if(estruturaLexica.verificaOperadorAritmetico(linha[posicao])){
                    operadorAritmetico();
                }else if(estruturaLexica.verificaOperadorRelacional(linha[posicao])){
                    operadorRelacional();
                }else if(estruturaLexica.verificaOperadorLogico(linha[posicao])){
                    operadorLogico();
                } else if(estruturaLexica.verificaDelimitador(linha[posicao])){
                    delimitador();
                } else if(estruturaLexica.verificaDigito(linha[posicao])){
                   numero(); 
                }else if(estruturaLexica.verificaEspaco(linha[posicao])){
                    posicao = ignorarEspaco(posicao+1);
                } else if(linha[posicao] == '"'){
                    cadeia();
                }else if(!estruturaLexica.verificaSimbolo(linha[posicao])){
//                    simboloInvalido();
                    char[] lexema = new char[1];
                    lexema[0] = linha[posicao];
                    Tokens token1 = new Tokens(numLinha, "SIMBOLO INVALIDO",lexema);
                    tokens.add(token1);
                    posicao++;
                    auxPosicao = posicao+1;
                }
            } else if(proximaLinha()){
                posicao = 0;
                auxPosicao = posicao+1;
            } else if(numLinha == codigo.size()){
                return "FIM";
            }
        }
        
    }
    
    public boolean simboloInvalido(){
        char[] lexema;
        ArrayList<Character> lista = new ArrayList<Character>();
        while(true){
            posicao = ignorarEspaco(posicao);
            proximaLinha();
            posicao = ignorarEspaco(posicao);
            if(posicao< linha.length){
                if(!estruturaLexica.verificaSimbolo(linha[posicao])){
                    lista.add(linha[posicao]);
                    posicao++;
                }
            }
            lexema = toCharArray(lista);
            Tokens token1 = new Tokens(numLinha, "SIMBOLO INVALIDO",lexema);
            tokens.add(token1);
            posicao++;
            auxPosicao = posicao+1;
            return true;
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
                    }else if(linha[posicao] == '!'){
                        operadorLogico();
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
                        if(auxPosicao < linha.length && linha[auxPosicao] == '&' ){
                            lexema = new char[2];
                            lexema[0] = linha[posicao];
                            lexema[1] = linha[auxPosicao];
                            Tokens token1 = new Tokens(numLinha, "OPERADOR LOGICO", lexema);
                            tokens.add(token1);
                            posicao = auxPosicao+1;
                            return true;
                        } else{
                            lexema = new char[1];
                            lexema[0] = linha[posicao];
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
                            lexema[1] = linha[auxPosicao];
                            Tokens token1 = new Tokens(numLinha, "OPERADOR LOGICO", lexema);
                            tokens.add(token1);
                            posicao = auxPosicao+1;
                            return true;
                        } else{
                            lexema = new char[1];
                            lexema[0] = linha[posicao];
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
                        if(numLinha < codigo.size())
                            linha = codigo.get(numLinha);
                        return true;
                    }
                    else if(linha[auxPosicao] == '*'){
                        ArrayList<Character> lex = new ArrayList<>();
                        auxPosicao++;
                        for(int b = numLinha; b <codigo.size(); b++){
                            linha = codigo.get(b);

                            for(int a = auxPosicao; a<linha.length; a++){
                                lex.add(linha[a]);
                                if(linha[a] == '*' && linha[a+1] == '/'){
                                    lexema = new char[lex.size()];
                                    for(int e = 0; e<lexema.length; e++){
                                        lexema[e] = lex.get(e);
                                    }
                                    
                                    Tokens token = new Tokens(b, "COMENTARIO BLOCO", lexema);
                                    tokens.add(token);
                                    
                                    posicao = 0;
                                    numLinha = b+1;
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
   
    private char[] toCharArray(ArrayList<Character> charList){
        char[] arrayChar = new char[charList.size()];
        for (int i = 0; i<charList.size(); i++){
            arrayChar[i] = charList.get(i);
        }
        return arrayChar;
    }
    private boolean numero() {
        ArrayList<Character> lexema = new ArrayList<Character>();
        boolean controlePonto = false;
       
        while(true){
            auxPosicao = posicao+1;
            if(posicao <linha.length){
                if(estruturaLexica.verificaDigito(linha[posicao])){
                    lexema.add(linha[posicao]);
                    posicao++;
                } else if(linha[posicao] == '.' && controlePonto == false){
                    lexema.add('.');
                    controlePonto = true;
                    if(auxPosicao == linha.length){
                        Tokens token = new Tokens(numLinha, "ERRO NUMERO", toCharArray(lexema));
                        tokens.add(token);
                        proximaLinha();
                        posicao=0;
                        return true;
                    }
                    if(!estruturaLexica.verificaDigito(linha[auxPosicao])){
                        Tokens token = new Tokens(numLinha, "ERRO NUMERO", toCharArray(lexema));
                        tokens.add(token);
                        posicao++;
                        return true;
                    }
                    posicao++;
                } else if(linha[posicao] == '.' && controlePonto == true){
                    Tokens token = new Tokens(numLinha, "ERRO NUMERO", toCharArray(lexema));
                    tokens.add(token);
                    return true;
                } else if(!estruturaLexica.verificaDigito(linha[posicao])){
                    Tokens token = new Tokens(numLinha, "NUMERO", toCharArray(lexema));
                    tokens.add(token);
                    //posicao++;
                    return true;
                }
            } else{
                Tokens token = new Tokens(numLinha, "NUMERO", toCharArray(lexema));
                tokens.add(token);
                //numLinha++;
                proximaLinha();
                posicao = 0;
                return true;
            }
        }
    }
    public boolean cadeia(){
        ArrayList<Character> lexema = new ArrayList<Character>();
        if(linha[posicao] == '"'){
            lexema.add('"');
        }
        posicao++;
        while(true){
            auxPosicao=posicao+1;
            if(posicao < linha.length && linha[posicao] != '"' && auxPosicao != linha.length){
                if(estruturaLexica.verificaDigito(linha[posicao]) || estruturaLexica.verificaLetra(linha[posicao]) 
                        || estruturaLexica.verificaSimbolo(linha[posicao])
                        || estruturaLexica.verificaEspaco(linha[posicao])){
                    lexema.add(linha[posicao]);
                    if(auxPosicao != linha.length)
                        posicao++;
                } else if(linha[posicao] == '\\' && linha[auxPosicao] == '"'){
                    lexema.add(linha[posicao]);
                    posicao++;
                }
            }else if(linha[posicao] == '"' && linha[posicao-1] != '\\'){
                lexema.add(linha[posicao]);
                Tokens token = new Tokens(numLinha, "CADEIRA DE CARACTERES", toCharArray(lexema));
                tokens.add(token);
                posicao++;
                return true;
            }else if(linha[posicao] == '"' && linha[posicao-1] == '\\'){
                lexema.add(linha[posicao]);
                posicao++;
            }
            else if(auxPosicao == linha.length && linha[posicao] != '"'){
                lexema.add(linha[posicao]);
                Tokens token = new Tokens(numLinha, "ERRO CADEIRA DE CARACTERES", toCharArray(lexema));
                tokens.add(token);
                posicao++;
                return true;
            }
                        proximaLinha();

        }
    }
    
    public String listToString(ArrayList<Character> lista){
        String palavra = "";
        for(int i = 0; i< lista.size(); i++){
            palavra = palavra + String.valueOf(lista.get(i));
            //palavra = palavra.concat(Character.toString(lista.get(i)));
        }
        return palavra;
    }
    
    public boolean identificadores(){
        ArrayList<Character> lexema = new ArrayList<Character>();
        lexema.add(linha[posicao]);
        posicao++;
        if(posicao == linha.length){
            Tokens token = new Tokens(numLinha, "IDENTIFICADOR", toCharArray(lexema));
            tokens.add(token);
            proximaLinha();
            posicao = 0;
            return true;
        }
        while(true){
            auxPosicao = posicao+1;
            if(posicao <linha.length){
                if(estruturaLexica.verificaLetra(linha[posicao]) || estruturaLexica.verificaDigito(linha[posicao])
                        || linha[posicao] == '_'){
                    lexema.add(linha[posicao]);
                    posicao++;
                } else{
                     Tokens token;
                    if(estruturaLexica.verificaPalavraReservada( listToString(lexema))){
                        token = new Tokens(numLinha, "PALAVRA RESERVADA", toCharArray(lexema));
                    } else{
                        token = new Tokens(numLinha, "IDENTIFICADOR", toCharArray(lexema));
                    }
                    tokens.add(token);
//                    posicao++;
                    return true;
                }
                if(posicao == linha.length){
                    Tokens token;
                    if(estruturaLexica.verificaPalavraReservada( listToString(lexema))){
                        token = new Tokens(numLinha, "PALAVRA RESERVADA", toCharArray(lexema));
                    } else{
                        token = new Tokens(numLinha, "IDENTIFICADOR", toCharArray(lexema));
                    }
                    tokens.add(token);
                    proximaLinha();
                    posicao = 0;
                    return true;
                }else if(estruturaLexica.verificaEspaco(linha[posicao])){
                    Tokens token;
                    if(estruturaLexica.verificaPalavraReservada( listToString(lexema))){
                        token = new Tokens(numLinha, "PALAVRA RESERVADA", toCharArray(lexema));
                    } else{
                        token = new Tokens(numLinha, "IDENTIFICADOR", toCharArray(lexema));
                    }
                    tokens.add(token);
                    
                    while(posicao < linha.length && estruturaLexica.verificaEspaco(linha[posicao])){
                        posicao++;
                    }
                    return true;
                }
            }
        }
    }
}
