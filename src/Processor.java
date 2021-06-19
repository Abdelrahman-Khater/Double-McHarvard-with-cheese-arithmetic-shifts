import java.util.Arrays;

public class Processor {
    private static final int C_INDEX = 4, V_INDEX = 3, N_INDEX = 2, S_INDEX = 1, Z_INDEX = 0;
    private final int RSize = 64;
    private final int DMSize = 1 << 11;
    private byte[] dataMemory;
    private short[] instructionMemory;
    private byte[] registers;
    private boolean[] statusRegister;
    private int numberOfInstructions;
    private short PC;
    private short IR;
    private byte opCode;
    private byte R1;
    private byte R2orIMM;

    private boolean fetchFlag;
    private boolean decodeFlag;
    private boolean executeFlag;

    public Processor(short[] instructionMemory, int numberOfInstructions) {
        this.instructionMemory = instructionMemory;
        this.numberOfInstructions = numberOfInstructions;
        dataMemory = new byte[DMSize];
        registers = new byte[RSize];
        statusRegister = new boolean[8];
        PC = 0;
        runInstructions();
        printAllRegisters();
        printInstructionMemory();
        printDataMemory();
    }

    private void printDataMemory() {
        System.out.printf("*Content of the Data Memory:\n%s\n", Arrays.toString(dataMemory));
    }

    private void printInstructionMemory() {
        System.out.printf("*Content of the Instruction Memory:\n%s\n", Arrays.toString(instructionMemory));
    }

    private void printAllRegisters() {
        System.out.println("*Content of the registers:");
        for(int i = 0; i < RSize; i++) {
            //System.out.printf("Content of register R%d is: %s:\n", i, fixNumberOfBits(Integer.toBinaryString(registers[i]), 8));
            System.out.printf("Content of register R%d is: %s:\n", i, registers[i]);
        }
        String SREG = "";
        for(int i = 0; i < 8; i++)
            SREG = (statusRegister[i] ? "1" : "0") + SREG;
        System.out.printf("Content of register SREG is: %s:\n", SREG);
        System.out.printf("Content of register PC is: %s:\n", PC);
    }

    private void runInstructions() {
        int clockCycles = 0;
        while (true) {
            if (PC < numberOfInstructions)
                fetchFlag = true;
            if(!fetchFlag && ! decodeFlag && !executeFlag)
                break;

            System.out.printf("*Current Clock Cycle: %d\n", ++clockCycles);
            if (executeFlag) {
                System.out.printf("*Current instruction being executed: %d\n", PC - 2);
                System.out.printf("*Input Parameters for execution:\nOperation: %s\nR1: %s\nR2orIMM: %s\n", fixNumberOfBits(Integer.toBinaryString(opCode), 4),
                        fixNumberOfBits(Integer.toBinaryString(R1), 6), fixNumberOfBits(Integer.toBinaryString(R2orIMM), 6));
                execute();
                executeFlag = false;
            }
            if (decodeFlag) {
                System.out.printf("*Current instruction being decoded: %d\n", PC - 1);
                System.out.printf("*Input Parameters for decoding:\nInstruction: %s\n", fixNumberOfBits(Integer.toBinaryString(IR), 16));
                decode();
                executeFlag = true;
                decodeFlag = false;
            }
            if (fetchFlag) {
                System.out.printf("*Current instruction being fetched: %d\n", PC);
                System.out.printf("*Input Parameters for fetching:\nPC: %d\n", PC);
                fetch();
                decodeFlag = true;
                fetchFlag = false;
            }
            System.out.print("\n");
        }
    }

    private void emptyPipeLine() {
        fetchFlag = false;
        decodeFlag = false;
        executeFlag = false;
    }

    private void fetch() {
        IR = instructionMemory[PC++];
    }

    private void decode() {
        opCode = (byte) (IR >>> 12);
        R1 = (byte) ((IR >>> 6) & ((1 << 6) - 1));
        R2orIMM = (byte) (IR & ((1 << 6) - 1));
    }

    // yahia first 4
    //omar next 4
    //khater last4
    private void execute() {
        switch (opCode) {
            case 0:
                ADD();
                break;
            case 1:
                SUB();
                break;
            case 2:
                MUL();
                break;
            case 3:
                MOVI();
                break;
            case 4:
                BEQZ();
                break;
            case 5:
                ANDI();
                break;
            case 6:
                EOR();
                break;
            case 7:
                BR();
                break;
            case 8:
                SAL();
                break;
            case 9:
                SAR();
                break;
            case 10:
                LDR();
                break;
            case 11:
                STR();
                break;
        }
    }

    private void setCarry(boolean value) {
        statusRegister[C_INDEX] = value;
    }

    private void setOverflow(boolean value) {
        statusRegister[V_INDEX] = value;
    }

    private void setNegative(boolean value) {
        statusRegister[N_INDEX] = value;
    }

    private void updateSFlag() {
        statusRegister[S_INDEX] = statusRegister[N_INDEX] ^ statusRegister[V_INDEX];
    }

    private void setZero(boolean value) {
        statusRegister[Z_INDEX] = value;
    }

    private boolean isSameSign(byte a, byte b) {
        return (a >= 0) == (b >= 0);
    }

    public String fixNumberOfBits(String number, int numberOfBits) {
        while (number.length() < numberOfBits)
            number = number.charAt(0) + number;
        return number.substring(number.length() - numberOfBits, number.length());
    }

    private void ADD() {
        byte op1 = registers[R1];
        byte op2 = registers[R2orIMM];
        int res = op1 + op2;
        if ((res & (1 << 8)) != 0) {
            setCarry(true);
        } else {
            setCarry(false);
        }

        byte byteRes = (byte) res;

        if (isSameSign(op1, op2) && !isSameSign(op1, byteRes)) {
            setOverflow(true);
        } else {
            setOverflow(false);
        }
        setNegative(byteRes < 0);
        updateSFlag();
        setZero(byteRes == 0);
        registers[R1] = byteRes;
    }

    private void SUB() {
        byte op1 = registers[R1];
        byte op2 = registers[R2orIMM];
        int res = op1 - op2;
        if ((res & (1 << 8)) != 0) {
            setCarry(true);
        } else {
            setCarry(false);
        }

        byte byteRes = (byte) res;

        if (!isSameSign(op1, op2) && isSameSign(op2, byteRes)) {
            setOverflow(true);
        } else {
            setOverflow(false);
        }

        setNegative(byteRes < 0);
        updateSFlag();
        setZero(byteRes == 0);
        registers[R1] = byteRes;
    }

    private void MUL() {
        int res = registers[R1] * registers[R2orIMM];
        if ((res & (1 << 8)) != 0) {
            setCarry(true);
        } else {
            setCarry(false);
        }
        byte byteRes = (byte) res;
        setNegative(byteRes < 0);
        setZero(byteRes == 0);
        registers[R1] = byteRes;
    }

    private void MOVI() {
        registers[R1] = R2orIMM;
    }

    private void BEQZ() {
        if (registers[R1] == 0) {
            PC = (short) (PC - 1 + R2orIMM);
            emptyPipeLine();
        }
    }

    private void ANDI() {
        byte res = (byte) (registers[R1] & R2orIMM);
        setNegative(res < 0);
        setZero(res == 0);
        registers[R1] = res;
    }

    private void EOR() {
        byte res = (byte) (registers[R1] ^ R2orIMM);
        setNegative(res < 0);
        setZero(res == 0);
        registers[R1] = res;
    }

    private void BR() {
        String R1Bits = fixNumberOfBits(Integer.toBinaryString(registers[R1]), 6);
        String R2Bits = fixNumberOfBits(Integer.toBinaryString(registers[R2orIMM]), 6);
        short concatenationResult = Short.parseShort(R1Bits + R2Bits, 2);
        PC = concatenationResult;
        emptyPipeLine();
    }

    private void SAL() {
        int res = registers[R1];
        res = res << R2orIMM;
        byte byteRes = (byte) res;
        setNegative(byteRes < 0);
        setZero(byteRes == 0);
        registers[R1] = byteRes;
    }

    private void SAR() {
        int res = registers[R1];
        res = res >> R2orIMM;
        byte byteRes = (byte) res;
        setNegative(byteRes < 0);
        setZero(byteRes == 0);
        registers[R1] = byteRes;
    }

    private void LDR() {
        registers[R1] = dataMemory[R2orIMM];
    }

    private void STR() {
        dataMemory[R2orIMM] = registers[R1];
    }
}