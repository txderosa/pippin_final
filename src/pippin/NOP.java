package pippin;

public class NOP extends Instruction {

	public NOP(Machine machine, MemoryInterface memory) {
		super(machine, memory);
	}

	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException {
		int retVal = getMachine().getAccumulator();
		getMachine().incrementCounter();
		return retVal;

	}

}
