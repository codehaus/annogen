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
package org.codehaus.annogen.override;

import org.codehaus.annogen.override.internal.reflect.ReflectElementIdPoolImpl;
import org.codehaus.annogen.override.internal.reflect.ReflectElementIdPoolImpl;
import org.codehaus.jam.internal.JamLoggerImpl;
import org.codehaus.jam.provider.JamLogger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Creates ElementIds using the Reflection API.
 * To maximize efficiency you should try to share instances of
 * IdPools as much as possible.
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public interface ReflectElementIdPool {

  // ========================================================================
  // Factory

  /**
   * Static factory for ReflectElementIdPools.
   */
  public static class Factory {
    public static ReflectElementIdPool create(JamLogger logger) {
      return new ReflectElementIdPoolImpl(logger);
    }

    public static ReflectElementIdPool create() {
      return new ReflectElementIdPoolImpl(new JamLoggerImpl());
    }
  }

  // ========================================================================
  // Reflection-based public methods

  public ElementId getIdFor(Class clazz);

  public ElementId getIdFor(Package pakkage);

  public ElementId getIdFor(Field field);

  public ElementId getIdFor(Constructor ctor);

  public ElementId getIdFor(Method method);

  public ElementId getIdFor(Method method, int paramNum);

  public ElementId getIdFor(Constructor ctor, int paramNum);

}
