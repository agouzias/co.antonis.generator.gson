package co.antonis.generator.gson;

import co.antonis.generator.gson.gwt.CodeGenerator;

import java.io.File;
import java.util.Set;

public class StartCodeGenerator {

    public static void main(String[] args) throws Exception {
        Set<Class<?>> st_guava = ListClasses.listClassesOfPackage("co.antonis.gwt.gson.pojo", null, null);

        Set<Class<?>> set_class_basic = ListClasses.listClass(
                "co.antonis.generator.model.sample.PojoParent"
                /*,
                "co.antonis.generator.model.sample.PojoSimple",
                "co.antonis.generator.model.sample.sub.PojoChild"
                */
        );


        new CodeGenerator()
                .setExportOnlyExpose(true)
                .setPrintLogInfo(false)
                .setGenerateToJsonMethods(true)
                .setGenerateFromJsonMethods(true)
                .setGeneratedClassName("SerGwtJson", true)
                .setGeneratedPackageName("co.antonis.gwt.example.client.generated")
                //Home
                //.setFilePathOfGeneratedFiles(new File("D:\\dev\\code\\apps\\co.antonis.generator\\generator-gson-gwt\\sample-gwt-app\\src\\main\\java"))
                //Work
                .setFilePathOfGeneratedFiles(new File("D:\\dev\\code\\git\\co.antonis.generator\\sample-app\\sample-gwt-app\\src\\main\\java"))
                .build(set_class_basic);
    }
}
