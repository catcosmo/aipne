
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import lenz.htw.aipne.Move;
import lenz.htw.aipne.net.NetworkClient;

public class AlphaBetaTrial {
	//get image
	private static BufferedImage getImage(){
		BufferedImage pic = null;
		try{ pic = ImageIO.read(new File("cat.png"));
		} catch (IOException ex){
		  System.out.println("Missing Image!");
		  System.exit(1);
		};	
		return pic;
	}
	
	//connect to server
	static NetworkClient network = new NetworkClient("localhost", "wumms", getImage());
	
	//create playing field
	static byte[] field = new byte[64];

	
	//function to find playing field coordinates in longs
	static public int abbildung(int x, int y){
		int z = y*15 - y*(y-1) +x;
		return z;
	};
	
	//TODO fill in functions
	static public int zToX(int z){
		int y = 0;
		return y;
	}
	
	static public int zToY(int z){
		int y = 0;
		return y;
	}
	
	//safe move to spielfeld-abbildung
	static public void applyMove(Move move, byte[] field){
		int oldX = move.fromX;
		int oldY = move.fromY;
		int newX = move.toX;
		int newY = move.toY;
		field[abbildung(newX, newY)] = field[abbildung(oldX, oldY)];
		field[abbildung(oldX, oldY)] = 3;
	}
	
	//initialize playing field
	public static void initializePlayingfield(byte[] field){
		for(int x=0; x<15; x++){
			for(int y=0; y<8; y++){
				int z = abbildung(x,y);
				//for player 0
				if(x>3 && x<11 && y==0 || x==6 && y==1){
					field[z]=0;
				//for player 1
				} else if((y==2 || y==4)&&x<2||x==0&&y==4||y==3&&x<4){
					field[z]=1;
				//for player 2
				} else if(y==5&&x==4||y==4&&(x==5||x==6)||y==3&&x<10&&x>5||y==2&&(x==10||x==9)){
					field[z]=2;

				}else if(x <= 15-2*y){
					field[z]=3;
				}
			}			
		}
	}
	
	
	
	

	public static void run() {
		Move move;
		initializePlayingfield(field);
		while(true){
			move = network.receiveMove();
			
			if(move != null){
				//safe opponents move in longs
				applyMove(move, field);
				
			}else{
				//make move & safe move
				move = ponder(field);
				network.sendMove(move);
			}
		}
	}
	
	public static boolean checkConstraints(byte[] field, int z, int playerNumber, int x, int y){
		//TODO fill constraints in if, dont forget sides of playing field
		int xCurrent = zToX(z);
		int yCurrent = zToY(z);
		
				if(field[z]==playerNumber){
					if(playerNumber==0){
					}else if(playerNumber==1){
						if(xCurrent%2==0){
							if(x == xCurrent+1 && (y==yCurrent || y==yCurrent+1)){
								
							}
						}
					}else if(playerNumber==2){
					}
				}
		
		//check no own player on goal field
		//check yNew >= yOld
		//check x%2==0 -> other constraints
		return false;
		
    }
	
	public static Move getBestMove(byte[] field, int playerNumber, int score){
		Move bestMove = null;
		Move trialMove = null;
		byte[] trialField = field;
		for(int z=0; z<64; z++){
			for(int y=0; y<8; y++){
				for(int x=0; x <= 14-2*y; x++){				
					if(checkConstraints(field,z,playerNumber, x, y)){
						trialMove = new Move(zToX(z), zToY(z), x, y);
						applyMove(trialMove, trialField);
						//TODO hier rekursionsaufruf? rekursionsstop = maxDepth berechnet durch network.getTimeLimitInSeconds()

						//get score, gute heuristik? i doubt it
						if(playerNumber == network.getMyPlayerNumber()){
							score += getScore(trialField, trialMove, score);
						}else score -= getScore(trialField, trialMove, score);
						//TODO vgl. score nach rekursion into max depth mit highScore
						//wenn highScore geknackt, speicher bestMove
					}
				}
			}
			
		}
		return bestMove;
	}
	
	//vor der anwendung von getScore MUSS checkConstraints aufgerufen werden
	public static int getScore(byte[] field, Move move, int score){
		//endpunkte je playernummer score 10
		int x = move.toX;
		int y = move.toY;
		if(x==14 && y == 0 && field[abbildung(x, y)]==1
			|| x==0 && y == 7 && field[abbildung(x, y)]==0
			|| x==0 && y == 0 && field[abbildung(x, y)]==2){
			score += 10;
		}
		//score normaler move
		else if(field[abbildung(move.toX, move.toY)] == 3){
			score += 1;
		}
		//score bei schlagen
		else if(field[abbildung(move.toX, move.toY)] != 3){
			score += 2;
		}
		
		return score;
	}
	
	static public Move ponder(byte[] field) {
		//double timer = network.getTimeLimitInSeconds(); berchechnen mit maxDepth
		int myPlayerNumber = network.getMyPlayerNumber();
		Move myMove = null;
		myMove = getBestMove(field, myPlayerNumber, 0);
		
		//returnValidMoves(field);
		//build tree, 
		//cut off crappy ones 
		//repeat
		return myMove;
	}
	

	
	public static void main(String[] args){
		run();
	}
}

/*
*für einen gegebenen move
-constraints checken
-die anderen spieler durchlaufen bis maxDepth, berechnet durch network.getTimeLimitInSeconds()
-score ermitteln
-score + move speichern
-dann für jeden validen move wiederholen
das klingt logisch
-wenn ein höherer score gefunden wird, diesen move speichern
dafür müsste man evt ein objekt bauen was einen move und einen score zurückgeben kann
class MoveWithScore { Move move; int score; }
*
*/