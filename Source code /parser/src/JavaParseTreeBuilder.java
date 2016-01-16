import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.net.*;

/**
 * Created by shekhar on 30/9/15.
 */
public class JavaParseTreeBuilder {
    String class_entered ;
    String  extendedclasses;
    List<String> list_of_interfaces = new ArrayList<>();
    List<List<String>> list_variables = new ArrayList<>();
    List<String> list_of_function = new ArrayList<>();
    Set<List<String>> list_of_classReferences = new HashSet<>();
    Set<List<String>> list_of_methodReferences = new HashSet<>();
    int typeclassorinterface = 1;  // type = 1 for class and type = 0 for interface




    public void setclass(String classs) {
        class_entered = classs;
    }

    public void add_Extended_class(String parentClass) {


        extendedclasses = parentClass;

    }

    public void add_Interfaces(String parentClass) {


        list_of_interfaces.add(parentClass);

    }


    public void addVariable(List<String> s) {

        list_variables.add(s);
    }



    public void addfunction(String functionprototype) {

        list_of_function.add(functionprototype);
    }

    public void addClassReferences(List<String> classReferences) {

        list_of_classReferences.add(classReferences);
    }
    public void addMethodReferences(List<String> classReferences) {

        list_of_methodReferences.add(classReferences);
    }

    public String addsquare(String n) {
        String temp1 = "[" + n + "]";
        return temp1;
    }


    public String  generate(String st) {
        String s = "http://yuml.me/diagram/scruffy/class/";


        s = s + addsquare(st);
        return s;
    }


    public void print ()
    {
        System.out.println("Class name " + class_entered);
        System.out.println("----------------------------------------------");
        for( String s : list_of_function)
            System.out.println(s);

        System.out.println("-----------------------------------------------");


    }





}
