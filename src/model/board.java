package model;
import model.space;
import java.util.List;
import java.util.ArrayList;

public class board{
	
	public space[][] boardArray;
	public int availableSpaces;
	public long totalGameTime;
	
	public board(String boardInfo){
		String[] values = boardInfo.split("	");
		boardArray = new space[5][5];
		totalGameTime = 0;
		for(int row = 0; row < 5; row++){
			for(int column = 0; column < 5; column++){
				int value = getValueFromStringArray(values, row, column);
				boardArray[row][column] = new space(value, row, column, "Nobody");
			}
		}
		availableSpaces = 25;
	}
	
	public board(board copyOther){
		boardArray = new space[5][5];
		for(int row = 0; row < 5; row++){
			for(int column = 0; column < 5; column++){
				int value = copyOther.boardArray[column][row].value;
				String owner = copyOther.boardArray[column][row].owner;
				boardArray[column][row] = new space(value, row, column, owner);
			}
		}
		availableSpaces = copyOther.availableSpaces;
				
	}
	
	
	public int getPointsFor(String owner){
		int points = 0;
		for(int row = 0; row < 5; row++){
			for(int column = 0; column < 5; column++){
				if(boardArray[column][row].owner.equals(owner))
					points+=boardArray[column][row].value;
			}
		}
		return points;
	}
	
	

	public void acquireSpace(String newOwner, int row, int column)
	{
		availableSpaces--;
		boardArray[row][column].owner = newOwner;
		if(isSpaceDeathBlizable(row, column, newOwner))
			if(row > 1 && !("Nobody".equals(boardArray[row-1][column].owner)))
				boardArray[row-1][column].owner = newOwner;
			else if(row < 4 && !("Nobody".equals(boardArray[row+1][column].owner)))
				boardArray[row+1][column].owner = newOwner;
			else if(column > 1 && !("Nobody".equals(boardArray[row][column-1].owner)))
				boardArray[row][column-1].owner = newOwner;
			else if(column < 4 && !("Nobody".equals(boardArray[row][column+1].owner)))
				boardArray[row][column+1].owner = newOwner;
	}
	
	
	/*
	 * Return available spaces for the owner to take
	 * in a ArrayList format
	 */
	
	public List<space> getAvailableSpaces(String owner){
		List<space> availableSpaces = new ArrayList<space>();
		for(int row = 0; row < 5; row++){
			for(int column = 0; column < 5; column++){
				if("Nobody".equals(boardArray[row][column].owner))
					availableSpaces.add(boardArray[row][column]); 
			}
		}
		return availableSpaces;
	}
	
	
	
	/*
	 * Returns whether given space can be Death blitz
	 * by the newOwner
	 */
	
	public boolean isSpaceDeathBlizable(int row, int column, String newOwner){
		if(row > 0 && boardArray[row-1][column].owner.equals(newOwner))
			return true;
		else if(row < 4 && boardArray[row+1][column].owner.equals(newOwner))
			return true;
		else if(column > 0 && boardArray[row][column-1].owner.equals(newOwner))
			return true; 
		else if(column < 4 && boardArray[row][column+1].owner.equals(newOwner))
			return true;
		return false;
	}
	
	
	public int getPointsGainedFromBlitz(int row, int column, String newOwner){
		int pointsGained = 0;
		if(row > 0){
			if(!boardArray[row-1][column].owner.equals(newOwner) && !boardArray[row-1][column].owner.equals("Nobody"))
				pointsGained+=boardArray[row-1][column].value;
		}
		if(row < 4){
			if(!boardArray[row+1][column].owner.equals(newOwner) && !boardArray[row+1][column].owner.equals("Nobody"))
				pointsGained+=boardArray[row+1][column].value;
		}
		if(column > 0){
			if(!boardArray[row][column-1].owner.equals(newOwner) && !boardArray[row][column-1].owner.equals("Nobody"))
				pointsGained+=boardArray[row][column-1].value;
		}
		if(column < 4){
			if(!boardArray[row][column+1].owner.equals(newOwner) && !boardArray[row][column+1].owner.equals("Nobody"))
				pointsGained+=boardArray[row][column+1].value;
		}
		return pointsGained;
	}
	
	
	public void printBoard(){
		System.out.println("[]=greenSpace, ()=blueSpace");
		for(int row = 0; row < 5; row++){
			for(int column = 0; column < 5; column++){
				//System.out.println("Row = :" + row + " Column = :" + column);
				String owner = boardArray[row][column].owner;
				int value = boardArray[row][column].value;
				if("Nobody".equals(owner))
					System.out.print(" " + value + " ");
				else if("greenPlayer".equals(owner))
					System.out.print("[" + value + "]");
				else
					System.out.print("(" + value + ")");
			}
			System.out.println("");
		}
	}
	
	
	public void printScores(){
		int blueScore = 0, greenScore = 0;
		for(int row = 0; row < 5; row++){
			for(int column = 0; column < 5; column++){
				if("greenPlayer".equals(boardArray[row][column].owner))
					greenScore+=boardArray[row][column].value;
				else if("bluePlayer".equals(boardArray[row][column].owner))
					blueScore+=boardArray[row][column].value;
			}
		}
		System.out.println("Green player's score is :" + greenScore);
		System.out.println("Blue player's score is :" + blueScore);
	}
	
	
	private int getValueFromStringArray(String[] values, int row, int column){
		int indexOfValue = column + (row*5);
		String valueStr = values[indexOfValue];
		
		int valueInt = 0;
		for(int index = 0; index < valueStr.length(); index++){
			valueInt *= 10;
			valueInt += (int) valueStr.charAt(index) - 48;
		}
		return valueInt;
	}
}