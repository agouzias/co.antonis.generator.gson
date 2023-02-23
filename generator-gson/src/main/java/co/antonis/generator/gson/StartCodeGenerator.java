package co.antonis.generator.gson;

import co.antonis.generator.gson.gwt.CodeGenerator;

import java.io.File;
import java.util.Set;

public class StartCodeGenerator {
    public static void main(String args[]) throws Exception {
        Set<Class<?>> st_guava = ReaderJava.listClassesOfPackage_GoogleGuice("co.antonis.gwt.gson.pojo");
        Set<Class<?>> st_class = ReaderJava.listClass(
                "co.antonis.generator.model.samplePojo.PojoParent",
                "co.antonis.generator.model.samplePojo.PojoSimple",
                "co.antonis.generator.model.samplePojo.sub.PojoChild"
        );

        new CodeGenerator()
                .setExportOnlyExpose(true)
                .setPrintLogInfo(true)
                .setGeneratedClassNameSingle("SerializationGWTJson")
                .setGeneratedPackageName("co.antonis.gwt.example.client.generated")
                //.setFilePathOfGeneratedFiles(new File("D:\\dev\\code\\apps\\co.antonis.gwt\\webapp-rest\\src\\main\\java\\"))
                .setFilePathOfGeneratedFiles(new File("D:\\dev\\code\\git\\co.antonis.generator\\webapp-gwt-rest\\src\\main\\java"))
                .build(st_class);
    }
}
