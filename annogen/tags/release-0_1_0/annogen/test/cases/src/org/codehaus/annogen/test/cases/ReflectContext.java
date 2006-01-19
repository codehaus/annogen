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
package org.codehaus.annogen.test.cases;

import org.codehaus.annogen.override.ElementId;
import org.codehaus.annogen.view.internal.reflect.ReflectIAE;
import org.codehaus.annogen.view.internal.reflect.ReflectAnnogenTigerDelegate;
import org.codehaus.annogen.view.internal.AnnoViewerBase;
import org.codehaus.annogen.view.AnnoViewerParams;
import org.codehaus.annogen.view.ReflectAnnoViewer;
import org.codehaus.jam.internal.JamLoggerImpl;

import java.lang.reflect.Method;

/**
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
/*package*/ class ReflectContext implements AnnogenContext {

  // ========================================================================
  // Variables

  private ReflectAnnogenTigerDelegate mTiger;
  private ClassLoader mClassloader;

  // ========================================================================
  // Constructors

  public ReflectContext(ClassLoader cl) {
    mClassloader = cl;
    mTiger = ReflectAnnogenTigerDelegate.create(new JamLoggerImpl());
  }

  // ========================================================================
  // ExtractorStuffer implementation

  public void stuffExtractor(TestElementId id) throws Exception {
    switch(id.getType()) {
      case ElementId.METHOD_TYPE:
        if (id.getSignature() != null && id.getSignature().length >1) {
          throw new IllegalStateException(); //FIXME
        }
        Class clazz = mClassloader.loadClass(id.getContainingClass());
        Method method = clazz.getMethod(id.getName(),null);
        id.setIAE(ReflectIAE.create(method,mTiger));
        return;

      default:
        throw new IllegalStateException();
    }

  }

  public AnnoViewerBase createViewer(AnnoViewerParams params) {
    return (AnnoViewerBase)ReflectAnnoViewer.Factory.create(params);
  }
}
