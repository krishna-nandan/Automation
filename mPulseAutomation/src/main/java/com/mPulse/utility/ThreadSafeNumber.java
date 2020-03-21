package com.mPulse.utility;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ThreadSafeNumber {
	
	private static int counter;
	
	private static Map<Long, String[]> runningThreadsMap = new HashMap<Long, String[]>();
	
	public synchronized static String getRandomPhone() {
		Long threadID = Thread.currentThread().getId();
		
		if (!runningThreadsMap.containsKey(threadID)) {
			String[] tmp= ThreadSafeNumber.getnextPhoneArray();
			runningThreadsMap.put(threadID, tmp);
			Random r=new Random();
	        return (String) tmp[r.nextInt(tmp.length)];
		}
		else {
			String[] tmp= runningThreadsMap.get(threadID);
			Random r=new Random();
	        return tmp[r.nextInt(tmp.length)];
		}
	}
	
	public synchronized static String[] getAllPhone() {
		Long threadID = Thread.currentThread().getId();
		
		if (!runningThreadsMap.containsKey(threadID)) {
			String[] tmp= ThreadSafeNumber.getnextPhoneArray();
			runningThreadsMap.put(threadID, tmp);
	        return tmp;
		}
		else {
			String[] tmp= runningThreadsMap.get(threadID);
	        return tmp;
		}
	}
	
	private synchronized static String[] getnextPhoneArray() {
		if (counter == 0) {
			counter = counter+1;
			String[] arr1 = {"16504211192", "16508635091"};
			return arr1;
		}
		if (counter == 1) {
			counter = counter+1;
			String[] arr2 = {"16502229405"};
			return arr2;
		}
		if (counter == 2) {
			counter = counter+1;
			String[] arr3 = {"16502063892"};
			return arr3;
		}
		else {
			counter = counter+1;
			String[] arr = {"16", "17", "18", "19", "20"};
			return arr;
		}
	}
	
}
