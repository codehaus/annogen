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
package org.codehaus.annogen.override.internal.reflect;

import org.codehaus.annogen.override.ElementId;
import org.codehaus.annogen.override.ReflectElementIdPool;
import org.codehaus.annogen.override.internal.ElementIdImpl;
import org.codehaus.annogen.view.internal.reflect.ReflectAnnogenTigerDelegate;
import org.codehaus.annogen.view.internal.reflect.ReflectIAE;
import org.codehaus.annogen.view.internal.reflect.ReflectIAE;
import org.codehaus.annogen.view.internal.reflect.ReflectAnnogenTigerDelegate;
import org.codehaus.jam.provider.JamLogger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Note that keeping the ElementIdFactories separate ensures that users
 * won't be forced at runtime to drag in classes they don't care about.
 * i.e., we don't want them to get NoClassDefFound when tools.jar isn't
 * in their classpath but they don't care about javadoc.
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class ReflectElementIdPoolImpl implements ReflectElementIdPool {

  // ========================================================================
  // Variables

  private ReflectAnnogenTigerDelegate mRTiger;

  // ========================================================================
  // Constructors

  public ReflectElementIdPoolImpl(JamLogger logger) {
    mRTiger = ReflectAnnogenTigerDelegate.create(logger);
  }

  // ========================================================================
  // ElementIdFactory implementation


  public ElementId getIdFor(Class clazz) {
    return ElementIdImpl.forClass(ReflectIAE.create(clazz,mRTiger),
                                  clazz.getName());
  }

  public ElementId getIdFor(Package pakkage) {
    return ElementIdImpl.forPackage(ReflectIAE.create(pakkage,mRTiger),
                                    pakkage.getName());
  }

  public ElementId getIdFor(Field field) {
    return ElementIdImpl.forField(ReflectIAE.create(field,mRTiger),
                                  field.getDeclaringClass().getName(),
                                  field.getName());
  }

  public ElementId getIdFor(Constructor ctor) {
    return ElementIdImpl.forConstructor(ReflectIAE.create(ctor,mRTiger),
                                        ctor.getDeclaringClass().getName(),
                                        getTypeNames(ctor.getParameterTypes()));
  }

  public ElementId getIdFor(Method method) {
    return ElementIdImpl.forMethod(ReflectIAE.create(method,mRTiger),
                                   method.getDeclaringClass().getName(),
                                   method.getName(),
                                   getTypeNames(method.getParameterTypes()));
  }

  public ElementId getIdFor(Method method, int paramNum) {
    return ElementIdImpl.forParameter(ReflectIAE.create(method,mRTiger),
                                      method.getDeclaringClass().getName(),
                                      method.getName(),
                                      getTypeNames(method.getParameterTypes()),
                                      paramNum);
  }

  public ElementId getIdFor(Constructor ctor, int paramNum) {
    return ElementIdImpl.forParameter(ReflectIAE.create(ctor,mRTiger),
                                      ctor.getDeclaringClass().getName(),
                                      ctor.getName(),
                                      getTypeNames(ctor.getParameterTypes()),
                                      paramNum);
  }

  // ========================================================================
  // Private methods

  private String[] getTypeNames(Class[] classes) {
    if (classes == null) return null;
    String[] out = new String[classes.length];
    for(int i=0; i<out.length; i++) out[i] = classes[i].getName();
    return out;
  }

}
