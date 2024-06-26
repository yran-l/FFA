import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    // x y coordinates of this player
    // write to ClientHandler

    private GamePanel panel;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private ArrayList<OtherPlayers> others = new ArrayList<>();
    private String previousLine;

    public Client(Socket socket, GamePanel panel) {
        try {
            this.panel = panel;
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void readFromClientHandler() { // gets movement from other ppl / reads it
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()) {
                    try {
                        String line = bufferedReader.readLine();
//                        if (!line.endsWith("SELF")) {
                            int temp_x = Integer.parseInt(line.substring(line.indexOf("X:") + 2, line.indexOf("Y:")));
                            int temp_y = Integer.parseInt(line.substring(line.indexOf("Y:") + 2, line.indexOf("IMAGE:")));
                            String image = line.substring(line.indexOf("IMAGE:") + 6, line.indexOf("DEAD:"));
                            boolean dead = Boolean.parseBoolean(line.substring(line.indexOf("DEAD:") + 5, line.indexOf("USERNAME:")));
                            String username = line.substring(line.indexOf("USERNAME:") + 9); // , line.indexOf("STATUS:"));
//                            boolean disconnected = Boolean.parseBoolean(line.substring(line.indexOf("STATUS:") + 6));
                            boolean playerAlreadyExists = false;
                            for (int i = 0; i < panel.getOtherPlayers().size(); i++) {
                                if (panel.getOtherPlayers().get(i).getUsername().equals(username)) {
                                    panel.getOtherPlayers().get(i).setX(temp_x);
                                    panel.getOtherPlayers().get(i).setY(temp_y);
                                    panel.getOtherPlayers().get(i).setImage(image);
                                    panel.getOtherPlayers().get(i).setDead(dead);
//                                    panel.getOtherPlayers().get(i).setStatus(disconnected);
                                    playerAlreadyExists = true;
                                }
                            }
                            if (!playerAlreadyExists) {
                                OtherPlayers newPlayer = new OtherPlayers(temp_x, temp_y, image, username); //disconnected);
                                panel.getOtherPlayers().add(newPlayer);
                            }
//                        }
//                        if (previousLine.endsWith("SELF")){
//                            boolean disconnected = Boolean.parseBoolean(line.substring(line.indexOf("STATUS:") + 6, line.indexOf("SELF")));
//                            System.out.println(disconnected);
//                            panel.player.setStatus(disconnected);
//                        }
                    } catch (IOException e) {
                        System.out.println("UNABLE TO READ OTHER PLAYERS MOVEMENT");
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }
    public void writeToClientHandler(Map map, Player player){ // sets movement / writes it
        while(socket.isConnected()) {
            try {
                String tempLine = "X:" + map.x + "Y:" + map.y + "IMAGE:" + player.imageFile + "DEAD:" + player.isDead();
                if (!tempLine.equals(previousLine)) {
                    //System.out.println(tempLine);
                    previousLine = tempLine;
                    bufferedWriter.write(tempLine);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}