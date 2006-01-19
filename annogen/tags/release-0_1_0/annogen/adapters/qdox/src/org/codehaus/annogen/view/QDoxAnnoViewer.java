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

import com.thoughtworks.qdox.model.AbstractJavaEntity;
import com.thoughtworks.qdox.model.JavaParameter;
import org.codehaus.annogen.override.AnnoOverrider;

/**
 * Retrieves annotations using
 * <a href='http://qdox.codehaus.org' target='_blank'>QDox</a>.
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public interface QDoxAnnoViewer {

  // ========================================================================
  // Factory

  /**
   * Static factory for JavadocAnnoViewers.
   */
  public static class Factory {

    public static QDoxAnnoViewer create(AnnoViewerParams params) {
      throw new IllegalStateException("NYI");
    }

    public static QDoxAnnoViewer create() {
      throw new IllegalStateException("NYI");
    }

    public static QDoxAnnoViewer create(AnnoOverrider o) {
      throw new IllegalStateException("NYI");
    }

  }

  // ========================================================================
  // Public methods

  public /*AnnoBean*/ Object getAnnotation(Class annotationType,
                                           AbstractJavaEntity qdoxElement);

  public /*AnnoBean*/ Object getAnnotation(Class annotationType,
                                           JavaParameter parameter);

  public /*AnnoBean*/ Object[] getAnnotations(AbstractJavaEntity qdoxElement);

  public /*AnnoBean*/ Object[] getAnnotations(JavaParameter parameter);




}
