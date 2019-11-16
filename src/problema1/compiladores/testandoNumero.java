/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problema1.compiladores;

import java.util.Scanner;

/**
 *
 * @author Henrique
 */
public class testandoNumero {
    
     public static  void main(String[] args)  {
           Scanner leitor = new Scanner(System.in);
           
           System.out.println("escreva o numero:\n");
            String numero= leitor.nextLine();
           char[]extenso=numero.toCharArray();
           AutomatoNumero aut= new AutomatoNumero(extenso,0);
           aut.negativo();
            
            
            
        // TODO code application logic here
    }
}
