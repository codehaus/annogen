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

import com.sun.mirror.declaration.Declaration;
import org.codehaus.annogen.override.AnnoOverrider;

import java.lang.annotation.Annotation;

/**
 * Retrieves annotations using the Mirror API.
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public interface MirrorAnnoViewer {

  // ========================================================================
  // Factory

  /**
   * Static factory for JavadocAnnoViewers.
   */
  public static class Factory {

    public static MirrorAnnoViewer create(AnnoViewerParams params) {
      throw new IllegalStateException("NYI");
    }

    public static MirrorAnnoViewer create() {
      throw new IllegalStateException("NYI");
    }

    public static MirrorAnnoViewer create(AnnoOverrider o) {
      throw new IllegalStateException("NYI");
    }

  }

  // ========================================================================
  // Public methods

  public <A extends Annotation> A getAnnotation(Class<A> annotationClass,
                                                Declaration mirrorElement);

  public Annotation[] getAnnotations(Declaration mirrorElement);

}
