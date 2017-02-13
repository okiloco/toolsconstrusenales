package co.tecnosystems.util;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 */
public class Utils {
    public static boolean[] parseDigitToBoolArray(char digit){
        boolean b[]= new boolean[10];
        b[Integer.parseInt(""+digit)]= true;
        return b;
    }
    
    public static boolean[] parseLetterToBoolArray(char letter){
        boolean b[]= new boolean[26];
        if(letter >= 65 && letter <= 90){
            b[((int)letter) - 65]= true;
        }
        return b;
    }

    public static boolean[] parseCharacterToBoolArray(char character){
        if(character >= 65 && character <= 90){
            return parseLetterToBoolArray(character);
        }else{
            return parseDigitToBoolArray(character);
        }
    }
    
    public static boolean[] parseMesToBoolArray(String mes){
        boolean b[]= new boolean[12];
        b[Integer.parseInt(mes) -1 ]= true;
        return b;
    }

    public static boolean[] parseHoraToBoolArray(String hora){
        boolean b[]= new boolean[24];
        b[Integer.parseInt(hora)]= true;
        return b;
    }

    public static boolean[] parseMinToBoolArray(String mins){
        boolean b[]= new boolean[6];
        b[Integer.parseInt(mins)/10]= true;
        return b;
    }


    public static boolean validacionnumero(char character){
        if(character >= 48 && character <= 57){
            return true;
        }else{
            return false;
        }
    }

}
