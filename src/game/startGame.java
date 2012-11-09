package game;
import model.board;
import model.GameBoards;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class startGame {
	public static void main(String args[]){
		Scanner in = new Scanner(System.in);
		
//		TODO: Ask what playerone/blueplayer is
		player bluePlayer = new player("miniMax", "bluePlayer");		
//		TODO: Ask if what playertwo/greenplayer is
		player greenPlayer = new player("miniMax", "greenPlayer");
		
//		TODO: Ask what board to play
		String boardName = null, boardInfo = null;
		while(true){
			System.out.println("What board would you like to play on?");
			System.out.println("Kalamazoo, Peoria, Piqua, Punxsutawney, or Wallawalla");
			boardName = in.nextLine();
			if(boardNameIsValid(boardName)){
				boardInfo = GameBoards.getInfo(boardName);
				break;
			}
			else
				System.out.println("Invalid board");
		}
		
		board choosenBoard = new board(boardInfo);
		board completedBoard = simulateGame(bluePlayer, greenPlayer,
														choosenBoard);
			
		printStats(completedBoard);
	}
	
	public static board simulateGame(player player1, player player2,
												board currentBoard){

		while(currentBoard.availableSpaces!=0){
			player1.makeTurn(currentBoard);
			if(currentBoard.availableSpaces==0)	break;
			player2.makeTurn(currentBoard);
		}
		return currentBoard;
	}
	

	
	public static void printStats(board completedBoard){
		completedBoard.printBoard();
		completedBoard.printScores();
//		TODO: System.out. all the stats needed for the report 
	}
	
	
	public static boolean boardNameIsValid(String boardName){
		if(boardName.equals("Kalamazoo"))	
			return true;
		else if(boardName.equals("Peoria"))
			return true;
		else if(boardName.equals("Piqua"))
			return true;
		else if(boardName.equals("Punxsutawney"))
			return true;
		else if(boardName.equals("Wallawalla"))
			return true;
		else
			return false;
	}
	
}