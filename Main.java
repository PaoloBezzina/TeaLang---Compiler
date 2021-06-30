import lexer.*;
import java.io.*;


public class Main {

    public static void main(String[] args){

        String filePath = "sampleCode/teaTest.txt";

        File file = new File(filePath);
        String program = "";

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String st = null;
        while (true) {
            try {
                if (!((st = br.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println(st);
            program += (st + "\n");
        }

        //Lexer
        System.out.println(program);
        Lexer lex = new Lexer(program);
    }
}