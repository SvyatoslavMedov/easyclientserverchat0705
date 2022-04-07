package medov.testserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADRESS = "localhost";
    private static final int SERVER_PORT = 8189;

    public static void main(String[] args) {
        Socket socket = null;
        Scanner scanner = new Scanner(System.in);
        try {
            socket = new Socket(SERVER_ADRESS,SERVER_PORT);
            System.out.println("Подключен к серверу: " + socket.getRemoteSocketAddress());
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            Thread threadReader = new Thread(()->{
                try {
                    while (true){
                        outputStream.writeUTF(scanner.nextLine());
                    }
                }catch(IOException e) {
                    e.printStackTrace();
                }
            });
            threadReader.setDaemon(true);
            threadReader.start();

            while (true) {
                String str = inputStream.readUTF();
                if(str.equals("/close")) {
                    System.out.println("Клиент покинул сервер");
                    outputStream.writeUTF("/close");
                    break;
                }else{
                    System.out.println("Клиент: " + str);
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                socket.close();
            }catch (IOException | NullPointerException e){
                e.printStackTrace();
            }
        }
    }
}
