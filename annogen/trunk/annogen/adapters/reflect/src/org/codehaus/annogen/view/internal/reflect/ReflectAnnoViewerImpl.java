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

import org.codehaus.annogen.override.ReflectElementIdPool;
import org.codehaus.annogen.view.ReflectAnnoViewer;
import org.codehaus.annogen.view.internal.AnnoViewerBase;
import org.codehaus.annogen.view.internal.AnnoViewerParamsImpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class ReflectAnnoViewerImpl
    extends AnnoViewerBase implements ReflectAnnoViewer {

  // ========================================================================
  // Variables

  private ReflectElementIdPool mIdPool;

  // ========================================================================
  // Constructors

  public ReflectAnnoViewerImpl(AnnoViewerParamsImpl asp) {
    super(asp);
    mIdPool = ReflectElementIdPool.Factory.create(asp.getLogger());
  }

  // ========================================================================
  // ReflectAnnoViewer implementation

  public Object getAnnotation(Class annoClass, Package x) {
    return super.getAnnotation(annoClass, mIdPool.getIdFor(x));
  }

  public Object getAnnotation(Class annoClass, Class x) {
    return super.getAnnotation(annoClass, mIdPool.getIdFor(x));
  }

  public Object getAnnotation(Class annoClass, Constructor x) {
    return super.getAnnotation(annoClass, mIdPool.getIdFor(x));
  }

  public Object getAnnotation(Class annoClass, Field x) {
    return super.getAnnotation(annoClass, mIdPool.getIdFor(x));
  }

  public Object getAnnotation(Class annoClass, Method x) {
    return super.getAnnotation(annoClass, mIdPool.getIdFor(x));
  }

  public Object getAnnotation(Class annoClass, Constructor x, int pnum) {
    return super.getAnnotation(annoClass, mIdPool.getIdFor(x,pnum));
  }

  public Object getAnnotation(Class annoClass, Method x, int pnum) {
    return super.getAnnotation(annoClass, mIdPool.getIdFor(x,pnum));
  }

  public Object[] getAnnotations(Package x) {
    return super.getAnnotations(mIdPool.getIdFor(x));
  }

  public Object[] getAnnotations(Class x) {
    return super.getAnnotations(mIdPool.getIdFor(x));
  }

  public Object[] getAnnotations(Field x) {
    return super.getAnnotations(mIdPool.getIdFor(x));
  }

  public Object[] getAnnotations(Constructor x) {
    return super.getAnnotations(mIdPool.getIdFor(x));
  }

  public Object[] getAnnotations(Method x) {
    return super.getAnnotations(mIdPool.getIdFor(x));
  }

  public Object[] getAnnotations(Constructor x, int paramNum) {
    return super.getAnnotations(mIdPool.getIdFor(x,paramNum));
  }

  public Object[] getAnnotations(Method x, int paramNum) {
    return super.getAnnotations(mIdPool.getIdFor(x,paramNum));
  }

}
