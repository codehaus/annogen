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
import org.codehaus.annogen.view.JamAnnoViewer;
import org.codehaus.annogen.view.internal.AnnoViewerBase;
import org.codehaus.annogen.view.internal.AnnoViewerParamsImpl;
import org.codehaus.jam.JAnnotatedElement;

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
    return super.getAnnotation(annotationType,mIdPool.getIdFor(element));
  }

  public Object[] getAnnotations(JAnnotatedElement element)
  {
    return super.getAnnotations(mIdPool.getIdFor(element));
  }
}