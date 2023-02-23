package co.antonis.generator.gson;

import com.google.common.reflect.ClassPath;
import com.google.gson.annotations.Expose;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class ReaderJava {

    /**
     * @return null safe set
     */
    public static Set<Field> findFields(Class<?> classs, Class<? extends Annotation> annot) {
        Set<Field> set = new HashSet<>();
        Class<?> c = classs;
        while (c != null) {
            for (Field field : c.getDeclaredFields()) {
                if (annot == null || field.isAnnotationPresent(annot)) {
                    set.add(field);
                }
            }
            c = c.getSuperclass();
        }
        return set;
    }

    public static void printClasses(Set<Class<?>> setClasses) {
        setClasses.forEach((c) -> {
            System.out.println(c.getName());
            Set<Field> fields = findFields(c, Expose.class);
            fields.forEach((f) -> {
                System.out.println("\t" + f.getType().getName() + "[" + f.getName() + "]");
            });
        });
    }


    public static Set<Class<?>> listClassesOfPackage_GoogleGuice(String packageName) throws IOException {
        return ClassPath.from(ClassLoader.getSystemClassLoader())
                .getAllClasses()
                .stream()
                .filter(clazz -> clazz.getPackageName()
                        .startsWith(packageName))
                .map(clazz -> clazz.load())
                .collect(Collectors.toSet());
    }

    public static Set<Class<?>> listClass(String... className) throws IOException, ClassNotFoundException {
        List<Class<?>> list = new ArrayList<>();
        for(String cn:className){
            list.add(Class.forName(cn));
        }
        return new HashSet<>(list);
    }

   /* public static Set<Class<?>> listClassesOfPackage_ClassLoader(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName))
                .collect(Collectors.toSet());
    }*/

    private static Class<?> getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            // handle the exception
        }
        return null;
    }
}
