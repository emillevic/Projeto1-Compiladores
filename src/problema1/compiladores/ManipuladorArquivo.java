/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problema1.compiladores;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Mille
 */
public class ManipuladorArquivo {

    /**
     * @param args the command line arguments
     */
    private String path;

    public ManipuladorArquivo(String path) {
        this.path = path;
    }
   
    
    public  ArrayList<String> leitor() throws IOException {
        BufferedReader buffRead = new BufferedReader(new FileReader(this.path));
        String linha = "";
        ArrayList<String> arquivo = null;
        while (true) {
            linha = buffRead.readLine();
            if (linha != null) {
                arquivo.add(linha);
             return arquivo;
            }  
            else{ 
                buffRead.close();
                return null;
                }
        }
    }
 
    public  void escritor(String path, String escrito) throws IOException {
        BufferedWriter buffWrite = new BufferedWriter(new FileWriter(path));
        escrito = "";
        Scanner in = new Scanner(System.in);
        System.out.println("Escreva algo: ");
        escrito = in.nextLine();
        buffWrite.append(escrito + "\n");
        buffWrite.close();
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }
}
