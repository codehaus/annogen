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

import org.codehaus.jam.internal.TigerDelegateHelper;
import org.codehaus.jam.internal.elements.ElementContext;
import org.codehaus.jam.mutable.MClass;
import org.codehaus.jam.mutable.MConstructor;
import org.codehaus.jam.mutable.MField;
import org.codehaus.jam.mutable.MMember;
import org.codehaus.jam.mutable.MParameter;
import org.codehaus.jam.provider.JamLogger;
import org.codehaus.jam.JamClassLoader;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public abstract class ReflectTigerDelegate {

  // ========================================================================
  // Constants

  private static final String IMPL_NAME =
    "org.codehaus.jam.internal.reflect.ReflectTigerDelegateImpl_150";

  // ========================================================================
  // Static methods

  /**
   */
  public static ReflectTigerDelegate create(ElementContext ctx) {
    if (!TigerDelegateHelper.isTigerReflectionAvailable(ctx.getLogger()))
      return null;
    // ok, if we could load that, let's new up the extractor delegate
    try {
      ReflectTigerDelegate out = (ReflectTigerDelegate)
        Class.forName(IMPL_NAME).newInstance();
      out.init(ctx);
      return out;
    } catch (ClassNotFoundException e) {
      TigerDelegateHelper.issue14BuildWarning(e,ctx.getLogger());
    } catch (IllegalAccessException e) {
      ctx.getLogger().error(e);
    } catch (InstantiationException e) {
      ctx.getLogger().error(e);
    }
    return null;
  }


  // ========================================================================
  // Constructors

  protected ReflectTigerDelegate() {}

  // ========================================================================
  // Protected methods

  protected abstract void init(ElementContext ctx);

  // ========================================================================
  // Public methods

  public abstract void populateAnnotationTypeIfNecessary(Class cd,
                                                         MClass clazz,
                                                         ReflectClassBuilder builder);

  public abstract boolean isEnum(Class clazz);

  public abstract Constructor getEnclosingConstructor(Class clazz);

  public abstract Method getEnclosingMethod(Class clazz);

  public abstract void extractAnnotations(MMember dest, Method src);

  public abstract void extractAnnotations(MConstructor dest, Constructor src);

  public abstract void extractAnnotations(MField dest, Field src);

  public abstract void extractAnnotations(MClass dest, Class src);

  public abstract void extractAnnotations(MParameter dest, Method src, int paramNum);

  public abstract void extractAnnotations(MParameter dest, Constructor src, int paramNum);

}
