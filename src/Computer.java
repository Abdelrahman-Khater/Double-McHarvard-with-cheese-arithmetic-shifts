public class Computer {
    final int IMSize = 1<<10;
    short[] instructionMemory;
    Assembler assembler ;
    Processor processor;


    public Computer(String filePath){
        instructionMemory = new short[IMSize];
        assembler = new Assembler(filePath,instructionMemory);
        assembler.parse();
        processor = new Processor(instructionMemory);
    }

    public static void main(String[] args) {
        String filePath = "assembly.txt";
        Computer computer = new Computer(filePath);
    }

}