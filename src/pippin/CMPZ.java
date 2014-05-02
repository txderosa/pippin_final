package pippin;

public class CMPZ extends Instruction {

	public CMPZ(Machine machine, MemoryInterface memory) {

		super(machine, memory);
	}

	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException {

		int retVal = getMachine().getAccumulator();
		int operand =  getMemoryInterface().getData(arg);

		if (operand == 0) {
			retVal = 1;
		} else {
			retVal = 0;
		}

		getMachine().incrementCounter();
		return retVal;

	}

}
