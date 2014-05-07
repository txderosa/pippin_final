package pippin;

public class LOD extends Instruction {

	public LOD(Machine machine,MemoryInterface memory) {
		super(machine, memory);
	}

	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException {
		int retVal = getMemoryInterface().getData(arg);
		if (indirect) {
			 retVal =  getMemoryInterface().getData(retVal);
		}
		getMachine().incrementCounter();
		return retVal;
	}
}
