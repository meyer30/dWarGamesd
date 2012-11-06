package game;
import model.board;
import java.util.Scanner;

public class startGame {
	public static void main(String args[]){
//		TODO: Ask what playerone/blueplayer is
		player bluePlayer = new player("miniMaxStategy", "bluePlayer");		
//		TODO: Ask if what playertwo/greenplayer is
		player greenPlayer = new player("miniMaxStategy", "greenPlayer");
//		TODO: Ask what board to play
		board choosenBoard = new board("Kolla");
		
		board completedBoard = simulateGame(bluePlayer, greenPlayer,
														choosenBoard);
			
		printStats(completedBoard);
	}
	
	public static board simulateGame(player player1, player player2,
												board currentBoard){
		Scanner in = new Scanner(System.in);

		while(currentBoard.availableSpaces!=0){
			player1.makeTurn(currentBoard);
			if(currentBoard.availableSpaces==0)	break;
			player2.makeTurn(currentBoard);
						
			System.out.println("Enter a string to continue");
			String s = in.nextLine();
			currentBoard.printBoard();
		}
		in.close();
		return currentBoard;
	}
	

	
	public static void printStats(board completedBoard){
//		TODO: System.out. all the stats needed for the report 
	}
}