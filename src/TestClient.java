
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import lenz.htw.aipne.Move;
import lenz.htw.aipne.net.NetworkClient;

public class TestClient {
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
	
	public boolean validMoves(byte[] field, int myPlayerNumber){
		for(int x=0; x<15; x++){
			for(int y=0; y<8; y++){
				int z = abbildung(x,y);
				if(field[z]==myPlayerNumber){
					if(myPlayerNumber==0){
						return true;
					}else if(myPlayerNumber==1){
						if(x%2==0){
							if()
							return true;
						}
					}else if(myPlayerNumber==2){
						return true;
					}
				}
				
			}			
		}
		return false;
		//check no own player on goal field
		//check yNew >= yOld
		//check x%2==0 -> other constraints
		
    }
	
	
	//feld drehen
	public void turnField(byte[] field){
		
	}
	
	static public Move ponder(byte[] field) {
		//double timer = network.getTimeLimitInSeconds();
		//int myPlayerNumber = network.getMyPlayerNumber();
		Move myMove = null;
		
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