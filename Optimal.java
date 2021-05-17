import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Optimal {
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
	static void optimalAlgorithm(ArrayList<Memory> refStrings) {
		int counter = 0;
		for (int i=0;i<refStrings.size();i++) { // loop list of reference strings
			ArrayList<Integer> frame = new ArrayList<>();
			int [] frameValues = new int [refStrings.get(i).pageFrames];
			for (int l=0;l<frameValues.length;l++) { // initialize array
				frameValues[l] = 0;
			}
			System.out.print("Reference String: ");
			refStrings.get(i).printRefString(); // print reference string
			System.out.println();
			System.out.print("Page frame size: " + refStrings.get(i).pageFrames);
			System.out.println();
			for(int j=0;j<refStrings.get(i).refString.length;j++) { // loop specific reference string
				if(frame.contains(refStrings.get(i).refString[j])) {
					System.out.print(frame);
					counter++;
				}
				else if(frame.size()<refStrings.get(i).pageFrames) { // if the frame is not full add onto frame
					frame.add(refStrings.get(i).refString[j]); // put the number into the list and value already in frameValues
					System.out.print(frame);
					refStrings.get(i).incrementPageFaults(); // add to the amount of page faults for this reference string
					counter++;
				} 
				else { // remove the least recently used page 
					int [] subArrRefStrings = subArray(refStrings.get(i).refString, j, refStrings.get(i).refString.length);
					// got the subarray of the rest of the reference string 
					int index = findPageToReplace(frame, subArrRefStrings, frameValues); // find page to replace
					frame.set(index, refStrings.get(i).refString[j]);
					refStrings.get(i).incrementPageFaults(); // add to the amount of page faults for this reference string
					System.out.print(frame);
					counter++;
				}
				if(counter==6) {
					System.out.println();
					counter = 0;
				}
				//reset the values
				for (int l=0;l<frameValues.length;l++) { // initialize array
					frameValues[l] = 0;
				}
			}
			System.out.println("Page faults = " + refStrings.get(i).pageFaults);
			System.out.println();
		}
	}
	public static void main(String[] args ) {
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
		System.out.println("Optimal");
		optimalAlgorithm(refStrings);
	}
}
