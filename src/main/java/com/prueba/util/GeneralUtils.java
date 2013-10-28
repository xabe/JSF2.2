package com.prueba.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneralUtils {
	
	public GeneralUtils(){
		super();
		
	}
	
	public boolean isNumeric(String str){
		char[] all = str.toCharArray();
	    boolean isNumeric=true;
	    if (all.length==0)
	    	isNumeric=false;
	    else{
	    	for(int i = 0; i < all.length;i++) {
		        if(!Character.isDigit(all[i])) {
		            isNumeric=false;
		        }
	    	}
	    }
	    return isNumeric;
	}
	
	 public boolean isLong(String in) {
	        boolean isLong=true;
		 	try {
	            if (in.trim().length()>0){
	            	Long.parseLong(in);
	            	isLong=true;
	            }else
	            	isLong=false;
	        } catch (NumberFormatException ex) {
	            return false;
	        }
	        return isLong;
	    }
	 
	 public int generateRandom(){
		 Random randomGenerator = new Random();
		 int randomInt = randomGenerator.nextInt(1000000);
		 return randomInt;
	 }
	 
	 public <T> List<T> copyList(List<T> source) {
		   List<T> dest = new ArrayList<T>();
		   for (T item : source){ 
			   dest.add(item); 
		   }
		   return dest;
		}
	 
	 public float round2(float input){
		 float out = (float) (Math.round(input*100.0)/100.0);
		 return out;
	 }
	
}
