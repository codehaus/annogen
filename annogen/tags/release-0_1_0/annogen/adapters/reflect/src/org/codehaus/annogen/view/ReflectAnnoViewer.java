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
package org.codehaus.annogen.view;

import org.codehaus.annogen.view.internal.AnnoViewerParamsImpl;
import org.codehaus.annogen.view.internal.reflect.ReflectAnnoViewerImpl;
import org.codehaus.annogen.override.AnnoOverrider;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Retrieves annotations using the Reflection API.
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public interface ReflectAnnoViewer {

  // ========================================================================
  // Factory

  /**
   * Static factory for ReflectAnnoViewers.
   */
  public static class Factory {

    public static ReflectAnnoViewer create(AnnoViewerParams params) {
      return new ReflectAnnoViewerImpl((AnnoViewerParamsImpl)params);
    }

    public static ReflectAnnoViewer create() {
      return new ReflectAnnoViewerImpl(new AnnoViewerParamsImpl());
    }

    public static ReflectAnnoViewer create(AnnoOverrider o) {
      AnnoViewerParamsImpl params = new AnnoViewerParamsImpl();
      params.addOverrider(o);
      return new ReflectAnnoViewerImpl(params);
    }

  }


  // ========================================================================
  // Public methods

  public Object getAnnotation(Class annotationType, Package pakkage);

  public Object getAnnotation(Class annotationType, Class clazz);

  public Object getAnnotation(Class annotationType, Constructor ctor);

  public Object getAnnotation(Class annotationType, Field field);

  public Object getAnnotation(Class annotationType, Method method);

  public Object getAnnotation(Class annotationType, Method method, int parameterNumber);

  public Object getAnnotation(Class annotationType, Constructor ctor, int parameterNumber);

  public Object[] getAnnotations(Package pakkage);

  public Object[] getAnnotations(Class clazz);

  public Object[] getAnnotations(Field field);

  public Object[] getAnnotations(Constructor ctor);

  public Object[] getAnnotations(Method field);

  public Object[] getAnnotations(Constructor ctor, int paramNum);

  public Object[] getAnnotations(Method field, int paramNum);


}
