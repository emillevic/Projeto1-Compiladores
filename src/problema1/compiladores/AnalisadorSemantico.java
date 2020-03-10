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
public class AnalisadorSemantico {
    private ArrayList<Tokens> codigoTratado;
    private ArrayList<String> TIPO = new ArrayList<String>();
    
    private ArrayList<Structs> STRUCTS = new ArrayList<Structs>();
    private ArrayList<Variaveis> CONSTS = new ArrayList<Variaveis>();
    private ArrayList<Variaveis> GLOBALVAR = new ArrayList<Variaveis>();
    private ArrayList<Variaveis> STARTVAR = new ArrayList<Variaveis>();
    private ArrayList<FunctionsProcedures> FUNCTIONS = new ArrayList<FunctionsProcedures>();
    private ArrayList<FunctionsProcedures> PROCEDURES = new ArrayList<FunctionsProcedures>();
    private ArrayList<Variaveis> LOCALVAR;
   
    private Tokens atual;
    private Tokens anterior;
    private Tokens proximo;
    private int indice;
    
    
    public AnalisadorSemantico(ArrayList<Tokens> codigoTratado){
        this.codigoTratado = codigoTratado;
        this.indice = 0;
        
       TIPO.add("int");
       TIPO.add("real");
       TIPO.add("boolean");
       TIPO.add("string");
    }

    public ArrayList<String> getTIPO() {
        return TIPO;
    }

    public ArrayList<Structs> getSTRUCTS() {
        return STRUCTS;
    }

    public ArrayList<Variaveis> getCONSTS() {
        return CONSTS;
    }

    public ArrayList<Variaveis> getGLOBALVAR() {
        return GLOBALVAR;
    }

    public ArrayList<Variaveis> getSTARTVAR() {
        return STARTVAR;
    }

    public ArrayList<FunctionsProcedures> getFUNCTIONS() {
        return FUNCTIONS;
    }

    public ArrayList<FunctionsProcedures> getPROCEDURES() {
        return PROCEDURES;
    }

    public ArrayList<Variaveis> getLOCALVAR() {
        return LOCALVAR;
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
//    Método de controle
    public void controle(){
        System.out.println("entrei aqui");
        atual= codigoTratado.get(indice);
        proximo= codigoTratado.get(indice+1);
        while(true){
                                System.out.println(atual.getLexemaString() + " linha: " + atual.getLinha());

            if("structs".equals(atual.getLexemaString())){
                System.out.println("struct--------------------");
                AnaliseStruct();
            }else if("typedefs".equals(atual.getLexemaString())){
                System.out.println("typedefs--------------------");
               AnaliseTypedef();
            }else if("const".equals(atual.getLexemaString())){
                System.out.println("const---------------");
                AnaliseConst();
            }else if("var".equals(atual.getLexemaString())){
                System.out.println("var------------------");
                AnaliseVariavel("global", null);
            }else if("functions".equals(atual.getLexemaString())){
                System.out.println("functions---------------------");
                AnaliseFunctions();
            }else if("procedures".equals(atual.getLexemaString())){
                System.out.println("procedures------------------");
                AnaliseProcedures();
            }else if("start".equals(atual.getLexemaString())){
                System.out.println("start----------------");
                AnaliseStart();
            }

            if(indice+2 < codigoTratado.size())
                andaUm();
            else
                return;
        }
    }
    
//    Módulo de Structs
     private void AnaliseStruct() {
        if("DELIMITADOR".equals(proximo.getTipo()) && "{".equals(proximo.getLexemaString())){
            andaUm();andaUm();
            while(!"}".equals(atual.getLexemaString())){
                auxStruct();
                andaUm();
            }
            return;     
        }
    }
    
    private void auxStruct(){
        if("struct".equals(atual.getLexemaString())){
            andaUm();
           if("IDENTIFICADOR".equals(atual.getTipo()) ){
                Structs str = new Structs("struct " + atual.getLexemaString());
                TIPO.add("struct " + atual.getLexemaString());
                STRUCTS.add(str);
                andaUm();
                Extends();
               if("{".equals(atual.getLexemaString())){
                    andaUm();
                    attributes();
                   if("}".equals(atual.getLexemaString()) && ";".equals(proximo.getLexemaString())){
                       andaUm();
                        return;
                   }
               }
           }
       }
    }
    
    private void Extends() {
        if("extends".equals(atual.getLexemaString())){
             andaUm();
            if("IDENTIFICADOR".equals(atual.getTipo())){
                 andaUm();
                 return;
            }
        }
         
    }
     
    private void attributes() {
        if(TIPO.contains(atual.getLexemaString())){
            andaUm();
            if("IDENTIFICADOR".equals(atual.getTipo())){
                Variaveis var = new Variaveis(atual.getLexemaString(), anterior.getLexemaString());
                STRUCTS.get(STRUCTS.size()-1).addVar(var);
                andaUm();
                if(";".equals(atual.getLexemaString())){
                    andaUm();
                    attributes();
                }
            }
        }
    }  

    
//    Método de typedefs
    
    private void AnaliseTypedef() {
         //To change body of generated methods, choose Tools | Templates.
        if("DELIMITADOR".equals(proximo.getTipo()) && "{".equals(proximo.getLexemaString())){
            andaUm(); andaUm();
            while(!"}".equals(atual.getLexemaString())){
                typedef();
                andaUm();
            } 
            return;
        }
    }

    private void typedef() {      
         if("typedef".equals(atual.getLexemaString())){
             andaUm();
             if("struct".equals(atual.getLexemaString())){
                 andaUm();
                 if("IDENTIFICADOR".equals(atual.getTipo())){
                     andaUm();
                     if("IDENTIFICADOR".equals(atual.getTipo())){
                        for(int i = 0; i< STRUCTS.size(); i++){
                            if(STRUCTS.get(i).getNome().equals("struct " + anterior.getLexemaString())){
                                STRUCTS.get(i).setNome(atual.getLexemaString());
                            }
                        }
                        for(int i = 0; i< TIPO.size(); i++){
                            if(TIPO.get(i).equals("struct " + anterior.getLexemaString())){
                                TIPO.set(i, atual.getLexemaString());
                            }
                         }
                        andaUm();
                        if(";".equals(atual.getLexemaString())){
                            return;
                        }
                    }
                }
            }
        }
    }
    
    //    Método de constantes

    private void AnaliseConst() {
        if("DELIMITADOR".equals(proximo.getTipo()) && "{".equals(proximo.getLexemaString())){
            andaUm();
            while(!"}".equals(atual.getLexemaString())){
                andaUm();
                ConstantStructure();
            }
        } 
    }
    
    private void ConstantStructure(){
        if(TIPO.contains(atual.getLexemaString())){
            andaUm();
            if("IDENTIFICADOR".equals(atual.getTipo()) && "=".equals(proximo.getLexemaString())){
                Variaveis var = new Variaveis(atual.getLexemaString(), anterior.getLexemaString());
                CONSTS.add(var);
                andaUm();
                if("CADEIA DE CARACTERES".equals(proximo.getTipo()) || "NUMERO".equals(proximo.getTipo())
                        || "true".equals(proximo.getLexemaString()) || "false".equals(proximo.getLexemaString())){
                   andaUm();
                    if(";".equals(proximo.getLexemaString())){
                        
                    }
                }
            }
        }
    }
    
//    Método de variáveis
//    Tipo: global, start, func
    private void AnaliseVariavel(String tipo, FunctionsProcedures func) {
        if("DELIMITADOR".equals(proximo.getTipo()) && "{".equals(proximo.getLexemaString())){
            andaUm(); andaUm();
           while(!"}".equals(atual.getLexemaString())){
               VarV(tipo, func);
               andaUm();
           }
        }
    }
    private void VarV(String tipo, FunctionsProcedures func) {
        if(TIPO.contains(atual.getLexemaString())){
            andaUm();
            complementV(tipo, func);
        }
    }

    private void complementV(String tipo, FunctionsProcedures func) {
        if("IDENTIFICADOR".equals(atual.getTipo())){
            Variaveis var = new Variaveis(atual.getLexemaString(), anterior.getLexemaString());
            if(tipo == "global"){
                GLOBALVAR.add(var);
            } else if(tipo == "start"){
                STARTVAR.add(var);
            } else if(tipo == "func"){
                func.addVar(var);
            }
            if("=".equals(proximo.getLexemaString())){
                andaUm();
                if("CADEIA DE CARACTERES".equals(proximo.getTipo()) || "NUMERO".equals(proximo.getTipo())
                        || "true".equals(proximo.getLexemaString()) || "false".equals(proximo.getLexemaString())
                        || "IDENTIFICADOR".equals(proximo.getTipo())){
                    andaUm(); andaUm();
                    varEqType(tipo, func);
               }
            }else{
                andaUm();
                varEqType(tipo, func);
            }
        }
    }

    private void varEqType(String tipo, FunctionsProcedures func) {
        if(",".equals(atual.getLexemaString())){
            andaUm();
            complementV(tipo, func);
        }
        else if(";".equals(atual.getLexemaString())){
            return;
        }
    }
    
//    Método de funções e procedures
    
    private void AnaliseFunctions() {
        if("{".equals(proximo.getLexemaString())){
            andaUm(); andaUm();
            while(!"}".equals(atual.getLexemaString())){
                function();
                andaUm();
            }
        }
    }
    
    private void function(){
        FunctionsProcedures func;
        if("function".equals(atual.getLexemaString())){
            andaUm();
            if(TIPO.contains(atual.getLexemaString())){
                andaUm();
                if("IDENTIFICADOR".equals(atual.getTipo())){
                    func = new FunctionsProcedures(atual.getLexemaString(), anterior.getLexemaString());
                    andaUm();
                    if("(".equals(atual.getLexemaString())){
                        while(!")".equals(atual.getLexemaString())){
                           andaUm();
                           func = DeclaraParam(func);
                        }
                        AnaliseVariavel("func", func);
                        andaUm();
                        if("return".equals(atual.getLexemaString())){
                            retorno();
                            andaUm();
                        }else{
                            comandos();
                            retorno();
                            andaUm();
                        }
                        FUNCTIONS.add(func);
                        return;
                    }
                }
            }
        }
    }
    private void retorno(){
        if("return".equals(atual.getLexemaString())){
            andaUm();
//            variavel();
        }
    }
    
    private FunctionsProcedures DeclaraParam(FunctionsProcedures func){
        if(TIPO.contains(atual.getLexemaString())){
            andaUm();
            if("IDENTIFICADOR".equals(atual.getTipo())){
                Variaveis var = new Variaveis(atual.getLexemaString(), anterior.getLexemaString());
                func.addParametro(var);
                if(",".equals(proximo.getLexemaString())){
                    andaUm();
                    DeclaraParam(func);
                }
            }
        }else if(")".equals(atual.getLexemaString())){
        }
        return func;
    }
    
    private void AnaliseProcedures() {
        if("DELIMITADOR".equals(proximo.getTipo()) && "{".equals(proximo.getLexemaString())){
            andaUm(); andaUm();
            while(!"}".equals(atual.getLexemaString())){
                procedures();
                andaUm();
//                comandos();
                return;
            }
        }
    }
    
    private void procedures(){
        FunctionsProcedures proc;
                    System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa " + atual.getLexemaString());
        if("procedure".equals(atual.getLexemaString())){
            andaUm();
            if("IDENTIFICADOR".equals(atual.getTipo())){
                proc = new FunctionsProcedures(atual.getLexemaString(), null);
                andaUm();
                if("(".equals(atual.getLexemaString())){
                    while(!")".equals(atual.getLexemaString())){
                       andaUm();
                       proc = DeclaraParam(proc);
                    }
                    AnaliseVariavel("func", proc);
                    andaUm();
                    comandos();
                    andaUm();
                    PROCEDURES.add(proc);
                    return;
                }
            }

        }
    }
    
    
//    Método de Start()
    private void AnaliseStart() {
         if("DELIMITADOR".equals(proximo.getTipo()) && "(".equals(proximo.getLexemaString())){
           andaUm();
            if("DELIMITADOR".equals(proximo.getTipo()) && ")".equals(proximo.getLexemaString())){
                andaUm();
                if("DELIMITADOR".equals(proximo.getTipo()) && "{".equals(proximo.getLexemaString())){
                    andaUm(); andaUm();
                    if("var".equals(atual.getLexemaString()) ){
                            AnaliseVariavel("start", null);
                            andaUm();
                       }
                    while( !"}".equals(atual.getLexemaString())){
//                       comandos();
                       andaUm();
                    }
                }
            }
        }
    }
    
     private void comandos() {
        if("print".equals(atual.getLexemaString())){
           andaUm();
           AnalisePrint();
           return;
        }else if("read".equals(atual.getLexemaString())){
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
    private boolean analiseSemStruct(){
        boolean existeStruct=false;
        boolean existeAtributo=false;
        Tokens struct;
        Tokens atributo;
        struct=atual;
       atributo= codigoTratado.get( indice +2);
       
       int qtdVar;
        for(int i=0;i<STRUCTS.size();i++){
                if(STRUCTS.get(i).getNome().equals(atual.getLexemaString())){
                    existeStruct=true;
                }
            }
            if(!existeStruct){
                ErroSintatico erro = new ErroSintatico("struct não existe", atual.getLinha());
            }else{
                for(int j=0;j<STRUCTS.size();j++){
                if(STRUCTS.get(j).getNome().equals(atual.getLexemaString())){
                    qtdVar =STRUCTS.get(j).getLocalvar().size();
                    for(int k=0;k<(qtdVar); k++){
                        if(STRUCTS.get(j).getLocalvar().get(k).getNome().equals(atributo.getLexemaString())){
                            existeAtributo=true;
                        }
                    }
                }
            }
        }
            return existeStruct && existeAtributo;
    }
      private void variavel() {
          
         if("IDENTIFICADOR".equals(atual.getTipo())){
             analiseSemStruct();
             andaUm();
            
            if(".".equals(atual.getLexemaString())){
                andaUm();
               if("IDENTIFICADOR".equals(atual.getTipo())) {
                   return;
               }
            }else if("[".equals(atual.getLexemaString())){
                andaUm();
                if("IDENTIFICADOR".equals(atual.getTipo())|| "NUMERO".equals(atual.getTipo())){
                    andaUm();
                    if("]".equals(atual.getLexemaString())){
                        andaUm();
                        if(!"[".equals(atual.getLexemaString())){
                            return;
                        }else{
                            andaUm();
                            if("IDENTIFICADOR".equals(atual.getTipo())|| "NUMERO".equals(atual.getTipo())){
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
                analiseSemVar();
                andaUm();
                if(".".equals(atual.getLexemaString())){
                    andaUm();
                    if("IDENTIFICADOR".equals(atual.getTipo())) {
                        return;
                    }
            }
        }
    }
    private void analiseSemVar(){
        boolean existe  =false;
        Tokens variavel = codigoTratado.get( indice +2);
        Tokens escopo=atual;
        if(escopo.getLexemaString().equals("global")){
        for(int i=0;i<GLOBALVAR.size();i++){
                if(GLOBALVAR.get(i).getNome()==atual.getLexemaString()){
                    existe=true;
                }
            }
        }else if(escopo.getLexemaString().equals("local")){
           for(int i=0;i<LOCALVAR.size();i++){
                if(LOCALVAR.get(i).getNome()==atual.getLexemaString()){
                    existe=true;
                }
            } 
        }
    }
    private void AnalisePrint() {
        if("(".equals(atual.getLexemaString())){
            while(!")".equals(atual.getLexemaString())){
                andaUm();
                if("CADEIA DE CARACTERES".equals(atual.getTipo()) || "NUMERO".equals(atual.getTipo()) ){
                    andaUm();
                }
                else if("IDENTIFICADOR".equals(atual.getTipo()) || "global".equals(atual.getLexemaString()) 
              ||"local".equals(atual.getLexemaString())){
                    variavel();
                }

               if(",".equals(atual.getLexemaString())){
                   andaUm();
               }
            }
            return;
             
         }
    }
    
       private void AnaliseRead() {
       if("(".equals(atual.getLexemaString())){
             while(!")".equals(atual.getLexemaString())){
                 andaUm();
                 if("CADEIA DE CARACTERES".equals(atual.getTipo()) || "NUMERO".equals(atual.getTipo()) ){
                     andaUm();
                 }
                 else if("IDENTIFICADOR".equals(atual.getTipo()) || "global".equals(atual.getLexemaString()) 
               ||"local".equals(atual.getLexemaString())){
                     variavel();
                 }
                if(",".equals(atual.getLexemaString())){
                    andaUm();
                }
             }
             return;
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
                    }
            }
            if("else".equals(atual.getLexemaString())){
               andaUm();
               comandos(); //precisa de mais coisa aqui no else?
           }
        }
    }
    
    private void Condicao() {
        if("OPERADOR RELACIONAL".equals(proximo.getTipo())){
            expressaoRel();
        }else if("true".equals(atual.getLexemaString())|| "false".equals(atual.getLexemaString())){
            andaUm();
            return;
        }else if("!".equals(atual.getLexemaString())||"(".equals(atual.getLexemaString())){ 
             expressaoLogica();   
        }
    }
    
    private void AnaliseWhile() {
        if("(".equals(atual.getLexemaString())){
             while(!")".equals(atual.getLexemaString())){
                 andaUm();
                 Condicao();
                  if("{".equals(atual.getLexemaString())){
                        while(!"}".equals(atual.getLexemaString())){
                          andaUm();
                          comandos();
                      }
                    }
             }
           return;
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
        if("NUMERO".equals(atual.getTipo())){
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
    
    private void AssignmentVariable() {
        variavel();
        if("=".equals(atual.getLexemaString())){
            andaUm();
            if("IDENTIFICADOR".equals(atual.getTipo()) || "global".equals(atual.getLexemaString()) 
                ||"local".equals(atual.getLexemaString())){
                variavel();
                return;
            }else if("NUMERO".equals(atual.getTipo())||"CADEIA DE CARACTERES".equals(atual.getTipo())
                    ||"true".equals(atual.getLexemaString())|| "false".equals(atual.getLexemaString())){
                return;
            }
        }
    }

    
    private void expressaoLogica() {
        if("!".equals(atual.getLexemaString())||"(".equals(atual.getLexemaString())){
            andaUm();
            auxLogica();
         if("OPERADOR LOGICO".equals(atual.getTipo())){
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
        if("OPERADOR LOGICO".equals(atual.getTipo())){
            andaUm();
        }
        auxLogica3();
    }
    private void auxLogica3() {
        if("IDENTIFICADOR".equals(atual.getTipo())|| "global".equals(atual.getLexemaString()) 
            ||"local".equals(atual.getLexemaString())){
            variavel();
            return;
        }
        else if("true".equals(atual.getLexemaString())||"false".equals(atual.getLexemaString())){
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
        if("CADEIA DE CARACTERES".equals(atual.getTipo()) || "NUMERO".equals(atual.getTipo())
                || "true".equals(atual.getLexemaString())||"false".equals(atual.getLexemaString())){
                andaUm();
        }else if("IDENTIFICADOR".equals(atual.getTipo()) || "global".equals(atual.getLexemaString()) 
            ||"local".equals(atual.getLexemaString())){
            variavel();
            andaUm();
       }
        
       if("OPERADOR RELACIONAL".equals(atual.getTipo())){
            andaUm();
            if("CADEIA DE CARACTERES".equals(atual.getTipo()) || "NUMERO".equals(atual.getTipo())
                    || "true".equals(atual.getLexemaString()) || "false".equals(atual.getLexemaString()) ){
                andaUm();
            }
            else if("IDENTIFICADOR".equals(atual.getTipo()) || "global".equals(atual.getLexemaString()) 
            ||"local".equals(atual.getLexemaString())){
                variavel();
                andaUm();
            }
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
            }
        }
    }

    private void incremment() {
        //To change body of generated methods, choose Tools | Templates.
        if("++".equals(atual.getLexemaString())||"--".equals(atual.getLexemaString())){
            return;
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
                    }
                }
            }
        }
        
    }

    private void chParam() {
        //To change body of generated methods, choose Tools | Templates.
        if("IDENTIFICADOR".equals(atual.getTipo())){
            andaUm();
            chParam2();
            return;
        }else if("CADEIA DE CARACTERES".equals(atual.getTipo()) || "NUMERO".equals(atual.getTipo())
                || "true".equals(atual.getLexemaString()) || "false".equals(atual.getLexemaString())){
            andaUm();
            chParam2();
            return;
        }
    }

    private void chParam2() {
         //To change body of generated methods, choose Tools | Templates.
         if(",".equals(atual.getLexemaString())){
             andaUm();
            if("IDENTIFICADOR".equals(atual.getTipo())){
                 andaUm();
                 chParam2();
            }else if("CADEIA DE CARACTERES".equals(atual.getTipo()) || "NUMERO".equals(atual.getTipo())
                     || "true".equals(atual.getLexemaString())|| "false".equals(atual.getLexemaString())){
                 
                 andaUm();
                 chParam2();
             }
         }
    }
}
