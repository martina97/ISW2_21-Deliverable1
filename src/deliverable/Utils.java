package deliverable;

import java.time.Month;

import java.util.List;

public class Utils {
	
	private Utils() {}
	
	public static Integer countOccurrences(List<Month> list, Month month) {
		
		int count = 0;
		for (Month m : list) {
			if (m == month) {
				count++;
			}
		}
		return count;
	}
	

}
