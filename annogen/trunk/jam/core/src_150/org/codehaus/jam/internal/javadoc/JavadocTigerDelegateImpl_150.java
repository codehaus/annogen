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
package org.codehaus.jam.internal.javadoc;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationTypeDoc;
import com.sun.javadoc.AnnotationTypeElementDoc;
import com.sun.javadoc.AnnotationValue;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.SourcePosition;
import com.sun.javadoc.Type;
import org.codehaus.jam.JClass;
import org.codehaus.jam.JamClassLoader;
import org.codehaus.jam.internal.elements.ElementContext;
import org.codehaus.jam.mutable.MAnnotatedElement;
import org.codehaus.jam.mutable.MAnnotation;
import org.codehaus.jam.mutable.MClass;
import org.codehaus.jam.provider.JamLogger;
import org.codehaus.jam.provider.JamServiceContext;



/**
 * Implementation of JavadocTigerDelegate.  This is where we sequester away
 * all of the code for dealing with 1.5-specific stuff in javadoc.
 * This class is compiled and loaded only under JRE 1.5.
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public final class JavadocTigerDelegateImpl_150 extends JavadocTigerDelegate {

  // ========================================================================
  // Hack, remove please


  private boolean mAnnotationDefaultsDisabled = false;

  // ========================================================================
  // Variables

  private ElementContext mContext;

  // ========================================================================
  // Javadoc15Delegate implementation

  public void init(ElementContext ctx) {
    if (ctx == null) throw new IllegalArgumentException();
    mContext = ctx;
    if (((JamServiceContext)ctx).getProperty(ANNOTATION_DEFAULTS_DISABLED_PROPERTY) != null) {
      mAnnotationDefaultsDisabled = true;
    }
  }


  //AnnotationType... are 1.5-specific, so we have to hide this little
  //chunk of code away down here
  public void populateAnnotationTypeIfNecessary(ClassDoc cd,
                                                MClass clazz,
                                                JavadocClassBuilder builder) {
    if (cd instanceof AnnotationTypeDoc) {
      clazz.setIsAnnotationType(true);
      AnnotationTypeElementDoc[] elements = ((AnnotationTypeDoc)cd).elements();
      for(int i=0; i<elements.length; i++) {
        builder.addMethod(clazz,elements[i]);
        //FIXME deal with defaults here
      }
    }
  }

  /*


  private void setAnnotationValue(String memberName,
                                  Type returnType,
                                  AnnotationValue aval,
                                  AnnoBean dest,
                                  SourcePosition sp) {
    String typeName = getFdFor(returnType);
    Object valueObj;
    try {
      valueObj = aval.value();
    } catch(NullPointerException npe) {
      //FIXME temporary workaround for sun bug
      mLogger.warning
        ("Encountered a known javadoc bug which usually \n"+
         "indicates a syntax error in an annotation value declaration.\n"+
         "The value is being ignored.\n"+
         "[file="+sp.file()+", line="+sp.line()+"]");
      return;
    }
    if (mLogger.isVerbose(this)) {
      mLogger.verbose(memberName+" is a "+typeName+" with valueObj "+
                                   valueObj+", class is "+valueObj.getClass());
    }
    // ok, take a look at how what it really is and translate it into an
    // appropriate represenatation
    if (valueObj instanceof AnnotationDesc) {
      MAnnotation nested = dest.createNestedValue(memberName,typeName);
      populateAnnotation(nested,(AnnotationDesc)valueObj,sp);
    } else if (valueObj instanceof Number || valueObj instanceof Boolean) {
      String tn = JavadocClassBuilder.getFdFor(returnType);
      JClass type = mContext.getClassLoader().loadClass(tn);
      dest.setSimpleValue(memberName,valueObj,type);
    } else if (valueObj instanceof FieldDoc) {
      String tn = JavadocClassBuilder.getFdFor(((FieldDoc)valueObj).containingClass());
      // this means it's an enum constant
      JClass type = mContext.getClassLoader().loadClass(tn);
      String val = ((FieldDoc)valueObj).name(); //REVIEW is this right?
      dest.setSimpleValue(memberName,val,type);
    } else if (valueObj instanceof ClassDoc) {
      String tn = JavadocClassBuilder.getFdFor(((FieldDoc)valueObj).containingClass());
       JClass clazz = mContext.getClassLoader().loadClass(tn);
      dest.setSimpleValue(memberName,clazz,loadClass(JClass.class));
    } else if (valueObj instanceof String) {
      String v = ((String)valueObj).trim();
      if (v.startsWith("\"") && v.endsWith("\"")) {
        //javadoc gives us the quotes, which seems kinda dumb.  just deal.
        valueObj = v.substring(1,v.length()-1);
      }
      dest.setSimpleValue(memberName,valueObj,loadClass(String.class));
    } else if (valueObj instanceof AnnotationValue[]) {
      populateArrayMember(dest,memberName,returnType,(AnnotationValue[])valueObj,sp);
    } else {
      mLogger.error("Value of annotation member "+memberName+" is " +
                                 "of an unexpected type: "+
                                 valueObj.getClass()+"   ["+valueObj+"]");
    }
  }
  */







  // ========================================================================
  // OLD STUFF remove someday


  public void extractAnnotations(MAnnotatedElement dest,
                                 ProgramElementDoc src) {
    if (dest == null) throw new IllegalArgumentException("null dest");
    if (src == null) throw new IllegalArgumentException("null src");
    extractAnnotations(dest,src.annotations(),src.position());
  }

  public void extractAnnotations(MAnnotatedElement dest,
                                 ExecutableMemberDoc method,
                                 Parameter src)
  {
    extractAnnotations(dest,src.annotations(),method.position());
  }

  public boolean isEnum(ClassDoc cd) {
    return cd.isEnum();
  }

  // ========================================================================
  // Private methods


  private void extractAnnotations(MAnnotatedElement dest,
                                  AnnotationDesc[] anns,
                                  SourcePosition sp)
  {
    if (anns == null) return; //?
    for(int i=0; i<anns.length; i++) {
      AnnotationTypeDoc type = getAnnotationTypeFor(anns[i],sp);
      if (type == null) continue; //oh well, this is javadoc's fault
      String tn = JavadocClassBuilder.getFdFor(type);
      MAnnotation destAnn = dest.findOrCreateAnnotation(tn);
      populateAnnotation(destAnn,anns[i],sp);
    }
  }

  private void populateAnnotation(MAnnotation dest,
                                  AnnotationDesc src,
                                  SourcePosition sp) {
    if (sp != null) JavadocClassBuilder.addSourcePosition(dest,sp);
    {
      AnnotationDesc.ElementValuePair[] mvps = src.elementValues();
      for(int i=0; i<mvps.length; i++) {
        Type jmt = mvps[i].element().returnType();
        String name = mvps[i].element().name();
        AnnotationValue aval = mvps[i].value();
        setAnnotationValue(name,jmt,aval,dest,sp);
      }
    }
if (mAnnotationDefaultsDisabled) return; 
    { // also set values for the type's defaults
      AnnotationTypeDoc atd = getAnnotationTypeFor(src,sp);
      if (atd == null) return;
      AnnotationTypeElementDoc[] elements = atd.elements();
      for(int i=0; i<elements.length; i++) {
        AnnotationValue value = elements[i].defaultValue();
        if (value != null) {
          String name = elements[i].name();
          if (dest.getValue(name) == null) {
            setAnnotationValue(name,elements[i].returnType(),
                               value,dest,sp);
          }
        }
      }
    }
  }

  private void setAnnotationValue(String memberName,
                                  Type returnType,
                                  AnnotationValue aval,
                                  MAnnotation dest,
                                  SourcePosition sp) {
    String typeName = getFdFor(returnType);
    Object valueObj;
    try {
      valueObj = aval.value();
    } catch(NullPointerException npe) {
      //FIXME temporary workaround for sun bug
      mContext.getLogger().warning
        ("Encountered a known javadoc bug which usually \n"+
         "indicates a syntax error in an annotation value declaration.\n"+
         "The value is being ignored.\n"+
         "[file="+sp.file()+", line="+sp.line()+"]");
      return;
    }
    if (mContext.getLogger().isVerbose(this)) {
      mContext.getLogger().verbose(memberName+" is a "+typeName+" with valueObj "+
                                   valueObj+", class is "+valueObj.getClass());
    }
    // ok, take a look at how what it really is and translate it into an
    // appropriate represenatation
    if (valueObj instanceof AnnotationDesc) {
      MAnnotation nested = dest.createNestedValue(memberName,typeName);
      populateAnnotation(nested,(AnnotationDesc)valueObj,sp);
    } else if (valueObj instanceof Number || valueObj instanceof Boolean) {
      String tn = JavadocClassBuilder.getFdFor(returnType);
      JClass type = mContext.getClassLoader().loadClass(tn);
      dest.setSimpleValue(memberName,valueObj,type);
    } else if (valueObj instanceof FieldDoc) {
      String tn = JavadocClassBuilder.getFdFor(((FieldDoc)valueObj).containingClass());
      // this means it's an enum constant
      JClass type = mContext.getClassLoader().loadClass(tn);
      String val = ((FieldDoc)valueObj).name(); //REVIEW is this right?
      dest.setSimpleValue(memberName,val,type);
    } else if (valueObj instanceof ClassDoc) {
      String tn = JavadocClassBuilder.getFdFor(((ClassDoc)valueObj).containingClass());
       JClass clazz = mContext.getClassLoader().loadClass(tn);
      dest.setSimpleValue(memberName,clazz,loadClass(JClass.class));
    } else if (valueObj instanceof String) {
      String v = ((String)valueObj).trim();
      if (v.startsWith("\"") && v.endsWith("\"")) {
        //javadoc gives us the quotes, which seems kinda dumb.  just deal.
        valueObj = v.substring(1,v.length()-1);
      }
      dest.setSimpleValue(memberName,valueObj,loadClass(String.class));
    } else if (valueObj instanceof AnnotationValue[]) {
      populateArrayMember(dest,memberName,returnType,(AnnotationValue[])valueObj,sp);
    } else {
      mContext.getLogger().error("Value of annotation member "+memberName+" is " +
                                 "of an unexpected type: "+
                                 valueObj.getClass()+"   ["+valueObj+"]");
    }
  }

  /**
   *
   * The javadocs for com.sun.javadoc.AnnotationValue.value() view as follows:
   *
   * <p><pre><i>
   *
   * Returns the value. The type of the returned object is one of the
   * following:
   *
   * a wrapper class for a primitive type
   * String
   * ClassDoc
   * FieldDoc (representing an enum constant)
   * AnnotationDesc
   * AnnotationValue[]
   *
   * </i></pre></p>
   *
   * <p>It seems quite broken to me that in the array case, it returns an array
   * of AnnotationValues.  It would be a lot easier to deal with the API
   * if that last line instead view "or an array of any of the above."
   * As it is, it's imposible to get the doclet API to give you a simple
   * array of ints, for example.  It's not at all clear what the extra
   * wrapping buys you.</p>
   *
   * <p>So, this method does a bunch of work so that JAM does the unwrapping
   * for the user.</p>
   */
  private void populateArrayMember(MAnnotation dest,
                                   String memberName,
                                   Type returnType,
                                   AnnotationValue[] annValueArray,
                                   SourcePosition sp)
  {
    if (sp != null) JavadocClassBuilder.addSourcePosition(dest,sp);
    if (annValueArray.length == 0) {
      Object[] value = new Object[0];
      //FIXME this is a little bit busted - we should try to give them
      //more type information than this.  it's just a little bit harder
      //to figure it out from the javadoc objects
      dest.setSimpleValue(memberName,value,loadClass(returnType));
      return;
    }
    // unpack the AnnotationValue values into a single array.
    Object[] valueArray = new Object[annValueArray.length];
    for(int i=0; i<valueArray.length; i++) {
      try {
        valueArray[i] = annValueArray[i].value();
        if (valueArray[i] == null) {
          mContext.getLogger().error
            ("Javadoc provided an array annotation member value which contains "+
             "[file="+sp.file()+", line="+sp.line()+"]");
          return;
        }
      } catch(NullPointerException npe) {
        //FIXME temporary workaround for sun bug
        mContext.getLogger().warning
          ("Encountered a known javadoc bug which usually \n"+
           "indicates a syntax error in an annotation value declaration.\n"+
           "The value is being ignored.\n"+
           "[file="+sp.file()+", line="+sp.line()+"]");
        return;
      }
    }
    // now go do something with them
    if (valueArray[0] instanceof AnnotationDesc) {
      AnnotationTypeDoc atd = getAnnotationTypeFor((AnnotationDesc)valueArray[0],sp);
      if (atd == null) return;
      String annType = getFdFor(atd);
      MAnnotation[] anns = dest.createNestedValueArray
        (memberName, annType, valueArray.length);
      for(int i=0; i<anns.length; i++) {
        populateAnnotation(anns[i],(AnnotationDesc)valueArray[i],sp);
      }
    } else if (valueArray[0] instanceof Number || valueArray[0] instanceof Boolean) {
      JClass type = loadClass(returnType);
      dest.setSimpleValue(memberName,annValueArray,type);
    } else if (valueArray[0] instanceof FieldDoc) {
      // this means it's an array of an enum constants
      String enumTypeName = JavadocClassBuilder.getFdFor(
        ((FieldDoc)valueArray[0]).containingClass());
      JClass memberType = loadClass("[L"+enumTypeName+";");
      String[] value = new String[valueArray.length];
      for(int i=0; i<valueArray.length; i++) {
        value[i] = ((FieldDoc)valueArray[i]).name(); //REVIEW is this right?
      }
      dest.setSimpleValue(memberName,value,memberType);
    } else if (valueArray[0] instanceof ClassDoc) {
      JClass[] value = new JClass[valueArray.length];
      for(int i=0; i<value.length; i++) {
        value[i] = loadClass(((ClassDoc)valueArray[0]));
      }
      dest.setSimpleValue(memberName,value,loadClass(JClass[].class));
    } else if (valueArray[0] instanceof String) {
      String[] value = new String[valueArray.length];
      for(int i=0; i<value.length; i++) {
        String v = ((String)valueArray[i]).trim();
        if (v.startsWith("\"") && v.endsWith("\"")) {
          //javadoc gives us the quotes, which seems kinda dumb.  just deal.
          v = v.substring(1,v.length()-1);
        }
        value[i] = v;
      }
      dest.setSimpleValue(memberName,value,loadClass(String[].class));
    } else {
      mContext.getLogger().error("Value of array annotation member "+
                                 memberName+" is of an unexpected type: "+
                                 valueArray[0].getClass()+"   ["+
                                 valueArray[0]+"]");
    }
  }

  private String getFdFor(Type t) { return JavadocClassBuilder.getFdFor(t); }

  private JClass loadClass(Type type) { return loadClass(getFdFor(type)); }

  //maybe we should put this on JamContext.getClassLoader()?
  private JClass loadClass(Class clazz) {
    return loadClass(clazz.getName());
  }

  private JClass loadClass(String fd) {
    return mContext.getClassLoader().loadClass(fd);
  }

  /**
   * This method is part of the fix/workaround for a javadoc bug
   * as described in issue 14: http://jira.codehaus.org/browse/ANNOGEN-14
   */
  private AnnotationTypeDoc getAnnotationTypeFor(AnnotationDesc d,
                                                 SourcePosition sp) {
    try {
      return d.annotationType();
    } catch(ClassCastException cce) {
      mContext.getLogger().error(new JavadocParsingException(d,sp,cce));
      return null;
    }
  }

}
