
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
		first |= abbildung(newX, newY) << abbildung(oldX, oldY);
		second |= abbildung(newX, newY) << abbildung(oldX, oldY);
	}
	
	

	public static void run() {
		Move move;
		while(true){
			move = network.receiveMove();
			
			if(move != null){
				//safe in longs
				applyMove(move, first, second);
				
			}else{
				network.sendMove(ponder(first, second));
				applyMove(ponder(first, second), first, second);
			};
		}
	}
	
	static public Move ponder(long first, long second) {
		network.getTimeLimitInSeconds();
		//network.getMyPlayerNumber(); braucht man?
		Move myMove = null;
		//clever strategy
		return myMove;
	}
	

	
	public static void main(String[] args){
		run();
	}
}