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
package org.codehaus.jam.internal.reflect;

import org.codehaus.jam.JClass;
import org.codehaus.jam.internal.elements.ElementContext;
import org.codehaus.jam.mutable.MAnnotatedElement;
import org.codehaus.jam.mutable.MAnnotation;
import org.codehaus.jam.mutable.MClass;
import org.codehaus.jam.mutable.MConstructor;
import org.codehaus.jam.mutable.MField;
import org.codehaus.jam.mutable.MMember;
import org.codehaus.jam.mutable.MParameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Implementation of ReflectTigerDelegate.  This is where we sequester away
 * all of the code for dealing with 1.5-specific stuff in java.lang.reflect.
 * This class is compiled and loaded only under JRE 1.5.
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public final class ReflectTigerDelegateImpl_150 extends ReflectTigerDelegate {


  // ========================================================================
  // Private

  private ElementContext mContext;

  // ========================================================================
  // Reflect15Delegate implementation

  public void init(ElementContext ctx) {
    if (ctx == null) throw new IllegalArgumentException();
    mContext = ctx;
  }

  public void populateAnnotationTypeIfNecessary(Class cd,
                                                MClass clazz,
                                                ReflectClassBuilder builder) {
    clazz.setIsAnnotationType(cd.isAnnotation());
    //FIXME deal with defaults here
  }

  public void extractAnnotations(MMember dest, Method src) {
    Annotation[] anns = src.getDeclaredAnnotations();
    if (anns == null) return;
    extractAnnotations(dest,anns);
  }

  public void extractAnnotations(MConstructor dest, Constructor src) {
    Annotation[] anns = src.getDeclaredAnnotations();
    if (anns == null) return;
    extractAnnotations(dest,anns);
  }

  public void extractAnnotations(MField dest, Field src) {
    Annotation[] anns = src.getDeclaredAnnotations();
    if (anns == null) return;
    extractAnnotations(dest,anns);
  }

  public void extractAnnotations(MClass dest, Class src) {
    Annotation[] anns = src.getDeclaredAnnotations();
    if (anns == null) return;
    extractAnnotations(dest,anns);
  }

  public void extractAnnotations(MParameter dest, Method src,
                                 int paramNum) {
    Annotation[][] anns;
    try {
      anns = src.getParameterAnnotations();
    } catch(NullPointerException wtf) {
      //FIXME workaround, sun code throws an NPE here
      if (mContext.getLogger().isVerbose(this)) {
        mContext.getLogger().verbose
          ("ignoring unexpected error while calling Method.getParameterAnnotations():");
        mContext.getLogger().verbose(wtf);
      }
      //wtf.printStackTrace();
      return;
    }
    if (anns == null) return;
    if (paramNum >= anns.length) {
      if (mContext.getLogger().isVerbose(this)) {
        mContext.getLogger().warning("method "+src.getName()+
                        " has fewer than expected parameter annotations ");
      }
      return;
    }
    extractAnnotations(dest,anns[paramNum]);
  }

  public void extractAnnotations(MParameter dest, Constructor src,
                                 int paramNum) {
    Annotation[][] anns;
    try {
      anns = src.getParameterAnnotations();
    } catch(NullPointerException wtf) {
      //FIXME workaround, sun code throws an NPE here
      if (mContext.getLogger().isVerbose(this)) {
        mContext.getLogger().verbose("ignoring unexpected error while calling Constructor.getParameterAnnotations():");
        mContext.getLogger().verbose(wtf);
      }
      //wtf.printStackTrace();
      return;
    }
    if (anns == null) return;
    if (paramNum >= anns.length) {
      if (mContext.getLogger().isVerbose(this)) {
        mContext.getLogger().warning("constructor "+src.getName()+
                        " has fewer than expected parameter annotations ");
      }
      return;
    }
    extractAnnotations(dest,anns[paramNum]);
  }

  public boolean isEnum(Class clazz) { return clazz.isEnum(); }

  public Constructor getEnclosingConstructor(Class clazz) {
    return clazz.getEnclosingConstructor();
  }

  public Method getEnclosingMethod(Class clazz) {
    return clazz.getEnclosingMethod();
  }

  // ========================================================================
  // Private methods

  private void extractAnnotations(MAnnotatedElement dest,
                                  Annotation[] anns)
  {
    if (anns == null || anns.length == 0) return;
    for(int i=0; i<anns.length; i++) {
      MAnnotation destAnn = dest.findOrCreateAnnotation
        (anns[i].annotationType().getName());
      destAnn.setAnnotationInstance(anns[i]);
      populateAnnotation(destAnn,anns[i]);
    }
    //FIXME also overlay default values once they're working in the JDK
  }

  private void populateAnnotation(MAnnotation dest, Annotation src) {
    boolean isVerbose = mContext.getLogger().isVerbose(this);
    if (src == null) throw new IllegalArgumentException();
    Class annType = src.annotationType();
    if (isVerbose) mContext.getLogger().verbose("type is "+annType.getName());
    //FIXME this is a bit clumsy right now - I think we need to be a little
    // more surgical in identifying the annotation member methods
    Method[] methods = annType.getMethods();
    for(int i=0; i<methods.length; i++) {
      if (isVerbose) mContext.getLogger().verbose("examining "+methods[i].toString());
      int mods = methods[i].getModifiers();
      if (Modifier.isStatic(mods)) continue;
      if (!Modifier.isPublic(mods)) continue;
      if (methods[i].getParameterTypes().length > 0) continue;
      {
        // try to limit it to real annotation methods.
        // FIXME seems like this could be better
        Class c = methods[i].getDeclaringClass();
        if (Object.class.equals(c) || Annotation.class.equals(c)) {
          continue;
        }
      }
      try {
        if (isVerbose) mContext.getLogger().verbose("invoking "+methods[i].getName()+"()");
        Object value = methods[i].invoke(src,(Object[])null);
        if (isVerbose) mContext.getLogger().verbose("value is "+value);
        if (value instanceof Annotation) {
          if (isVerbose) mContext.getLogger().verbose("it's nested!!  creating for "+
                                         methods[i].getName()+" and "+
                                         annType.getName());
          MAnnotation nested = dest.createNestedValue(methods[i].getName(),
                                                      annType.getName());
          nested.setAnnotationInstance(value);
          populateAnnotation(nested,(Annotation)value);
        } else if (value instanceof Annotation[]) {
          Annotation[] anns = (Annotation[])value;
          MAnnotation[] nested = dest.createNestedValueArray
            (methods[i].getName(),
             methods[i].getReturnType().getComponentType().getName(),
             anns.length);
          for(int j=0; j<anns.length; j++) populateAnnotation(nested[j],anns[j]);
        } else {
          JClass type = mContext.getClassLoader().
            loadClass(methods[i].getReturnType().getName());
          dest.setSimpleValue(methods[i].getName(),value,type);
        }
      } catch (IllegalAccessException e) {
        mContext.getLogger().warning(e);
      } catch (InvocationTargetException e) {
        mContext.getLogger().warning(e);
      }
    }
  }
}
