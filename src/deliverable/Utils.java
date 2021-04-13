package deliverable;

import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class Utils {
	
	
	public static void printTreeMap(TreeMap<Object,Object> map) {
		  map.forEach((key, value) -> System.out.println(key + "= " + value + "\n\n"));
	}
	
	public static Integer countOccurrences(ArrayList<Month> list, Month month) {
		
		int count = 0;
		for (Month m : list) {
			if (m == month) {
				count++;
			}
		}
		return count;
	}
	

public static void main(String[] args){
		// Do nothing because is a main method

	}
}
