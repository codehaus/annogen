/*
* The Apache Software License, Version 1.1
*
*
* Copyright (c) 2003 The Apache Software Foundation.  All rights
* reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions
* are met:
*
* 1. Redistributions of source code must retain the above copyright
*    notice, this list of conditions and the following disclaimer.
*
* 2. Redistributions in binary form must reproduce the above copyright
*    notice, this list of conditions and the following disclaimer in
*    the documentation and/or other materials provided with the
*    distribution.
*
* 3. The end-user documentation included with the redistribution,
*    if any, must include the following acknowledgment:
*       "This product includes software developed by the
*        Apache Software Foundation (http://www.apache.org/)."
*    Alternately, this acknowledgment may appear in the software itself,
*    if and wherever such third-party acknowledgments normally appear.
*
* 4. The names "Apache" and "Apache Software Foundation" must
*    not be used to endorse or promote products derived from this
*    software without prior written permission. For written
*    permission, please contact apache@apache.org.
*
* 5. Products derived from this software may not be called "Apache
*    XMLBeans", nor may "Apache" appear in their name, without prior
*    written permission of the Apache Software Foundation.
*
* THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
* WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
* OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
* ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
* SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
* LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
* USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
* ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
* OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
* OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
* SUCH DAMAGE.
* ====================================================================
*
* This software consists of voluntary contributions made by many
* individuals on behalf of the Apache Software Foundation and was
* originally based on software copyright (c) 2003 BEA Systems
* Inc., <http://www.bea.com/>. For more information on the Apache Software
* Foundation, please see <http://www.apache.org/>.
*/
package org.codehaus.jam.test.cases;

import org.codehaus.jam.JAnnotation;
import org.codehaus.jam.JAnnotationValue;
import org.codehaus.jam.JClass;
import org.codehaus.jam.JamService;
import org.codehaus.jam.JMethod;
import org.codehaus.jam.JParameter;
import org.codehaus.jam.test.samples.jsr175.EmployeeAnnotation;
import org.codehaus.jam.test.samples.jsr175.EmployeeGroupAnnotation;
import org.codehaus.jam.test.samples.jsr175.RFEAnnotation_150;
import org.codehaus.jam.test.cases.JamTestBase;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

/**
 * <p>Abstract base class for basic jam test samples.  These test samples work
 * against an abstract JamService - they don't care how the java types
 * were loaded.  Extending classes are responsible for implementing the
 * getService() method which should create the service from sources, or
 * classes, or whatever is appropriate.</p>
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public abstract class JamTestBase_150 extends JamTestBase {

  // ========================================================================
  // Cosntants

  private static final String[] ADDITIONAL_15_CLASSES = {

    "org.codehaus.jam.test.samples.jsr175.AnnotatedClass",
    "org.codehaus.jam.test.samples.jsr175.NestedAnnotatedClass",
    "org.codehaus.jam.test.samples.jsr175.AddressAnnotation",
    "org.codehaus.jam.test.samples.jsr175.EmployeeAnnotation",
    "org.codehaus.jam.test.samples.jsr175.EmployeeGroupAnnotation",
    "org.codehaus.jam.test.samples.jsr175.Constants",

    "org.codehaus.jam.test.samples.jsr175.RFEAnnotation_150",

    "org.codehaus.jam.test.samples.MyEnum",
    "org.codehaus.jam.test.samples.MyGenericThing",
    "org.codehaus.jam.test.samples.MyGenericThingSubclass",
  };


  // ========================================================================
  // Constructors

  public JamTestBase_150() {
    super("JamTestBase");
    System.out.println("constructed JamTestBase");
  }

  public JamTestBase_150(String casename) {
    super(casename);
    System.out.println("constructed JamTestBase "+casename);
  }

  // ========================================================================
  // Test methods

  public void testIsEnum() {
    JClass gts = resolved(mLoader.loadClass("org.codehaus.jam.test.samples.MyEnum"));
    assertTrue(gts.getQualifiedName()+" - isEnumType() must be true for "+
               gts.getQualifiedName(), gts.isEnumType() == true);
  }

  public void testGenerics() {
    JClass gts = mLoader.loadClass("org.codehaus.jam.test.samples.MyGenericThingSubclass");
    resolved(gts);
//    System.out.println("=========== "+gts.getSuperclass().getQualifiedName());
  }


  public void test175Annotations() throws IOException, XMLStreamException {
    JClass clazz = resolved(mLoader.loadClass("org.codehaus.jam.test.samples.jsr175.AnnotatedClass"));
    JAnnotation ann = clazz.getAnnotation(RFEAnnotation_150.class);
    assertTrue("no "+RFEAnnotation_150.class+ " on "+clazz.getQualifiedName(),
               ann != null);
    if (!is175AnnotationInstanceAvailable()) return; //FIXME test untyped access
    RFEAnnotation_150 rfe = (RFEAnnotation_150)ann.getAnnotationInstance();
    assertTrue("id = "+rfe.id(), rfe.id() == 4561414);
    assertTrue("synopsis = '"+rfe.synopsis()+"'",
               rfe.synopsis().equals("Balance the federal budget"));
  }

  public void testGoofyParamNamesCase() throws Exception {
    JamService js = getResultToTest();
    JClass fooImpl = js.getClassLoader().loadClass("org.codehaus.jam.test.samples.Baz");
    JMethod[] methods = fooImpl.getDeclaredMethods();
    for(int i=0; i<methods.length; i++) {
      if (methods[i].getSimpleName().equals("getAString")) {
        JClass string = methods[i].getReturnType();
        JMethod[] smethods = string.getMethods();
        for(int j=0; j<smethods.length; j++) {
          if (smethods[j].getSimpleName().equals("regionMatches")) {
            JParameter[] params = smethods[j].getParameters();
            for(int x=0; x<params.length; x++) {
              System.out.println("=== "+params[x].getSimpleName());
            }
            return;
          }
        }
      }
    }
    assertTrue("couldn't find something",false);
  }

  public void testNested175AnnotationsUntyped() throws IOException, XMLStreamException {
    JClass clazz = resolved(mLoader.loadClass("org.codehaus.jam.test.samples.jsr175.NestedAnnotatedClass"));
    JAnnotation employeeGroup = clazz.getAnnotation(EmployeeGroupAnnotation.class);

    assertTrue("employeeGroup is null", employeeGroup != null);
    JAnnotationValue employeeListValue = employeeGroup.getValue("employees");

    // assert some stuff about the type of the value
    JClass employeeArrayClass = employeeListValue.getType();
    assertTrue("'employees' is not an array", employeeArrayClass.isArrayType());
    assertTrue("'employees' is an annotation?!", !employeeArrayClass.isAnnotationType());
    JClass employeeClass = employeeArrayClass.getArrayComponentType();
    assertTrue("'employees type component' is an array?!",
               !employeeClass.isArrayType());
    assertTrue("'employees type component' is not an annotation",
               employeeClass.isAnnotationType());



    JClass listType = employeeListValue.getType();
    assertTrue("listType is null", listType != null);
    assertTrue("listType is "+listType.getFieldDescriptor()+", expecting"+
               EmployeeAnnotation[].class.getName(),
               EmployeeAnnotation[].class.getName().equals(listType.getFieldDescriptor()));
    assertTrue("employees list is null", employeeListValue != null);
    JAnnotation[] employeeList = employeeListValue.asAnnotationArray();
    assertTrue("employees list is null", employeeList != null);
    assertTrue("employees list length is "+employeeList.length+", expecting 2",
               employeeList.length == 2);
    JAnnotation boog = employeeList[0];
    assertTrue("boog annotation is null",boog != null);
    //FIXME shouldnt be isCommentsAvailable
    if (isCommentsAvailable()) assertTrue(boog.getSourcePosition() != null);
    {
      JAnnotationValue firstName = boog.getValue("firstName");
      assertTrue("firstName is null",firstName != null);
      assertTrue("firstName is "+firstName.asString(),
                 firstName.asString().equals("Boog"));
      JClass type = firstName.getType();
      assertTrue("firstName type is null",type != null);
      assertTrue("firstName type is "+type.getQualifiedName(),
                 "java.lang.String".equals(type.getQualifiedName()));
    }
    {
      JAnnotationValue aka = boog.getValue("aka");
      assertTrue("aka is null",aka != null);
      JClass type = aka.getType();
      assertTrue("aka type is null",type != null);
      assertTrue("aka type is "+type.getFieldDescriptor(),
                 String[].class.getName().equals(type.getFieldDescriptor()));
      String[] akas = aka.asStringArray();
      assertTrue("akas is null",akas != null);
      assertTrue("akas length is "+akas.length, akas.length == 3);
      assertTrue("akas type is not an array ", type.isArrayType());

      for(int i=0; i<akas.length; i++) {
        assertTrue("akas "+i+" is empty '"+akas[i]+"'",
                   (akas[i] != null && akas[i].trim().length() > 0));
      }
    }
    {
      JAnnotationValue active = boog.getValue("active");
      assertTrue("active is null",active != null);
      assertTrue("active = "+active.asString(),
                 active.asString().equals("TRUE"));
      JClass type = active.getType();
      assertTrue("active type is null",type != null);

      assertTrue("active type is "+type.getQualifiedName(),
               type.getQualifiedName().equals
                 ("org.codehaus.jam.test.samples.jsr175.Constants$Bool"));

      //FIXME javadoc seems to have a bug in it, not telling is it's an enum
      //assertTrue("active type is not an enum", ((ClassImpl)type).isEnumType());
    }
    {
      JAnnotationValue lastName = boog.getValue("lastName");
      assertTrue("lastName is null",lastName != null);
      assertTrue("lastName is "+lastName.asString(),
                 lastName.asString().equals("Powell"));
      JClass lastNameType = lastName.getType();
      assertTrue("street type is null",lastNameType != null);
      assertTrue("lastNameType "+lastNameType.getQualifiedName(),
                 "java.lang.String".equals(lastNameType.getQualifiedName()));
    }
    {
      JAnnotationValue specialDigits = boog.getValue("specialDigits");
      assertTrue("specialDigits is null",specialDigits != null);
      int[] expect = { 8, 6, 7, 5, 3, 0, 9 };
      assertTrue("specialDigits does not contain expected digits",
                 Arrays.equals(expect,specialDigits.asIntArray()));
      JClass specialDigitsType = specialDigits.getType();
      assertTrue("specialDigits type is null",specialDigitsType != null);
      assertTrue("specialDigits type is "+specialDigitsType.getFieldDescriptor()+
                 ", expecting "+int[].class.getName(),
                 specialDigitsType.getFieldDescriptor().equals(int[].class.getName()));
      assertTrue("specialDigits type is not an array ",
                 specialDigitsType.isArrayType());
    }
    {
      JAnnotationValue addressValue = boog.getValue("address");
      assertTrue("address is null",addressValue != null);
      JAnnotation address = addressValue.asAnnotation();
      assertTrue("address is null",address != null);
      //FIXME shouldnt be isCommentsAvailable
      if (isCommentsAvailable()) assertTrue(boog.getSourcePosition() != null);

      {
        JAnnotationValue street = address.getValue("street");
        assertTrue("street is null",street != null);
        assertTrue("street is "+street.asString(),
                   street.asString().equals("123 shady lane"));
        JClass streetType = street.getType();
        assertTrue("street type is null",streetType != null);
        assertTrue("streetType "+streetType.getQualifiedName(),
                   streetType.getQualifiedName().equals("java.lang.String"));
      }
      {
        JAnnotationValue city = address.getValue("city");
        assertTrue("city is null",city != null);
        assertTrue("city is "+city.asString(),
                   city.asString().equals("Cooperstown"));
        JClass cityType = city.getType();
        assertTrue("street type is null",cityType != null);
        assertTrue("cityType "+cityType.getQualifiedName(),
                   cityType.getQualifiedName().equals("java.lang.String"));

      }
      {
        JAnnotationValue zip = address.getValue("zip");
        assertTrue("zip is null",zip != null);
        assertTrue("zip is "+zip.asInt(),
                   zip.asInt() == 123456);
        JClass zipType = zip.getType();
        assertTrue("street type is null",zipType != null);
        assertTrue("zipType "+zipType.getQualifiedName(),
                   zipType.getQualifiedName().equals("int"));
        assertTrue("zipType not primitive", zipType.isPrimitiveType());
      }
    }
  }

  protected void addAllKnownClasses(Collection c) {
    c.addAll(Arrays.asList(ADDITIONAL_15_CLASSES));
    super.addAllKnownClasses(c);
  }

  protected File[] getSamplesSourcepath() {
    File[] path = super.getSamplesSourcepath();
    File[] out = new File[path.length+1];
    for(int i=0; i<path.length; i++) out[i] = path[i];
    out[out.length-1] = new File(SAMPLES_DIR,"src_150");
    return out;
  }

}