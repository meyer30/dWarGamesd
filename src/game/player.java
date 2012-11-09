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
	int totalNodesExamined;

	public player(String strategy, String name){
		this.strategy = strategy;
		this.name = name;
		this.opponent = "greenPlayer";
		if(this.name.equals("greenPlayer"))
			this.opponent = "bluePlayer";
		totalNodesExamined = 0;
	}
		
	public void makeTurn(board currentBoard){
		int turns = 2;
		if(turns*2 + 1 > currentBoard.availableSpaces)
			turns = currentBoard.availableSpaces / 2;
		projectedGainForSpace chosenSpaceAndGain;
		if("alpha-beta".equals(strategy)){
		    projectedGainForSpace min = new projectedGainForSpace(-50000, null);
            projectedGainForSpace max = new projectedGainForSpace(50000, null);
			chosenSpaceAndGain = getAlphaBetaBestSpace(currentBoard, turns, min, max, this.name);
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
		
    
    public projectedGainForSpace getAlphaBetaBestSpace(board currentBoard, int turns, projectedGainForSpace min, projectedGainForSpace max, String playerName){
        if(turns==0)    return getBestSpaceForOneMove(currentBoard, playerName);
    //  if(currentBoard.availableSpaces==1) return getBestSpaceForOneMove(currentBoard, this.name);
        int bestGain;
        if(playerName == this.name) {
            bestGain = -50000;
        } else {
            bestGain = 50000;
        }
        
        List<space> availableSpaces = currentBoard.getAvailableSpaces(playerName);
        
        if(availableSpaces.isEmpty())   return new projectedGainForSpace(0, null); 
        space bestSpace = availableSpaces.get(0);
        for(int index = 0; index < availableSpaces.size(); index++){         
            space currentSpace = availableSpaces.get(index);
//            System.out.println("min is " + min.gain +", max is " + max.gain);
            int projectedGain = findProjectedGainAlphaBeta(currentSpace, currentBoard, turns, min, max, playerName);
                
            if(playerName == this.name) {
                if(projectedGain > bestGain){
                    bestGain = projectedGain;
                    bestSpace = currentSpace;
                }
            } else {
                if(projectedGain < bestGain){
                    bestGain = projectedGain;
                    bestSpace = currentSpace;
                }
            }
               
        }
        projectedGainForSpace bestSpaceGain = new projectedGainForSpace(bestGain, bestSpace);
        return bestSpaceGain;
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
			this.totalNodesExamined++;
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
	
   public int findProjectedGainAlphaBeta(space choosenSpace, board currentBoard, int turns, projectedGainForSpace min, projectedGainForSpace max, String playerName){
        //make a deep copy of the board for recursion
        board futureBoard = new board(currentBoard);
        
        futureBoard.acquireSpace(playerName, choosenSpace.row, choosenSpace.column);
        
        projectedGainForSpace opponentSpaceAndGain,nextSpaceAndGain;
        int opponentRow, opponentColumn;
        if (playerName == this.name) {
            opponentSpaceAndGain = getBestSpaceForOneMoveAlphaBetaMin(futureBoard, min, max, this.opponent);
            opponentRow = opponentSpaceAndGain.nextSpace.row;
            opponentColumn  = opponentSpaceAndGain.nextSpace.column;
            max.gain = opponentSpaceAndGain.gain;
            max.nextSpace = opponentSpaceAndGain.nextSpace;
            futureBoard.acquireSpace(this.opponent, opponentRow, opponentColumn);
            
            nextSpaceAndGain = getAlphaBetaBestSpace(futureBoard, turns - 1, min, max, this.opponent);
        }else {
           opponentSpaceAndGain = getBestSpaceForOneMoveAlphaBetaMax(futureBoard, min, max, this.name);
           opponentRow = opponentSpaceAndGain.nextSpace.row;
           opponentColumn  = opponentSpaceAndGain.nextSpace.column;
           min.gain = opponentSpaceAndGain.gain;
           min.nextSpace = opponentSpaceAndGain.nextSpace;
           futureBoard.acquireSpace(this.name, opponentRow, opponentColumn);
           
           nextSpaceAndGain = getAlphaBetaBestSpace(futureBoard, turns - 1, min, max, this.name);
        }
        
        int thisTurnsPoints = findGainsInBoard(currentBoard, futureBoard);
        
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
	
	public projectedGainForSpace getBestSpaceForOneMoveAlphaBetaMax(board currentBoard, projectedGainForSpace min, projectedGainForSpace max, String playerName){
        List<space> availableSpaces = currentBoard.getAvailableSpaces(playerName);
        if(availableSpaces.isEmpty())   return new projectedGainForSpace(0, null);
        int bestPoints = min.gain;
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
            if(pointsGained > max.gain) {
                return max;
            }
        }
        min.gain = bestPoints;
        min.nextSpace = bestSpace;
        projectedGainForSpace bestSpaceGain = new projectedGainForSpace(bestPoints, bestSpace);
        return bestSpaceGain;
    }
	
	public projectedGainForSpace getBestSpaceForOneMoveAlphaBetaMin(board currentBoard, projectedGainForSpace min, projectedGainForSpace max, String playerName){
        List<space> availableSpaces = currentBoard.getAvailableSpaces(playerName);
        if(availableSpaces.isEmpty())   return new projectedGainForSpace(0, null);
        int bestPoints = max.gain;
        space bestSpace = availableSpaces.get(0);
        
        for(int index = 0; index < availableSpaces.size(); index++){
            space currentSpace = availableSpaces.get(index);
            int row = currentSpace.row;
            int column = currentSpace.column;
            int pointsGained = currentSpace.value;
            if(currentBoard.isSpaceDeathBlizable(currentSpace.row, currentSpace.column, playerName))
                pointsGained += currentBoard.getPointsGainedFromBlitz(row, column, playerName);
            if(pointsGained < bestPoints){
                bestPoints = pointsGained;
                bestSpace = currentSpace;
            }
            if(pointsGained < min.gain) {
                return min;
            }
        }
        max.gain = bestPoints;
        max.nextSpace = bestSpace;
        projectedGainForSpace bestSpaceGain = new projectedGainForSpace(bestPoints, bestSpace);
        return bestSpaceGain;
    }
}

