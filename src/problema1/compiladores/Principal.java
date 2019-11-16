/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problema1.compiladores;

import java.io.IOException;

/**
 *
 * @author Mille
 */
public class Principal  {
    Automatos automatos;
    ManipuladorArquivo codigo;
    Tokens atual;
    
        public   void main(String[] args) throws IOException {
            SetUp();
            for(int i; i++; i<automatos.getTokens().lenght ){
            atual = automatos.getTokens().get(i);
            atual.toString();
            codigo.setPath(".../PastaSaida/codigo1Saida");
            codigo.escritor();
            }
            
        // TODO code application logic here
    }
    

    public void SetUp() throws IOException {
      String caminho =".../PastaEntrada/codigo1";
      codigo = new ManipuladorArquivo(caminho);
      automatos= new Automatos(codigo.leitor());
      
    }
    
    
    
    
    
    
}
