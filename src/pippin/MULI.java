package pippin;

public class MULI extends Instruction {

	public MULI(Machine machine, MemoryInterface memory) {

		super(machine, memory);
	}

	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException {

		int retVal = getMachine().getAccumulator();
		retVal *= arg;
		getMachine().incrementCounter();
		return retVal;

	}

}
