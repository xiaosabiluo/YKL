package com.ycr.util;

public class Start {
	/**
	    * 根据月日判断星座   
	    * @param month
	    * @param day
	    * @return int
	    */
	   public static String getConstellation(int m,int d){
		   
		  
	       final String[] constellationArr = {"魔羯座" ,"水瓶座", "双鱼座", "牡羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座","天蝎座", "射手座", "魔羯座" }; 
		   
		   final int[] constellationEdgeDay = { 20,18,20,20,20,21,22,22,22,22,21,21};
		   int month=m;   
		   int day =d;   
	       if (day <= constellationEdgeDay[month-1]) {   
	          month = month - 1;   
	       }   
	       if (month >= 0) {   
	           return constellationArr[month];   
	       }   
	    return constellationArr[11];   

	   }

}
