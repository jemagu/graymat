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
import java.util.*;
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
        System.out.println(args.length);
        try {
            Set<String> dictionary = new HashSet<>();
            Set<Path> paths = new HashSet<>();
            paths.add(Paths.get(args[1]));
            for (Path p : paths) {
                try {
                    dictionary.addAll(Files.readAllLines(p));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Gramat.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try (Scanner s = new Scanner(new File(args[2]))) {
                while (s.hasNext()) {
                    dictionary.add(s.next().split(";")[0]);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Gramat.class.getName()).log(Level.SEVERE, null, ex);
            }

            Path folder = Paths.get(args[0]);

            final List<JavaFile> files = new ArrayList<>();

            Files.walkFileTree(folder, new FileVisitor<>() {

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

            System.out.println("Bad words: "+errorMap.size());
            for (Map.Entry<String, Long> entry : errorMap.entrySet()) {
                System.out.println(entry.getKey()+ " : " + entry.getValue());
            }
            learn(errorMap);
        } catch (IOException ex) {
            Logger.getLogger(Gramat.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Gramat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void learn(HashMap<String, Long> errorMap) {
        try (Scanner scanner = new Scanner(System.in)) {
            List<String> one = new ArrayList<>();
            List<String> two = new ArrayList<>();
            for (Map.Entry<String, Long> entry : errorMap.entrySet()) {
                System.out.print("Add '" + entry.getKey() + "' to which dict? (1) Words, (2) Local or (3) None. ");
                int anInt;
                try {
                    anInt = scanner.nextInt();
                } catch (InputMismatchException ime) {
                    continue;
                }
                if (anInt == 1) {
                    one.add(entry.getKey());
                } else if (anInt == 2) {
                    two.add(entry.getKey());
                }
            }
            one.stream().forEach(word -> System.out.println(word));
            two.stream().forEach(word -> System.out.println(word));
        }
    }

    private static JavaFile doFile(File file) {
        CompilationUnit cu;
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
