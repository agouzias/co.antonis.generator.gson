package co.antonis.generator.gson;

import co.antonis.generator.gson.gwt.CodeGenerator;

import java.io.File;
import java.util.Set;

public class StartCodeGenerator {

    public static void main(String args[]) throws Exception {
        Set<Class<?>> st_guava = ReaderJava.listClassesOfPackage_GoogleGuice("co.antonis.gwt.gson.pojo");

        Set<Class<?>> set_class_basic = ReaderJava.listClass(
                "co.antonis.generator.model.samplePojo.PojoParent",
                "co.antonis.generator.model.samplePojo.PojoSimple",
                "co.antonis.generator.model.samplePojo.sub.PojoChild"
        );

        Set<Class<?>> set_class_solo = ReaderJava.listClass(
                "co.antonis.generator.model.samplePojo.PojoParent"
        );

        new CodeGenerator()
                .setExportOnlyExpose(true)
                .setPrintLogInfo(false)
                .setGenerateToJsonMethods(false)
                .setGenerateFromJsonMethods(true)
                .setGeneratedClassNameSingle("SerializationGWTJson")
                .setGeneratedPackageName("co.antonis.gwt.example.client.generated")
                //.setFilePathOfGeneratedFiles(new File("D:\\dev\\code\\apps\\co.antonis.gwt\\webapp-rest\\src\\main\\java\\"))
                .setFilePathOfGeneratedFiles(new File("D:\\dev\\code\\git\\co.antonis.generator\\sample-gwt-app\\src\\main\\java"))
                .build(set_class_solo);
    }
}
