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
package org.codehaus.annogen.view.internal;

import org.codehaus.annogen.generate.AnnogenInfo;
import org.codehaus.annogen.override.AnnoBeanSet;
import org.codehaus.annogen.override.AnnoBean;
import org.codehaus.annogen.view.internal.reflect.ReflectAnnogenTigerDelegate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public final class ReflectAnnogenTigerDelegateImpl_150
  extends ReflectAnnogenTigerDelegate
 {

  // ========================================================================
  // Reflect15Delegate implementation

  public Class getAnnogenInfo_annoBeanClass(Class a175class)
    throws ClassNotFoundException
  {
    if (!a175class.isAnnotation()) {
      throw new IllegalArgumentException
        ("Requested type is not an Annotation: "+a175class.getName());
    }
    AnnogenInfo info = (AnnogenInfo)a175class.getAnnotation(AnnogenInfo.class);
    if (info == null) {
      throw new ClassNotFoundException
        ("Missing @AnnogenInfo on specified class "+a175class.getName());
    }
    return a175class.getClassLoader().loadClass(info.annoBeanClass());
  }

  public Class getAnnotationClassFor(/*Annotation*/Object annotation) {
    return ((Annotation)annotation).annotationType();
  }

  public boolean extractAnnotations(AnnoBeanSet out, Package on) {
    return doExtract(out,on.getAnnotations());
  }

  public boolean extractAnnotations(AnnoBeanSet out, Class on) {
    return doExtract(out,on.getAnnotations());
  }

  public boolean extractAnnotations(AnnoBeanSet out, Method on) {
    return doExtract(out,on.getAnnotations());
  }

  public boolean extractAnnotations(AnnoBeanSet out, Field on) {
    return doExtract(out,on.getAnnotations());
  }

  public boolean extractAnnotations(AnnoBeanSet out, Constructor on) {
    return doExtract(out,on.getAnnotations());
  }

  public boolean extractAnnotations(AnnoBeanSet out, Method on, int pnum) {
    Annotation[][] raw = on.getParameterAnnotations();
    if (out == null || raw.length <= pnum) return false;
    return doExtract(out,raw[pnum]);
  }

  public boolean extractAnnotations(AnnoBeanSet out, Constructor on, int pnum) {
    Annotation[][] raw = on.getParameterAnnotations();
    if (out == null || raw.length <= pnum) return false;
    return doExtract(out,raw[pnum]);
  }

  public Class getAnnogenInfoClass() {
    return AnnogenInfo.class;  // will only load under 1.5
  }


  // ========================================================================
  // Private methods

  private boolean doExtract(AnnoBeanSet out, Object[] raw) {
    if (raw == null || raw.length == 0) return false;
    for(int i=0; i<raw.length; i++) {
      Class declClass = getAnnotationClassFor(raw[i]);
      AnnoBean proxy = out.findOrCreateBeanFor(declClass);
      copyValues(raw[i],proxy,declClass);
    }
    return true;
  }

  private void copyValues(Object src, AnnoBean dest, Class declaredClass) {
    boolean isVerbose = false;
    if (src == null) throw new IllegalArgumentException();
    if (isVerbose) mLogger.verbose("type is "+declaredClass.getName());
    //FIXME this is a bit clumsy right now - I think we need to be a little
    // more surgical in identifying the annotation member methods
    Method[] methods = declaredClass.getMethods();
    for(int i=0; i<methods.length; i++) {
      if (isVerbose) mLogger.verbose("examining "+methods[i].toString());
      int mods = methods[i].getModifiers();
      if (Modifier.isStatic(mods)) continue;
      if (!Modifier.isPublic(mods)) continue;
      if (methods[i].getParameterTypes().length > 0) continue;
      {
        // try to limit it to real annotation methods.
        // FIXME seems like this could be better
        Class c = methods[i].getDeclaringClass();
        if (Object.class.equals(c)) continue;
      }
      if (isVerbose) mLogger.verbose("invoking "+methods[i].getName()+"()");
      Object value;
      try {
        value = methods[i].invoke(src, (Object[]) null);
      } catch (IllegalAccessException e) {
        mLogger.error(e);
        continue;
      } catch (InvocationTargetException e) {
        mLogger.error(e);
        continue;
      }
      if (isVerbose) mLogger.verbose("value is "+value);
      Class valClass = value.getClass();
      try {
      if (isSimpleType(valClass)) {
        dest.setValue(methods[i].getName(),value);
      } else if (valClass.isArray()) {
        if (isSimpleType(valClass.getComponentType())) {

        } else {

        }
        throw new IllegalArgumentException("array annotation properties NYI");

      } else {
        throw new IllegalArgumentException("complex annotation properties NYI "+
                                           valClass.getName());
      }
      } catch(Exception fixme) {
        mLogger.error(fixme);
      }
    }
  }

  private boolean isSimpleType(Class c) {
    return c.isPrimitive() ||
           java.lang.String.class.equals(c) ||
           java.lang.Number.class.isAssignableFrom(c) ||
           java.lang.Boolean.class.equals(c) ||
           java.lang.Class.class.equals(c);
  }
}
