package game;
import model.board;
import model.space;
import java.util.List;
import game.projectedGainForSpace;


/*
 * Player makes decision on how to change board
 * based on the paramater board and the value Strategy
 */
public class player {
	String strategy, name, opponent;

	public player(String strategy, String name){
		this.strategy = strategy;
		this.name = name;
		this.opponent = "greenPlayer";
		if(this.name.equals("greenPlayer"))
			this.opponent = "bluePlayer";
	}
		
	public void makeTurn(board currentBoard){
		int turns = 2;
		if(turns*2 + 1 > currentBoard.availableSpaces)
			turns = currentBoard.availableSpaces / 2;
		projectedGainForSpace chosenSpaceAndGain;
		if("alpha-beta".equals(strategy)){
			chosenSpaceAndGain = getMiniMaxBestSpace(currentBoard, turns);
			//TODO: IMPLEMENT A GETALPHABETABESTSPACE(CURRENTBOARD,TURNS);
		}
		else if("miniMax".equals(strategy)){
			chosenSpaceAndGain = getMiniMaxBestSpace(currentBoard, turns);
		}
		else{
			//TODO: Human player, get input.
			chosenSpaceAndGain = getMiniMaxBestSpace(currentBoard, turns);
		}
		int row = chosenSpaceAndGain.nextSpace.row;
		int column = chosenSpaceAndGain.nextSpace.column;
			
		currentBoard.acquireSpace(this.name, row, column);
	}
		
		
		
	public projectedGainForSpace getMiniMaxBestSpace(board currentBoard, int turns){
		if(turns==0)	return getBestSpaceForOneMove(currentBoard, this.name);
//		if(currentBoard.availableSpaces==1) return getBestSpaceForOneMove(currentBoard, this.name);
		int bestGain = -50000;
		
		List<space> availableSpaces = currentBoard.getAvailableSpaces(this.name);
		
		if(availableSpaces.isEmpty())	return new projectedGainForSpace(0, null); 
		space bestSpace = availableSpaces.get(0);
		for(int index = 0; index < availableSpaces.size(); index++){
				
			space currentSpace = availableSpaces.get(index);
			int projectedGain = findProjectedGain(currentSpace, currentBoard, turns);
				
			if(projectedGain > bestGain){
				bestGain = projectedGain;
				bestSpace = currentSpace;
			}
		}
		projectedGainForSpace bestSpaceGain = new projectedGainForSpace(bestGain, bestSpace);
		return bestSpaceGain;
	}
		
		
		public int findProjectedGain(space choosenSpace, board currentBoard, int turns){
			//make a deep copy of the board for recursion
			board futureBoard = new board(currentBoard);
			
			futureBoard.acquireSpace(this.name, choosenSpace.row, choosenSpace.column);
			
			projectedGainForSpace opponentSpaceAndGain = getBestSpaceForOneMove(futureBoard, this.opponent);
			int opponentRow = opponentSpaceAndGain.nextSpace.row;
			int opponentColumn = opponentSpaceAndGain.nextSpace.column;
			
			futureBoard.acquireSpace(this.opponent, opponentRow, opponentColumn);
			
			int thisTurnsPoints = findGainsInBoard(currentBoard, futureBoard);
			
			projectedGainForSpace nextSpaceAndGain = getMiniMaxBestSpace(futureBoard, turns - 1);
			
			int futureTurnsPoints = nextSpaceAndGain.gain;
			return thisTurnsPoints + futureTurnsPoints;
		}
		
		
		
		public int findGainsInBoard(board oldBoard, board newBoard){
			int gains = newBoard.getPointsFor(this.name) - oldBoard.getPointsFor(this.name);
			int loses = newBoard.getPointsFor(this.opponent) - oldBoard.getPointsFor(this.opponent);
			return gains - loses;
		}
		
			
		public projectedGainForSpace getBestSpaceForOneMove(board currentBoard, String playerName){
			List<space> availableSpaces = currentBoard.getAvailableSpaces(playerName);
			if(availableSpaces.isEmpty())	return new projectedGainForSpace(0, null);
			int bestPoints = -50000;
			space bestSpace = availableSpaces.get(0);
			
			for(int index = 0; index < availableSpaces.size(); index++){
				space currentSpace = availableSpaces.get(index);
				int row = currentSpace.row;
				int column = currentSpace.column;
				int pointsGained = currentSpace.value;
				if(currentBoard.isSpaceDeathBlizable(currentSpace.row, currentSpace.column, playerName))
					pointsGained += currentBoard.getPointsGainedFromBlitz(row, column, playerName);
				if(pointsGained > bestPoints){
					bestPoints = pointsGained;
					bestSpace = currentSpace;
				}
			}
			projectedGainForSpace bestSpaceGain = new projectedGainForSpace(bestPoints, bestSpace);
			return bestSpaceGain;
		}
	}