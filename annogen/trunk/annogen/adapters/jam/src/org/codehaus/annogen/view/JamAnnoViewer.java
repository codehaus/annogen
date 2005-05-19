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

import org.codehaus.annogen.override.AnnoOverrider;
import org.codehaus.annogen.view.internal.AnnoViewerParamsImpl;
import org.codehaus.annogen.view.internal.jam.JamAnnoViewerImpl;
import org.codehaus.jam.JAnnotatedElement;

/**
 * Retrieves annotations usin JAM.
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public interface JamAnnoViewer {

  // ========================================================================
  // Factory

  /**
   * Static factory for JamAnnoViewers.
   */
  public static class Factory {


    public static JamAnnoViewer create(AnnoViewerParams params) {
      return new JamAnnoViewerImpl((AnnoViewerParamsImpl)params);
    }


    public static JamAnnoViewer create() {
      return new JamAnnoViewerImpl(new AnnoViewerParamsImpl());
    }

    public static JamAnnoViewer create(AnnoOverrider o) {
      AnnoViewerParamsImpl params = new AnnoViewerParamsImpl();
      params.addOverrider(o);
      return new JamAnnoViewerImpl(params);
    }

  }

  // ========================================================================
  // Public methods

  public Object getAnnotation(Class annotationType, JAnnotatedElement element);

  public Object[] getAnnotations(JAnnotatedElement element);

}
