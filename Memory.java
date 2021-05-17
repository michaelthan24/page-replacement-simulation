
public class Memory {
	int pageFrames;
	int [] frame;
	int [] refString;
	int pageFaults;
	
	public Memory(int pageFrames) {
		this.pageFrames = pageFrames;
		this.frame = new int[pageFrames];
		for(int i=0;i<this.frame.length;i++) {
			frame[i] = -1;
		}
		this.refString = new int[30];
		this.pageFaults = 0;
		
	}
	public int getFrame (int index) {
		return this.frame[index];
	}
	public void setFrame (int index, int value) {
		this.frame[index] = value;
	}
	public void printRefString() {
		for(int i=0;i<refString.length;i++) {
			System.out.print(refString[i]);
		}
	}
	public boolean frameFull () { // checks to see if all spots are full on the frame
		for (int i=0;i<this.frame.length;i++) {
			if(this.frame[i]==-1) {
				return false;
			}
		}
		return true;
	}
	public void incrementPageFaults() {
		this.pageFaults++;
	}
}
