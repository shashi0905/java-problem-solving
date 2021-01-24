package com.shashi.practise;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RepeatingItems {
	
	public static void main(String[] args) {
		
		Integer[] arr = {1,3,23,11,44,3,23,2,3,11}; 
		Map<Integer, Integer> map = new HashMap<>();
		
		for(Integer a : arr) {
			if(map.containsKey(a)) {
				map.put(a, map.get(a)+1);
			}
			else {
				map.put(a, 1);
			}
		}
		
		List<Integer> keys = map.keySet().stream().filter(key->map.get(key) > 1).collect(Collectors.toList());
		keys.forEach(e->{System.out.println(e);});
	}

}
