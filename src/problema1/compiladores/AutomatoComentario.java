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
public class AutomatoComentario {
    
    public void começa(char[] palavra, int pos){
        if(palavra[pos]=='/')
            q1(palavra, pos+1);
        /*
        
        */
    
    }

    private void q1(char[] palavra, int pos) {
        if(palavra[pos]=='*')
            q2(palavra, pos+1);
     //To change body of generated methods, choose Tools | Templates.
    }

    private void q2(char[] palavra, int pos) {
        //To change body of generated methods, choose Tools | Templates.
        if(!(palavra[pos]=='*'))
            q2(palavra, pos+1);
        else
            q3(palavra, pos+1);
    }

    private void q3(char[] palavra, int pos) {
        if(!(palavra[pos]=='/'))
            q2(palavra, pos+1);
        else
            qf();
        
    }

    private void qf() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
