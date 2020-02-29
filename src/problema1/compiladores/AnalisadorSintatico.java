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
public class AnalisadorSintatico {
   private ArrayList<Tokens> codigoTratado;
   private Tokens atual;
   private Tokens anterior;
   private Tokens proximo;
   private int indice =0;
   private ArrayList<String> Tipo = new ArrayList<String>();
   private ArrayList<ErroSintatico> saida = new ArrayList<ErroSintatico>();

//   private String BooleanType[] = {"true", "false"};;;
//   private String Increment = {"++" | "--"};
//   private String RelationalOp[] = {'!=' | '==' |'<' | '<=' | '>' |'>='};
//   private String[] LogicOp;
//   private String string;
//   private String[] number;
//   private String[] Type;
   
   public AnalisadorSintatico(Tokens atual, Tokens anterior, Tokens proximo){
       this.atual= atual;
       this.anterior= anterior;
       this.proximo= proximo;
       
       
   }

   public AnalisadorSintatico(ArrayList<Tokens> codigoTratado){
       this.codigoTratado = codigoTratado;
       Tipo.add("int");
       Tipo.add("real");
       Tipo.add("boolean");
       Tipo.add("string");
       
   }
   
   public ArrayList<ErroSintatico> AnalisePrograma (){
       atual= codigoTratado.get(indice);
       proximo= codigoTratado.get(indice+1);
       System.out.println("analise Programa");
       System.out.println( "lexema atual - " + atual.getLexemaString());
       while(true){
           System.out.println(atual);
            if("const".equals(atual.getLexemaString())){
                System.out.println("const---------------");
                AnaliseConst();
            } else if("start".equals(atual.getLexemaString())){
                System.out.println("start----------------");
                AnaliseStart();
            }else if("var".equals(atual.getLexemaString())){
                System.out.println("var------------------");
                AnaliseVariavel();
            }else if("structs".equals(atual.getLexemaString())){
                System.out.println("structs-----------------");
                AnaliseStruct();
            }else if("typedefs".equals(atual.getLexemaString())){
                 System.out.println("typedefs--------------------");
                AnaliseTypedef();
            }else if("functions".equals(atual.getLexemaString())){
                System.out.println("functions---------------------");
                AnaliseFunctions();
            }else if("procedures".equals(atual.getLexemaString())){
                System.out.println("procedures------------------");
                AnaliseProcedures();
            }
            if(indice+2 < codigoTratado.size())
                andaUm();
            else
                return saida;
       }
       
       
   }
    /**
     * @return the atual
     */
    public Tokens getAtual() {
        return atual;
    }

    /**
     * @param atual the atual to set
     */
    public void setAtual(Tokens atual) {
        this.atual = atual;
    }
    public ArrayList<ErroSintatico> getSaida(){
        return saida;
    }
    public ArrayList<String> getSaidaString(){
        ArrayList<String> arq = new ArrayList<String>();
        for(int i = 0; i< saida.size(); i++){
            arq.add(saida.get(i).toStringLista());
        }
        return arq;
    }
    /**
     * @return the anterior
     */
    public Tokens getAnterior() {
        return anterior;
    }

    /**
     * @param anterior the anterior to set
     */
    public void setAnterior(Tokens anterior) {
        this.anterior = anterior;
    }

    /**
     * @return the proximo
     */
    public Tokens getProximo() {
        return proximo;
    }

    /**
     * @param proximo the proximo to set
     */
    public void setProximo(Tokens proximo) {
        this.proximo = proximo;
    }

    private void AnaliseConst() {
        if("DELIMITADOR".equals(proximo.getTipo()) && "{".equals(proximo.getLexemaString())){
            andaUm();
            while(!"DELIMITADOR".equals(atual.getTipo()) && !"}".equals(atual.getLexemaString())){
                andaUm();
                ConstantStructure();
            }
        } else{
            ErroSintatico erro = new ErroSintatico("{ expected", atual.getLinha());
            saida.add(erro);
        }
    }
    
    private void ConstantStructure(){
        if(Tipo.contains(atual.getLexemaString())){
            andaUm();
            if("IDENTIFICADOR".equals(atual.getTipo()) && "=".equals(proximo.getLexemaString())){
                andaUm();
                if("cadeiaDeCaracteres".equals(proximo.getTipo()) || "numeros".equals(proximo.getTipo())
                        || "boolean".equals(proximo.getTipo())){
                   andaUm();
                    if(";".equals(proximo.getLexemaString())){
                        
                    } else{
                        ErroSintatico erro = new ErroSintatico("; expected", atual.getLinha());
                        saida.add(erro);
                    }
                } else{
                    ErroSintatico erro = new ErroSintatico("String, Number or Boolean expected", atual.getLinha());
                    saida.add(erro);
                }
            } else{
                ErroSintatico erro = new ErroSintatico("IDENTIFICADOR & '=' expected", atual.getLinha());
                saida.add(erro);
                }
        }else{
            ErroSintatico erro = new ErroSintatico("Tipo unexpected", atual.getLinha());
            saida.add(erro);
        }
    }

    private void AnaliseStart() {
         if("DELIMITADOR".equals(proximo.getTipo()) && "(".equals(proximo.getLexemaString())){
           andaUm();
            if("DELIMITADOR".equals(proximo.getTipo()) && ")".equals(proximo.getLexemaString())){
                andaUm();
                if("DELIMITADOR".equals(proximo.getTipo()) && "{".equals(proximo.getLexemaString())){
                    
                    while(!"DELIMITADOR".equals(atual.getTipo()) && !"}".equals(atual.getLexemaString())){
                       andaUm();
                       if("var".equals(atual.getLexemaString()) ){
                            AnaliseVariavel();
                            comandos();
                       }
                       else{
                           ErroSintatico erro = new ErroSintatico("var expected", atual.getLinha());
                           saida.add(erro);
                       }
                    }
                }else{
                    ErroSintatico erro = new ErroSintatico("{ expected", atual.getLinha());
                    saida.add(erro);
                }
            }else{
                ErroSintatico erro = new ErroSintatico(") expected", atual.getLinha());
                saida.add(erro);
            }
        } else{
            ErroSintatico erro = new ErroSintatico("( expected", atual.getLinha());
            saida.add(erro);
         }
    }
    
    private void andaUm(){
        indice++;
        anterior=atual;
        atual=proximo;
        proximo=codigoTratado.get(indice+1);
    }
    private void voltaUm(){
        indice--;
        proximo=atual;
        atual=anterior;
        anterior=codigoTratado.get(indice-1);
    }

    private void AnaliseVariavel() {
    //To change body of generated methods, choose Tools | Templates.
        if("DELIMITADOR".equals(proximo.getTipo()) && "{".equals(proximo.getLexemaString())){
            andaUm();
           while(!"DELIMITADOR".equals(atual.getTipo()) && !"}".equals(atual.getLexemaString())){
               andaUm();
               VarV();
           }
        }else{
            ErroSintatico erro = new ErroSintatico("{ expected", atual.getLinha());
            saida.add(erro);
        }
    }
    private void VarV() {
        System.out.println("AQUIIIIIIIIIIIIII " + atual.getLexemaString());
        //To change body of generated methods, choose Tools | Templates.
        if(Tipo.contains(atual.getLexemaString())){
            andaUm();
            complementV();
            VarV();
        } else{
            System.out.println(atual.getLexemaString() + "     -     " + Tipo.toString());
            ErroSintatico erro = new ErroSintatico("Tipo unexpected aqui", atual.getLinha());
            saida.add(erro);
        }
    }

    private void complementV() {
        //To change body of generated methods, choose Tools | Templates.
        if("IDENTIFICADOR".equals(atual.getTipo()) ){
            if(",".equals(proximo.getLexemaString())){
                andaUm();
                varEqType();
            }else if("=".equals(proximo.getLexemaString())){
                andaUm();
                if("cadeiaDeCaracteres".equals(proximo.getTipo()) || "numeros".equals(proximo.getTipo())
                        || "boolean".equals(proximo.getTipo())){
                    andaUm();
                    if(";".equals(proximo.getLexemaString())){

                    }else{
                        ErroSintatico erro = new ErroSintatico("; expected", proximo.getLinha());
                        saida.add(erro);
                    }
               }else{
                    ErroSintatico erro = new ErroSintatico("String, Number or Boolean expected", proximo.getLinha());
                    saida.add(erro);
                }
            }else{
                ErroSintatico erro = new ErroSintatico(", or = expected", proximo.getLinha());
                saida.add(erro);
            }
        }else{
            ErroSintatico erro = new ErroSintatico("IDENTIFICADOR expected", atual.getLinha());
            saida.add(erro);
        }
         
    }

    private void varEqType() {
        //To change body of generated methods, choose Tools | Templates.
        complementV();
        if(";".equals(proximo.getLexemaString())){
            
        } else{
            ErroSintatico erro = new ErroSintatico("; expected", proximo.getLinha());
            saida.add(erro);
        }
        
    }

    private void AnaliseStruct() {
         //To change body of generated methods, choose Tools | Templates.
        if("DELIMITADOR".equals(proximo.getTipo()) && "{".equals(proximo.getLexemaString())){
            andaUm();
            while(!"}".equals(atual.getLexemaString())){
                andaUm();
                System.out.println(atual);
                if("struct".equals(atual.getLexemaString())){
                     andaUm();
                    if("IDENTIFICADOR".equals(atual.getTipo()) ){
                         andaUm();
                         Extends();
                        if("{".equals(atual.getLexemaString())){
                             andaUm();
                             attributes();
                            if("}".equals(atual.getLexemaString())){
                                 andaUm();
                                if(";".equals(atual.getLexemaString())){

                                }else{
                                    ErroSintatico erro = new ErroSintatico("; expected", atual.getLinha());
                                    saida.add(erro);
                                 }
                            }else{
                                ErroSintatico erro = new ErroSintatico("} expected", atual.getLinha());
                                saida.add(erro);
                            }
                        }else{
                            ErroSintatico erro = new ErroSintatico("{ expected", atual.getLinha());
                            saida.add(erro);
                        }
                         
                    }else{
                        ErroSintatico erro = new ErroSintatico("IDENTIFICADOR expected", atual.getLinha());
                        saida.add(erro);
                    }
                }else{
                    ErroSintatico erro = new ErroSintatico("'struct' expected", atual.getLinha());
                    saida.add(erro);
                }   
            }
            return;     
        }else{
            ErroSintatico erro = new ErroSintatico("{ expected", proximo.getLinha());
            saida.add(erro);
        }
    }
     private void Extends() {
        if("extends".equals(atual.getLexemaString())){
             andaUm();
            if("IDENTIFICADOR".equals(atual.getTipo())){
                 andaUm();
                 return;
            }else{
                ErroSintatico erro = new ErroSintatico("IDENTIFICADOR expected", atual.getLinha());
                saida.add(erro);
             }
        }else{
            ErroSintatico erro = new ErroSintatico("'extends' expected", atual.getLinha());
            saida.add(erro);
        }
         
    }

    private void attributes() {                
        if(Tipo.contains(atual.getLexemaString())){
            andaUm();
            if("IDENTIFICADOR".equals(atual.getTipo())){
                andaUm();
                if(";".equals(atual.getLexemaString())){
                    andaUm();
                    attributes();
                }else{
                    ErroSintatico erro = new ErroSintatico("; expected", atual.getLinha());
                    saida.add(erro);
                }
            }else{
                ErroSintatico erro = new ErroSintatico("IDENTIFICADOR expected", atual.getLinha());
                saida.add(erro);
            }
        }else{
            ErroSintatico erro = new ErroSintatico("Tipo unexpected", atual.getLinha());
            saida.add(erro);
        }
    }   
        
        
    

    private void AnaliseTypedef() {
         //To change body of generated methods, choose Tools | Templates.
        if("DELIMITADOR".equals(proximo.getTipo()) && "{".equals(proximo.getLexemaString())){
            andaUm();
            while(!"DELIMITADOR".equals(atual.getTipo()) && !"}".equals(atual.getLexemaString())){
                typedef();
            }  
        } else{
            ErroSintatico erro = new ErroSintatico("{ expected", proximo.getLinha());
            saida.add(erro);
        }
    }

    private void typedef() {
         //To change body of generated methods, choose Tools | Templates.
         if("typedef".equals(atual.getLexemaString())){
             andaUm();
             if("struct".equals(atual.getLexemaString())){
                 andaUm();
                 if("IDENTIFICADOR".equals(atual.getTipo())){
                     andaUm();
                     if("IDENTIFICADOR".equals(atual.getTipo())){
                         andaUm();
                        if(";".equals(atual.getLexemaString())){
                            andaUm();
                            return;
                        }else{
                            ErroSintatico erro = new ErroSintatico("; expected", atual.getLinha());
                            saida.add(erro);
                        }
                    }else{
                         ErroSintatico erro = new ErroSintatico("IDENTIFICADOR expected", atual.getLinha());
                        saida.add(erro);
                     }
                }else{
                     ErroSintatico erro = new ErroSintatico("IDENTIFICADOR expected", atual.getLinha());
                    saida.add(erro);
                 }
            } else{
                 ErroSintatico erro = new ErroSintatico("'struct' expected", atual.getLinha());
                saida.add(erro);
             }
        } else{
             ErroSintatico erro = new ErroSintatico("'typedef' expected", atual.getLinha());
            saida.add(erro);
         }
    }
    
    private void Incremments(){
        if("IDENTIFICADOR".equals(atual.getTipo())){
            andaUm();
            variavel();
            incremment();
            andaUm();
            if(";".equals(atual.getLexemaString())){
                return;
            } else{
                ErroSintatico erro = new ErroSintatico("; expected", atual.getLinha());
                saida.add(erro);
            }
        }else{
            ErroSintatico erro = new ErroSintatico("IDENTIFICADOR expected", atual.getLinha());
            saida.add(erro);
        }
    }

    private void incremment() {
        //To change body of generated methods, choose Tools | Templates.
        if("++".equals(atual.getLexemaString())||"--".equals(atual.getLexemaString())){
            return;
        }else{
            ErroSintatico erro = new ErroSintatico("++ or -- expected", atual.getLinha());
            saida.add(erro);
        }
    }
    private void chFunProc(){
        if("IDENTIFICADOR".equals(atual.getTipo())){
            andaUm();
            if("(".equals(atual.getLexemaString())){
                andaUm();
                chParam();
                andaUm();
                if(")".equals(atual.getLexemaString())){
                    andaUm();
                    if(";".equals(atual.getLexemaString())){
                        return;
                    }else{
                        ErroSintatico erro = new ErroSintatico("; expected", atual.getLinha());
                        saida.add(erro);
                    }
                } else{
                    ErroSintatico erro = new ErroSintatico(") expected", atual.getLinha());
                    saida.add(erro);
                }
                    
            }else{
                ErroSintatico erro = new ErroSintatico("( expected", atual.getLinha());
                saida.add(erro);
            }
        }else{
            ErroSintatico erro = new ErroSintatico("IDENTIFICADOR expected", atual.getLinha());
            saida.add(erro);
        }
        
    }

    private void chParam() {
        //To change body of generated methods, choose Tools | Templates.
        if("IDENTIFICADOR".equals(atual.getTipo())){
            andaUm();
            chParam2();
            return;
        }
        else if("cadeiaDeCaracteres".equals(atual.getTipo()) || "numeros".equals(atual.getTipo())
                || "boolean".equals(atual.getTipo())){
            andaUm();
            chParam2();
            return;
        }else{
            ErroSintatico erro = new ErroSintatico("IDENTIFICADOR or String or Number or Boolean expected", atual.getLinha());
            saida.add(erro);
        }
    }

    private void chParam2() {
         //To change body of generated methods, choose Tools | Templates.
         if(",".equals(atual.getLexemaString())){
             andaUm();
             if("IDENTIFICADOR".equals(atual.getTipo())){
                 andaUm();
                 chParam2();
             }
             else if("cadeiaDeCaracteres".equals(atual.getTipo()) || "numeros".equals(atual.getTipo())
                     || "boolean".equals(atual.getTipo())){
                 
                 andaUm();
                 chParam2();
             }else{
                ErroSintatico erro = new ErroSintatico("IDENTIFICADOR or String or Number or Boolean expected", atual.getLinha());
                saida.add(erro);
             }
         }else{
            ErroSintatico erro = new ErroSintatico(", expected", atual.getLinha());
            saida.add(erro);
         }
    }
    private void AnaliseFunctions() {
        if("DELIMITADOR".equals(proximo.getTipo()) && "{".equals(proximo.getLexemaString())){
            andaUm();
            while(!"DELIMITADOR".equals(atual.getTipo()) && !"}".equals(atual.getLexemaString())){
                if("int".equals(atual.getLexemaString())||"real".equals(atual.getLexemaString())||
                        "boolean".equals(atual.getLexemaString())||"string".equals(atual.getLexemaString())){
                    andaUm();
                    if("IDENTIFICADOR".equals(atual.getTipo())){
                        andaUm();
                        if("(".equals(atual.getLexemaString())){
                            while(!")".equals(atual.getLexemaString())){
                               andaUm();
                               DeclaraParam();
                            }
                            AnaliseVariavel();
                            andaUm();
                            comandos();
                            return;
                        }else{
                            ErroSintatico erro = new ErroSintatico("( expected", atual.getLinha());
                            saida.add(erro);
                        }
                    }else{
                        ErroSintatico erro = new ErroSintatico("IDENTIFICADOR expected", atual.getLinha());
                        saida.add(erro);
                    }
                }else{
                    ErroSintatico erro = new ErroSintatico("String or Number or Boolean expected", atual.getLinha());
                    saida.add(erro);
                }
            }
        }else{
            ErroSintatico erro = new ErroSintatico("{ expected", atual.getLinha());
            saida.add(erro);
        }
    }
    
    private void DeclaraParam(){
        if("int".equals(atual.getLexemaString())||"real".equals(atual.getLexemaString())||
                "boolean".equals(atual.getLexemaString())||"string".equals(atual.getLexemaString())){
            andaUm();
            if("IDENTIFICADOR".equals(atual.getTipo())){
                if(",".equals(proximo.getLexemaString())){
                    andaUm();
                    DeclaraParam();
                }else{
                    ErroSintatico erro = new ErroSintatico(", expected", atual.getLinha());
                    saida.add(erro);
                }
            }else{
                ErroSintatico erro = new ErroSintatico("IDENTIFICADOR expected", atual.getLinha());
                saida.add(erro);
            }
        }else{
            ErroSintatico erro = new ErroSintatico("String or Number or Boolean expected", atual.getLinha());
            saida.add(erro);
        }
    }
    
    private void AnaliseProcedures() {
        if("DELIMITADOR".equals(proximo.getTipo()) && "{".equals(proximo.getLexemaString())){
            andaUm();
            while(atual.getTipo()!="DELIMITADOR" && atual.getLexemaString()!= "}"){
                AnaliseVariavel();
                andaUm();
                comandos();
                return;
            }
        }else{
            ErroSintatico erro = new ErroSintatico("{ expected", atual.getLinha());
            saida.add(erro);
        }
    }
    
    private void ExpressaoAritimetica(){
        if("+".equals(proximo.getLexemaString())||"-".equals(proximo.getLexemaString())){
          ExpressaoAritimetica();
          andaUm(); andaUm();
          MultExp();
        }else {
            MultExp();
            return;
        }
    }
    private void MultExp(){
       if("*".equals(proximo.getLexemaString())|| "/".equals(proximo.getLexemaString())){
           MultExp();
           andaUm(); andaUm();
           ValorNeg();
       }
       else{
           ValorNeg();
           return;
       }
        
    }
    private void ValorNeg(){
        if("-".equals(atual.getLexemaString())){
        andaUm();
        ValorNumerico();
        return;
        }else{
            ValorNumerico();
            return;
        }
        
    }
    
    private void ValorNumerico(){
    if("numeros".equals(atual.getTipo())){
        return;
    }else if("(".equals(atual.getLexemaString())){
        andaUm();
        ExpressaoAritimetica();
        if(")".equals(atual.getLexemaString())){
            andaUm();
            return;
        }
    }else{
        variavel();
        return;
    }
    }

    private void comandos() {
         //To change body of generated methods, choose Tools | Templates.
         if("print".equals(atual.getLexemaString())){
           andaUm();
           AnalisePrint();
           return;
       } else if("read".equals(atual.getLexemaString())){
           andaUm();
           AnaliseRead();
           return;
       }else if("while".equals(atual.getLexemaString())){
           andaUm();
           AnaliseWhile();
           return;
       }else if("if".equals(atual.getLexemaString())){
           andaUm();
           AnaliseIf();
           return;
       }else if("++".equals(proximo.getLexemaString())||"--".equals(proximo.getLexemaString())){
           Incremments();
           return;
       }else if("IDENTIFICADOR".equals(atual.getTipo()) || "global".equals(atual.getLexemaString()) 
               ||"local".equals(atual.getLexemaString())){
           andaUm(); andaUm();
           if("=".equals(proximo.getLexemaString())){
            voltaUm(); voltaUm();
            AssignmentVariable();
            return;
           }else if("OPERADOR ARITMETICO".equals(proximo.getTipo())){
            voltaUm(); voltaUm();
            ExpressaoAritimetica();
            return;  
           }
       }
     return;
       
    }

    private void AnalisePrint() {
         if("(".equals(atual.getLexemaString())){
             while(!")".equals(atual.getLexemaString())){
                 andaUm();
                 if("cadeiaDeCaracteres".equals(atual.getTipo()) || "numeros".equals(atual.getTipo()) ){
                     andaUm();
                 }
                 else{
                     variavel();
                 }
                if(",".equals(atual.getLexemaString())){
                    andaUm();
                }
             }
             return;
             
         }else{
             ErroSintatico erro = new ErroSintatico("( expected", atual.getLinha());
            saida.add(erro);
         }
    }

    private void AnaliseRead() {
       if("(".equals(atual.getLexemaString())){
             while(!")".equals(atual.getLexemaString())){
                 andaUm();
                 if("cadeiaDeCaracteres".equals(atual.getTipo()) || "numeros".equals(atual.getTipo()) ){
                     andaUm();
                 }
                 else{
                     variavel();
                 }
                if(",".equals(atual.getLexemaString())){
                    andaUm();
                }
             }
             return;
         } else{
           ErroSintatico erro = new ErroSintatico("( expected", atual.getLinha());
            saida.add(erro);
       }
    }

    private void AnaliseWhile() {
         if("(".equals(atual.getLexemaString())){
             while(!")".equals(atual.getLexemaString())){
                 andaUm();
                 Condicao();
             }
           return;
        } else{
             ErroSintatico erro = new ErroSintatico("( expected", atual.getLinha());
            saida.add(erro);
         }
    }

    private void AnaliseIf() {
        if("(".equals(atual.getLexemaString())){
            while(!")".equals(atual.getLexemaString())){
                andaUm();
                Condicao();
             }
            if("then".equals(atual.getLexemaString())){
                andaUm();
                    if("{".equals(atual.getLexemaString())){
                        while(!"}".equals(atual.getLexemaString())){
                          andaUm();
                          comandos();
                      }
                    } else{
                        ErroSintatico erro = new ErroSintatico("{ expected", atual.getLinha());
                        saida.add(erro);
                    }
            }else{
                ErroSintatico erro = new ErroSintatico("'then' expected", atual.getLinha());
                saida.add(erro);
            }
            if("else".equals(atual.getLexemaString())){
               andaUm();
               comandos(); //precisa de mais coisa aqui no else?
           }
        } else{
            ErroSintatico erro = new ErroSintatico("( expected", atual.getLinha());
            saida.add(erro);
        }
        
    }

    private void variavel() {
         if("IDENTIFICADOR".equals(atual.getTipo())){
            andaUm();
            if(".".equals(atual.getLexemaString())){
                andaUm();
               if("IDENTIFICADOR".equals(atual.getTipo())) {
                   return;
               }
            }else if("[".equals(atual.getLexemaString())){
                andaUm();
                if("IDENTIFICADOR".equals(atual.getTipo())|| "Numeros".equals(atual.getTipo())){
                    andaUm();
                    if("]".equals(atual.getLexemaString())){
                        andaUm();
                        if(!"[".equals(atual.getLexemaString())){
                            return;
                        }else{
                            andaUm();
                            if("IDENTIFICADOR".equals(atual.getTipo())|| "Numeros".equals(atual.getTipo())){
                                andaUm();
                                if("]".equals(atual.getLexemaString())){
                                    andaUm();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }else if("local".equals(atual.getLexemaString()) ||"global".equals(atual.getLexemaString())){
                andaUm();
                if(".".equals(atual.getLexemaString())){
                    andaUm();
                    if("IDENTIFICADOR".equals(atual.getTipo())) {
                        return;
                    }
            }
        }
    }

    private void Condicao() {
        if("OPERADOR RELACIONAL".equals(proximo.getTipo())){
            expressaoRel();
        }else if("boleano".equals(atual.getTipo())){
            andaUm();
            return;
        }else{ 
             expressaoLogica();   
        }
    }

    private void AssignmentVariable() {
        variavel();
        if("=".equals(atual.getLexemaString())){
            andaUm();
            if("IDENTIFICADOR".equals(atual.getTipo()) || "global".equals(atual.getLexemaString()) 
                ||"local".equals(atual.getLexemaString())){
                variavel();
                return;
            }else if("numeros".equals(atual.getTipo())||"cadeia de caracteres".equals(atual.getTipo())
                    ||"booleano".equals(atual.getTipo())){
                return;
            } else{
                ErroSintatico erro = new ErroSintatico("value expected", atual.getLinha());
                saida.add(erro);
            }
        
        } else{
            ErroSintatico erro = new ErroSintatico("= expected", atual.getLinha());
            saida.add(erro);
        }
    }

    
    private void expressaoLogica() {
        if("!".equals(atual.getLexemaString())||"(".equals(atual.getLexemaString())){
            andaUm();
            auxLogica();
         if("operador Logico".equals(atual.getTipo())){
             andaUm();
             expressaoLogica();
         }else{
             return;
         }
        }
    }
    private void auxLogica() {
        if("!".equals(atual.getLexemaString())){
            andaUm();
            if("(".equals(atual.getLexemaString())){
                while(!")".equals(atual.getLexemaString())){
                    andaUm();
                    auxLogica2();
                }
            }else{
                    auxLogica2();
                    }
        }else if("(".equals(atual.getLexemaString())){
             while(!")".equals(atual.getLexemaString())){
                    andaUm();
                    auxLogica2();
                }
        }else{
            auxLogica2();
        }
    }
    private void auxLogica2() {
        auxLogica3();
        if("operador logico".equals(atual.getTipo())){
            andaUm();
        }
        auxLogica3();
    }
    private void auxLogica3() {
        if("IDENTIFICADOR".equals(atual.getTipo())){
            variavel();
            return;
        }
        else if("boleano".equals(atual.getTipo())){
            andaUm();
            return;
        }else{
            auxOpRel();
            return;
        }   
    }
    private void auxOpRel() {
        if("!".equals(atual.getLexemaString())){
            andaUm();
            expressaoRel();
        }else{
            expressaoRel();
        }
    }

    private void expressaoRel() {
        if("cadeiaDeCaracteres".equals(atual.getTipo()) || "numeros".equals(atual.getTipo())
                || "boolean".equals(atual.getTipo())){
                andaUm();
           }
            else{
                variavel();
                andaUm();
           }
            if("OPERADOR RELACIONAL".equals(atual.getTipo())){
                andaUm();
                if("cadeiaDeCaracteres".equals(atual.getTipo()) || "numeros".equals(atual.getTipo())
                        || "boolean".equals(atual.getTipo())){
                    andaUm();
                }
                else{
                    variavel();
                    andaUm();
                }
            }
    }

} 

