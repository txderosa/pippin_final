package pippin;

public class CMPL extends Instruction {

	public CMPL(Machine machine,MemoryInterface memory) {
		super(machine, memory);
	}

	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException {

		int retVal = getMachine().getAccumulator();
		int operand=  getMemoryInterface().getData(arg);
		
		if(operand<0){
			retVal=1;
		}else{
			retVal=0;
		}
		getMachine().incrementCounter();
		return retVal;

	}

}
