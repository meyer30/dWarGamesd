package game;
import model.space;


/*
 * If the player picks nextSpace as their next space to take
 * gain represents the projected gain in points they can expect
 */

public class projectedGainForSpace {
	int gain;
	space nextSpace;
	
	public projectedGainForSpace(int gain, space choosenSpace){
		this.gain = gain;
		this.nextSpace = choosenSpace;
	}
}
