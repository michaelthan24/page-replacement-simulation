import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class RanGenInputs {
	public static int [] subArray(int []arr, int beg, int end) {
		return Arrays.copyOfRange(arr, beg, end);
	}
	public static int findPageToReplace(ArrayList<Integer> frame, int [] subArr, int [] values) {
		for (int i=0;i<subArr.length;i++) { // loop the subarray
			int numOfZeros = 0;
			for (int j=0;j<values.length;j++) { // find the amount of zero values
				if(values[j]==0)
					numOfZeros++;
			}
			if (numOfZeros==1) { // if there is 1 zero value left return that value as the index of the optimal page to replace
				for(int n=0;n<values.length;n++) {
					if(values[n]==0)
						return n;
				}
			}
			if (frame.contains(subArr[i])) { // if frame contains the number the increment the value of the number
				int index = frame.indexOf(subArr[i]);
				values[index]++;
			}
		}
		return 0;
	}
	static int optimalAlgorithm(ArrayList<Memory> refStrings) {
		int totalPageFaults = 0;
		for (int i=0;i<refStrings.size();i++) { // loop list of reference strings
			ArrayList<Integer> frame = new ArrayList<>();
			int [] frameValues = new int [refStrings.get(i).pageFrames];
			for (int l=0;l<frameValues.length;l++) { // initialize array
				frameValues[l] = 0;
			}
			for(int j=0;j<refStrings.get(i).refString.length;j++) { // loop specific reference string
				if(frame.contains(refStrings.get(i).refString[j])) {
				}
				else if(frame.size()<refStrings.get(i).pageFrames) { // if the frame is not full add onto frame
					frame.add(refStrings.get(i).refString[j]); // put the number into the list and value already in frameValues
					refStrings.get(i).incrementPageFaults(); // add to the amount of page faults for this reference string
				} 
				else { // remove the least recently used page 
					int [] subArrRefStrings = subArray(refStrings.get(i).refString, j, refStrings.get(i).refString.length);
					// got the subarray of the rest of the reference string 
					int index = findPageToReplace(frame, subArrRefStrings, frameValues); // find page to replace
					frame.set(index, refStrings.get(i).refString[j]);
					refStrings.get(i).incrementPageFaults(); // add to the amount of page faults for this reference string
				}
				//reset the values
				for (int l=0;l<frameValues.length;l++) { 
					frameValues[l] = 0;
				}
			}
			totalPageFaults = totalPageFaults + refStrings.get(i).pageFaults;
			refStrings.get(i).pageFaults = 0;
		}
		return totalPageFaults;
	}
	static int maxValueIndex(int [] arr) {
		int max = arr[0];
		int index = 0;
		for (int i=0;i<arr.length;i++) {
			if (max<arr[i]) {
				max = arr[i];
				index = i;
			}
		}
		return index;
	}
	static int LRU (ArrayList<Memory> refStrings) {
		int totalPageFaults = 0;
		for (int i=0;i<refStrings.size();i++) { // loop list of reference strings
			ArrayList<Integer> frame = new ArrayList<>();
			int [] value = new int[refStrings.get(i).pageFrames]; // list of values for each page in frame
			for (int l=0;l<value.length;l++) { // initialize array
				value[l] = 0;
			}
			for(int j=0;j<refStrings.get(i).refString.length;j++) { // loop specific reference string
				if(frame.size()<refStrings.get(i).pageFrames) { // if the frame queue is not full add onto frame
					frame.add(refStrings.get(i).refString[j]);
					refStrings.get(i).incrementPageFaults(); // add to the amount of page faults for this reference string
					int index = frame.indexOf(refStrings.get(i).refString[j]);
					value[index] = 0; // reset value of page
				} 
				else if(frame.contains(refStrings.get(i).refString[j])) {
					int index = frame.indexOf(refStrings.get(i).refString[j]);
					value[index] = 0; // reset value of page
				}
				else { // remove the least recently used page 
					int index = maxValueIndex(value);
					value[index] = 0; // reset value
					frame.set(index, refStrings.get(i).refString[j]); // add new page into frame
					refStrings.get(i).incrementPageFaults(); // add to the amount of page faults for this reference string
				}
				for (int n=0; n<value.length;n++) {
					value[n]++;
				}
				
			}
			totalPageFaults = totalPageFaults + refStrings.get(i).pageFaults;
			refStrings.get(i).pageFaults = 0;
		}
		return totalPageFaults;
	}
	static int FIFO(ArrayList<Memory> refStrings) {
		int totalPageFaults = 0;
		for(int i=0;i<refStrings.size();i++) { // loop the list of strings
			Queue<Integer> q = new LinkedList<>(); // every reference string has its own frame queue
			for(int j=0;j<refStrings.get(i).refString.length;j++) { // loop the reference string
				if(q.size()<refStrings.get(i).pageFrames) { // if the frame queue is not full add onto frame
					q.add(refStrings.get(i).refString[j]);
					refStrings.get(i).incrementPageFaults(); // add to the amount of page faults for this reference string
				} 
				else if(q.contains(refStrings.get(i).refString[j])) {
					
				}
				else {
					q.remove(); // dequeue the first page in 
					q.add(refStrings.get(i).refString[j]); // enqueue the new page
					refStrings.get(i).incrementPageFaults(); // add to the amount of page faults for this reference string
				}
				
			}
			totalPageFaults = totalPageFaults + refStrings.get(i).pageFaults;
			refStrings.get(i).pageFaults = 0;
		}
		return totalPageFaults;

	}
	public static void main(String[] args) {
		try {
			int max1 = 9;
			int min1 = 0;
			FileWriter writer = new FileWriter("ReferenceString.txt");
			for(int i=0;i<50;i++) {
				writer.write("NumberOfPageFrame Value:\n");
				int NumOfPg = 3 ;
				int [] refStringInt = new int[30];
				writer.write(NumOfPg + "\n");
				writer.write("Reference String:\n");
				for (int j=0;j<30;j++) {
					int ranNum = (int) ((Math.random() * (max1-min1)) + min1);
					refStringInt[j] = ranNum;
					String castRan = String.valueOf(ranNum);
					writer.write(castRan);
				}
				if (i<49)
					writer.write("\n");
			}
			writer.close();
			FileWriter writer1 = new FileWriter("ReferenceString1.txt");
			for(int i=0;i<50;i++) {
				writer1.write("NumberOfPageFrame Value:\n");
				int NumOfPg = 4 ;
				int [] refStringInt = new int[30];
				writer1.write(NumOfPg + "\n");
				writer1.write("Reference String:\n");
				for (int j=0;j<30;j++) {
					int ranNum = (int) ((Math.random() * (max1-min1)) + min1);
					refStringInt[j] = ranNum;
					String castRan = String.valueOf(ranNum);
					writer1.write(castRan);
				}
				if (i<49)
					writer1.write("\n");
			}
			writer1.close();
			FileWriter writer2 = new FileWriter("ReferenceString2.txt");
			for(int i=0;i<50;i++) {
				writer2.write("NumberOfPageFrame Value:\n");
				int NumOfPg = 5 ;
				int [] refStringInt = new int[30];
				writer2.write(NumOfPg + "\n");
				writer2.write("Reference String:\n");
				for (int j=0;j<30;j++) {
					int ranNum = (int) ((Math.random() * (max1-min1)) + min1);
					refStringInt[j] = ranNum;
					String castRan = String.valueOf(ranNum);
					writer2.write(castRan);
				}
				if (i<49)
					writer2.write("\n");
			}
			writer2.close();
			FileWriter writer3 = new FileWriter("ReferenceString3.txt");
			for(int i=0;i<50;i++) {
				writer3.write("NumberOfPageFrame Value:\n");
				int NumOfPg = 6 ;
				int [] refStringInt = new int[30];
				writer3.write(NumOfPg + "\n");
				writer3.write("Reference String:\n");
				for (int j=0;j<30;j++) {
					int ranNum = (int) ((Math.random() * (max1-min1)) + min1);
					refStringInt[j] = ranNum;
					String castRan = String.valueOf(ranNum);
					writer3.write(castRan);
				}
				if (i<49)
					writer3.write("\n");
			}
			writer3.close();
		} catch(IOException e) {
			System.out.println(e);
		}
		ArrayList<Memory> refStrings = new ArrayList<>();
		try {
			File refString = new File ("ReferenceString.txt");
			Scanner stringScanner = new Scanner(refString);
			while(stringScanner.hasNext()) {
				stringScanner.nextLine(); // line to describe page frame
				int frames = stringScanner.nextInt(); // scan number of page frames
				Memory mem = new Memory(frames);
				stringScanner.nextLine(); // line to describe string
				stringScanner.nextLine(); // blank line
				String ref = stringScanner.nextLine();
				char [] temp = ref.toCharArray(); // reference string into char array to cast to integer
				for (int i=0;i<temp.length;i++) {
					mem.refString[i] = Character.getNumericValue(temp[i]);
				}
				refStrings.add(mem); // add new mem object to list of mem objects
			}
		stringScanner.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
		ArrayList<Memory> refStrings1 = new ArrayList<>();
		try {
			File refString = new File ("ReferenceString1.txt");
			Scanner stringScanner = new Scanner(refString);
			while(stringScanner.hasNext()) {
				stringScanner.nextLine(); // line to describe page frame
				int frames = stringScanner.nextInt(); // scan number of page frames
				Memory mem = new Memory(frames);
				stringScanner.nextLine(); // line to describe string
				stringScanner.nextLine(); // blank line
				String ref = stringScanner.nextLine();
				char [] temp = ref.toCharArray(); // reference string into char array to cast to integer
				for (int i=0;i<temp.length;i++) {
					mem.refString[i] = Character.getNumericValue(temp[i]);
				}
				refStrings1.add(mem); // add new mem object to list of mem objects
			}
		stringScanner.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
		ArrayList<Memory> refStrings2 = new ArrayList<>();
		try {
			File refString = new File ("ReferenceString2.txt");
			Scanner stringScanner = new Scanner(refString);
			while(stringScanner.hasNext()) {
				stringScanner.nextLine(); // line to describe page frame
				int frames = stringScanner.nextInt(); // scan number of page frames
				Memory mem = new Memory(frames);
				stringScanner.nextLine(); // line to describe string
				stringScanner.nextLine(); // blank line
				String ref = stringScanner.nextLine();
				char [] temp = ref.toCharArray(); // reference string into char array to cast to integer
				for (int i=0;i<temp.length;i++) {
					mem.refString[i] = Character.getNumericValue(temp[i]);
				}
				refStrings2.add(mem); // add new mem object to list of mem objects
			}
		stringScanner.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
		ArrayList<Memory> refStrings3 = new ArrayList<>();
		try {
			File refString = new File ("ReferenceString3.txt");
			Scanner stringScanner = new Scanner(refString);
			while(stringScanner.hasNext()) {
				stringScanner.nextLine(); // line to describe page frame
				int frames = stringScanner.nextInt(); // scan number of page frames
				Memory mem = new Memory(frames);
				stringScanner.nextLine(); // line to describe string
				stringScanner.nextLine(); // blank line
				String ref = stringScanner.nextLine();
				char [] temp = ref.toCharArray(); // reference string into char array to cast to integer
				for (int i=0;i<temp.length;i++) {
					mem.refString[i] = Character.getNumericValue(temp[i]);
				}
				refStrings3.add(mem); // add new mem object to list of mem objects
			}
		stringScanner.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
		int fifototPageFaults3 = FIFO(refStrings);
		System.out.println("Average for FIFO with 3 page frames: " + fifototPageFaults3/50);
		int fifototPageFaults4 = FIFO(refStrings1);
		System.out.println("Average for FIFO with 4 pages frames: " + fifototPageFaults4/50);
		int fifototPageFaults5 = FIFO(refStrings2);
		System.out.println("Average for FIFO with 5 pages frames: " + fifototPageFaults5/50);
		int fifototPageFaults6 = FIFO(refStrings3);
		System.out.println("Average for FIFO with 6 pages frames: " + fifototPageFaults6/50);
		
		int LRUTotPageFaults3 = LRU(refStrings);
		System.out.println("Average for LRU with 3 frames: " + LRUTotPageFaults3/50);
		int LRUTotPageFaults4 = LRU(refStrings1);
		System.out.println("Average for LRU with 4 frames: " + LRUTotPageFaults4/50);
		int LRUTotPageFaults5 = LRU(refStrings2);
		System.out.println("Average for LRU with 5 frames: " + LRUTotPageFaults5/50);
		int LRUTotPageFaults6 = LRU(refStrings3);
		System.out.println("Average for LRU with 6 frames: " + LRUTotPageFaults6/50);
		
		int optTotPageFaults3 = optimalAlgorithm(refStrings);
		System.out.println("Average for optimal with 3 page frames: " + optTotPageFaults3/50);
		int optTotPageFaults4 = optimalAlgorithm(refStrings1);
		System.out.println("Average for optimal with 4 page frames: " + optTotPageFaults4/50);
		int optTotPageFaults5 = optimalAlgorithm(refStrings2);
		System.out.println("Average for optimal with 5 page frames: " + optTotPageFaults5/50);
		int optTotPageFaults6 = optimalAlgorithm(refStrings3);
		System.out.println("Average for optimal with 6 page frames: " + optTotPageFaults6/50);
		
		
		
	}
}
