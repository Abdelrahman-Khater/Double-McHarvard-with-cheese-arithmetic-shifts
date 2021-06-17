public class Processor {
    private final int RSize = 64;
    private final int DMSize = 1<<11;
    private byte [] dataMemory;
    private short[] instructionMemory;
    private byte[] registers;
    private boolean[] statusRegister;
    private short PC;
    private short IR;
    private byte opCode;
    private byte R1;
    private byte R2orIMM;

    private int clockCycles;

    public Processor(short[] instructionMemory){
        this.instructionMemory = instructionMemory;
        dataMemory = new byte[DMSize];
        registers = new byte[RSize];
        statusRegister = new boolean[8];
        PC = 0;
        clockCycles =0;
        runInstructions();
        printAllRegisters();
        printInstructionMemory();
        printDataMemory();
    }

    private void printDataMemory() {
    }

    private void printInstructionMemory() {
    }

    private void printAllRegisters() {
    }

    private void runInstructions(){

    }

    private void fetch(){
        IR = instructionMemory[PC++];
    }

    private void decode(){
        opCode = (byte)(IR>>>12);
        R1 = (byte)((IR>>>6)&((1<<6)-1));
        R2orIMM = (byte) (IR &((1<<6)-1));
    }

    private void execute(){
        switch (opCode){
            case 0 : ADD();break;
        }
    }

    private void ADD(){

    }
}