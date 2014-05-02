package pippin;

public class NOT extends Instruction {

	public NOT(Machine machine,MemoryInterface memory) {

		super(machine, memory);
	}

	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException {

		int retVal = getMachine().getAccumulator();

		if (retVal == 0) {
			retVal = 1;
		} else {
			retVal = 0;
		}

		getMachine().incrementCounter();
		return retVal;

	}

}
