package com.bluepandora.therap.donatelife.validation;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Biswajit Debnath
 */
public class DataValidation {

    public static boolean isValidMobileNumber(String mobileNumber) {
        if (mobileNumber == null) {
            return false;
        }
        if (mobileNumber.length() == 0) {
            return false;
        }

        for (int index = 0; index < mobileNumber.length(); index++) {
            if (mobileNumber.charAt(index) < '0' || mobileNumber.charAt(index) > '9') {
                return false;
            }
        }

        return true;
    }

    public static boolean isValidKeyWord(String keyWord) {

        if (keyWord == null) {
            return false;
        }
        
        if (keyWord.length() <= 8) {
            return false;
        }
        
        boolean upperCaseFound=false;
        boolean lowerCaseFound=false;
        boolean numberFound=false;
        boolean invalidCharacterFound=false;
        
        for (int index = 0; index < keyWord.length(); index++) {
            
            char ch = keyWord.charAt(index);
            
            if(ch>='A' && ch<='Z'){
                upperCaseFound=true;
            } else if(ch>='a' && ch<='z'){
                lowerCaseFound=true;
            }else if(ch>='0' && ch<='9'){
                numberFound=true;
            }else{
                invalidCharacterFound=true;
            }
        }
        
        return (upperCaseFound && lowerCaseFound && numberFound && !invalidCharacterFound);
    }
    
    public static boolean isValidString(String word){
        if(word==null) return false;
        word = word.replace(" ","");
        if(word.length()==0) return false;
        return true;
    }
}
