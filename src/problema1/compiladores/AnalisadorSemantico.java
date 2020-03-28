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
    private ArrayList<Erro> ERROS = new ArrayList<Erro>();
    private String escopoAtual = new String();
    private Variaveis atualVar;
   
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

    public ArrayList<Erro> getERROS() {
        return ERROS;
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
    
    public ArrayList<String> Semantico(){
       ArrayList<Erro> recebe = controle();
       ArrayList<String> retorno = new ArrayList<String>();
       if(recebe.isEmpty()){
           retorno.add("Sem Erros Semanticos");
       } else{
            for(int i = 0; i < recebe.size(); i++){
                retorno.add(recebe.get(i).toStringLista());
            }
       }
       return retorno;
   }
    
    public ArrayList<String> getSaidaString(){
        ArrayList<String> arq = new ArrayList<String>();
        for(int i = 0; i< ERROS.size(); i++){
            arq.add(ERROS.get(i).toStringLista());
        }
        return arq;
    }
//    Método de controle
    public ArrayList<Erro> controle(){
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
                System.out.println("VARIAVEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEIS      " + GLOBALVAR.toString());
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
                return ERROS;
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
               System.out.println("STRUCTSSSSSSSSSSSSSSSSSSSSSSS " + STRUCTS.toString());
               if(existStruct("struct " + atual.getLexemaString())){
                   Erro e = new Erro("Struct já existente", atual.getLinha());
                   ERROS.add(e);
               }
                Structs str = new Structs("struct " + atual.getLexemaString());
                TIPO.add("struct " + atual.getLexemaString());
                STRUCTS.add(str);
                andaUm();
                Extends(str.getNome());
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
    
    private boolean existStruct(String nome){
        for(int i = 0; i< STRUCTS.size(); i++){
            if(nome.equals(STRUCTS.get(i).getNome())){
                return true;
            }
        }
        return false;
    }
    
    private void Extends(String nome) {
        if("extends".equals(atual.getLexemaString())){
             andaUm(); andaUm();
            if("IDENTIFICADOR".equals(atual.getTipo())){
                if(nome.equals("struct " + atual.getLexemaString())){
                    Erro e = new Erro("Extends mesma Struct", atual.getLinha());
                    ERROS.add(e);
                }else if(!existStruct("struct " + atual.getLexemaString())){
                    Erro e = new Erro("Struct inexistente", atual.getLinha());
                    ERROS.add(e);
                }
                if(existStruct("struct " + atual.getLexemaString())){
                    for(int i = 0; i< STRUCTS.size(); i++){
                        if(("struct "+ atual.getLexemaString()).equals(STRUCTS.get(i).getNome())){
                            for(int j = 0; j < STRUCTS.size(); j++){
                                if(nome.equals(STRUCTS.get(j).getNome())){
                                    STRUCTS.get(j).extendsVar(STRUCTS.get(i).getLocalvar());
                                }
                            }
                        }
                    }
                }
                andaUm();
                return;
            }
        }
         
    }
     
    private void attributes() {
        String tipoVar = "";
        String nomeVar = "";
        String varMat = null;
        if(TIPO.contains(atual.getLexemaString())){
            tipoVar = atual.getLexemaString();
            andaUm();
            if("IDENTIFICADOR".equals(atual.getTipo())){
                nomeVar = atual.getLexemaString();
                if("[".equals(proximo.getLexemaString())){
                    nomeVar = nomeVar + "[";
                    andaUm();andaUm();
                    if("]".equals(atual.getLexemaString())){
                        nomeVar = nomeVar + "]";
                        andaUm();
                        if("[".equals(atual.getLexemaString())){
                            nomeVar = nomeVar + "[";
                            andaUm();
                            if("]".equals(atual.getLexemaString())){
                                nomeVar = nomeVar + "]";
                                varMat = "matriz";
                            }
                        }else{
                            varMat = "vetor";

                            voltaUm();
                        }
                    }
                }
                if(existVarStruct(STRUCTS.get(STRUCTS.size() - 1), atual.getLexemaString(), anterior.getLexemaString())){
                    Erro e = new Erro("Variavel ja existente", atual.getLinha());
                    ERROS.add(e);
                }
                Variaveis var = new Variaveis(nomeVar, tipoVar);
                var.setVetMat(varMat);
                STRUCTS.get(STRUCTS.size()-1).addVar(var);
                andaUm();
                if(";".equals(atual.getLexemaString())){
                    andaUm();
                    attributes();
                }
            }
        }
    }  
    
    private boolean existVarStruct(Structs str, String nome, String tipo){
        for(int i = 0; i < str.getLocalvar().size(); i++){
            if(nome.equals(str.getLocalvar().get(i).getNome())){
                return true;
            }
        }
        
        return false;
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
                    if(!existStruct("struct " + atual.getLexemaString())){
                        Erro e = new Erro("Struct inexistente", atual.getLinha());
                        ERROS.add(e);
                    }
                    andaUm();
                    if("IDENTIFICADOR".equals(atual.getTipo())){
                        System.out.println("TYPEDEFSSSSSSSSSSSSS:: " + atual.getLexemaString());
                        if(TIPO.contains(atual.getLexemaString())){
                            Erro e = new Erro("Tipo já existente", atual.getLinha());
                            ERROS.add(e);
                        }
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
    
    private boolean existConst(String nome){
        int flag = 0;
        for(int i = 0; i< CONSTS.size(); i++){
            if(nome.equals(CONSTS.get(i).getNome())){
                flag = 1;
            }
        }
        for(int i = 0; i< GLOBALVAR.size(); i++){
            if(nome.equals(GLOBALVAR.get(i).getNome())){
                flag = 1;
            }
        }
        if(flag == 1){
            return true;
        }
        return false;
    }
    
    private void ConstantStructure(){
        String tipoVar;
        String flag = "global";
        FunctionsProcedures func = null;
        if(TIPO.contains(atual.getLexemaString())){
            tipoVar = atual.getLexemaString();
            andaUm();
            if("IDENTIFICADOR".equals(atual.getTipo()) && "=".equals(proximo.getLexemaString())){
                if(existConst(atual.getLexemaString())){
                    Erro e = new Erro("Variável/Constante já existente", atual.getLinha());
                    ERROS.add(e);
                } 
                Variaveis var = new Variaveis(atual.getLexemaString(), anterior.getLexemaString());
                CONSTS.add(var);
                andaUm();
                if("CADEIA DE CARACTERES".equals(proximo.getTipo()) || "NUMERO".equals(proximo.getTipo())
                        || "true".equals(proximo.getLexemaString()) || "false".equals(proximo.getLexemaString())){
                   andaUm();
                   if("string".equals(tipoVar)){
                        if("IDENTIFICADOR".equals(atual.getTipo())){
                            if(!containsVar(flag, func, atual.getLexemaString(), tipoVar)){
                                Erro e = new Erro("Atribuição tipo diferente - string", atual.getLinha());
                                ERROS.add(e);
                            }
                        }else if(!"CADEIA DE CARACTERES".equals(atual.getTipo())){
                            Erro e = new Erro("Atribuição tipo diferente - string", atual.getLinha());
                            ERROS.add(e);
                        } 
                    }else if("boolean".equals(tipoVar)){
                        if("IDENTIFICADOR".equals(atual.getTipo())){
                            if(!containsVar(flag, func, atual.getLexemaString(), tipoVar)){
                                Erro e = new Erro("Atribuição tipo diferente - boolean", atual.getLinha());
                                ERROS.add(e);
                            }
                        }else if(!"true".equals(atual.getLexemaString()) ||
                                !"false".equals(atual.getLexemaString())){
                            Erro e = new Erro("Atribuição tipo diferente - boolean", atual.getLinha());
                            ERROS.add(e);
                        }
                    }else if("int".equals(tipoVar) ){
                         if("IDENTIFICADOR".equals(atual.getTipo())){
                            if(!containsVar(flag, func, atual.getLexemaString(), tipoVar)){
                                Erro e = new Erro("Atribuição tipo diferente - int", atual.getLinha());
                                ERROS.add(e);
                            }
                        }else if(!"NUMERO".equals(atual.getTipo()) ||
                                ( "NUMERO".equals(atual.getTipo()) && atual.getLexemaString().contains("."))){
                            Erro e = new Erro("Atribuição tipo diferente - int", atual.getLinha());
                            ERROS.add(e);
                        }
                    }else if("real".equals(tipoVar)){
                        if("IDENTIFICADOR".equals(atual.getTipo())){
                            if(!containsVar(flag, func, atual.getLexemaString(), tipoVar)){
                                Erro e = new Erro("Atribuição tipo diferente - real", atual.getLinha());
                                ERROS.add(e);
                            }
                        } else if(!"NUMERO".equals(atual.getTipo()) ||
                                ( "NUMERO".equals(atual.getTipo()) && !atual.getLexemaString().contains("."))){
                            Erro e = new Erro("Atribuição tipo diferente - real", atual.getLinha());
                            ERROS.add(e);

                        }
                    } 
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
            complementV(tipo, func, anterior.getLexemaString());
        }
    }
    
    private void verificaVariavelEscopo(String tipo, FunctionsProcedures func, Variaveis var){
        for(int i = 0; i< CONSTS.size(); i++){
            if(var.getNome().equals(CONSTS.get(i).getNome())){
                Erro e = new Erro("Variavel já existente no escopo", atual.getLinha());
                ERROS.add(e);
            }
        }
        if("global".equals(tipo)){
            for(int i = 0; i<GLOBALVAR.size(); i++){
                if(var.getNome().equals(GLOBALVAR.get(i).getNome())){
                    Erro e = new Erro("Variavel já existente no escopo", atual.getLinha());
                    ERROS.add(e);
                }
            }
            } else if("start".equals(tipo)){
                for(int i = 0; i<STARTVAR.size(); i++){
                    if(var.getNome().equals(STARTVAR.get(i).getNome())){
                        Erro e = new Erro("Variavel já existente no escopo", atual.getLinha());
                        ERROS.add(e);
                    }
                }
            } else if("func".equals(tipo)){
                for(int i = 0; i<func.getLocalvar().size(); i++){
                    if(var.getNome().equals(func.getLocalvar().get(i).getNome())){
                        Erro e = new Erro("Variavel já existente no escopo", atual.getLinha());
                        ERROS.add(e);
                    }
                }
            }
    }
    
    private boolean containsVar(String flag, FunctionsProcedures func, String nome, String tipo){
        if("global".equals(flag)){
            for(int i = 0; i< GLOBALVAR.size(); i++){
                if(nome.equals(GLOBALVAR.get(i).getNome()) && tipo.equals(GLOBALVAR.get(i).getTipo())){
                    return true;
                }
            }
        }else if("start".equals(flag)){
            for(int i = 0; i< STARTVAR.size(); i++){
                if(nome.equals(STARTVAR.get(i).getNome()) && tipo.equals(GLOBALVAR.get(i).getTipo())){
                    return true;
                }
            }
        }else if("func".equals(flag)){
            for(int i = 0; i< func.getLocalvar().size(); i++){
                if(nome.equals(func.getLocalvar().get(i).getNome()) && tipo.equals(GLOBALVAR.get(i).getTipo())){
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private void complementV(String tipo, FunctionsProcedures func, String tipoVar) {
        String flag = "";
        String nomeVar ="";
        String varMat = null;
        if("IDENTIFICADOR".equals(atual.getTipo())){
            nomeVar = atual.getLexemaString();
            if("[".equals(proximo.getLexemaString())){
                nomeVar = nomeVar + "[";
                andaUm();andaUm();
                if("]".equals(atual.getLexemaString())){
                    nomeVar = nomeVar + "]";
                    andaUm();
                    if("[".equals(atual.getLexemaString())){
                        nomeVar = nomeVar + "[";
                        andaUm();
                        if("]".equals(atual.getLexemaString())){
                            nomeVar = nomeVar + "]";
                            varMat = "matriz";
                        }
                    }else{
                        varMat = "vetor";

                        voltaUm();
                    }
                }
            }
            Variaveis var = new Variaveis(nomeVar, tipoVar);
            var.setVetMat(varMat);
            if("global".equals(tipo)){
                flag = "global";
                verificaVariavelEscopo(tipo, func, var);
                GLOBALVAR.add(var);
            } else if("start".equals(tipo)){
                flag = "start";
                verificaVariavelEscopo(tipo, func, var);
                STARTVAR.add(var);
            } else if("func".equals(tipo)){
                flag = "func";
                verificaVariavelEscopo(tipo, func, var);
                func.addVar(var);
            }
            if("=".equals(proximo.getLexemaString())){
                andaUm();
                if("CADEIA DE CARACTERES".equals(proximo.getTipo()) || "NUMERO".equals(proximo.getTipo())
                        || "true".equals(proximo.getLexemaString()) || "false".equals(proximo.getLexemaString())
                        || "IDENTIFICADOR".equals(proximo.getTipo())){
                    andaUm();
                    if("string".equals(tipoVar)){
                        if("IDENTIFICADOR".equals(atual.getTipo())){
                            if(!containsVar(flag, func, atual.getLexemaString(), tipoVar)){
                                Erro e = new Erro("Atribuição tipo diferente - string", atual.getLinha());
                                ERROS.add(e);
                            }
                        }else if(!"CADEIA DE CARACTERES".equals(atual.getTipo())){
                            Erro e = new Erro("Atribuição tipo diferente - string", atual.getLinha());
                            ERROS.add(e);
                        } 
                    }else if("boolean".equals(tipoVar)){
                        if("IDENTIFICADOR".equals(atual.getTipo())){
                            if(!containsVar(flag, func, atual.getLexemaString(), tipoVar)){
                                Erro e = new Erro("Atribuição tipo diferente - boolean", atual.getLinha());
                                ERROS.add(e);
                            }
                        }else if(!("true".equals(atual.getLexemaString()) ||
                                "false".equals(atual.getLexemaString()))){
                            Erro e = new Erro("Atribuição tipo diferente - boolean", atual.getLinha());
                            ERROS.add(e);
                        }
                    }else if("int".equals(tipoVar) ){
                         if("IDENTIFICADOR".equals(atual.getTipo())){
                            if(!containsVar(flag, func, atual.getLexemaString(), tipoVar)){
                                Erro e = new Erro("Atribuição tipo diferente - int", atual.getLinha());
                                ERROS.add(e);
                            }
                        }else if(!"NUMERO".equals(atual.getTipo()) ||
                                ( "NUMERO".equals(atual.getTipo()) && atual.getLexemaString().contains("."))){
                            Erro e = new Erro("Atribuição tipo diferente - int", atual.getLinha());
                            ERROS.add(e);
                        }
                    }else if("real".equals(tipoVar)){
                        if("IDENTIFICADOR".equals(atual.getTipo())){
                            if(!containsVar(flag, func, atual.getLexemaString(), tipoVar)){
                                Erro e = new Erro("Atribuição tipo diferente - real", atual.getLinha());
                                ERROS.add(e);
                            }
                        } else if(!"NUMERO".equals(atual.getTipo()) ||
                                ( "NUMERO".equals(atual.getTipo()) && !atual.getLexemaString().contains("."))){
                            Erro e = new Erro("Atribuição tipo diferente - real", atual.getLinha());
                            ERROS.add(e);
                            
                        }
                    } 
                    andaUm();
                    varEqType(tipo, func, tipoVar);
               }
            }else{
                andaUm();
                varEqType(tipo, func, tipoVar);
            }
        }
    }

    private void varEqType(String tipo, FunctionsProcedures func, String tipoVar) {
        if(",".equals(atual.getLexemaString())){
            andaUm();
            complementV(tipo, func, tipoVar);
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
        escopoAtual = "func";
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
                        if(compareFuncProc(func)){
                            Erro e = new Erro("Função/Procedimento já existente", atual.getLinha());
                            ERROS.add(e);
                        }
                        AnaliseVariavel("func", func);
                        andaUm();
                        if("return".equals(atual.getLexemaString())){
                            retorno(func);
//                            andaUm();
                        }else{
                            comandos(null);
                            retorno(func);
//                            andaUm();
                        }
                        andaUm();
                        FUNCTIONS.add(func);
                        andaUm();
                        return;
                    }
                }
            }
        }
    }
    
    private boolean compareFuncProc(FunctionsProcedures func){
        for(int i = 0; i< FUNCTIONS.size(); i++){
            if(FUNCTIONS.get(i).compareTo(func)){
                return true;
            }
        }
        for(int i = 0; i< PROCEDURES.size(); i++){
            if(PROCEDURES.get(i).compareTo(func)){
                return true;
            }
        }
        return false;
    }
    
    private void retorno(FunctionsProcedures func){
        boolean isvar;
        boolean erro = false;
        if("return".equals(atual.getLexemaString())){
            andaUm();
            isvar = variavel(func);
            if(isvar){
                if("matriz".equals(atualVar.getVetMat())){
                    if(!func.getRetorno().equals(atualVar.getTipo()+"[][]")){
                        erro = true;
                    }
                }else if("vetor".equals(atualVar.getVetMat())){
                    if((!func.getRetorno().equals(atualVar.getTipo() +"[]"))){
                        erro = true;
                    }
                }else if(!atualVar.getTipo().equals(func.getRetorno())){
                    erro = true;
                }
            }else{
                if("NUMERO".equals(atual.getTipo())){
                    if(atual.getLexemaString().contains(".")){
                        if(!func.getRetorno().equals("real")){
                            erro = true;
                        }
                    }else{
                        if(!func.getRetorno().equals("int")){
                            erro = true;
                        }
                    }
                }else if("CADEIA DE CARACTERES".equals(atual.getTipo())){
                    if(!func.getRetorno().equals("string")){
                        erro = true;
                    }
                }else if("true".equals(atual.getLexemaString()) || "false".equals(atual.getLexemaString())){
                    if(!func.getRetorno().equals("boolean")){
                        erro = true;
                    }
                }
            }
        }
        
        if(erro){
            Erro e = new Erro("Tipo do retorno diferente", atual.getLinha());
            ERROS.add(e);
        }
    }
    
    private FunctionsProcedures DeclaraParam(FunctionsProcedures func){
        String nomeVar ="";
        String varMat = null;
        String tipoVar = "";
        if(TIPO.contains(atual.getLexemaString())){
            tipoVar = atual.getLexemaString();
            andaUm();
            if("IDENTIFICADOR".equals(atual.getTipo())){
                nomeVar = atual.getLexemaString();
                if("[".equals(proximo.getLexemaString())){
                    nomeVar = nomeVar + "[";
                    andaUm();andaUm();
                    if("]".equals(atual.getLexemaString())){
                        nomeVar = nomeVar + "]";
                        andaUm();
                        if("[".equals(atual.getLexemaString())){
                            nomeVar = nomeVar + "[";
                            andaUm();
                            if("]".equals(atual.getLexemaString())){
                                nomeVar = nomeVar + "]";
                                varMat = "matriz";
                            }
                        }else{
                            varMat = "vetor";
                            voltaUm();
                        }
                    }
                }
                Variaveis var = new Variaveis(nomeVar, tipoVar);
                var.setVetMat(varMat);
                verificaParametro(func, var);
                func.addParametro(var);
                func.addVar(var);
                if(",".equals(proximo.getLexemaString())){
                    andaUm();
                    DeclaraParam(func);
                }
            }
        }else if(")".equals(atual.getLexemaString())){
        }
        return func;
    }
    
    private void verificaParametro(FunctionsProcedures func, Variaveis var){
        for(int i = 0; i< func.getParametro().size(); i++){
            if(var.getNome().equals(func.getParametro().get(i).getNome())){
                Erro e = new Erro("Variável do parametro já existente", atual.getLinha());
                ERROS.add(e);
            }
        }
    }
    
    private void AnaliseProcedures() {
        if("DELIMITADOR".equals(proximo.getTipo()) && "{".equals(proximo.getLexemaString())){
            andaUm(); andaUm();
            while(!"}".equals(atual.getLexemaString())){
                procedures();
//                andaUm();
            }
        }
    }
    
    private void procedures(){
        escopoAtual = "func";
        FunctionsProcedures proc;
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
                    if(compareFuncProc(proc)){
                        Erro e = new Erro("Função/Procedimento já existente", atual.getLinha());
                        ERROS.add(e);
                    }
                    AnaliseVariavel("func", proc);
                    andaUm();
                    comandos(null);
                    andaUm();
                    PROCEDURES.add(proc);
                    andaUm();
                    return;
                }
            }

        }
    }
    
    
//    Método de Start()
    private void AnaliseStart() {
        escopoAtual = "start";
        FunctionsProcedures start= new FunctionsProcedures("start", null);
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
                        System.out.println("devia entrar em comandos aqui " +start.getNome());
                       comandos(start);
                       andaUm();
                    }
                }
            }
        }
    }
    
     private void comandos(FunctionsProcedures func) {
         System.out.println("entrou COMANDOS");
        if("print".equals(atual.getLexemaString())){
           System.out.println("entrou print" +func.getNome());
            andaUm();
           AnalisePrint(func);
           
           return;
        }else if("read".equals(atual.getLexemaString())){
           System.out.println("entrou read" +func.getNome());
           andaUm();
           AnaliseRead(func);
           return;
        }else if("while".equals(atual.getLexemaString())){
            System.out.println("entrou while" +func.getNome());
           andaUm();
           AnaliseWhile(func);
           return;
        }else if("if".equals(atual.getLexemaString())){
           andaUm();
           AnaliseIf(func);
           return;
        }else if("++".equals(proximo.getLexemaString())||"--".equals(proximo.getLexemaString())){
           Incremments(func);
           return;
        }else if(  "global".equals(atual.getLexemaString()) 
               ||"local".equals(atual.getLexemaString())){
           andaUm(); andaUm();
           if("=".equals(proximo.getLexemaString())){
               System.out.println("entrou atribucao" +func.getNome());
            voltaUm(); voltaUm();
            AssignmentVariable(func);
            return;
        }else if("OPERADOR ARITMETICO".equals(proximo.getTipo())){
            voltaUm(); voltaUm();
            ExpressaoAritimetica(func);
            return;  
        }
       }else if("IDENTIFICADOR".equals(atual.getTipo())){
         System.out.println("entrou chamada de func" +func.getNome());
         chFunProc();
     }
     return;
       
    }
    private boolean analiseSemStruct(Variaveis var){
        boolean existeAtributoStruct=false;
        
        String tipo = var.getTipo();
        Structs str = null;
        for(int i = 0; i<STRUCTS.size(); i++){
            if(STRUCTS.get(i).getNome().equals(tipo)){
                str = STRUCTS.get(i);
                for(int j = 0;j< str.getLocalvar().size(); j++){
                    if(atual.getLexemaString().equals(str.getLocalvar().get(j).getNome())){
                        existeAtributoStruct = true;
                    }
                }
            }
        }
        
        return existeAtributoStruct;
    }
      private boolean variavel(FunctionsProcedures func) {
        String escopoAcesso = "";
         if("local".equals(atual.getLexemaString()) ||"global".equals(atual.getLexemaString())){
            escopoAcesso = atual.getLexemaString();
                andaUm();
                if(".".equals(atual.getLexemaString())){
                    andaUm();
                    if("IDENTIFICADOR".equals(atual.getTipo())) {
                        return analiseSemVar(func, escopoAcesso);
                        
                    }
            }
        }
         return false;
    }
      
    private boolean varMatrizVetor(FunctionsProcedures func, String escopoAcesso, String vetMat){

        boolean existe  =false;
        Variaveis var = null;
        
        andaUm();
        if("vetor".equals(vetMat)){
            if("[".equals(atual.getLexemaString())){
                andaUm();
                if("]".equals(atual.getLexemaString())){
                    if("[".equals(proximo.getLexemaString())){
                        return false;
                    }
                    return true;
                }else if(("NUMERO".equals(atual.getTipo()) && !atual.getLexemaString().contains("."))
                || "local".equals(atual.getLexemaString()) || "global".equals(atual.getLexemaString())){
                    if("local".equals(atual.getLexemaString()) || "global".equals(atual.getLexemaString())){
                        variavel(func);
                        if(!"int".equals(atualVar.getTipo())){
                            Erro e = new Erro("Tipo do Acesso diferente", atual.getLinha());
                            ERROS.add(e);
                        }
                        andaUm();
                        if("]".equals(atual.getLexemaString())){
                            if("[".equals(proximo.getLexemaString())){
                                return false;
                            }
                            return true;
                        }
                    }else{
                        andaUm();
                        if("]".equals(atual.getLexemaString())){
                            if("[".equals(proximo.getLexemaString())){
                                return false;
                            }
                            return true;
                        }
                    }
                }else{
                    Erro e = new Erro("Tipo do Acesso diferente", atual.getLinha());
                    ERROS.add(e);
                    andaUm();
                    if("]".equals(atual.getLexemaString())){
                            if("[".equals(proximo.getLexemaString())){
                                return false;
                            }
                            return true;
                        }
                }
            }
        }else if("matriz".equals(vetMat)){
            if("[".equals(atual.getLexemaString())){
                andaUm();
                if("]".equals(atual.getLexemaString())){
                    if("[".equals(proximo.getLexemaString())){
                            andaUm(); andaUm();
                            if("]".equals(atual.getLexemaString())){
                                if("[".equals(proximo.getLexemaString())){
                                    return false;
                                }
                                return true;
                            }else if(("NUMERO".equals(atual.getTipo()) && !atual.getLexemaString().contains("."))
                            || "local".equals(atual.getLexemaString()) || "global".equals(atual.getLexemaString())){
                                if("local".equals(atual.getLexemaString()) || "global".equals(atual.getLexemaString())){
                                    variavel(func);
                                    if(!"int".equals(atualVar.getTipo())){
                                        Erro e = new Erro("Tipo do Acesso diferente", atual.getLinha());
                                        ERROS.add(e);
                                    }
                                    andaUm();
                                    if("]".equals(atual.getLexemaString())){
                                         if("[".equals(proximo.getLexemaString())){
                                            return false;
                                        }
                                        return true;
                                    }
                                }else{
                                    andaUm();
                                    if("]".equals(atual.getLexemaString())){
                                        if("[".equals(proximo.getLexemaString())){
                                            return false;
                                        }
                                        return true;
                                    }
                                }
                            }else{
                                Erro e = new Erro("Tipo do Acesso diferente", atual.getLinha());
                                ERROS.add(e);
                                andaUm();
                                if("]".equals(atual.getLexemaString())){
                                    if("[".equals(proximo.getLexemaString())){
                                        return false;
                                    }
                                    return true;
                                }
                            }
                    }else{
                        return false;
                    }
                }else if(("NUMERO".equals(atual.getTipo()) && !atual.getLexemaString().contains("."))
                || "local".equals(atual.getLexemaString()) || "global".equals(atual.getLexemaString())){
                    if("local".equals(atual.getLexemaString()) || "global".equals(atual.getLexemaString())){
                        variavel(func);
                        if(!"int".equals(atualVar.getTipo())){
                            Erro e = new Erro("Tipo do Acesso diferente", atual.getLinha());
                            ERROS.add(e);
                        }
                        andaUm();
                        if("]".equals(atual.getLexemaString())){
                            if("[".equals(proximo.getLexemaString())){
                                andaUm(); andaUm();
                                if("]".equals(atual.getLexemaString())){
                                    if("[".equals(proximo.getLexemaString())){
                                        return false;
                                    }
                                    return true;
                                }else if(("NUMERO".equals(atual.getTipo()) && !atual.getLexemaString().contains("."))
                                || "local".equals(atual.getLexemaString()) || "global".equals(atual.getLexemaString())){
                                    if("local".equals(atual.getLexemaString()) || "global".equals(atual.getLexemaString())){
                                        variavel(func);
                                        if(!"int".equals(atualVar.getTipo())){
                                            Erro e = new Erro("Tipo do Acesso diferente", atual.getLinha());
                                            ERROS.add(e);
                                        }
                                        andaUm();
                                        if("]".equals(atual.getLexemaString())){
                                             if("[".equals(proximo.getLexemaString())){
                                                return false;
                                            }
                                            return true;
                                        }
                                    }else{
                                        andaUm();
                                        if("]".equals(atual.getLexemaString())){
                                            if("[".equals(proximo.getLexemaString())){
                                                return false;
                                            }
                                            return true;
                                        }
                                    }
                                }else{
                                    Erro e = new Erro("Tipo do Acesso diferente", atual.getLinha());
                                    ERROS.add(e);
                                    andaUm();
                                    if("]".equals(atual.getLexemaString())){
                                        if("[".equals(proximo.getLexemaString())){
                                            return false;
                                        }
                                        return true;
                                    }
                                }
                            }
                            return true;
                        }
                    }else{
                        andaUm();
                        if("]".equals(atual.getLexemaString())){
                            if("[".equals(proximo.getLexemaString())){
                                andaUm(); andaUm();
                                if("]".equals(atual.getLexemaString())){
                                    if("[".equals(proximo.getLexemaString())){
                                        return false;
                                    }
                                    return true;
                                }else if(("NUMERO".equals(atual.getTipo()) && !atual.getLexemaString().contains("."))
                                || "local".equals(atual.getLexemaString()) || "global".equals(atual.getLexemaString())){
                                    if("local".equals(atual.getLexemaString()) || "global".equals(atual.getLexemaString())){
                                        variavel(func);
                                        if(!"int".equals(atualVar.getTipo())){
                                            Erro e = new Erro("Tipo do Acesso diferente", atual.getLinha());
                                            ERROS.add(e);
                                        }
                                        andaUm();
                                        if("]".equals(atual.getLexemaString())){
                                             if("[".equals(proximo.getLexemaString())){
                                                return false;
                                            }
                                            return true;
                                        }
                                    }else{
                                        andaUm();
                                        if("]".equals(atual.getLexemaString())){
                                            if("[".equals(proximo.getLexemaString())){
                                                return false;
                                            }
                                            return true;
                                        }
                                    }
                                }else{
                                    Erro e = new Erro("Tipo do Acesso diferente", atual.getLinha());
                                    ERROS.add(e);
                                    andaUm();
                                    if("]".equals(atual.getLexemaString())){
                                        if("[".equals(proximo.getLexemaString())){
                                            return false;
                                        }
                                        return true;
                                    }
                                }
                            }
                            return true;
                        }
                    }
                }else{
                    Erro e = new Erro("Tipo do Acesso diferente", atual.getLinha());
                    ERROS.add(e);
                    andaUm();
                    if("]".equals(atual.getLexemaString())){
                        if("[".equals(proximo.getLexemaString())){
                                andaUm(); andaUm();
                                if("]".equals(atual.getLexemaString())){
                                    if("[".equals(proximo.getLexemaString())){
                                        return false;
                                    }
                                    return true;
                                }else if(("NUMERO".equals(atual.getTipo()) && !atual.getLexemaString().contains("."))
                                || "local".equals(atual.getLexemaString()) || "global".equals(atual.getLexemaString())){
                                    if("local".equals(atual.getLexemaString()) || "global".equals(atual.getLexemaString())){
                                        variavel(func);
                                        if(!"int".equals(atualVar.getTipo())){
                                            Erro x = new Erro("Tipo do Acesso diferente", atual.getLinha());
                                            ERROS.add(x);
                                        }
                                        andaUm();
                                        if("]".equals(atual.getLexemaString())){
                                             if("[".equals(proximo.getLexemaString())){
                                                return false;
                                            }
                                            return true;
                                        }
                                    }else{
                                        andaUm();
                                        if("]".equals(atual.getLexemaString())){
                                            if("[".equals(proximo.getLexemaString())){
                                                return false;
                                            }
                                            return true;
                                        }
                                    }
                                }else{
                                    Erro y = new Erro("Tipo do Acesso diferente", atual.getLinha());
                                    ERROS.add(y);
                                    andaUm();
                                    if("]".equals(atual.getLexemaString())){
                                        if("[".equals(proximo.getLexemaString())){
                                            return false;
                                        }
                                        return true;
                                    }
                                }
                            }
                            return true;
                    }
                }
            }
        }else{
            return false;
        }
        return false;
    }
      
    
    private void auxMatriz(){
        
    }

    private boolean analiseSemVar(FunctionsProcedures func, String escopoAcesso){
        boolean existe  =false;
        Variaveis var = null;

        if(escopoAcesso.equals("global")){
            for(int i=0;i<GLOBALVAR.size();i++){
                if(GLOBALVAR.get(i).getNome().equals(atual.getLexemaString())){
                    existe=true;
                    var = GLOBALVAR.get(i);
                    atualVar = GLOBALVAR.get(i);
                }else if(GLOBALVAR.get(i).getNome().startsWith(atual.getLexemaString()) && 
                           "[".equals(proximo.getLexemaString())){
                    if("[".equals(proximo.getLexemaString())){
                        existe = varMatrizVetor(func, escopoAcesso, GLOBALVAR.get(i).getVetMat());
                        atualVar = GLOBALVAR.get(i);
                    }
                }
            }
            for(int i = 0; i< CONSTS.size(); i++){
                if(CONSTS.get(i).getNome().equals(atual.getLexemaString())){
                    existe=true;
                    var = CONSTS.get(i);
                    atualVar = CONSTS.get(i);
                }else if(CONSTS.get(i).getNome().startsWith(atual.getLexemaString())  && 
                           "[".equals(proximo.getLexemaString())){
                    if("[".equals(proximo.getLexemaString())){
                        existe = varMatrizVetor(func, escopoAcesso, CONSTS.get(i).getVetMat());
                        atualVar = CONSTS.get(i);
                    }
                }
            }
        }else if(escopoAcesso.equals("local")){
            if(escopoAtual.equals("func")){
                for(int i = 0; i< func.getLocalvar().size(); i++){
                    if(atual.getLexemaString().equals(func.getLocalvar().get(i).getNome())){
                        existe=true;
                        var = func.getLocalvar().get(i);
                        atualVar = func.getLocalvar().get(i);
                    }else if(func.getLocalvar().get(i).getNome().startsWith(atual.getLexemaString()) && 
                           "[".equals(proximo.getLexemaString())){
                        if("[".equals(proximo.getLexemaString())){
                            existe = varMatrizVetor(func, escopoAcesso, func.getLocalvar().get(i).getVetMat());
                            atualVar = func.getLocalvar().get(i);
                        }
                    }
                }
            }else if(escopoAtual.equals("start")){
                for(int i=0;i<STARTVAR.size();i++){
                    if(STARTVAR.get(i).getNome().equals(atual.getLexemaString())){
                        existe=true;
                        var = STARTVAR.get(i);
                        atualVar = STARTVAR.get(i);
                    }else if(STARTVAR.get(i).getNome().startsWith(atual.getLexemaString()) && 
                           "[".equals(proximo.getLexemaString())){
                        if("[".equals(proximo.getLexemaString())){
                            existe = varMatrizVetor(func, escopoAcesso, STARTVAR.get(i).getVetMat());
                            atualVar = STARTVAR.get(i);
                        }
                    }
                }
            }
        }
        andaUm();
        if(".".equals(atual.getLexemaString()) && existe == true){
            andaUm();
            if(!analiseSemStruct(var)){
                Erro e = new Erro("Variável de struct não existente", atual.getLinha());
                ERROS.add(e);
                return false;
            }
        }else{
            voltaUm();
        }
        if(!existe){
            System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee " + existe);
            Erro e = new Erro("Variável não existente no escopo", atual.getLinha());
            ERROS.add(e);
            return false;
        }
        return true;
    }
    private void AnalisePrint(FunctionsProcedures func) {
        String tipoStruct;
        Erro e;
        System.out.println("entrou print");
        if("(".equals(atual.getLexemaString())){
            while(!")".equals(atual.getLexemaString())){
                andaUm();
                if("CADEIA DE CARACTERES".equals(atual.getTipo()) || "NUMERO".equals(atual.getTipo()) ){
                    andaUm();
                }
                else if(  "global".equals(atual.getLexemaString()) 
              ||"local".equals(atual.getLexemaString())){
                    
                    variavel(func);
                    for(int i=0;i<STRUCTS.size();i++){
                        tipoStruct=STRUCTS.get(i).getNome();
                        if(atualVar.getTipo().equals(tipoStruct))
                        {
                             e=new Erro("impossivel imprimir Struct", atual.getLinha());
                             ERROS.add(e);
                        }
                    }
                }

               if(",".equals(atual.getLexemaString())){
                   andaUm();
               }
            }
            return;
             
         }
    }
    
       private void AnaliseRead(FunctionsProcedures func) {
        String tipoStruct;
        Erro e;
        if("(".equals(atual.getLexemaString())){
             while(!")".equals(atual.getLexemaString())){
                 andaUm();
                 if("CADEIA DE CARACTERES".equals(atual.getTipo()) || "NUMERO".equals(atual.getTipo()) ){
                     andaUm();
                 }
                 else if("IDENTIFICADOR".equals(atual.getTipo()) || "global".equals(atual.getLexemaString()) 
               ||"local".equals(atual.getLexemaString())){
                     variavel(func);
                        for(int i=0;i<STRUCTS.size();i++){
                         tipoStruct = STRUCTS.get(i).getNome();
                        if(atualVar.getTipo().equals(tipoStruct))
                            {
                              e = new Erro("impossivel ler Struct", atual.getLinha());
                             ERROS.add(e);
                            }
                        }
                    }
                
                if(",".equals(atual.getLexemaString())){
                    andaUm();
                }
             }
             return;
         }
    }
       
    private void AnaliseIf(FunctionsProcedures func) {
        if("(".equals(atual.getLexemaString())){
            while(!")".equals(atual.getLexemaString())){
                andaUm();
                Condicao(func);
             }
            if("then".equals(atual.getLexemaString())){
                andaUm();
                    if("{".equals(atual.getLexemaString())){
                        while(!"}".equals(atual.getLexemaString())){
                          andaUm();
                          comandos(null);
                      }
                    }
            }
            if("else".equals(atual.getLexemaString())){
               andaUm();
               comandos(func); //precisa de mais coisa aqui no else?
           }
        }
    }
    
    private void Condicao(FunctionsProcedures func) {
        Erro e;
        if("OPERADOR RELACIONAL".equals(proximo.getTipo())){
            expressaoRel(func);
        }else if("true".equals(atual.getLexemaString())|| "false".equals(atual.getLexemaString())){
            andaUm();
            return;
        }else if("!".equals(atual.getLexemaString())||"(".equals(atual.getLexemaString())){ 
             expressaoLogica(func);   
        }
        else if("global".equals(atual.getLexemaString())|| "local".equals(atual.getLexemaString())){
            variavel(func);
            if(!(atualVar.getTipo().equals("boolean"))){
                e= new Erro("condição esperada, boolean ou expressão", atual.getLinha());
                ERROS.add(e);
                 return;
                         }
        }
    }
    
    private void AnaliseWhile(FunctionsProcedures func) {
        if("(".equals(atual.getLexemaString())){
             while(!")".equals(atual.getLexemaString())){
                 andaUm();
                 Condicao(func);
                  if("{".equals(atual.getLexemaString())){
                        while(!"}".equals(atual.getLexemaString())){
                          andaUm();
                          comandos(null);
                      }
                    }
             }
           return;
        }
    }
    
    private void ExpressaoAritimetica(FunctionsProcedures func){
        if("+".equals(proximo.getLexemaString())||"-".equals(proximo.getLexemaString())){
          ExpressaoAritimetica(func);
          andaUm(); andaUm();
          MultExp(func);
        }else {
            MultExp(func);
            return;
        }
    }
    private void MultExp(FunctionsProcedures func){
       if("*".equals(proximo.getLexemaString())|| "/".equals(proximo.getLexemaString())){
           MultExp(func);
           andaUm(); andaUm();
           ValorNeg(func);
       }
       else{
           ValorNeg(func);
           return;
       }
        
    }
    private void ValorNeg(FunctionsProcedures func){
        if("-".equals(atual.getLexemaString())){
        andaUm();
        ValorNumerico(func);
        return;
        }else{
            ValorNumerico(func);
            return;
        }
    }
    
    private void ValorNumerico(FunctionsProcedures func){
        Erro e=null;
        if("NUMERO".equals(atual.getTipo())){
            return;
        }else if("(".equals(atual.getLexemaString())){
            andaUm();
            
            ExpressaoAritimetica(func);
            if(")".equals(atual.getLexemaString())){
                andaUm();
                return;
            }
        }else{
            variavel(func);
            if(!atualVar.getTipo().equals("int")||!atualVar.getTipo().equals("real")){
              e= new Erro("uma expressão aritimetica so aceita numeros reais ou inteiros", atual.getLinha());
              ERROS.add(e);
            return;
            }
        }
    }
    
    private void AssignmentVariable(FunctionsProcedures func) {
        
        variavel(func);
        Variaveis v1 = atualVar;
        if("=".equals(atual.getLexemaString())){
            andaUm();
            if("IDENTIFICADOR".equals(atual.getTipo()) || "global".equals(atual.getLexemaString()) 
                ||"local".equals(atual.getLexemaString())){
                variavel(func);
                atribuicaoVar(v1,atualVar);
                return;
            }else if("NUMERO".equals(atual.getTipo())||"CADEIA DE CARACTERES".equals(atual.getTipo())
                    ||"true".equals(atual.getLexemaString())|| "false".equals(atual.getLexemaString())){
                atribuicaoValor(v1, atual);
                return;
            }
        }
    }
    private boolean atribuicaoVar(Variaveis v1, Variaveis v2){
        if(!(CONSTS.contains(v1))){
        return(v1.getTipo().equals(v2.getTipo()));
        }
        return false;
    }
     private boolean atribuicaoValor(Variaveis v, Tokens Valor){
        if(Valor.equals("CADEIA DE CARACTERES") && v.getTipo().equals("string")){
            return true;
        }
        else if(Valor.getTipo().equals("NUMERO") && v.getTipo().equals("int")){
            return true;
        }else if(Valor.getTipo().equals("NUMERO") && v.getTipo().equals("real")){
            return true;
        }else if((Valor.getLexemaString().equals("true") && v.getTipo().equals("boolean"))|| (Valor.getLexemaString().equals("false") && v.getTipo().equals("boolean"))){
            return true;
        }
        return false;
    }
    
    private void expressaoLogica(FunctionsProcedures func) {
        if("!".equals(atual.getLexemaString())||"(".equals(atual.getLexemaString())){
            andaUm();
            auxLogica(func);
         if("OPERADOR LOGICO".equals(atual.getTipo())){
             andaUm();
             expressaoLogica(func);
         }else{
             return;
         }
        }
    }
    private void auxLogica(FunctionsProcedures func) {
        if("!".equals(atual.getLexemaString())){
            andaUm();
            if("(".equals(atual.getLexemaString())){
                while(!")".equals(atual.getLexemaString())){
                    andaUm();
                    auxLogica2(func);
                }
            }else{
                auxLogica2(func);
            }
        }else if("(".equals(atual.getLexemaString())){
             while(!")".equals(atual.getLexemaString())){
                    andaUm();
                    auxLogica2(func);
                }
        }else{
            auxLogica2(func);
        }
    }
    private void auxLogica2(FunctionsProcedures func) {
        
        auxLogica3(func);
        if("OPERADOR LOGICO".equals(atual.getTipo())){
            andaUm();
        }
        auxLogica3(func);
    }
    private void auxLogica3(FunctionsProcedures func) {
        
        if( "global".equals(atual.getLexemaString()) 
            ||"local".equals(atual.getLexemaString())){
            variavel(func);
            if(!atualVar.getTipo().equals("boolean"))
                
            return;
        }
        else if("true".equals(atual.getLexemaString())||"false".equals(atual.getLexemaString())){
            andaUm();
            return;
        }else{
            auxOpRel(func);
            return;
        }  
        return;
    }
    private void auxOpRel(FunctionsProcedures func) {
        if("!".equals(atual.getLexemaString())){
            andaUm();
            expressaoRel(func);
        }else{
            expressaoRel(func);
        }
    }

    private void expressaoRel(FunctionsProcedures func) {
        String tipo1=null, tipo2=null;
        Erro e;
        if("CADEIA DE CARACTERES".equals(atual.getTipo())){
                tipo1="string";
            andaUm();
        }else if("NUMERO".equals(atual.getTipo())){
                if(atual.getLexemaString().contains("."))
                    tipo1="real";
                else
                    tipo1="int";
        }else if("true".equals(atual.getLexemaString())||"false".equals(atual.getLexemaString())){
                tipo1="boolean";
        }else if(  "global".equals(atual.getLexemaString()) 
            ||"local".equals(atual.getLexemaString())){
            variavel(func);
            tipo1=atualVar.getTipo();
            andaUm();
       }
        
       if("OPERADOR RELACIONAL".equals(atual.getTipo())){
            andaUm();
             if("CADEIA DE CARACTERES".equals(atual.getTipo())){
                tipo2="string";
            andaUm();
            }else if("NUMERO".equals(atual.getTipo())){
                if(atual.getLexemaString().contains("."))
                    tipo2="real";
                else
                    tipo2="int";
            }else if("true".equals(atual.getLexemaString())||"false".equals(atual.getLexemaString())){
                tipo2="boolean";
            }else if(  "global".equals(atual.getLexemaString()) 
            ||"local".equals(atual.getLexemaString())){
            variavel(func);
            tipo2=atualVar.getTipo();
            andaUm();
            }
        }
       if(!tipo1.equals(tipo2)){
           e=new Erro("comparação entre tipos diferentes", atual.getLinha());
           ERROS.add(e);
       }
    }
    
    private void Incremments(FunctionsProcedures func){
        if("IDENTIFICADOR".equals(atual.getTipo())){
            andaUm();
            
            variavel(func);
            incrementoPermitido(atualVar);
            incremment();
            andaUm();
            if(";".equals(atual.getLexemaString())){
                return;
            }
        }
    }
     private boolean incrementoPermitido(Variaveis v){
        return(v.getTipo().equals("int")||v.getTipo().equals("real"));
        
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
