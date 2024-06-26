import javax.swing.JPanel;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class Map extends JPanel{
    public Tile[][] tiles = new Tile[44][70];
    public int x, y, speed; // make private

    public Map(int x, int y, int speed){
        fillTiles();
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public void fillTiles(){
        File f = null;
        try{
            f = new File("MAPS/4");
            Scanner s = new Scanner(f);
            int col = 0;
            while (s.hasNextLine()){
                String str = s.nextLine();
                for (int i = 0;i < str.length();i++){
                    if (str.charAt(i) == '('){
                        tiles[i][col] = new Border("tl");
                    }
                    else if (str.charAt(i) == '<'){
                        tiles[i][col] = new Border("bl");
                    }
                    else if (str.charAt(i) == ')'){
                        tiles[i][col] = new Border("tr");
                    }
                    else if (str.charAt(i) == '>'){
                        tiles[i][col] = new Border("br");
                    }
                    else if (str.charAt(i) == '0'){
                        tiles[i][col] = new Border("v");
                    }
                    else if (str.charAt(i) == '1'){
                        tiles[i][col] = new Border("h");
                    }
                    else if (str.charAt(i) == '2'){
                        tiles[i][col] = new Floor();
                    }
                    else if (str.charAt(i) == '3'){
                        tiles[i][col] = new Border("3D");
                    }
                    else if (str.charAt(i) == 'W'){
                        tiles[i][col] = new Border("dt");
                    }
                    else if (str.charAt(i) == 'A'){
                        tiles[i][col] = new Border("dl");
                    }
                    else if (str.charAt(i) == 'S'){
                        tiles[i][col] = new Border("dd");
                    }
                    else if (str.charAt(i) == 'D'){
                        tiles[i][col] = new Border("dr");
                    }
                    else if (str.charAt(i) == 'B'){
                        tiles[i][col] = new Background();
                    }
                }
                col++;
            }
        }
        catch (FileNotFoundException e){
            System.out.println("gg");
        }
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }
}
