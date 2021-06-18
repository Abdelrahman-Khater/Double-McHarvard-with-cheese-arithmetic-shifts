import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Locale;

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
    // mustafa and youssef
    //can imm be negative?
    void parseInstruction(String line , int index){
        line=line.toUpperCase();
        String[] tmp=line.split(" ");
        int opcode=0;
        int R1index=Integer.parseInt(tmp[1].substring(1));
        int R2orimm=tmp[2].charAt(0)=='R'?Integer.parseInt(tmp[2].substring(1)):Integer.parseInt(tmp[2]);
        switch (tmp[0]){
            case "ADD":opcode=0;break;
            case "SUB":opcode=1;;break;
            case "MUL":opcode=2;break;
            case "MOVI":opcode=3;break;
            case "BEQZ":opcode=4;break;
            case "ANDI":opcode=5;break;
            case "EOR":opcode=6;break;
            case "BR":opcode=7;break;
            case "SAL":opcode=8;break;
            case "SAR":opcode=9;break;
            case "LDR":opcode=10;break;
            case "STR":opcode=11;break;
            default:
        }
        short instruction= (short) (((opcode<<6|R1index)<<6)|R2orimm);
        instructionMemory[index]= instruction;
    }
}