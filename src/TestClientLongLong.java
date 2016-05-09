
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import lenz.htw.aipne.Move;
import lenz.htw.aipne.net.NetworkClient;

public class TestClientLongLong {
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
	static long first;
	static long second;
	
	//function to find playing field coordinates in longs
	static public int abbildung(int x, int y){
		int z = y*15 - y*(y-1) +x;
		return z;
	};
	
	//safe move to spielfeld-abbildung
	static public void applyMove(Move move, long first, long second){
		int oldX = move.fromX;
		int oldY = move.fromY;
		int newX = move.toX;
		int newY = move.toY;
		/*was jetzt kommt ist falsch, weil:
		*set bit (make it 1): long |= 1 << bit;
		*clear bit (make it 0): long &= ~(1 << bit);
		*ich will aber den alten bit an die neue schieben und umgekehrt
		*und ist die bitposition dann nicht 64-abbildung(x, y)?
		*/
		first |= abbildung(newX, newY) << abbildung(oldX, oldY);
		second |= abbildung(newX, newY) << abbildung(oldX, oldY);
	}
	
	//initialize playing field
	public static void initializePlayingfield(long first, long second){
		for(int x=0; x<15; x++){
			for(int y=0; y<8; y++){
				//for player 0
				if(x>3 && x<11 && y==0 || x==6 && y==1){
					first &= ~(1 << abbildung(x, y)); //clear bit -> 0
					second &= ~(1 << abbildung(x, y));//clear bit -> 0
				//for player 1
				} else if((y==2 || y==4)&&x<2||x==0&&y==4||y==3&&x<4){
					first &= ~(1 << abbildung(x, y)); //clear bit -> 0
					second |= 1 << abbildung(x, y);	//set bit -> 1
				//for player 2
				} else if(y==5&&x==4||y==4&&(x==5||x==6)||y==3&&x<10&&x>5||y==2&&(x==10||x==9)){
					first |= 1 << abbildung(x, y); //set bit -> 1
					second &= ~(1 << abbildung(x, y)); //clear bit -> 0
				}else{
					first |= 1 << abbildung(x, y); //set bit -> 1
					second |= 1 << abbildung(x, y); //set bit -> 1
				}
			}			
		}
	}
	
	
	
	

	public static void run() {
		Move move;
		initializePlayingfield(first, second);
		//print long
		int mask = 1 << 63;
		   for(int i=1; i<=64; i++) {
		        if( (mask & first) != 0 )
		            System.out.print(1);
		        else
		            System.out.print(0);


		        if( (i % 4) == 0 )
		            System.out.print(" ");

		        mask = mask >> 1;
		    }
		while(true){
			move = network.receiveMove();
			
			if(move != null){
				//safe opponents move in longs
				applyMove(move, first, second);
				
			}else{
				//make move & safe move
				network.sendMove(ponder(first, second));
				applyMove(ponder(first, second), first, second);
			};
		}
	}
	
	static public Move ponder(long first, long second) {
		network.getTimeLimitInSeconds();
		//network.getMyPlayerNumber(); braucht man?
		Move myMove = null;
		//check constraints moving patterns
		//clever strategy
		return myMove;
	}
	

	
	public static void main(String[] args){
		run();
	}
}