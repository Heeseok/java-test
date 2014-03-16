package com.github.heeseok.lotto;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.github.heeseok.util.Log;


public class Lotto {

	public static void main(String[] args) {
		Lotto lotto = new Lotto();
		lotto.lottery();
	}

	private ArrayList<int[]> loadNumber() throws IOException {

		ArrayList<int[]> lottoList = new ArrayList<int[]>();

		// 당첨 번호 모두 로딩.
		BufferedReader br = new BufferedReader(new FileReader("data/lotto.csv"));
	    try {
	        String line = br.readLine();

	        while (line != null) {

	        	//System.out.println(line);
	        	int[] array = new int[7];
	        	String[] split = line.split(",");
	        	for(int i=0; i<split.length; i++) {
	        		array[i] = Integer.parseInt(split[i]);
	        	}
	        	lottoList.add(0, array);

	            line = br.readLine();
	        }
	    } finally {
	        br.close();
	    }
	    return lottoList;
	}

	private void lottery() {
		ArrayList<int[]> lottoList = null;
		try {
			lottoList = loadNumber();
		} catch (IOException e) {
			e.printStackTrace();
		}

		int[] lottoNumberArray = new int[45];

		for( int i=1; i<=lottoList.size(); i++) {
			int[] array = lottoList.get(i-1);
			assert(i+1 == array[0]);
			for(int j=1; j < array.length; j++ ) {
				int number = array[j];
				lottoNumberArray[number-1]++;
			}
			// 번호 별 확률 계산해서 가장 확률이 높은 6개를 뽑아서 맞은 개수를 리턴한다.
			System.out.println("----------------------------------------------------------------");
			int result = calcLotto(lottoList, lottoNumberArray, i);
			String msg = i+1 + " round - " + result;
			System.out.println(msg);
			Log.write(msg);
		}

	}

	/**
	 * @param lottoList 당첨 번호를 가지고 있는 int[]의 List. int[]의 맨 첫 숫자는 회차 번호이다.
	 * @param lottoNumberArray 0~45 index. round+1 회차까지의 당첨 횟수가 저장되어 있다.
	 * @param round 0부터 시작. 회차 번호-1.
	 * @return round+1 회차의 예상 당첨 숫자를 반환한다.
	 */
	private int calcLotto(ArrayList<int[]> lottoList, int[] lottoNumberArray, int round)
	{
		HashMap<Integer,Double> hashMap = new HashMap<Integer,Double>();

		// 해당 회차의 확률을 계산한다.
		for(int i=0; i<lottoNumberArray.length; i++) {
			double probability = (4500.0/45 - lottoNumberArray[i])/(4500.0-round);
			hashMap.put(i,probability);
		}

		List<Integer> keys = new ArrayList<Integer>(hashMap.keySet());

		//Sort keys by values.
		final Map<Integer,Double> mapForComp = hashMap;
		Collections.sort(keys, new Comparator<Integer>(){
			public int compare(Integer left, Integer right){
				Integer leftKey = left;
				Integer rightKey = right;

				Double leftValue = mapForComp.get(leftKey);
				Double rightValue = mapForComp.get(rightKey);
				//return leftValue.compareTo(rightValue);
				return rightValue.compareTo(leftValue); // 내림차순으로 정렬.
			}
		});

		int result = 0;
		int count = 0;

		boolean isPast = true;

		int[] victoryArray = null;
		try {
			victoryArray = lottoList.get(round);
		} catch (IndexOutOfBoundsException e) {
			isPast = false;
		}

		// List the key value
		for(Iterator<Integer> i=keys.iterator(); i.hasNext();){
			count++;
			if( count > 6 ) break; // 확률이 가장 높은 6개만 뽑는다.
			int k = (Integer) i.next();
			String msg = k+1 + " : " + hashMap.get(k);
			System.out.println(msg);
			Log.write(msg);

			if( isPast ) {
				// 다음 회차 당첨 번호에 포함되어 있는지 확인해 본다.
				for (int j = 1; j < victoryArray.length; j++) {
					if( k+1 == victoryArray[j] ) { // k는 0부터 시작한다.
						result++;
						break;
					}
				}
			} else {
				result = -1;
			}
		}

		return result;
	}

	/**
	 * 주사위 확률 계산
	 */
	public static void calcDiceProbability() {
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
