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

import org.codehaus.annogen.generate.internal.PropfileUtils;
import org.codehaus.annogen.generate.internal.joust.Variable;
import org.codehaus.annogen.generate.internal.joust.CompilingJavaOutputStream;
import org.codehaus.annogen.override.internal.AnnoBeanBase;
import org.codehaus.jam.JClass;
import org.codehaus.jam.JMethod;
import org.codehaus.jam.JamService;
import org.codehaus.jam.JamServiceFactory;
import org.codehaus.jam.JamServiceParams;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Code generator for AnnoBeans which can be driven programmatically
 * or from the command line.  This is wrapped by Annogentask - if you use ant
 * for your build, it's probably easirer to use that instead.
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class Annogen {

  // ========================================================================
  // Constants

  public static final String ANNOBEAN_FOR_FIELD = "ANNOBEAN_FOR";

  public static final String SETTER_PREFIX = "set_";
  private static final String FIELD_PREFIX = "_";

  private static final String BASE_CLASS = AnnoBeanBase.class.getName();


  // ========================================================================
  // Variables

  private List mClassesLeftTodo = null;
  private Collection mClassesDone = null;
  private CompilingJavaOutputStream mJoust = new CompilingJavaOutputStream();
  private boolean mImplementAnnotationTypes = true;
  private AnnoBeanMapping[] mMappings = null;
  private File mOutputDir = null;

  // ========================================================================
  // Constructors

  public Annogen() {
    mClassesLeftTodo = new LinkedList();
    mClassesDone = new HashSet();
  }

  // ========================================================================
  // Public methods

  public void addAnnotationClasses(JClass[] classes) {
    for(int i=0; i<classes.length; i++) {
      if (classes[i].isAnnotationType()) {
        mClassesLeftTodo.add(classes[i]);
      } else {
        warn("Ignoring "+classes[i].getQualifiedName()+
             " because it is not an annotation type.");
      }
    }
  }

  public void setOutputDir(File dir) {
    mJoust.setSourceDir(dir);
    mJoust.setCompilationDir(dir);
    mOutputDir = dir;
  }

  public void setKeepGenerated(boolean b) { mJoust.setKeepGenerated(b); }

  public void setMappings(AnnoBeanMapping[] mappings) { mMappings = mappings; }

  public void setClasspath(File[] cp) { mJoust.setJavacClasspath(cp); }

  public void doCodegen() throws IOException {
    if (mOutputDir == null) throw new IllegalStateException("destdir not set");
    while(mClassesLeftTodo.size() > 0) {
      JClass clazz = (JClass)mClassesLeftTodo.get(0);
      mClassesLeftTodo.remove(0);
      mClassesDone.add(clazz);
      doCodegen(clazz);
    }
    mJoust.close();
  }

  public void setImplementAnnotationTypes(boolean b) {
    mImplementAnnotationTypes = b;
  }

  /**
   * Sets the character encoding to use for generating anno beans.
   * If not set, the vm default is used.
   */
  public void setOutputEncoding(String enc) {
    mJoust.setEncoding(enc);
  }

  // ========================================================================
  // Private methods

  /*package*/ File getOutputDir() { return mOutputDir; }


  private void doCodegen(JClass jsr175type) throws IOException {
    if (jsr175type == null) throw new IllegalArgumentException();
    // figure out the name of the class to codegen and get started
    String annoBeanClassname = getAnnobeanClassnameFor(jsr175type);
    {
      if (annoBeanClassname == null) throw new IllegalStateException();
      int lastDot = annoBeanClassname.lastIndexOf('.');
      String simpleImplName = (lastDot == -1) ? annoBeanClassname :
        annoBeanClassname.substring(lastDot+1);
      String packageName = (lastDot == -1) ? null :
        annoBeanClassname.substring(0,lastDot);
      mJoust.startFile(packageName,simpleImplName);
    }
    JMethod[] methods = jsr175type.getDeclaredMethods();
    String[] implementInterface =
      (!mImplementAnnotationTypes ? null : new String[] {jsr175type.getQualifiedName()});
    mJoust.writeComment("THIS IS GENERATED CODE! DO NOT EDIT!\n\n\n"+
                        "Generated by "+getClass().getName()+
                        "\n on "+new Date()+"\n"+
                        "\n");
    mJoust.startClass(Modifier.PUBLIC,
                      BASE_CLASS,
                      implementInterface);
    // write the 'PROXY_FOR_FIELD' field which we use at runtime to map back
    // to the annotation class that we are proxying for.  In the case
    // where we implement that interface (i.e. m14Compatible == false)
    // this is redundant (since we could just look at what the annobean type
    // implements).  But we err on the side of most concrete and direct by
    // just always doing it this way.
    mJoust.writeField(Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL,
                      "java.lang.String",
                      ANNOBEAN_FOR_FIELD,
                      mJoust.getExpressionFactory().
                      createString(jsr175type.getQualifiedName()));

    for(int i=0; i<methods.length; i++) doOneProperty(methods[i]);
    mJoust.endClassOrInterface();
    mJoust.endFile();
    {
      // also generate a little properties file that will tell us at runtime
      // the name of the annobean class that the annotation type maps to.
      PropfileUtils.getInstance().writeAnnobeanTypeFor
          (jsr175type,annoBeanClassname,mOutputDir);
    }
  }



  /**
   * Generate code for a single 175 property.
   */
  private void doOneProperty(JMethod getter) throws IOException
  {
    if (getter == null) throw new IllegalArgumentException();
    String fieldName = FIELD_PREFIX+ getter.getSimpleName();
    JClass type = getter.getReturnType();
    String typeName = (!mImplementAnnotationTypes) ?
      getAnnobeanClassForIfGenerated(type) : type.getQualifiedName();
    Variable fieldVar =
      mJoust.writeField(Modifier.PRIVATE,typeName,fieldName,null);
    { // write the 'getter' implementation
      mJoust.startMethod(Modifier.PUBLIC,
                         typeName,
                         getter.getSimpleName(),
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
                                             SETTER_PREFIX+getter.getSimpleName(),
                                             paramTypeNames,
                                             paramNames,
                                             null // no throws
      );
      mJoust.writeAssignmentStatement(fieldVar,params[0]);
      mJoust.endMethodOrConstructor();
    }
    { // check to see if we need to also do annogen for the field's type
      JClass c = getter.getContainingClass().forName(typeName);
      if (c.isAnnotationType()) {
        if (!mClassesLeftTodo.contains(c) && !mClassesDone.contains(c)) {
          mClassesLeftTodo.add(c);
        }
      }
    }
  }

  private void warn(String msg) {
    System.out.println("[Warning] "+msg);
  }

  private String getAnnobeanClassForIfGenerated(JClass jsr175type) {
    if (jsr175type == null) throw new IllegalArgumentException();
    if (jsr175type.isArrayType()) {
      JClass comp = jsr175type.getArrayComponentType();
      StringWriter out = new StringWriter();
      out.write(getAnnobeanClassForIfGenerated(comp));
      for(int i=0; i<jsr175type.getArrayDimensions(); i++) out.write("[]");
      return out.toString();
    } else if (mClassesLeftTodo.contains(jsr175type) ||
        mClassesDone.contains(jsr175type)) {
      return getAnnobeanClassnameFor(jsr175type);
    }
    return jsr175type.getQualifiedName();
  }

  private String getAnnobeanClassnameFor(JClass jsr175type) {
    if (jsr175type == null) throw new IllegalArgumentException();
    String classname = jsr175type.getQualifiedName();
    if (mMappings != null) {
      for(int i=0; i<mMappings.length; i++) {
        String match = mMappings[i].getAnnoBeanFor(classname);
        if (match != null) return match;
      }
    }
    return getDefaultAnnobeanClassnameFor(jsr175type);
  }

  /**
   * Returns the annobean type name to use when the jsr175 type doesn't
   * match any of the task's mapping elements.
   */
  public static String getDefaultAnnobeanClassnameFor(JClass jsr175type) {
    if (jsr175type == null) throw new IllegalArgumentException();
    return
        // put it in a subpackage
        jsr175type.getContainingPackage().getQualifiedName()+".annobeans."+
        // with a suffix
        jsr175type.getSimpleName()+"Annobean";
  }



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

}