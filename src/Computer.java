import jdk.jshell.EvalException;

public class Computer {
    final int IMSize = 1 << 10;
    short[] instructionMemory;
    Assembler assembler;
    Processor processor;


    public Computer(String filePath) {
        instructionMemory = new short[IMSize];
        assembler = new Assembler(filePath, instructionMemory);
        int numberOfInstructions = assembler.parse();
        processor = new Processor(instructionMemory, numberOfInstructions);
    }

    public static void main(String[] args) {
        String filePath = "assembly.txt";
        Computer computer = new Computer(filePath);
    }
}