package pippin;

abstract public class Instruction { 
    private Machine machine;
    private MemoryInterface memoryInterface;

    public Instruction(Machine machine, MemoryInterface memoryInterface) {
        this.machine = machine;
        this.memoryInterface = memoryInterface;
    }

    /** 
     * Method to return the name of this instruction, e.g. "NOP" "LOD" 
     * @return the name of the instruction 
     */ 
    @Override
    public String toString() { 
        return getClass().getSimpleName();
    } 

    /** 
     * Method to execute this instruction for the given argument. The details 
     * are explained in list of instructions for the Pippin computer. 
     * NOTE: If the instruction does not use an argument, then the argument 
     * is passed as 0 
     * @param arg the argument passed to the instruction
     * @param indirect indicates if the addressing mode is indirect
     * @return returns the new value of the accumulator in the CPU
     * @throws DataAccessException if access to the data in MemoryInterface throws an 
     * exception
     */
    public abstract int execute(int arg, boolean indirect) 
            throws DataAccessException;

    /**
     * Getter method for machine
     * @return a reference to the machine field 
     */
    public Machine getMachine() {
        return machine;
    }

    /**
     * Getter method for MemoryInterface
     * @return a reference to the MemoryInterface field 
     */
    public MemoryInterface getMemoryInterface() {
        return memoryInterface;
    }
} 