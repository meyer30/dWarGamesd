package game;
import model.board;
import model.GameBoards;
import java.util.Scanner;

public class startGame {
	public static void main(String args[]){
		Scanner in = new Scanner(System.in);
		
		player bluePlayer = new player("alpha-beta", "bluePlayer");	
		player greenPlayer = new player("alpha-beta", "greenPlayer");
		
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
		in.close();
		
		board choosenBoard = new board(boardInfo);
		board completedBoard = simulateGame(bluePlayer, greenPlayer,
														choosenBoard);
			
		printStats(completedBoard, bluePlayer, greenPlayer);
	}
	
	public static board simulateGame(player player1, player player2,
												board currentBoard){
		long beginTime = System.currentTimeMillis();

		while(currentBoard.availableSpaces!=0){
			player1.makeTurn(currentBoard);
			if(currentBoard.availableSpaces==0)	break;
			player2.makeTurn(currentBoard);
		}
		currentBoard.totalGameTime = System.currentTimeMillis() - beginTime;
		return currentBoard;
	}
	

	
	public static void printStats(board completedBoard, player blueP, player greenP){
		completedBoard.printBoard();
		completedBoard.printScores();
		int totalMoves = 25;	//There is only 25 spaces.
		
		long avgMoveTime = completedBoard.totalGameTime / (long) totalMoves;
		int avgNodesExam = (greenP.totalNodesExamined + blueP.totalNodesExamined)/totalMoves;
		System.out.println("");
		System.out.println("Total game time was " + completedBoard.totalGameTime + " milliseconds.");
		System.out.println("Average time per move was " + avgMoveTime + " milliseconds.");
		System.out.println("Total nodes examined for blue player is " + blueP.totalNodesExamined + ".");
		System.out.println("Total nodes examined for green player is " + greenP.totalNodesExamined + ".");
		System.out.println("Average nodes examined per move is " + avgNodesExam + ".");
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