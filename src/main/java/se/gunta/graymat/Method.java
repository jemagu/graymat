package se.gunta.graymat;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Magnus Gunnarsson
 */
public class Method {
    private final String methodName;
    private final String sentance;
    private final int rowNumber;
    private final List<String> errors;

    public Method(String methodName, String sentance, int rowNumber) {
        this.methodName = methodName;
        this.sentance = sentance;
        this.rowNumber = rowNumber;
        this.errors = new ArrayList();
    }

    public String getMethodName() {
        return methodName;
    }

    public String getSentance() {
        return sentance;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public List<String> getErrors() {
        return errors;
    }
    
    @Override
    public String toString() {
        return "Method{" + "methodName=" + methodName + ", sentance=" + sentance + ", rowNumber=" + rowNumber + '}';
    }
}
