import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.Socket;
import java.util.Vector;
import java.util.Collections;

public class SocketListener extends Thread{

    private Socket socket;
    private BufferedReader is;
    private PlayerInit pi;
	private byte[] buffer = new byte[1024];
	String line;
	public Vector<Character> characterList;

    public SocketListener(Socket socket, PlayerInit pi){
        this.socket = socket;
        this.pi = pi;
		characterList = new Vector<Character>();
    }


    @Override
    public void run(){
        try {
            is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
		try{
			while((line = is.readLine()) != null){
				try {
					Character c = new Character();
					String[] split = line.trim().split("\\s+");
					String command = split[0];
					if(!command.equals("next") || !command.equals("reorder")){
						c.setName(split[1]);
						c.setCurHP(Integer.parseInt(split[2]));
						c.setMaxHP(Integer.parseInt(split[3]));
						c.setInit(Integer.parseInt(split[4]));
					}
					if(command.equals("add")){
						PlayerInit.Characters.add(c);
					}
					else if(command.equals("remove")){
						int index = -1;
						for(Character u : PlayerInit.Characters){
							if(u.getName().equals(c.getName())){ 
								index = PlayerInit.Characters.indexOf(u);
								break;
							}
						}
						System.out.printf("%d\n", index);
						if(index != -1){
							PlayerInit.Characters.remove(index);
						}
						else{
							System.out.println("Unable to find character");
						}
					}
					else if(command.equals("update")){
						for(Character u : PlayerInit.Characters){
							if(u.getName().equals(c.getName())){
								u.setCurHP(c.getCurHP());
								u.setMaxHP(c.getMaxHP());
								u.setInit(c.getInit());
							}
						}
					}
					else if(command.equals("reorder")){
						Collections.sort(PlayerInit.Characters, Collections.reverseOrder());
					}
					else if(command.equals("next")){
						PlayerInit.rotate(PlayerInit.Characters, 1);
					}
					else{
						System.out.println("Unknown Command");
						c = null;
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				finally{
					pi.reDraw(pi);
				}
			}
		}
		catch(IOException io){
			io.printStackTrace();
		}
    }
}