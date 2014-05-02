package pippin;

public class HALT extends Instruction {

	public HALT(Machine machine, MemoryInterface memory) {
		super(machine, memory);
	}

	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException {
		if (getMachine() != null) {
			getMachine().halt();
		}
		return getMachine().getAccumulator();

	}

}
