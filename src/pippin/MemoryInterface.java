package pippin;
public interface MemoryInterface { 
	static final int DATA_SIZE = 512; 
	static final int CODE_SIZE = 256;
	
	void setData(int index, int value) throws DataAccessException; 
	int getData(int index) throws DataAccessException;
	void setCode(int index, long value) throws CodeAccessException; 
	long getCode(int index) throws CodeAccessException;
	void clearCode();
	void clearData();
}