package se.gunta.graymat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Magnus Gunnarsson
 */
public class JavaFile {
    private String fileName;
    private List<Method> methods;

    public JavaFile() {
        methods = new ArrayList<>();
    }
    
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public void setMethods(List<Method> methods) {
        this.methods = methods;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (Method method : getMethods()) {
            string.append(method.toString());
            string.append('\n');
        }
        return string.toString();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.fileName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JavaFile other = (JavaFile) obj;
        return true;
    }
    
    
}
