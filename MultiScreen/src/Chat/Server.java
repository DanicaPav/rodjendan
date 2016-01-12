package Chat;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Server {
	private AcceptThread connThread = new AcceptThread();
	private MessageHandlerThread connThread2 = new MessageHandlerThread();

    private Consumer<Serializable> onReceiveCallback;
    private ArrayList<Socket> sockets = new ArrayList<Socket>();
    private ArrayList<ObjectOutputStream> outs = new ArrayList<ObjectOutputStream>();
    private ArrayList<ObjectInputStream> ins = new ArrayList<ObjectInputStream>();
    private ServerSocket server;
    
    public String ip;
    public int port;

    public Server(int port, Consumer<Serializable> onReceiveCallback) {
        this.onReceiveCallback = onReceiveCallback;
        this.port = port;
        connThread.setDaemon(true);
        connThread2.setDaemon(true);
    }

    public void startConnection() throws Exception {
        connThread.start();
        connThread2.start();
        
    }

    public void send(Serializable data) throws Exception {
        
    }

    public void closeConnection() throws Exception {
        //connThread.socket.close();
    	for (int i = 0; i < sockets.size(); i++) {
    		sockets.get(i).close();
    	}
        server.close();
    }

    private class AcceptThread extends Thread {
   
        @Override
        public void run() {
            try {
            	server = new ServerSocket(port);
            
            	while (true) {
                	Socket socket = server.accept();
                	socket.setTcpNoDelay(true);
                	sockets.add(socket); 
                	ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                	outs.add(out);
                	ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                	ins.add(in);
                	         
            	}
            }
            catch (Exception e) {
                onReceiveCallback.accept("Connection closed");
            }
        }
    }


	private class MessageHandlerThread extends Thread {
	    
	
	    
	
		@Override
	    public void run() {
	        try {
	
	        
	        	while(true) {
	        	
	            	for (int i = 0; i < ins.size(); i++) { 
	            		System.out.println("Proba");
	        			Serializable data = (Serializable) ins.get(i).readObject();          		             		
	            		for (int j = 0; j < outs.size(); j++) {
	            			outs.get(j).writeObject(data);
	            			onReceiveCallback.accept(data); 
	            		}
	            	}
	            }
	        }
	        catch (Exception e) {
	            onReceiveCallback.accept("Connection closed2");
	        }
	    }
	}
}
