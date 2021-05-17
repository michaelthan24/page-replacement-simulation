import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class LeastRecentlyUsed {
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
	static void LRU (ArrayList<Memory> refStrings) {
		int counter = 0;
		for (int i=0;i<refStrings.size();i++) { // loop list of reference strings
			ArrayList<Integer> frame = new ArrayList<>();
			int [] value = new int[refStrings.get(i).pageFrames]; // list of values for each page in frame
			for (int l=0;l<value.length;l++) { // initialize array
				value[l] = 0;
			}
			System.out.print("Reference String: ");
			refStrings.get(i).printRefString(); // print reference string
			System.out.println();
			System.out.print("Page frame size: " + refStrings.get(i).pageFrames);
			System.out.println();
			for(int j=0;j<refStrings.get(i).refString.length;j++) { // loop specific reference string
				if(frame.size()<refStrings.get(i).pageFrames) { // if the frame queue is not full add onto frame
					frame.add(refStrings.get(i).refString[j]);
					System.out.print(frame);
					refStrings.get(i).incrementPageFaults(); // add to the amount of page faults for this reference string
					int index = frame.indexOf(refStrings.get(i).refString[j]);
					value[index] = 0; // reset value of page
					counter++;
				} 
				else if(frame.contains(refStrings.get(i).refString[j])) {
					int index = frame.indexOf(refStrings.get(i).refString[j]);
					value[index] = 0; // reset value of page
					System.out.print(frame);
					counter++;
				}
				else { // remove the least recently used page 
					int index = maxValueIndex(value);
					value[index] = 0; // reset value
					frame.set(index, refStrings.get(i).refString[j]); // add new page into frame
					refStrings.get(i).incrementPageFaults(); // add to the amount of page faults for this reference string
					System.out.print(frame);
					counter++;
				}
				for (int n=0; n<value.length;n++) {
					value[n]++;
				}
				if(counter==6) {
					System.out.println();
					counter = 0;
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
		System.out.println("Least Recently Used");
		LRU(refStrings);
	}
}
