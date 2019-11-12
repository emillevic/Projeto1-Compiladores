/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problema1.compiladores;

/**
 *
 * @author Henrique
 */
public class AutomatoComentarioLinha {
      public void começa(char[] palavra, int pos){
        if(palavra[pos]=='/')
            q1(palavra, pos+1);
        /*
        
        */
    
    }

    private void q1(char[] palavra, int pos) {
        if(palavra[pos]=='/')
            q2(palavra, pos+1);
    }

    private void q2(char[] palavra, int pos) {
        if(palavra[pos]!='\n')
            q2(palavra, pos+1);
        else
            qf();
    }

    private void qf() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
