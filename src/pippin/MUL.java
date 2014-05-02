package pippin;

public class MUL extends Instruction {

	public MUL(Machine machine, MemoryInterface memory) {
		super(machine, memory);
	}

	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException {

		int retVal = getMachine().getAccumulator();

		if (indirect) {
			int arg1 =  getMemoryInterface().getData(arg);
			retVal *=  getMemoryInterface().getData(arg1);
		} else {
			retVal *=  getMemoryInterface().getData(arg);
		}

		getMachine().incrementCounter();
		return retVal;

	}

}
