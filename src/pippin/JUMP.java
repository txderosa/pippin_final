package pippin;

public class JUMP extends Instruction {

	public JUMP(Machine machine, MemoryInterface memory) {
		super(machine, memory);
	}

	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException {

		int retVal = getMachine().getAccumulator();
		if (indirect) {
			int arg1 =  getMemoryInterface().getData(arg);
			getMachine().setProgramCounter(arg1);
		} else {
			getMachine().setProgramCounter(arg);
		}

		return retVal;
	}

}
