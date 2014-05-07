package pippin;

public class DIV extends Instruction {

	public DIV(Machine machine, MemoryInterface memory) {

		super(machine, memory);
	}

	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException {

		int retVal = getMachine().getAccumulator();

		if ( getMemoryInterface().getData(arg) == 0) {
			throw new DivideByZeroException("attempt to divide by zero");
		}

		if (indirect) {
			int arg1 =  getMemoryInterface().getData(arg);
			if ( getMemoryInterface().getData(arg1) == 0) {
				throw new DivideByZeroException("attempt to divide by zero");
			}

			retVal /=  getMemoryInterface().getData(arg1);

		} else {

			retVal /=  getMemoryInterface().getData(arg);
		}

		getMachine().incrementCounter();
		return retVal;

	}

}
