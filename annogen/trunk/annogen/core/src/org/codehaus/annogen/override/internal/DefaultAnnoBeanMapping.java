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
package org.codehaus.annogen.override.internal;

import org.codehaus.annogen.override.AnnoBean;
import org.codehaus.annogen.override.AnnoBeanMapping;
import org.codehaus.annogen.view.internal.reflect.ReflectAnnogenTigerDelegate;
import org.codehaus.annogen.view.internal.reflect.ReflectAnnogenTigerDelegate;
import org.codehaus.jam.provider.JamLogger;

import java.lang.reflect.Field;

/**
 * Utility class for mapping between declared anno types and annobean types.
 * This is done by looking at the AnnogenInfo annotation (on a
 * declared type) or at the 'PROXY_FOR' static string (on an annobean).
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class DefaultAnnoBeanMapping implements AnnoBeanMapping {

  // ========================================================================
  // Constants

  public static final String PROXY_FOR = "PROXY_FOR";

  // ========================================================================
  // Variables

  private ReflectAnnogenTigerDelegate mDelegate = null;

  // ========================================================================
  // Constructor

  //REVIEW maybe make this a singleton?
  public DefaultAnnoBeanMapping(JamLogger logger) {
    mDelegate = ReflectAnnogenTigerDelegate.create(logger);
  }

  // ========================================================================
  // Public methods

  public Class getAnnoBeanClassForRequest(Class requestedClass)
    throws ClassNotFoundException
  {
    if (AnnoBean.class.isAssignableFrom(requestedClass)) {
      return requestedClass;
    } else {
      if (mDelegate == null) {
        throw new IllegalStateException
          ("Delegate failed to initialize, check log for errors.");
      }
      return mDelegate.getAnnogenInfo_annoBeanClass(requestedClass);
    }
  }

  public Class getDeclaredClassForAnnoBeanClass(Class beanClass)
    throws ClassNotFoundException
  {
    if (!AnnoBean.class.isAssignableFrom(beanClass)) {
      throw new IllegalArgumentException(beanClass.getName()+
                                         " is not a AnnoBean");
    }
    Field f;
    try {
      f = beanClass.getField(PROXY_FOR);
    } catch(NoSuchFieldException nsfe) {
      throw new IllegalArgumentException
        (beanClass.getName()+" is an AnnoBean but does not have a "+
         PROXY_FOR+" field");
    }
    String declaredTypeName;
    try {
      declaredTypeName = (String)f.get(null);
      return beanClass.getClass().getClassLoader().loadClass(declaredTypeName);
    } catch (IllegalAccessException e) {
      try {
        throw new ClassNotFoundException().initCause(e);
      } catch(Throwable unlikely) {
        unlikely.printStackTrace();
        throw new IllegalStateException();
      }
    }
  }

}