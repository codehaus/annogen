/*   Copyright 2004 The Apache Software Foundation
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.codehaus.annogen.generate;

import org.codehaus.annogen.generate.internal.joust.FileWriterFactory;
import org.codehaus.annogen.generate.internal.joust.JavaOutputStream;
import org.codehaus.annogen.generate.internal.joust.SourceJavaOutputStream;
import org.codehaus.annogen.generate.internal.joust.Variable;
import org.codehaus.annogen.generate.internal.joust.WriterFactory;
import org.codehaus.annogen.override.internal.AnnoBeanBase;
import org.codehaus.annogen.override.internal.DefaultAnnoBeanMapping;
import org.codehaus.annogen.view.internal.reflect.ReflectAnnogenTigerDelegate;
import org.codehaus.annogen.view.internal.reflect.ReflectAnnogenTigerDelegate;
import org.codehaus.jam.JAnnotation;
import org.codehaus.jam.JAnnotationValue;
import org.codehaus.jam.JClass;
import org.codehaus.jam.JMethod;
import org.codehaus.jam.JamService;
import org.codehaus.jam.JamServiceFactory;
import org.codehaus.jam.JamServiceParams;
import org.codehaus.jam.internal.JamLoggerImpl;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Code generator for AnnoBeans which can be driven programmatically
 * or from the command line.  This is wrapped by Annogentask - if you use ant
 * for your build, it's probably easirer to use that instead.
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class Annogen {

  // ========================================================================
  // main() method

  //FIXME need to clean up and add parameters, add usage statement
  public static void main(String[] args) {
    try {
    JamServiceFactory jsf = JamServiceFactory.getInstance();
    JamServiceParams params = jsf.createServiceParams();
    Annogen ag = new Annogen();
    for(int i=0; i<args.length; i++) {
      if (args[i].equals("-d")) {
        i++;
        ag.setOutputDir(new File(args[i]));
        i++;
      } else {
        File f = new File(args[i]);
        if (f.isDirectory()) {
          File[] fs = f.listFiles();
          for(int j=0; j<fs.length; j++) params.includeSourceFile(fs[j]);
        } else {
          params.includeSourceFile(f);
        }
      }
    }
      JamService js = jsf.createService(params);
      ag.addAnnotationClasses(js.getAllClasses());
      ag.doCodegen();
    } catch(IOException ioe) {
      ioe.printStackTrace();
      System.out.flush();
      System.exit(-1);
    }
  }


  // ========================================================================
  // Constants

  public static final String SETTER_PREFIX = "set_";
  private static final String FIELD_PREFIX = "_";

  private static final String BASE_CLASS = AnnoBeanBase.class.getName();

  //the name of the 'annobeanClasssname' element of an AnnogenInfo annotation
  private static final String ANNOGENINFO_ANNOBEAN_CLASSNAME = "annoBeanClass";

  // ========================================================================
  // Variables

  private List mClassesLeftTodo = null;
  private Collection mClassesDone = null;
  private JavaOutputStream mJoust = null;
  private Map mDeclaredJclass2beanClassname = new HashMap();
  private boolean mImplementAnnotationTypes = true;
  private ReflectAnnogenTigerDelegate mTiger;

  // ========================================================================
  // Constructors

  public Annogen() {
    mTiger = ReflectAnnogenTigerDelegate.create(new JamLoggerImpl());
    if (mTiger == null) {
      throw new IllegalStateException
        ("The annogen code generator must be run under JDK 1.5 or later.");
    }
    mClassesLeftTodo = new LinkedList();
    mClassesDone = new HashSet();
  }

  // ========================================================================
  // Public methods

  public void addAnnotationClasses(JClass[] classes) {
    for(int i=0; i<classes.length; i++) {
      if (classes[i].isAnnotationType()) {
        JAnnotation ann = classes[i].getAnnotation(mTiger.getAnnogenInfoClass());
        if (ann == null) {
          warn("Ignoring "+classes[i].getQualifiedName()+
               " because it does not declare an @AnnogenInfo annotation.");
          continue; //REVIEW should this be an error instead?
        }
        JAnnotationValue val = ann.getValue(ANNOGENINFO_ANNOBEAN_CLASSNAME);
        if (val == null) {  //should not happen - it's not an optional value
          warn("Ignoring "+classes[i].getQualifiedName()+
               " because its @AnnogenInfo annotation does specify an "+
               ANNOGENINFO_ANNOBEAN_CLASSNAME);
          continue; //REVIEW should this be an error instead?
        }
        mDeclaredJclass2beanClassname.put(classes[i],val.asString());
        mClassesLeftTodo.add(classes[i]);
      } else {
        warn("Ignoring "+classes[i].getQualifiedName()+
             " because it is not an annotation type.");
      }
    }
  }

  public void setOutputDir(File dir) {
    WriterFactory wf = new FileWriterFactory(dir);
    setJavaOutputStream(new SourceJavaOutputStream(wf));
  }

  public void setJavaOutputStream(JavaOutputStream joust) {
    mJoust = joust;
  }

  public void doCodegen() throws IOException {
    while(mClassesLeftTodo.size() > 0) {
      JClass clazz = (JClass)mClassesLeftTodo.get(0);
      mClassesLeftTodo.remove(0);
      mClassesDone.add(clazz);
      doCodegen(clazz);
    }
  }

  public void setPre15CompatibilityMode(boolean b) {
    mImplementAnnotationTypes = b;
  }

  // ========================================================================
  // Private methods

  private void doCodegen(JClass clazz) throws IOException {
    {
      // figure out the name of the class to codegen and get started
      String annoBeanClassname = getAnnoBeanClassnameFor(clazz);
      if (annoBeanClassname == null) throw new IllegalStateException();
      int lastDot = annoBeanClassname.lastIndexOf('.');
      String simpleImplName = (lastDot == -1) ? annoBeanClassname :
        annoBeanClassname.substring(lastDot+1);
      String packageName = (lastDot == -1) ? null :
        annoBeanClassname.substring(0,lastDot);
      mJoust.startFile(packageName,simpleImplName);
    }
    JMethod[] methods = clazz.getDeclaredMethods();
    String[] implementInterface =
      (!mImplementAnnotationTypes ? null : new String[] {clazz.getQualifiedName()});
    mJoust.writeComment("THIS IS GENERATED CODE! DO NOT EDIT!\n\n\n"+
                        "Generated by "+getClass().getName()+
                        "\n on "+new Date()+"\n"+
                        "\n");
    mJoust.startClass(Modifier.PUBLIC,
                      BASE_CLASS,
                      implementInterface);
    // write the 'PROXY_FOR' field which we use at runtime to map back
    // to the annotation class that we are proxying for.  In the case
    // where we implement that interface (i.e. m14Compatible == false)
    // this is arguably redundant; we err on the side of most concrete
    // and direct by just always doing it this way.
    mJoust.writeField(Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL,
                      "java.lang.String",
                      DefaultAnnoBeanMapping.PROXY_FOR,
                      mJoust.getExpressionFactory().
                      createString(clazz.getQualifiedName()));

    for(int i=0; i<methods.length; i++) {
      String fieldName = FIELD_PREFIX+ methods[i].getSimpleName();
      JClass type = methods[i].getReturnType();
      String typeName = (!mImplementAnnotationTypes) ?
        getImplClassForIfGenerated(type) : type.getQualifiedName();
      Variable fieldVar =
        mJoust.writeField(Modifier.PRIVATE,typeName,fieldName,null);
      { // write the 'getter' implementation
        mJoust.startMethod(Modifier.PUBLIC,
                           typeName,
                           methods[i].getSimpleName(),
                           null, // no parameters
                           null, // no parameters
                           null // no throws
                           );
        mJoust.writeReturnStatement(fieldVar);
        mJoust.endMethodOrConstructor();
      }
      { // write the 'setter' implementation
        String[] paramTypeNames = new String[] {typeName};
        String[] paramNames = new String[] {"in"};
        Variable[] params = mJoust.startMethod(Modifier.PUBLIC,
                                               "void",
                                               SETTER_PREFIX+ methods[i].getSimpleName(),
                                               paramTypeNames,
                                               paramNames,
                                               null // no throws
        );
        mJoust.writeAssignmentStatement(fieldVar,params[0]);
        mJoust.endMethodOrConstructor();
      }
      { // check to see if we need to also do annogen for the field's type
        JClass c = clazz.forName(typeName);
        if (c.isAnnotationType()) {
          if (!mClassesLeftTodo.contains(c) && !mClassesDone.contains(c)) {
            mClassesLeftTodo.add(c);
          }
        }
      }
    }
    mJoust.endClassOrInterface();
    mJoust.endFile();
  }

  private void warn(String msg) {
    System.out.println("[Warning] "+msg);
  }


  // ========================================================================
  // Private methods

  private String getImplClassForIfGenerated(JClass clazz) {
    if (clazz.isArrayType()) {
      JClass comp = clazz.getArrayComponentType();
      StringWriter out = new StringWriter();
      out.write(getImplClassForIfGenerated(comp));
      for(int i=0; i<clazz.getArrayDimensions(); i++) out.write("[]");
      return out.toString();
    } else if (mClassesLeftTodo.contains(clazz) || mClassesDone.contains(clazz)) {
      return getAnnoBeanClassnameFor(clazz);
    }
    return clazz.getQualifiedName();
  }

  // ========================================================================
  // These methods keep the runtime in sync with codegen

  private String getAnnoBeanClassnameFor(JClass clazz) {
    return (String)mDeclaredJclass2beanClassname.get(clazz);
  }


}