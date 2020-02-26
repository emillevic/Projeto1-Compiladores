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
   private ArrayList<String> Tipo;
   
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
       
   }
   
   public ArrayList<String> AnalisePrograma (){
       atual= codigoTratado.get(indice);
       proximo= codigoTratado.get(indice+1);
       if((atual.getLexema()).toString()=="const"){
           AnaliseConst();
       } else if((atual.getLexema()).toString()=="START"){
           AnaliseStart();
       }else if((atual.getLexema()).toString()=="var"){
           AnaliseVariavel();
       }else if((atual.getLexema()).toString()=="structs"){
           AnaliseStruct();
       }else if((atual.getLexema()).toString()=="typedefs"){
           AnaliseTypedef();
       }else if((atual.getLexema()).toString()=="functions"){
           AnaliseFunctions();
       }else if((atual.getLexema()).toString()=="procedures"){
           AnaliseProcedures();
       }
       return null;
       
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
        if(proximo.getTipo()=="DELIMITADOR" && proximo.getLexema().toString() == "{"){
            andaUm();
            while(atual.getTipo()!="DELIMITADOR" && atual.getLexema().toString()!= "}"){
                andaUm();
                ConstantStructure();
            }
                
        }
    }
    
    private void ConstantStructure(){
        if(Tipo.contains(atual.getLexema().toString())){
            andaUm();
            if(atual.getTipo() == "IDENTIFICADOR" && proximo.getLexema().toString() == "="){
                andaUm();
                
                if(proximo.getTipo() == "cadeiaDeCaracteres" || proximo.getTipo()
                        == "numeros" || proximo.getTipo() == "boolean"){
                   andaUm();
                    if(proximo.getLexema().toString() == ";"){
                        
                    }
                }
            }
        }
    }

    private void AnaliseStart() {
         if(proximo.getTipo()=="DELIMITADOR" && proximo.getLexema().toString() == "("){
           andaUm();
            if(proximo.getTipo() == "DELIMITADOR" && proximo.getLexema().toString() == ")"){
                andaUm();
                if(proximo.getTipo()=="DELIMITADOR" && proximo.getLexema().toString() == "{"){
                    
                    while(atual.getTipo()!="DELIMITADOR" && atual.getLexema().toString()!= "}"){
                       andaUm();
                       if(atual.getLexema().toString() == "var" )
                       AnaliseVariavel();
                       comandos();
                       
                    }
            }
         }
           
                
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
           if(proximo.getTipo()=="DELIMITADOR" && proximo.getLexema().toString() == "{")
        {
            while(atual.getTipo()!="DELIMITADOR" && atual.getLexema().toString()!= "}"){
                andaUm();
                VarV();
            }
                
        }
    }
    private void VarV() {
        //To change body of generated methods, choose Tools | Templates.
        if(Tipo.contains(atual.getLexema().toString())){
            andaUm();
            complementV();
            VarV();
        }
    }

    private void complementV() {
        //To change body of generated methods, choose Tools | Templates.
        if(atual.getTipo()=="IDENTIFICADOR" ){
            if(proximo.getLexema().toString()==","){
                andaUm();
                varEqType();
            }else if(proximo.getLexema().toString()=="="){
                andaUm();
                if(proximo.getTipo() == "cadeiaDeCaracteres" || proximo.getTipo()
                       == "numeros" || proximo.getTipo() == "boolean"){
                  andaUm();
                   if(proximo.getLexema().toString() == ";"){

                   }
               }
            }
        }
         
    }

    private void varEqType() {
        //To change body of generated methods, choose Tools | Templates.
        complementV();
        if(proximo.getLexema().toString()==";"){
            
        }
        
    }

    private void AnaliseStruct() {
         //To change body of generated methods, choose Tools | Templates.
           if(proximo.getTipo()=="DELIMITADOR" && proximo.getLexema().toString() == "{")
        {
            andaUm();
            while(atual.getTipo()!="DELIMITADOR" && atual.getLexema().toString()!= "}"){
                andaUm();
                 if((atual.getLexema()).toString()=="struct"){
                     andaUm();
                     if(atual.getTipo() == "IDENTIFICADOR" ){
                         andaUm();
                         Extends();
                         if(atual.getLexema().toString()=="{"){
                             attributes();
                             if(atual.getLexema().toString()=="}"){
                                 andaUm();
                                 if(atual.getLexema().toString()==";"){
                                     return;
                                 }
                             }
                        }
                         
                     }
                }
                
            }
                
        }
    }
     private void Extends() {
         if((atual.getLexema()).toString()=="extends"){
             andaUm();
             if(atual.getTipo()=="IDENTIFICADOR"){
                 andaUm();
                 return;
             }
             
         }
         
    }

    private void attributes() {
        if(Tipo.contains(atual.getLexema().toString())){
            andaUm();
            if(atual.getTipo()=="IDENTIFICADOR"){
                andaUm();
                if(atual.getLexema().toString()==";"){
                    andaUm();
                    attributes();
                }
            }
        }
    }   
        
        
    

    private void AnaliseTypedef() {
         //To change body of generated methods, choose Tools | Templates.
           if(proximo.getTipo()=="DELIMITADOR" && proximo.getLexema().toString() == "{")
               andaUm();
        {
            while(atual.getTipo()!="DELIMITADOR" && atual.getLexema().toString()!= "}"){
                typedef();
                
            }
                
        }
    }

    private void typedef() {
         //To change body of generated methods, choose Tools | Templates.
         if((atual.getLexema()).toString()=="typedef"){
             andaUm();
             if((atual.getLexema()).toString()=="struct"){
                 andaUm();
                 if(atual.getTipo()=="IDENTIFICADOR"){
                     andaUm();
                     if(atual.getTipo()=="IDENTIFICADOR"){
                         andaUm();
                        if(atual.getLexema().toString()==";"){
                            andaUm();
                            return;
                        }
                    }
                }
            }
        }
    }
    
    private void Incremments(){
        if(atual.getTipo()=="IDENTIFICADOR"){
            andaUm();
            variavel();
            incremment();
            andaUm();
            if(atual.getLexema().toString()==";"){
                return;
            }
        }
    }

    private void incremment() {
        //To change body of generated methods, choose Tools | Templates.
        if(atual.getLexema().toString()=="++"||atual.getLexema().toString()=="--"){
            return;
        }
    }
    private void chFunProc(){
        if(atual.getTipo()=="IDENTIFICADOR"){
            andaUm();
            if(atual.getLexema().toString()=="("){
                andaUm();
                chParam();
                andaUm();
                if(atual.getLexema().toString()==")"){
                    andaUm();
                    if(atual.getLexema().toString() == ";"){
                        return;
                    }
                }
                    
            }
        }
        
    }

    private void chParam() {
        //To change body of generated methods, choose Tools | Templates.
        if(atual.getTipo()=="IDENTIFICADOR"){
            andaUm();
            chParam2();
            return;
        }
        else if(atual.getTipo() == "cadeiaDeCaracteres" || atual.getTipo()
                        == "numeros" || atual.getTipo() == "boolean"){
            andaUm();
            chParam2();
            return;
        }
    }

    private void chParam2() {
         //To change body of generated methods, choose Tools | Templates.
         if(atual.getLexema().toString() == ","){
             andaUm();
             if(atual.getTipo()=="IDENTIFICADOR"){
                 andaUm();
                 chParam2();
             }
             else if(atual.getTipo() == "cadeiaDeCaracteres" || atual.getTipo()
                        == "numeros" || atual.getTipo() == "boolean"){
                 
                 andaUm();
                 chParam2();
             }
         }
    }
    private void AnaliseFunctions() {
        if(proximo.getTipo()=="DELIMITADOR" && proximo.getLexema().toString() == "{")
            andaUm();
        {
            while(atual.getTipo()!="DELIMITADOR" && atual.getLexema().toString()!= "}"){
                if(atual.getLexema().toString()=="int"||atual.getLexema().toString()=="real"||
                        atual.getLexema().toString()=="boolean"||atual.getLexema().toString()=="string"){
                    andaUm();
                    if(atual.getTipo()== "IDENTIFICADOR"){
                        andaUm();
                        if(atual.getLexema().toString()=="("){
                            while(atual.getLexema().toString()!=")"){
                               andaUm();
                               DeclaraParam();
                            }
                            AnaliseVariavel();
                            andaUm();
                            comandos();
                            return;
                        }
                    }
                }
            }
        }
    }
    
    private void DeclaraParam(){
        if(atual.getLexema().toString()=="int"||atual.getLexema().toString()=="real"||
                        atual.getLexema().toString()=="boolean"||atual.getLexema().toString()=="string"){
            andaUm();
            if(atual.getTipo()=="IDENTIFICADOR"){
                if(proximo.getLexema().toString()==","){
                    andaUm();
                    DeclaraParam();
                }else{
                    return;
                }
            }
        }
    }
    
    private void AnaliseProcedures() {
        if(proximo.getTipo()=="DELIMITADOR" && proximo.getLexema().toString() == "{")
            andaUm();
        {
            while(atual.getTipo()!="DELIMITADOR" && atual.getLexema().toString()!= "}"){
                AnaliseVariavel();
                andaUm();
                comandos();
                return;
            }
        }
    }
    
    private void ExpressaoAritimetica(){
        if(proximo.getLexema().toString()=="+"||proximo.getLexema().toString()=="-"){
          ExpressaoAritimetica();
          andaUm(); andaUm();
          MultExp();
        }else {
            MultExp();
            return;
        }
    }
    private void MultExp(){
       if(proximo.getLexema().toString()=="*"|| proximo.getLexema().toString()=="/"){
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
        if(atual.getLexema().toString()=="-"){
        andaUm();
        ValorNumerico();
        return;
        }else{
            ValorNumerico();
            return;
        }
        
    }
    
    private void ValorNumerico(){
    if(atual.getTipo()=="numeros"){
        return;
    }else if(atual.getLexema().toString()=="("){
        andaUm();
        ExpressaoAritimetica();
        if(atual.getLexema().toString()==")"){
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
         if((atual.getLexema()).toString()=="print"){
           andaUm();
           AnalisePrint();
           return;
       } else if((atual.getLexema()).toString()=="read"){
           andaUm();
           AnaliseRead();
           return;
       }else if((atual.getLexema()).toString()=="while"){
           andaUm();
           AnaliseWhile();
           return;
       }else if((atual.getLexema()).toString()=="if"){
           andaUm();
           AnaliseIf();
           return;
       }else if(proximo.getLexema().toString()=="++"||proximo.getLexema().toString()=="--"){
           Incremments();
           return;
       }else if(atual.getTipo()=="IDENTIFICADOR" || atual.getLexema().toString()=="global" 
               ||atual.getLexema().toString()=="local"){
           andaUm(); andaUm();
           if(proximo.getLexema().toString()=="="){
           voltaUm(); voltaUm();
           AssignmentVariable();
           return;
           }else if(proximo.getTipo()=="operador aritimético"){
           voltaUm(); voltaUm();
           ExpressaoAritimetica();
           return;  
           }
       }
     return;
       
    }

    private void AnalisePrint() {
         if(atual.getLexema().toString() =="("){
             while(atual.getLexema().toString()!=")"){
                 andaUm();
                 if(atual.getTipo()=="cadeiaDeCaracteres" || atual.getTipo()
                        == "numeros" ){
                     andaUm();
                 }
                 else{
                     variavel();
                 }
                if(atual.getLexema().toString() ==","){
                    andaUm();
                }
             }
             return;
             
         }
    }

    private void AnaliseRead() {
       if(atual.getLexema().toString() =="("){
             while(atual.getLexema().toString()!=")"){
                 andaUm();
                 if(atual.getTipo()=="cadeiaDeCaracteres" || atual.getTipo()
                        == "numeros" ){
                     andaUm();
                 }
                 else{
                     variavel();
                 }
                if(atual.getLexema().toString() ==","){
                    andaUm();
                }
             }
             return;
         }
    }

    private void AnaliseWhile() {
         if(atual.getLexema().toString() =="("){
             while(atual.getLexema().toString()!=")"){
                 andaUm();
                 Condicao();
             }
           return;
        }
    }

    private void AnaliseIf() {
        if(atual.getLexema().toString() =="("){
            while(atual.getLexema().toString()!=")"){
                andaUm();
                Condicao();
             }
            if(atual.getLexema().toString() =="then"){
                andaUm();
                    if(atual.getLexema().toString() =="{"){
                        while(atual.getLexema().toString()!="}"){
                          andaUm();
                          comandos();
                      }
                    }
                }
            if(atual.getLexema().toString() =="else"){
               andaUm();
               comandos();
           }
        }
        
    }

    private void variavel() {
         if(atual.getTipo()=="IDENTIFICADOR"){
            andaUm();
            if(atual.getLexema().toString()=="."){
                andaUm();
               if(atual.getTipo()=="IDENTIFICADOR") {
                   return;
               }
            }else if(atual.getLexema().toString()=="["){
                andaUm();
                if(atual.getTipo()=="IDENTIFICADOR"|| atual.getTipo()=="Numeros"){
                    andaUm();
                    if(atual.getLexema().toString()=="]"){
                        andaUm();
                        if(atual.getLexema().toString()!="["){
                            return;
                        }else{
                            andaUm();
                            if(atual.getTipo()=="IDENTIFICADOR"|| atual.getTipo()=="Numeros"){
                                andaUm();
                                if(atual.getLexema().toString()=="]"){
                                    andaUm();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }else if(atual.getLexema().toString()=="local" ||atual.getLexema().toString()=="global"){
                andaUm();
                if(atual.getLexema().toString()=="."){
                    andaUm();
                    if(atual.getTipo()=="IDENTIFICADOR") {
                        return;
                    }
            }
        }
    }

    private void Condicao() {
        if(proximo.getTipo()=="Operador relacional"){
            expressaoRel();
        }else if(atual.getTipo()=="boleano"){
            andaUm();
            return;
        }else{ 
             expressaoLogica();   
            
        }
    }

    private void AssignmentVariable() {
        variavel();
        if(atual.getLexema().toString()=="="){
            andaUm();
            if(atual.getTipo()=="IDENTIFICADOR" || atual.getLexema().toString()=="global" 
                ||atual.getLexema().toString()=="local"){
                variavel();
                return;
            }else if(atual.getTipo() == "numeros"||atual.getTipo() == "cadeia de caracteres"||atual.getTipo() == "booleano"){
                return;
            }
        
        }
    }

    
    private void expressaoLogica() {
        if(atual.getLexema().toString()=="!"||atual.getLexema().toString()=="("){
            andaUm();
            auxLogica();
         if(atual.getTipo()=="operador Logico"){
             andaUm();
             expressaoLogica();
         }else{
             return;
         }
        }
    }
    private void auxLogica() {
        if(atual.getLexema().toString()=="!"){
            andaUm();
            if(atual.getLexema().toString()=="("){
                while(atual.getLexema().toString()!=")"){
                    andaUm();
                    auxLogica2();
                }
            }else{
                    auxLogica2();
                    }
        }else if(atual.getLexema().toString()=="("){
             while(atual.getLexema().toString()!=")"){
                    andaUm();
                    auxLogica2();
                }
        }else{
            auxLogica2();
        }
    }
    private void auxLogica2() {
        auxLogica3();
        if(atual.getTipo() == "operador logico"){
            andaUm();
        }
        auxLogica3();
    }
    private void auxLogica3() {
        if(atual.getTipo() == "IDENTIFICADOR"){
            variavel();
            return;
        }
        else if(atual.getTipo() == "boleano"){
            andaUm();
            return;
        }else{
            auxOpRel();
            return;
        }   
    }
    private void auxOpRel() {
        if(atual.getLexema().toString()=="!"){
            andaUm();
            expressaoRel();
        }else{
            expressaoRel();
        }
    }

    private void expressaoRel() {
        if(atual.getTipo() == "cadeiaDeCaracteres" || atual.getTipo()
                        == "numeros" || atual.getTipo() == "boolean"){
                andaUm();
           }
            else{
                variavel();
                andaUm();
           }
            if(atual.getTipo()=="Operador relacional"){
                andaUm();
                if(atual.getTipo() == "cadeiaDeCaracteres" || atual.getTipo()
                        == "numeros" || atual.getTipo() == "boolean"){
                    andaUm();
                }
                else{
                    variavel();
                    andaUm();
                }
            }
    }

    

   

    

    

    

    

   

} 

