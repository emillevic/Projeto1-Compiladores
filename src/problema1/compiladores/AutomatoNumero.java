/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problema1.compiladores;

import java.util.ArrayList;

/**
 *
 * @author Henrique
 */
public class AutomatoNumero {

    public AutomatoNumero(char[] linha, int posicao) {
        this.linha = linha;
        this.posicao = posicao;
    }
    char[] linha;
    int posicao;
    int auxPosicao;
    int numLinha;
    char[] lexema;
    ArrayList<Tokens> tokens;
    
    public boolean negativo(){
        auxPosicao= posicao+1;
         while(true){
            if(posicao < linha.length){
                if(linha[posicao]=='-'){
                    
                        auxPosicao = ignorarEspaco(auxPosicao);
                        proximaLinha();
                        auxPosicao = ignorarEspaco(auxPosicao);
                        if(linha[auxPosicao] == '-'){
                            Tokens token = new Tokens(numLinha, "OPERADOR ARITMETICO", lexema);
                            System.out.println("operador");
                            tokens.add(token);
                            posicao = auxPosicao;
                            return true;
                        } else {
                            /*Tokens token = new Tokens(numLinha, "OPERADOR ARITMETICO", lexema);
                            tokens.add(token);
                            posicao = auxPosicao;
                            return true;
                            */
                            return numero();
                        }
                   }
                else{
                     return numero();
                }
                }
            }
    }

    private int ignorarEspaco(int auxPosicao) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void proximaLinha() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean numero() {
        while (Character.isDigit(linha[posicao])|| auxPosicao<linha.length){
            posicao=auxPosicao;
            auxPosicao= posicao +1;
            if( linha[auxPosicao]== '.'){
                posicao=auxPosicao;
                auxPosicao= posicao +1;
                if(!Character.isDigit(linha[auxPosicao])){
                    Tokens token = new Tokens(numLinha, "NUMERO", lexema);
                    System.out.println("Numero c ponto");
                    //tokens.add(token);
                    posicao = auxPosicao;
                    return true;           
                }
                else{
                    while (Character.isDigit(linha[posicao])){
                    
                    posicao=auxPosicao;
                    auxPosicao= posicao +1;
                    
                    if(auxPosicao>linha.length){
                        System.out.println("Numero decimal");
                        Tokens token = new Tokens(numLinha, "NUMERO", lexema);
                        System.out.println("Numero");
                       // tokens.add(token);
                        posicao = auxPosicao;
                        return true;
                        }
                    }
                }
                posicao=auxPosicao;
                auxPosicao= posicao +1;
            }
        }
             if(auxPosicao>linha.length){
                Tokens token = new Tokens(numLinha, "NUMERO", lexema);
                System.out.println("Numero\n" + linha[0]+linha[1]);
                //tokens.add(token);
                posicao = auxPosicao;
                return true;
                }    
        
        
        return false;
    }
}

