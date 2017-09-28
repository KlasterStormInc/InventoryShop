package cluster.shop.util;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

public class AmountRegex {
	// 0-8,36-39,41-44,45-53
		private List<Integer> numbers = new ArrayList<Integer>();
		private String pattern;
		
		
		private AmountRegex(String pattern) {
			this.pattern = pattern;
			if(pattern == null || pattern.trim().isEmpty()) {
				return;
			}
			
			if(!pattern.contains(",")) {
				// 1-17
				parseSQ(pattern);
				return;
			}
			
			String[] arr = pattern.split(",");
			for (String s : arr) {
				parseSQ(s);
			}
			
		}
		
		public static AmountRegex compile(String pattern) {
			return new AmountRegex(pattern);
		}
		
		public int[] getSlots() {
			
			int[] arr = new int[numbers.size()];
			for (int i = 0; i < arr.length; i++) {
				arr[i] = numbers.get(i);
			}
			return arr;
		}
		
		
		private void parseSQ(String sq) {
			try {
				if(!sq.contains("-")) {
					Integer a = Integer.parseInt(sq);
					numbers.add(a);
					return;
				}
				
				String[] arr = sq.split("\\-");
				if(arr.length != 2) throw new IllegalArgumentException("Interval must contain 2 numbers\n" + 
				"Pattern: " + pattern + "\n" + 
				"Error at: " + sq);
				
				Integer a = Integer.parseInt(arr[0]);
				Integer b = Integer.parseInt(arr[1]);
				
				if(a == b) {
					numbers.add(a);
					return;
				}
				
				if(a > b) throw new IllegalArgumentException("First value must be less than second\n" + 
						"Pattern: " + pattern + "\n" + 
						"Error at: " + sq);
				
				for (int i = a.intValue(); i <= b.intValue(); i++) {
					numbers.add(i);
				}
				
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Slot must be an integer!\n" + 
						"Pattern: " + pattern + "\n" + 
						"Error at: " + sq, e);
			}
			
			
		}

		public int next(int element) {
			boolean next = false;
			
			for (Integer i : numbers) {
				if(next) {
					return i;
				}
				
				if(i.intValue() == element) {
					next = true;
					continue;
				}
			}
			
			return next ? numbers.get(0) : -1;
		}

		public boolean contains(Integer i) {
			return numbers.contains(i);
		}

		public int previous(int element) {
			List<Integer> numbers = Lists.reverse(this.numbers);
			
			boolean next = false;
			
			for (Integer i : numbers) {
				if(next) {
					return i;
				}
				
				if(i.intValue() == element) {
					next = true;
					continue;
				}
			}
			
			return next ? numbers.get(0) : -1;
		}
		
		
		
		
		
}
