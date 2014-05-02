package pippin;

import java.util.Arrays;

public class Memory implements MemoryInterface{
    // this needs more work for the whole Pippin project
    private int[] data = new int[DATA_SIZE];
    private long[] code = new long [CODE_SIZE];

    public void setData(int index, int value) throws DataAccessException {
    	if(index < 0 || index >= data.length) {
            throw new DataAccessException("ERROR: Cannot access data location " + index);
        }
        data[index] = value;
    }

    public int getData(int index) throws DataAccessException {
        if(index < 0 || index >= data.length) {
            throw new DataAccessException("ERROR: Cannot access data location " + index);
        }
        return data[index];
    }

    public void setCode(int index, long value) throws CodeAccessException {
        if(index < 0 || index >= code.length) {
            throw new CodeAccessException("ERROR: Cannot access code location " + index);
        }
        code[index] = value;
    }

    public long getCode(int index) throws CodeAccessException {
        if(index < 0 || index >= code.length) {
            throw new CodeAccessException("ERROR: Cannot access code location " + index);
        }
        return code[index];
    }

    // Note package private -- just for JUnit test
    long[] getCode() {
        return code;
    }
    
    int[] getData() {
        return data;
    }

    public void clearCode() {
        Arrays.fill(code, 0);   
    }

    public void clearData() {
        Arrays.fill(data, 0);       
    }       
}