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

import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.ProgramElementDoc;
import org.codehaus.annogen.override.AnnoOverrider;
import org.codehaus.annogen.view.internal.AnnoViewerParamsImpl;
import org.codehaus.annogen.view.internal.javadoc.JavadocAnnoViewerImpl;

/**
 * Retrieves annotations using the 
 * <a href='http://java.sun.com/j2se/javadoc' target='_blank'>Doclet</a>
 * API.
 *
 * @author Patrick Calahan &lt;email: codehaus-at-bea-dot-com&gt;
 */
public interface JavadocAnnoViewer {

  // ========================================================================
  // Factory

  /**
   * Static factory for JavadocAnnoViewers.
   */
  public static class Factory {

    public static JavadocAnnoViewer create(AnnoViewerParams params) {
      return new JavadocAnnoViewerImpl((AnnoViewerParamsImpl)params);
    }

    public static JavadocAnnoViewer create() {
      return new JavadocAnnoViewerImpl(new AnnoViewerParamsImpl());
    }

    public static JavadocAnnoViewer create(AnnoOverrider o) {
      AnnoViewerParamsImpl params = new AnnoViewerParamsImpl();
      params.addOverrider(o);
      return new JavadocAnnoViewerImpl(params);
    }

  }

  // ========================================================================
  // Public methods

  public /*AnnoBean*/ Object getAnnotation(Class annotationType,
                                           ProgramElementDoc ped);

  public /*AnnoBean*/ Object getAnnotation(Class annotationType,
                                           ExecutableMemberDoc ctorOrMethod,
                                           int parameterNumber);

  public /*AnnoBean*/ Object[] getAnnotations(ProgramElementDoc ped);

  public /*AnnoBean*/ Object[] getAnnotations(ExecutableMemberDoc ctorOrMethod,
                                              int parameterNumber);


}
