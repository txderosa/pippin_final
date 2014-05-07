package pippin;

public class ADDI extends Instruction {

    public ADDI(Machine machine,MemoryInterface memory) {
        super(machine, memory);
    }

    @Override
    public int execute(int arg, boolean indirect)
            throws DataAccessException {
        int retVal = getMachine().getAccumulator() + arg;           
        getMachine().incrementCounter();
        return retVal;      
    }
    // helllo work?
}