
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
	
	

	public static void run() {
		Move move;
		while(true){
			move = network.receiveMove();
			if(move != null){
				//safe in array
			}else{
				network.sendMove(ponder(field));
			};
		}
	}
	
	static public Move ponder(byte[][] field) {
		Move myMove = null;
		//clever strategy
		return myMove;
	}
	

	
	public static void main(String[] args){
		run();
	}
}