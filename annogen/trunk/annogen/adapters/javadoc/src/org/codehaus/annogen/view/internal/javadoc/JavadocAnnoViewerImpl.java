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
package org.codehaus.annogen.view.internal.javadoc;

import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.ProgramElementDoc;
import org.codehaus.annogen.override.JavadocElementIdPool;
import org.codehaus.annogen.view.JavadocAnnoViewer;
import org.codehaus.annogen.view.internal.AnnoViewerBase;
import org.codehaus.annogen.view.internal.AnnoViewerParamsImpl;

/**
 * @author Patrick Calahan &lt;email: codehaus-at-bea-dot-com&gt;
 */
public class JavadocAnnoViewerImpl
    extends AnnoViewerBase implements JavadocAnnoViewer {

  // ========================================================================
  // Variables

  private JavadocElementIdPool mIdPool;

  // ========================================================================
  // Constructors

  public JavadocAnnoViewerImpl(AnnoViewerParamsImpl asp) {
    super(asp);
    mIdPool = JavadocElementIdPool.Factory.create(asp.getLogger());
  }

  // ========================================================================
  // JavadocAnnoViewer implementation

  public Object getAnnotation(Class annoClass, ProgramElementDoc ped) {
    return super.getAnnotation(annoClass,mIdPool.getIdFor(ped));
  }

  public /*AnnoBean*/ Object[] getAnnotations(ProgramElementDoc ped) {
    return super.getAnnotations(mIdPool.getIdFor(ped));
  }

  public /*AnnoBean*/ Object getAnnotation(Class annotationType,
                                            ExecutableMemberDoc ctorOrMethod,
                                            int paramNum) {
    return super.getAnnotation(annotationType,
                               mIdPool.getIdFor(ctorOrMethod,paramNum));
  }


  public /*AnnoBean*/ Object[] getAnnotations(ExecutableMemberDoc ctorOrMethod,
                                               int paramNum) {
    return super.getAnnotations(mIdPool.getIdFor(ctorOrMethod,paramNum));
  }

}