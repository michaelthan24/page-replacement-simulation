import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Queue;

public class FirstInFirstOut {
	static void FIFO(ArrayList<Memory> refStrings) {
		int counter = 0;
		for(int i=0;i<refStrings.size();i++) { // loop the list of strings
			Queue<Integer> q = new LinkedList<>(); // every reference string has its own frame queue
			System.out.print("Reference String: ");
			refStrings.get(i).printRefString(); // print reference string
			System.out.println();
			System.out.print("Page frame size: " + refStrings.get(i).pageFrames);
			System.out.println();
			for(int j=0;j<refStrings.get(i).refString.length;j++) { // loop the reference string
				if(q.size()<refStrings.get(i).pageFrames) { // if the frame queue is not full add onto frame
					q.add(refStrings.get(i).refString[j]);
					System.out.print(q);
					refStrings.get(i).incrementPageFaults(); // add to the amount of page faults for this reference string
					counter++;
				} 
				else if(q.contains(refStrings.get(i).refString[j])) {
					System.out.print(q);
					counter++;
				}
				else {
					q.remove(); // dequeue the first page in 
					q.add(refStrings.get(i).refString[j]); // enqueue the new page
					refStrings.get(i).incrementPageFaults(); // add to the amount of page faults for this reference string
					System.out.print(q);
					counter++;
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
		System.out.println("First in First Out");
		FIFO(refStrings);
	}
}
