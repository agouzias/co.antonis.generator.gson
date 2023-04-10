package co.antonis.generator.gson;

import com.google.common.reflect.ClassPath;
import com.google.gson.annotations.Expose;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class ListClasses {

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


    public static Set<Class<?>> listClassesOfPackage(String packageName, String[] listExcludePackage, String[] listExcludeClass) throws IOException {
        Set<Class<?>> setClasses = ClassPath.from(ClassLoader.getSystemClassLoader())
                .getAllClasses()
                .stream()
                .filter(clazz -> clazz.getPackageName()
                        .startsWith(packageName))
                .map(clazz -> clazz.load())
                .collect(Collectors.toSet());
        return filterClasses(setClasses, listExcludePackage, listExcludeClass);
    }

    public static Set<Class<?>> filterClasses(Set<Class<?>> setClass, String[] listExcludePackage, String[] listExcludeClass) {
        if ((listExcludePackage != null && listExcludePackage.length > 0) || (listExcludeClass != null && listExcludeClass.length > 0))
            return setClass.stream().filter(
                            (clazz) -> {
                                boolean isIncludeClass = true;

                                if (listExcludeClass != null) {
                                    for (String classExclude : listExcludeClass) {
                                        if (clazz.getName().equalsIgnoreCase(classExclude)) {
                                            isIncludeClass = false;
                                            break;
                                        }
                                    }
                                }

                                if (listExcludePackage != null) {
                                    for (String packageExclude : listExcludePackage) {
                                        if (clazz.getPackageName().equalsIgnoreCase(packageExclude)) {
                                            isIncludeClass = false;
                                            break;
                                        }
                                    }
                                }

                                return isIncludeClass;

                            })
                    .collect(Collectors.toSet());

        return setClass;
    }

    public static Set<Class<?>> listClass(String... className) throws IOException, ClassNotFoundException {
        List<Class<?>> list = new ArrayList<>();
        for (String cn : className) {
            list.add(Class.forName(cn));
        }
        return new HashSet<>(list);
    }

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
