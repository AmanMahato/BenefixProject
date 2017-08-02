package com.interview.test;

/**
 * Created by amanmahato on 8/1/17.
 * This is a helper class which
 * helps with numberFormat and Validation
 */
public class HelperClass {

    public static String getPlanName(String input){
        StringBuilder sb=new StringBuilder();
        int len=input.length();
        for(int i=len-1;i>=0;i--){
            if(input.charAt(i)!=':'){
                sb.append(input.charAt(i));
            }else{
                break;
            }
        }
        return sb.reverse().toString().trim();
    }

    public static String getNumbers(String input){
        return input.replaceAll("\\D+","");
    }


    public static String checkValid(String input){
        if (input.startsWith("0")){
            return "0";
        }else if(input.endsWith("+")){
            return "64";
        } else{
            return input;
        }
    }

}
