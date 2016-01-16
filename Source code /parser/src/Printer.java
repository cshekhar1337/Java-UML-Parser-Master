
import net.sourceforge.plantuml.SourceStringReader;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class Printer implements JavaListener {

    List<JavaParseTreeBuilder> classList = new ArrayList<>();

    JavaParseTreeBuilder obj = new JavaParseTreeBuilder();
    String filename;



    Printer() {


    }
    Printer(String imagename) {
        filename = imagename;


    }


    public String getCollectionName( String s) {
        String res = "";

        int start = 0 ; int end = 0;

        for( int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '<')
                start = i + 1;
            if ( s. charAt(i) == '>')
                end = i;

        }


        res = s.substring(start ,end);

        return res;


    }

    public String addsquare(String n) {
        String temp1 = "[" + n + "]";
        return temp1;
    }

    public String removelastcomma(String s) {
        if (s == "")
            return s;
        if (s.charAt((s.length() - 1)) == ',')
            s = s.substring(0, s.length() - 1);
        return s;
    }



    public void plantumlgenerate(String diagramtext, String imagename )  throws IOException {
        OutputStream png = new FileOutputStream((imagename + ".png"));
        diagramtext = "@startuml\n" + "skinparam classAttributeIconSize 0 \n" + diagramtext + "@enduml\n";


        SourceStringReader reader = new SourceStringReader(diagramtext);
// Write the first image to "png"
        String desc = reader.generateImage(png);
        if(desc.equals(null))
            System.out.println("Image generation failed");
    }

    public void print_List(List<JavaParseTreeBuilder> classList1) throws IOException {
        String full_url = "";
        String plantumlstring = "";

        for (JavaParseTreeBuilder i : classList1) {


            String stringclassreferences = "";
            String identifierclassorinterface;
            if(i.typeclassorinterface == 0)
            { identifierclassorinterface = "interface ";
              }
            else
                identifierclassorinterface = "class ";

                plantumlstring += identifierclassorinterface + i.class_entered + " { \n";


            for (List<String> j : i.list_variables) {

                if(j.get(1).equals("+") || j.get(1).equals("-"))
                    plantumlstring += j.get(1) +  " " + j.get(0) + "\n";
            }

            plantumlstring += "--\n";
            for (String j : i.list_of_function) {


                int flag = 1;
                for (List<String> z : i.list_variables) {
                    String temp = z.get(0).substring(0, z.get(0).indexOf(':'));

                    if ('a' <= temp.charAt(0) || temp.charAt(0) <= 'z') {
                        char c = (char) (temp.charAt(0) ^ ' ');
                        temp = c + temp.substring(1, temp.length());
                        if (j.contains("get" + temp) || j.contains("set" + temp))
                            flag = 0;
                    }

                }

                if (flag == 1)
                    if(j.charAt(0) == '+' )
                        plantumlstring += j + "\n";



            }
           plantumlstring += "}\n";




            if (i.extendedclasses != null)
            {  plantumlstring += i.class_entered + " --|> "+ i.extendedclasses + " : extends" + "\n";}



            for (String j : i.list_of_interfaces)
            {  if(i.typeclassorinterface == 1)
                plantumlstring += i.class_entered + " ..|>  "+ j + " : implements "+ "\n";
            else
                plantumlstring += i.class_entered + " ..|>  "+ j + " : extends "+ "\n";
            }








            for (List<String> k : i.list_of_classReferences)
            {


                if (k.get(1).equals("1"))
                    plantumlstring += i.class_entered + " --> \"*\" " + k.get(0)+ "\n";
                else
                if (k.get(1).equals("0"))
                    plantumlstring += i.class_entered + " --> "  + k.get(0) + "\n";


            }

            for (List<String> k : i.list_of_methodReferences)
            {



                if (k.get(1).equals("0"))
                    plantumlstring += i.class_entered + " ..> "  + k.get(0) + "\n";
                if (k.get(1).equals("1"))
                    plantumlstring += i.class_entered + " --> \"*\" " + k.get(0)+ "\n";

            }


            //System.out.println(plantumlstring);

        }


        try {
            plantumlgenerate(plantumlstring, filename);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    @Override
    public void enterCompilationUnit(JavaParser.CompilationUnitContext ctx) {
        obj.typeclassorinterface = 1;

    }

    @Override
    public void exitCompilationUnit(JavaParser.CompilationUnitContext ctx) {

        try {
            print_List(classList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Finished....!");



    }

    @Override
    public void enterPackageDeclaration(JavaParser.PackageDeclarationContext ctx) {

    }

    @Override
    public void exitPackageDeclaration(JavaParser.PackageDeclarationContext ctx) {

    }

    @Override
    public void enterImportDeclaration(JavaParser.ImportDeclarationContext ctx) {

    }

    @Override
    public void exitImportDeclaration(JavaParser.ImportDeclarationContext ctx) {

    }

    @Override
    public void enterTypeDeclaration(JavaParser.TypeDeclarationContext ctx) {

    }

    @Override
    public void exitTypeDeclaration(JavaParser.TypeDeclarationContext ctx) {

    }

    @Override
    public void enterModifier(JavaParser.ModifierContext ctx) {

    }

    @Override
    public void exitModifier(JavaParser.ModifierContext ctx) {

    }

    @Override
    public void enterClassOrInterfaceModifier(JavaParser.ClassOrInterfaceModifierContext ctx) {

    }

    @Override
    public void exitClassOrInterfaceModifier(JavaParser.ClassOrInterfaceModifierContext ctx) {

    }

    @Override
    public void enterVariableModifier(JavaParser.VariableModifierContext ctx) {

    }

    @Override
    public void exitVariableModifier(JavaParser.VariableModifierContext ctx) {

    }

    @Override
    public void enterClassDeclaration(JavaParser.ClassDeclarationContext ctx) {






    }

    public String valueOfModifier(String s) {

        String res = "";
        switch (s) {
            case "public": {
                res = "+";
                break;
            }
            case "private": {
                res = "-";
                break;
            }
            case "protected": {
                res = "#";
                break;
            }
            case "": {
                res = " ";
                break;

            }


        }
        return res;
    }

    @Override
    public void exitClassDeclaration(JavaParser.ClassDeclarationContext ctx) {

        ParserRuleContext parentTypeContext = ctx.getParent();
        String classmodifier = "";




         obj.setclass(ctx.Identifier().getText());
        if (ctx.type() != null) {

            ParserRuleContext superclasss = ctx.type().classOrInterfaceType(); // prints superclass .. the class after keyword extends
            if (superclasss != null) {

                obj.add_Extended_class(superclasss.getText());
            }


        }


        if (ctx.typeList() != null) {
            for (JavaParser.TypeContext interf : ctx.typeList().type()) {

                obj.add_Interfaces(interf.getText());
            }


        }

        JavaParseTreeBuilder objclone;
        objclone = obj;
        classList.add(objclone);
        obj = new JavaParseTreeBuilder();

    }


    @Override
    public void enterTypeParameters(JavaParser.TypeParametersContext ctx) {

    }

    @Override
    public void exitTypeParameters(JavaParser.TypeParametersContext ctx) {

    }

    @Override
    public void enterTypeParameter(JavaParser.TypeParameterContext ctx) {

    }

    @Override
    public void exitTypeParameter(JavaParser.TypeParameterContext ctx) {

    }

    @Override
    public void enterTypeBound(JavaParser.TypeBoundContext ctx) {

    }

    @Override
    public void exitTypeBound(JavaParser.TypeBoundContext ctx) {

    }

    @Override
    public void enterEnumDeclaration(JavaParser.EnumDeclarationContext ctx) {

    }

    @Override
    public void exitEnumDeclaration(JavaParser.EnumDeclarationContext ctx) {

    }

    @Override
    public void enterEnumConstants(JavaParser.EnumConstantsContext ctx) {

    }

    @Override
    public void exitEnumConstants(JavaParser.EnumConstantsContext ctx) {

    }

    @Override
    public void enterEnumConstant(JavaParser.EnumConstantContext ctx) {

    }

    @Override
    public void exitEnumConstant(JavaParser.EnumConstantContext ctx) {

    }

    @Override
    public void enterEnumBodyDeclarations(JavaParser.EnumBodyDeclarationsContext ctx) {

    }

    @Override
    public void exitEnumBodyDeclarations(JavaParser.EnumBodyDeclarationsContext ctx) {

    }

    @Override
    public void enterInterfaceDeclaration(JavaParser.InterfaceDeclarationContext ctx) {


    }

    @Override
    public void exitInterfaceDeclaration(JavaParser.InterfaceDeclarationContext ctx) {
        obj.typeclassorinterface = 0;

        String s =  ctx.Identifier().getText();


        obj.setclass(s);

        if (ctx.typeList() != null)
            for (JavaParser.TypeContext interf : ctx.typeList().type()) {
            // System.out.println("interfaces are-----------" + interf.getText());
                obj.add_Interfaces(interf.getText());
            }

        if (ctx.interfaceBody() != null)
            for(JavaParser.InterfaceBodyDeclarationContext j : ctx.interfaceBody().interfaceBodyDeclaration()) {


                if(j.interfaceMemberDeclaration() != null)
                    if(j.interfaceMemberDeclaration().interfaceMethodDeclaration() != null)
                    {
                        String temp = "";
                        String modifier = "";
                        String str = "";
                        String functionname = j.interfaceMemberDeclaration().interfaceMethodDeclaration().Identifier().getText();
                        String returntype = "";
                        returntype = j.interfaceMemberDeclaration().interfaceMethodDeclaration().getStart().getText();
                        if(returntype.equals("void"))
                            returntype = "void";
                        else
                            returntype = j.interfaceMemberDeclaration().interfaceMethodDeclaration().type().getText();


                        for(JavaParser.ModifierContext t : j.modifier())
                            modifier = j.modifier(0).getText();

                        modifier = valueOfModifier(modifier);

                        if(j.interfaceMemberDeclaration().interfaceMethodDeclaration().formalParameters().formalParameterList() != null)
                            for(JavaParser.FormalParameterContext k : j.interfaceMemberDeclaration().interfaceMethodDeclaration().formalParameters().formalParameterList().formalParameter() )
                                str +=  k.variableDeclaratorId().getText() + ":" + k.type().getText() + " ";
                        str = "(" + str + ")";
                        str = modifier  + functionname + str + ":" + returntype;



                        obj.addfunction(str);

                    }
            }


        JavaParseTreeBuilder objclone;
        objclone = obj;

        classList.add(objclone);
        obj = new JavaParseTreeBuilder();


    }

    @Override
    public void enterTypeList(JavaParser.TypeListContext ctx) {

    }

    @Override
    public void exitTypeList(JavaParser.TypeListContext ctx) {

    }

    @Override
    public void enterClassBody(JavaParser.ClassBodyContext ctx) {

    }

    @Override
    public void exitClassBody(JavaParser.ClassBodyContext ctx) {

    }

    @Override
    public void enterInterfaceBody(JavaParser.InterfaceBodyContext ctx) {

    }

    @Override
    public void exitInterfaceBody(JavaParser.InterfaceBodyContext ctx) {

    }

    @Override
    public void enterClassBodyDeclaration(JavaParser.ClassBodyDeclarationContext ctx) {

    }

    @Override
    public void exitClassBodyDeclaration(JavaParser.ClassBodyDeclarationContext ctx) {

    }

    @Override
    public void enterMemberDeclaration(JavaParser.MemberDeclarationContext ctx) {

    }

    @Override
    public void exitMemberDeclaration(JavaParser.MemberDeclarationContext ctx) {


    }

    @Override
    public void enterMethodDeclaration(JavaParser.MethodDeclarationContext ctx) {

    }

    @Override
    public void exitMethodDeclaration(JavaParser.MethodDeclarationContext ctx) {
        ParserRuleContext parentContext;

        parentContext = ctx.getParent();
        String temp = "";



        if (parentContext != null && parentContext.getParent() != null) {
            ParserRuleContext grandParentContext = parentContext.getParent();
            if (grandParentContext instanceof JavaParser.ClassBodyDeclarationContext) {

                JavaParser.ClassBodyDeclarationContext cBc = (JavaParser.ClassBodyDeclarationContext) grandParentContext;



                for (JavaParser.ModifierContext m : cBc.modifier()) {

                    String mod;
                    mod = m.classOrInterfaceModifier().getText();

                    temp = valueOfModifier(mod);
                    if(!temp.equals(""))
                        break;

                }
            }

                String parameter = "";
                if ( ctx.formalParameters().formalParameterList() != null)
                    for(JavaParser.FormalParameterContext k : ctx.formalParameters().formalParameterList().formalParameter() ) {
                        parameter += k.variableDeclaratorId().getText() + ":" + k.type().getText() + " ";

                    }


                parameter = "(" + parameter + ")";
                String m_type = ctx.getStart().getText();
                if(m_type.equals("void"))
                    m_type = "void";
                else
                    m_type = ctx.type().getText();
                String s =  temp + ctx.Identifier().getText() + "" + parameter + " : " + m_type;


                obj.addfunction(s);


            }





    }

    @Override
    public void enterGenericMethodDeclaration(JavaParser.GenericMethodDeclarationContext ctx) {

    }

    @Override
    public void exitGenericMethodDeclaration(JavaParser.GenericMethodDeclarationContext ctx) {

    }

    @Override
    public void enterConstructorDeclaration(JavaParser.ConstructorDeclarationContext ctx) {

    }

    @Override
    public void exitConstructorDeclaration(JavaParser.ConstructorDeclarationContext ctx) {
        ParserRuleContext parentContext;

        parentContext = ctx.getParent();

        String temp = "";

        if (parentContext != null && parentContext.getParent() != null) {
            ParserRuleContext grandParentContext = parentContext.getParent();
            if (grandParentContext instanceof JavaParser.ClassBodyDeclarationContext) {

                JavaParser.ClassBodyDeclarationContext cBc = (JavaParser.ClassBodyDeclarationContext) grandParentContext;



                for (JavaParser.ModifierContext m : cBc.modifier()) {

                    String mod;
                    mod = m.classOrInterfaceModifier().getText();

                    temp = valueOfModifier(mod);

                }
            }

        }
        String s = "";
        String parameter = "";
        if ( ctx.formalParameters().formalParameterList() != null)
            for(JavaParser.FormalParameterContext k : ctx.formalParameters().formalParameterList().formalParameter() ) {
                parameter += k.variableDeclaratorId().getText() + ":" + k.type().getText() + " ";

            }

        parameter = "(" + parameter + ")";
        String m_type = ctx.getStart().getText();
        if(m_type.equals("void"))
            m_type = "void";

        s =  temp + ctx.Identifier() + "" + parameter + " : " + m_type; ;
        obj.addfunction(s);

    }

    @Override
    public void enterGenericConstructorDeclaration(JavaParser.GenericConstructorDeclarationContext ctx) {

    }

    @Override
    public void exitGenericConstructorDeclaration(JavaParser.GenericConstructorDeclarationContext ctx) {

    }

    @Override
    public void enterFieldDeclaration(JavaParser.FieldDeclarationContext ctx) {


    }

    @Override
    public void exitFieldDeclaration(JavaParser.FieldDeclarationContext ctx) {
        List<String> stringarray = new ArrayList<>();
        ParserRuleContext parentContext;
        String temp = "";
        parentContext = ctx.getParent();



        if (parentContext != null && parentContext.getParent() != null) {
            ParserRuleContext grandParentContext = parentContext.getParent();
            if (grandParentContext instanceof JavaParser.ClassBodyDeclarationContext) {

                JavaParser.ClassBodyDeclarationContext cBc = (JavaParser.ClassBodyDeclarationContext) grandParentContext;



                for (JavaParser.ModifierContext m : cBc.modifier()) {

                    String mod;
                    mod = m.classOrInterfaceModifier().getText();

                    temp = valueOfModifier(mod);

                }

            }
        }



        String collection = "";
        if ( ctx.type().classOrInterfaceType() != null)
            collection = ctx.type().classOrInterfaceType().getText();
        if(collection.equals("String")) {
            collection = ctx.variableDeclarators().getText() + ":" + "String";

            stringarray.add(collection);
            stringarray.add(temp);


            obj.addVariable(stringarray);

        }
        else

        if(collection.contains("Collection<")){

            collection = getCollectionName(collection);

            stringarray.add(collection);
            stringarray.add("1");
            obj.addClassReferences(stringarray);




        }
        else
        if(!collection.isEmpty()) {

            String variablename = ctx.variableDeclarators().getText();
            String classname = ctx.type().classOrInterfaceType().getText();
            //List<String> stringarray;
            stringarray.add(collection);
            stringarray.add("0");
            obj.addClassReferences(stringarray);



        }


        else
            for (JavaParser.VariableDeclaratorContext var : ctx.variableDeclarators().variableDeclarator()) {
                //for (JavaParser.VariableDeclaratorContext var : fdx.variableDeclarators().variableDeclarator()) {
                String s, str;
                str = "";
                s = "";
                s = ctx.type().getText();

                if (s.contains("[]"))
                    str = var.variableDeclaratorId().getStart().getText() + ":" + ctx.type().primitiveType().getChild(0).getText() + "(*)";
                else
                    str = var.variableDeclaratorId().getText() + ":" + ctx.type().primitiveType().getText();


                stringarray.add(str);
                stringarray.add(temp);

                obj.addVariable(stringarray);

            }


    }

    @Override
    public void enterInterfaceBodyDeclaration(JavaParser.InterfaceBodyDeclarationContext ctx) {


    }

    @Override
    public void exitInterfaceBodyDeclaration(JavaParser.InterfaceBodyDeclarationContext ctx) {

    }


    @Override
    public void enterInterfaceMemberDeclaration(JavaParser.InterfaceMemberDeclarationContext ctx) {

    }

    @Override
    public void exitInterfaceMemberDeclaration(JavaParser.InterfaceMemberDeclarationContext ctx) {

    }

    @Override
    public void enterConstDeclaration(JavaParser.ConstDeclarationContext ctx) {

    }

    @Override
    public void exitConstDeclaration(JavaParser.ConstDeclarationContext ctx) {

    }

    @Override
    public void enterConstantDeclarator(JavaParser.ConstantDeclaratorContext ctx) {

    }

    @Override
    public void exitConstantDeclarator(JavaParser.ConstantDeclaratorContext ctx) {

    }

    @Override
    public void enterInterfaceMethodDeclaration(JavaParser.InterfaceMethodDeclarationContext ctx) {


    }

    @Override
    public void exitInterfaceMethodDeclaration(JavaParser.InterfaceMethodDeclarationContext ctx) {

    }

    @Override
    public void enterGenericInterfaceMethodDeclaration(JavaParser.GenericInterfaceMethodDeclarationContext ctx) {

    }

    @Override
    public void exitGenericInterfaceMethodDeclaration(JavaParser.GenericInterfaceMethodDeclarationContext ctx) {

    }

    @Override
    public void enterVariableDeclarators(JavaParser.VariableDeclaratorsContext ctx) {

    }

    @Override
    public void exitVariableDeclarators(JavaParser.VariableDeclaratorsContext ctx) {

    }

    @Override
    public void enterVariableDeclarator(JavaParser.VariableDeclaratorContext ctx) {

    }

    @Override
    public void exitVariableDeclarator(JavaParser.VariableDeclaratorContext ctx) {

    }

    @Override
    public void enterVariableDeclaratorId(JavaParser.VariableDeclaratorIdContext ctx) {

    }

    @Override
    public void exitVariableDeclaratorId(JavaParser.VariableDeclaratorIdContext ctx) {

    }

    @Override
    public void enterVariableInitializer(JavaParser.VariableInitializerContext ctx) {

    }

    @Override
    public void exitVariableInitializer(JavaParser.VariableInitializerContext ctx) {

    }

    @Override
    public void enterArrayInitializer(JavaParser.ArrayInitializerContext ctx) {

    }

    @Override
    public void exitArrayInitializer(JavaParser.ArrayInitializerContext ctx) {

    }

    @Override
    public void enterEnumConstantName(JavaParser.EnumConstantNameContext ctx) {

    }

    @Override
    public void exitEnumConstantName(JavaParser.EnumConstantNameContext ctx) {

    }

    @Override
    public void enterType(JavaParser.TypeContext ctx) {

    }

    @Override
    public void exitType(JavaParser.TypeContext ctx) {

    }

    @Override
    public void enterClassOrInterfaceType(JavaParser.ClassOrInterfaceTypeContext ctx) {

    }

    @Override
    public void exitClassOrInterfaceType(JavaParser.ClassOrInterfaceTypeContext ctx) {

    }

    @Override
    public void enterPrimitiveType(JavaParser.PrimitiveTypeContext ctx) {

    }

    @Override
    public void exitPrimitiveType(JavaParser.PrimitiveTypeContext ctx) {

    }

    @Override
    public void enterTypeArguments(JavaParser.TypeArgumentsContext ctx) {

    }

    @Override
    public void exitTypeArguments(JavaParser.TypeArgumentsContext ctx) {

    }

    @Override
    public void enterTypeArgument(JavaParser.TypeArgumentContext ctx) {

    }

    @Override
    public void exitTypeArgument(JavaParser.TypeArgumentContext ctx) {

    }

    @Override
    public void enterQualifiedNameList(JavaParser.QualifiedNameListContext ctx) {

    }

    @Override
    public void exitQualifiedNameList(JavaParser.QualifiedNameListContext ctx) {

    }

    @Override
    public void enterFormalParameters(JavaParser.FormalParametersContext ctx) {

    }

    @Override
    public void exitFormalParameters(JavaParser.FormalParametersContext ctx) {

    }

    @Override
    public void enterFormalParameterList(JavaParser.FormalParameterListContext ctx) {

    }

    @Override
    public void exitFormalParameterList(JavaParser.FormalParameterListContext ctx) {

    }

    @Override
    public void enterFormalParameter(JavaParser.FormalParameterContext ctx) {

    }

    @Override
    public void exitFormalParameter(JavaParser.FormalParameterContext ctx) {
        if(ctx.type().classOrInterfaceType()!= null)
        {
            String classname = ctx.type().classOrInterfaceType().getText();
            if (!classname.equals("String")) {

                List<String> arr = new ArrayList<>();
                arr.add(classname);
                arr.add("0");
                obj.addMethodReferences(arr);
            }
        }

    }

    @Override
    public void enterLastFormalParameter(JavaParser.LastFormalParameterContext ctx) {

    }

    @Override
    public void exitLastFormalParameter(JavaParser.LastFormalParameterContext ctx) {

    }

    @Override
    public void enterMethodBody(JavaParser.MethodBodyContext ctx) {

    }

    @Override
    public void exitMethodBody(JavaParser.MethodBodyContext ctx) {

    }

    @Override
    public void enterConstructorBody(JavaParser.ConstructorBodyContext ctx) {

    }

    @Override
    public void exitConstructorBody(JavaParser.ConstructorBodyContext ctx) {

    }

    @Override
    public void enterQualifiedName(JavaParser.QualifiedNameContext ctx) {

    }

    @Override
    public void exitQualifiedName(JavaParser.QualifiedNameContext ctx) {

    }

    @Override
    public void enterLiteral(JavaParser.LiteralContext ctx) {

    }

    @Override
    public void exitLiteral(JavaParser.LiteralContext ctx) {

    }

    @Override
    public void enterAnnotation(JavaParser.AnnotationContext ctx) {

    }

    @Override
    public void exitAnnotation(JavaParser.AnnotationContext ctx) {

    }

    @Override
    public void enterAnnotationName(JavaParser.AnnotationNameContext ctx) {

    }

    @Override
    public void exitAnnotationName(JavaParser.AnnotationNameContext ctx) {

    }

    @Override
    public void enterElementValuePairs(JavaParser.ElementValuePairsContext ctx) {

    }

    @Override
    public void exitElementValuePairs(JavaParser.ElementValuePairsContext ctx) {

    }

    @Override
    public void enterElementValuePair(JavaParser.ElementValuePairContext ctx) {

    }

    @Override
    public void exitElementValuePair(JavaParser.ElementValuePairContext ctx) {

    }

    @Override
    public void enterElementValue(JavaParser.ElementValueContext ctx) {

    }

    @Override
    public void exitElementValue(JavaParser.ElementValueContext ctx) {

    }

    @Override
    public void enterElementValueArrayInitializer(JavaParser.ElementValueArrayInitializerContext ctx) {

    }

    @Override
    public void exitElementValueArrayInitializer(JavaParser.ElementValueArrayInitializerContext ctx) {

    }

    @Override
    public void enterAnnotationTypeDeclaration(JavaParser.AnnotationTypeDeclarationContext ctx) {

    }

    @Override
    public void exitAnnotationTypeDeclaration(JavaParser.AnnotationTypeDeclarationContext ctx) {

    }

    @Override
    public void enterAnnotationTypeBody(JavaParser.AnnotationTypeBodyContext ctx) {

    }

    @Override
    public void exitAnnotationTypeBody(JavaParser.AnnotationTypeBodyContext ctx) {

    }

    @Override
    public void enterAnnotationTypeElementDeclaration(JavaParser.AnnotationTypeElementDeclarationContext ctx) {

    }

    @Override
    public void exitAnnotationTypeElementDeclaration(JavaParser.AnnotationTypeElementDeclarationContext ctx) {

    }

    @Override
    public void enterAnnotationTypeElementRest(JavaParser.AnnotationTypeElementRestContext ctx) {

    }

    @Override
    public void exitAnnotationTypeElementRest(JavaParser.AnnotationTypeElementRestContext ctx) {

    }

    @Override
    public void enterAnnotationMethodOrConstantRest(JavaParser.AnnotationMethodOrConstantRestContext ctx) {

    }

    @Override
    public void exitAnnotationMethodOrConstantRest(JavaParser.AnnotationMethodOrConstantRestContext ctx) {

    }

    @Override
    public void enterAnnotationMethodRest(JavaParser.AnnotationMethodRestContext ctx) {

    }

    @Override
    public void exitAnnotationMethodRest(JavaParser.AnnotationMethodRestContext ctx) {

    }

    @Override
    public void enterAnnotationConstantRest(JavaParser.AnnotationConstantRestContext ctx) {

    }

    @Override
    public void exitAnnotationConstantRest(JavaParser.AnnotationConstantRestContext ctx) {

    }

    @Override
    public void enterDefaultValue(JavaParser.DefaultValueContext ctx) {

    }

    @Override
    public void exitDefaultValue(JavaParser.DefaultValueContext ctx) {

    }

    @Override
    public void enterBlock(JavaParser.BlockContext ctx) {

    }

    @Override
    public void exitBlock(JavaParser.BlockContext ctx) {

    }

    @Override
    public void enterBlockStatement(JavaParser.BlockStatementContext ctx) {

    }

    @Override
    public void exitBlockStatement(JavaParser.BlockStatementContext ctx) {

    }

    @Override
    public void enterLocalVariableDeclarationStatement(JavaParser.LocalVariableDeclarationStatementContext ctx) {

    }

    @Override
    public void exitLocalVariableDeclarationStatement(JavaParser.LocalVariableDeclarationStatementContext ctx) {


    }

    @Override
    public void enterLocalVariableDeclaration(JavaParser.LocalVariableDeclarationContext ctx) {

    }

    @Override
    public void exitLocalVariableDeclaration(JavaParser.LocalVariableDeclarationContext ctx) {
        if(ctx.type().classOrInterfaceType() != null) {
            String classname = ctx.type().classOrInterfaceType().getText();
            if (!classname.equals("String")) {

                List<String> arr = new ArrayList<>();
                arr.add(classname);
                arr.add("0");
                obj.addMethodReferences(arr);

                //obj.addClassReferences(arr);
            }

        }

    }

    @Override
    public void enterStatement(JavaParser.StatementContext ctx) {

    }

    @Override
    public void exitStatement(JavaParser.StatementContext ctx) {

    }

    @Override
    public void enterCatchClause(JavaParser.CatchClauseContext ctx) {

    }

    @Override
    public void exitCatchClause(JavaParser.CatchClauseContext ctx) {

    }

    @Override
    public void enterCatchType(JavaParser.CatchTypeContext ctx) {

    }

    @Override
    public void exitCatchType(JavaParser.CatchTypeContext ctx) {

    }

    @Override
    public void enterFinallyBlock(JavaParser.FinallyBlockContext ctx) {

    }

    @Override
    public void exitFinallyBlock(JavaParser.FinallyBlockContext ctx) {

    }

    @Override
    public void enterResourceSpecification(JavaParser.ResourceSpecificationContext ctx) {

    }

    @Override
    public void exitResourceSpecification(JavaParser.ResourceSpecificationContext ctx) {

    }

    @Override
    public void enterResources(JavaParser.ResourcesContext ctx) {

    }

    @Override
    public void exitResources(JavaParser.ResourcesContext ctx) {

    }

    @Override
    public void enterResource(JavaParser.ResourceContext ctx) {

    }

    @Override
    public void exitResource(JavaParser.ResourceContext ctx) {

    }

    @Override
    public void enterSwitchBlockStatementGroup(JavaParser.SwitchBlockStatementGroupContext ctx) {

    }

    @Override
    public void exitSwitchBlockStatementGroup(JavaParser.SwitchBlockStatementGroupContext ctx) {

    }

    @Override
    public void enterSwitchLabel(JavaParser.SwitchLabelContext ctx) {

    }

    @Override
    public void exitSwitchLabel(JavaParser.SwitchLabelContext ctx) {

    }

    @Override
    public void enterForControl(JavaParser.ForControlContext ctx) {

    }

    @Override
    public void exitForControl(JavaParser.ForControlContext ctx) {

    }

    @Override
    public void enterForInit(JavaParser.ForInitContext ctx) {

    }

    @Override
    public void exitForInit(JavaParser.ForInitContext ctx) {

    }

    @Override
    public void enterEnhancedForControl(JavaParser.EnhancedForControlContext ctx) {

    }

    @Override
    public void exitEnhancedForControl(JavaParser.EnhancedForControlContext ctx) {

    }

    @Override
    public void enterForUpdate(JavaParser.ForUpdateContext ctx) {

    }

    @Override
    public void exitForUpdate(JavaParser.ForUpdateContext ctx) {

    }

    @Override
    public void enterParExpression(JavaParser.ParExpressionContext ctx) {

    }

    @Override
    public void exitParExpression(JavaParser.ParExpressionContext ctx) {

    }

    @Override
    public void enterExpressionList(JavaParser.ExpressionListContext ctx) {

    }

    @Override
    public void exitExpressionList(JavaParser.ExpressionListContext ctx) {

    }

    @Override
    public void enterStatementExpression(JavaParser.StatementExpressionContext ctx) {

    }

    @Override
    public void exitStatementExpression(JavaParser.StatementExpressionContext ctx) {

    }

    @Override
    public void enterConstantExpression(JavaParser.ConstantExpressionContext ctx) {

    }

    @Override
    public void exitConstantExpression(JavaParser.ConstantExpressionContext ctx) {

    }

    @Override
    public void enterExpression(JavaParser.ExpressionContext ctx) {

    }

    @Override
    public void exitExpression(JavaParser.ExpressionContext ctx) {

    }

    @Override
    public void enterPrimary(JavaParser.PrimaryContext ctx) {

    }

    @Override
    public void exitPrimary(JavaParser.PrimaryContext ctx) {

    }

    @Override
    public void enterCreator(JavaParser.CreatorContext ctx) {

    }

    @Override
    public void exitCreator(JavaParser.CreatorContext ctx) {

    }

    @Override
    public void enterCreatedName(JavaParser.CreatedNameContext ctx) {

    }

    @Override
    public void exitCreatedName(JavaParser.CreatedNameContext ctx) {

    }

    @Override
    public void enterInnerCreator(JavaParser.InnerCreatorContext ctx) {

    }

    @Override
    public void exitInnerCreator(JavaParser.InnerCreatorContext ctx) {

    }

    @Override
    public void enterArrayCreatorRest(JavaParser.ArrayCreatorRestContext ctx) {

    }

    @Override
    public void exitArrayCreatorRest(JavaParser.ArrayCreatorRestContext ctx) {

    }

    @Override
    public void enterClassCreatorRest(JavaParser.ClassCreatorRestContext ctx) {

    }

    @Override
    public void exitClassCreatorRest(JavaParser.ClassCreatorRestContext ctx) {

    }

    @Override
    public void enterExplicitGenericInvocation(JavaParser.ExplicitGenericInvocationContext ctx) {

    }

    @Override
    public void exitExplicitGenericInvocation(JavaParser.ExplicitGenericInvocationContext ctx) {

    }

    @Override
    public void enterNonWildcardTypeArguments(JavaParser.NonWildcardTypeArgumentsContext ctx) {

    }

    @Override
    public void exitNonWildcardTypeArguments(JavaParser.NonWildcardTypeArgumentsContext ctx) {

    }

    @Override
    public void enterTypeArgumentsOrDiamond(JavaParser.TypeArgumentsOrDiamondContext ctx) {

    }

    @Override
    public void exitTypeArgumentsOrDiamond(JavaParser.TypeArgumentsOrDiamondContext ctx) {

    }

    @Override
    public void enterNonWildcardTypeArgumentsOrDiamond(JavaParser.NonWildcardTypeArgumentsOrDiamondContext ctx) {

    }

    @Override
    public void exitNonWildcardTypeArgumentsOrDiamond(JavaParser.NonWildcardTypeArgumentsOrDiamondContext ctx) {

    }

    @Override
    public void enterSuperSuffix(JavaParser.SuperSuffixContext ctx) {

    }

    @Override
    public void exitSuperSuffix(JavaParser.SuperSuffixContext ctx) {

    }

    @Override
    public void enterExplicitGenericInvocationSuffix(JavaParser.ExplicitGenericInvocationSuffixContext ctx) {

    }

    @Override
    public void exitExplicitGenericInvocationSuffix(JavaParser.ExplicitGenericInvocationSuffixContext ctx) {

    }

    @Override
    public void enterArguments(JavaParser.ArgumentsContext ctx) {

    }

    @Override
    public void exitArguments(JavaParser.ArgumentsContext ctx) {

    }

    @Override
    public void visitTerminal(TerminalNode terminalNode) {

    }

    @Override
    public void visitErrorNode(ErrorNode errorNode) {

    }

    @Override
    public void enterEveryRule(ParserRuleContext parserRuleContext) {

    }

    @Override
    public void exitEveryRule(ParserRuleContext parserRuleContext) {


    }
}
