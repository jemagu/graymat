package se.gunta.graymat;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.util.List;

public class MethodVisitor extends VoidVisitorAdapter {

    @Override
    public void visit(MethodDeclaration methodDeclaration, Object javaFileObject) {
        JavaFile javaFile = (JavaFile) javaFileObject;
        javaFile.getMethods().add(new Method(methodDeclaration.getName(), CamelToSentence.doit(methodDeclaration.getName()), methodDeclaration.getBeginLine()));
        /*
        List<Parameter> params = methodDeclaration.getParameters();
        System.out.println(javaFile.getFileName());
        for (Parameter parameter : params) {
            System.out.println("|"+parameter.getId().getName());
        }
        */
    }
}
