package pippin;

public class JMPZ extends Instruction {

	public JMPZ(Machine machine, MemoryInterface memory) {
		super(machine, memory);
	}

	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException {

		int retVal = getMachine().getAccumulator();

		if (indirect) {

			if (retVal == 0) {
				int arg1 =  getMemoryInterface().getData(arg);
				getMachine().setProgramCounter(arg1);
			} else {
				getMachine().incrementCounter();
			}

		} else {
			if (retVal == 0) {
				getMachine().setProgramCounter(arg);
			} else {
				getMachine().incrementCounter();
			}

		}

	//	getMachine().incrementCounter();
		return retVal;

	}

}
