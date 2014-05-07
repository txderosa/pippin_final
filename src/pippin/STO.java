package pippin;

public class STO extends Instruction {

	public STO(Machine machine, MemoryInterface memory) {
		super(machine, memory);
	}

	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException {
		int retVal = getMachine().getAccumulator();
		if (indirect) {
			int location =  getMemoryInterface().getData(arg);
			 getMemoryInterface().setData(location, retVal);
		} else {
			// What is the index?
			 getMemoryInterface().setData(arg, retVal);
		}
		getMachine().incrementCounter();
		return retVal;
	}

}
