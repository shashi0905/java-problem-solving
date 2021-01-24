package com.shashi.practise;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RepeatingItems {
	
	public static void main(String[] args) {
		
		Integer[] arr = {1,3,23,11,44,3,23,2,3,11}; 
		Map<Integer, Integer> freqMap = new HashMap<>();
		
		for(Integer a : arr) {
			if(freqMap.containsKey(a)) {
				freqMap.put(a, freqMap.get(a)+1);
			}
			else {
				freqMap.put(a, 1);
			}
		}
		
		List<Integer> keys = freqMap.keySet().stream().filter(key->freqMap.get(key) > 1).collect(Collectors.toList());
		keys.forEach(e->{System.out.println(e);});
	}

}
