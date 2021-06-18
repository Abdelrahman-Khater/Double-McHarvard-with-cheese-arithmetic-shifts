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
        int instruction=0;
        int R1index=Integer.parseInt(tmp[1].substring(1));
        int R2orimm=tmp[2].charAt(0)=='R'?Integer.parseInt(tmp[2].substring(1)):Integer.parseInt(tmp[2]);
        switch (tmp[0]){
            case "ADD":instruction=((0<<6|R1index)<<6)|R2orimm;break;
            case "SUB":instruction=(1<<6|R1index)<<6|R2orimm;break;
            case "MUL":instruction=(2<<6|R1index)<<6|R2orimm;break;
            case "MOVI":instruction=(3<<6|R1index)<<6|R2orimm;break;
            case "BEQZ":instruction=(4<<6|R1index)<<6|R2orimm;break;
            case "ANDI":instruction=(5<<6|R1index)<<6|R2orimm;break;
            case "EOR":instruction=(6<<6|R1index)<<6|R2orimm;break;
            case "BR":instruction=(7<<6|R1index)<<6|R2orimm;break;
            case "SAL":instruction=(8<<6|R1index)<<6|R2orimm;break;
            case "SAR":instruction=(9<<6|R1index)<<6|R2orimm;break;
            case "LDR":instruction=(10<<6|R1index)<<6|R2orimm;break;
            case "STR":instruction=(11<<6|R1index)<<6|R2orimm;break;
            default:
        }
        instructionMemory[index]= (short) instruction;
    }
}