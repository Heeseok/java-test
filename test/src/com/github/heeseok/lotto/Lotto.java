package com.github.heeseok.lotto;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;


public class Lotto {
	public static void main(String[] args) {
		calcDiceProbability();
	}

	/**
	 * 주사위 확률 계산
	 */
	private static void calcDiceProbability() {
		HashMap<Integer, Integer> map = new HashMap<Integer,Integer>();
		Random random = new Random();
		int array[] = {10, 50, 100, 150, 200, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000};
		for (int j = 0; j < array.length; j++) {
			map.clear();
			int count = array[j];
			for (int i = 0; i < count; i++) {
				Integer key = random.nextInt(6);
				Integer value = map.get(key)==null?1:map.get(key) + 1;
				map.put(key, value);
			}
			System.out.println("######## " + count + " ##########");
			for (Entry<Integer, Integer> entry : map.entrySet()) {
				Integer key = entry.getKey();
				Integer value = entry.getValue();
				System.out.println(key + " : " + value + " : " + Math.round((((double)value/count)-(1.0/6))/(1.0/6)*100) );
			}
		}
	}
}
