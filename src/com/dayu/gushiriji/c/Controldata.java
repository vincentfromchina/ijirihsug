package com.dayu.gushiriji.c;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dayu.gushiriji.m.Stock;

public class Controldata
{
	private int getInt(String str) {  
	    int i = 0;  
	    try {  
	        Pattern p = Pattern.compile("^\\d+");  
	        Matcher m = p.matcher(str);  
	        if (m.find()) {  
	            i = Integer.valueOf(m.group());  
	        }  
	    } catch (NumberFormatException e) {  
	        e.printStackTrace();  
	    }  
	    return i;  
	}  
	
    public Map<String, String> sortMapByValue(Map<String, String> oriMap) {  
        Map<String, String> sortedMap = new LinkedHashMap<String, String>();  
        if (oriMap != null && !oriMap.isEmpty()) {  
            List<Map.Entry<String, String>> entryList = new ArrayList<Map.Entry<String, String>>(oriMap.entrySet());  
            Collections.sort(entryList,  
                    new Comparator<Map.Entry<String, String>>() {  
                        public int compare(Entry<String, String> entry1,  
                                Entry<String, String> entry2) {  
                            int value1 = 0, value2 = 0;  
                            try {  
                                value1 = getInt(entry1.getValue());  
                                value2 = getInt(entry2.getValue());  
                            } catch (NumberFormatException e) {  
                                value1 = 0;  
                                value2 = 0;  
                            }  
                            return value2 - value1;  
                        }  
                    });  
            Iterator<Map.Entry<String, String>> iter = entryList.iterator();  
            Map.Entry<String, String> tmpEntry = null;  
            while (iter.hasNext()) {  
                tmpEntry = iter.next();  
                sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());  
            }  
        }  
        return sortedMap;  
    }  
}
