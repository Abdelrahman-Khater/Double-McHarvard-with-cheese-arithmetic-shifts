import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Assembler {
    private String filePath;
    private short[] instructionMemory;
    public Assembler(String filepath, short[] instructionMemory){
        this.filePath = filepath;
        this.instructionMemory = instructionMemory;

    }

    void parse(){
        int index =0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            while (br.ready()){
                String line = br.readLine();
                parseInstruction(line,index);
                index++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // to parse the line of assembly to binary and added to instructionMemory at the given index;
    void parseInstruction(String line , int index){

    }
}