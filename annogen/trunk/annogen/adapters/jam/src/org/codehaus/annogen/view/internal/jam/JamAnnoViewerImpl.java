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
package org.codehaus.annogen.view.internal.jam;

import org.codehaus.annogen.override.JamElementIdPool;
import org.codehaus.annogen.override.ElementId;
import org.codehaus.annogen.view.JamAnnoViewer;
import org.codehaus.annogen.view.internal.AnnoViewerBase;
import org.codehaus.annogen.view.internal.AnnoViewerParamsImpl;
import org.codehaus.jam.JAnnotatedElement;
import org.codehaus.jam.JProperty;
import org.codehaus.jam.JMethod;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of JamAnnoViewer, which provides an annotation override
 * service using JAM constructs as keys (e.g. JClass).  This implementation
 * works by digging a little bit into the JElement to figure out what kind
 * of artifcat it wraps (i.e. what reflection or javadoc thing) and
 * dispatching appropriately to a Reflect- or Javadoc- AnnoService.
 * 
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class JamAnnoViewerImpl extends AnnoViewerBase
  implements JamAnnoViewer
 {
  // ========================================================================
  // Variables

  private JamElementIdPool mIdPool;

  // ========================================================================
  // Constructors

  public JamAnnoViewerImpl(AnnoViewerParamsImpl context) {
    super(context);
    mIdPool = JamElementIdPool.Factory.create(context.getLogger());
  }

  // ========================================================================
  // JamAnnoViewer implementation

  public Object getAnnotation(Class annotationType, JAnnotatedElement element)
  {
    if (element instanceof JProperty) {
      return getAnnotation(annotationType,(JProperty)element);
    } else {
      return super.getAnnotation(annotationType,mIdPool.getIdFor(element));
    }
  }



  public Object[] getAnnotations(JAnnotatedElement element)
  {
    if (element instanceof JProperty) {
      return getAnnotations((JProperty)element);
    } else {
      return super.getAnnotations(mIdPool.getIdFor(element));
    }
  }

  // ========================================================================
  // Private methods

  /**
   * JAM is unique among the introspection APIs in that it treats properties
   * as first-class citizens of the model.  They require some special
   * handling when the user asks for an annotation on one.
   */
  private Object getAnnotation(Class annotationType, JProperty property)
  {
    JMethod g = property.getGetter();
    JMethod s = property.getSetter();
    ElementId gId = (g == null) ? null : mIdPool.getIdFor(property.getGetter());
    ElementId sId = (s == null) ? null : mIdPool.getIdFor(property.getSetter());
    Object gAnn = (gId == null) ? null : super.getAnnotation(annotationType,gId);
    Object sAnn = (sId == null) ? null : super.getAnnotation(annotationType,sId);
    if (gAnn != null) {
      if (sAnn != null) {
      mLogger.warning("Property '"+property.getQualifiedName()+"' has a "+
                      annotationType.getName()+" annotation on both "+
                      "the getter and the setter.  Ignoring the one "+
                      "on the setter is being ignored.");
      }
      return gAnn;
    }  else if (sAnn != null) {
      return sAnn;
    } else{
      return null;
    }
  }

  /**
   * JAM is unique among the introspection APIs in that it treats properties
   * as first-class citizens of the model.  They require some special
   * handling when the user asks for an annotation on one.
   */
  private Object[] getAnnotations(JProperty property)
  {
    JMethod g = property.getGetter();
    JMethod s = property.getSetter();
    ElementId gId = (g == null) ? null : mIdPool.getIdFor(property.getGetter());
    ElementId sId = (s == null) ? null : mIdPool.getIdFor(property.getSetter());
    Object[] gAnn = (gId == null) ? null : super.getAnnotations(gId);
    Object[] sAnn = (gId == null) ? null : super.getAnnotations(sId);
    if (gAnn == null) return sAnn;
    if (sAnn == null) return gAnn;
    //REVIEW in the case where there are annotations on both, we just
    //return them all.  This introduces the possibility that they will get
    //two instances of the same annotation type in the array.  Arguably
    //we should detect this case and warn them, but I'm not sure.
    List list = new ArrayList();
    list.addAll(Arrays.asList(gAnn));
    list.addAll(Arrays.asList(sAnn));
    Object[] out = new Object[list.size()];
    list.toArray(out);
    return out;
  }
}