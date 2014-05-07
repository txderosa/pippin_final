package pippin;

public class DIVI extends Instruction {

    public DIVI(Machine machine, MemoryInterface memory) {
        super(machine, memory);
    }

    @Override
    public int execute(int arg, boolean indirect) throws DataAccessException {
        int retVal = getMachine().getAccumulator();
        if(arg == 0) {
            throw new DivideByZeroException("attempt to divide by zero");
        }
        retVal /= arg;          
        getMachine().incrementCounter();
        return retVal;      
    }
}