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
package org.codehaus.annogen.view.internal.reflect;

import org.codehaus.annogen.override.AnnoBeanSet;
import org.codehaus.jam.internal.TigerDelegateHelper;
import org.codehaus.jam.provider.JamLogger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public abstract class ReflectAnnogenTigerDelegate {

  // ========================================================================
  // Constants

  private static final String IMPL_NAME =
    "org.codehaus.annogen.view.internal.ReflectAnnogenTigerDelegateImpl_150";

  // ========================================================================
  // Static methods

  public static ReflectAnnogenTigerDelegate create(JamLogger logger) {
    if (!TigerDelegateHelper.isTigerReflectionAvailable(logger)) return null;
    // ok, if we could load that, let's new up the extractor delegate
    try {
      ReflectAnnogenTigerDelegate out = (ReflectAnnogenTigerDelegate)
        Class.forName(IMPL_NAME).newInstance();
      out.init(logger);
      return out;
    } catch (ClassNotFoundException e) {
      TigerDelegateHelper.issue14BuildWarning(e,logger);
    } catch (IllegalAccessException e) {
      logger.error(e);
    } catch (InstantiationException e) {
      logger.error(e);
    }
    return null;
  }


  // ========================================================================
  // Constructors

  protected ReflectAnnogenTigerDelegate() {}

  // ========================================================================
  // Proxy-type mapping methods

  /**
   * Returns the Jsr175 type of the given annotation object.  This is here
   * just to factor away static references to java.lang.Annotation.
   */
  public abstract Class getAnnotationClassFor(/*Annotation*/Object annotation);

  // ========================================================================
  // Annotation extraction methods

  public abstract void init(JamLogger l);

  public abstract boolean extractAnnotations(AnnoBeanSet out, Package on);

  public abstract boolean extractAnnotations(AnnoBeanSet out, Class on);

  public abstract boolean extractAnnotations(AnnoBeanSet out, Method on);

  public abstract boolean extractAnnotations(AnnoBeanSet out, Field on);

  public abstract boolean extractAnnotations(AnnoBeanSet out, Constructor on);

  public abstract boolean extractAnnotations(AnnoBeanSet out, Method on, int parmNum);

  public abstract boolean extractAnnotations(AnnoBeanSet out, Constructor on, int parmNum);

}
