package se.gunta.graymat;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Magnus Gunnarsson
 */
public class Gramat {

    private static Set<String> unikaParams = new HashSet<>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Set<String> dictionary = new HashSet<>();
            Set<Path> paths = new HashSet<>();
            //paths.add(Paths.get("C:\\p\\aardvark.txt"));
            paths.add(Paths.get("C:\\p\\svenska.txt"));
            for (Path p : paths) {
                try {
                    dictionary.addAll(Files.readAllLines(p));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Gramat.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try (Scanner s = new Scanner(new File("C:\\p\\local.txt"))) {
                while (s.hasNext()) {
                    dictionary.add(s.next().split(";")[0]);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Gramat.class.getName()).log(Level.SEVERE, null, ex);
            }

            //File folder = new File("C:\\p\\UFF\\Klyx\\");
            Path folder = Paths.get("C:\\p\\UFF\\Adressreg\\");
            //File folder = new File("C:\\p\\UFF\\Common\\");

            final List<JavaFile> files = new ArrayList<>();

            Files.walkFileTree(folder, new FileVisitor<Path>() {

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.toFile().getName().endsWith(".java")) {
                        files.add(doFile(file.toFile()));
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });

            int errors = 0;
            HashMap<String, Long> errorMap = new HashMap<>();
            for (JavaFile file : files) {
                if (file == null) {
                    continue;
                }
                System.out.println(file.getFileName());
                for (Method method : file.getMethods()) {
                    for (String word : method.getSentance().split(" ")) {
                        if (word.length() > 1 && !dictionary.contains(word)) {
                            method.getErrors().add(word + " not known.");
                            errors++;
                            System.out.println(word);
                            System.out.println(method);
                            if (errorMap.containsKey(word)) {
                                Long count = errorMap.get(word);
                                errorMap.put(word, count + 1);
                            } else {
                                errorMap.put(word, 1L);
                            }
                        }
                    }
                }
            }
            System.out.println("Errors: " + errors);
            System.out.println("Count: " + dictionary.size());

            for (Map.Entry<String, Long> entry : errorMap.entrySet()) {
                //System.out.println(entry.getKey()+ " : " + entry.getValue());
                System.out.println(entry.getKey());
            }
        } catch (IOException ex) {
            Logger.getLogger(Gramat.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Gramat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static JavaFile doFile(File file) {
        CompilationUnit cu = null;
        try (InputStream in = new FileInputStream(file)) {
            JavaFile javaFile = new JavaFile();
            javaFile.setFileName(file.getCanonicalPath());
            System.out.println(file.getCanonicalPath());
            cu = JavaParser.parse(in);
            MethodVisitor methodVisitor = new MethodVisitor();
            methodVisitor.visit(cu, javaFile);
            return javaFile;
        } catch (ParseException x) {
            Logger.getLogger(Gramat.class.getName()).log(Level.SEVERE, null, x);
            // handle parse exceptions here.
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Gramat.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Gramat.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
