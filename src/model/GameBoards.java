package model;

public class GameBoards {

	public static String Kalamazoo = 
			"1	1	1	1	1	"+
			"1	2	2	2	1	"+
			"1	2	3	2	1	"+
			"1	2	2	2	1	"+
			"1	1	1	1	1	";

	public static String Peoria = 
			"99	1	99	1	99	"+
			"1	99	1	99	1	"+
			"99	1	99	1	99	"+
			"1	99	1	99	1	"+
			"99	1	99	1	99";
	
	public static String Piqua =
			"1	1	1	1	1	"+
			"1	1	1	1	1	"+
			"1	1	1	1	1	"+
			"1	1	1	1	1	"+
			"1	1	1	1	1";

	public static String Punxsutawney =
			"10	5	2	1	1	"+
			"10	5	2	1	1	"+
			"10	5	2	1	1	"+
			"10	5	2	1	1	"+
			"10	5	2	1	1";
	
	public static String Wallawalla =
			"1	10	1	1	2	"+
			"10	1	2	5	10	"+
			"5	1	1	3	7	"+
			"2	1	1	1	3	"+
			"1	2	2	1	2";
	
	public static String getInfo(String boardName){
		if(boardName.equals("Kalamazoo"))
			return Kalamazoo;
		else if(boardName.equals("Peoria"))
			return Peoria;
		else if(boardName.equals("Piqua"))
			return Piqua;
		else if(boardName.equals("Punxsutawney"))
			return Punxsutawney;
		else
			return Wallawalla;
	}
}
