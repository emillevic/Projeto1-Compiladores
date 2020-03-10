/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problema1.compiladores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Mille
 */
public class Principal  {
    private static Automatos automatos;
    private static AnalisadorSintatico analisadorSintatico;
    private static AnalisadorSemantico analisadorSemantico;
    private static ManipuladorArquivo manipuladorArquivo = new ManipuladorArquivo();
    private static int numeroArquivo =1;
    private static String arquivoEntrada = new String();
    private static String arquivoSaida = new String();
    private static ArrayList<String> entrada;
    private static ArrayList<String> saida;
   
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insira a quantidade de arquivos de entrada:");
        int quantidade = scanner.nextInt();
        for(int i = 0; i<quantidade;i++){
            arquivoEntrada = "entrada" + numeroArquivo + ".txt";
            arquivoSaida = "saida" + numeroArquivo + ".txt";
            entrada = manipuladorArquivo.leitor(arquivoEntrada);
            if(!entrada.isEmpty()){
                automatos = new Automatos(entrada);
                saida = automatos.analisadorLexico();
                analisadorSemantico = new AnalisadorSemantico(automatos.getTokens());
                analisadorSemantico.controle();
                System.out.println("consts: " + analisadorSemantico.getCONSTS().toString());
                System.out.println("functions: " + analisadorSemantico.getFUNCTIONS().toString());
                System.out.println("globalvar: " + analisadorSemantico.getGLOBALVAR().toString());
                System.out.println("procedures: " + analisadorSemantico.getPROCEDURES().toString());
                System.out.println("startvar: " + analisadorSemantico.getSTARTVAR().toString());
                System.out.println("structs: " + analisadorSemantico.getSTRUCTS().toString());
                System.out.println("tipo: " + analisadorSemantico.getTIPO().toString());
//                analisadorSintatico = new AnalisadorSintatico(automatos.getTokens());
//                ArrayList<String> sintatico = analisadorSintatico.Sintatico();;
//                System.out.println(analisadorSintatico.getSaidaString().toString());
//                manipuladorArquivo.escritor(arquivoSaida, sintatico);
                System.out.println("Analise do arquivo" + arquivoEntrada + "feita no arquivo" + arquivoSaida);
            }
            else{
                System.out.println("Arquivo em Branco");
            }
            numeroArquivo++;
        }
    }
             
}
