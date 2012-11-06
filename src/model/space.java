package model;

public class space {
	public String owner;
	public int value, row, column;
	
	public space(int newValue, int row, int column, String newOwner){
		this.value = newValue;
		this.row = row;
		this.column = column;
		this.owner = newOwner;
	}
	
	public void changeOwner(String newOwner){
		this.owner = newOwner;
	}
}
