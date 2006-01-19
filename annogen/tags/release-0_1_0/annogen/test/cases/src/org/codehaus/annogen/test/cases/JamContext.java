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
import org.codehaus.annogen.view.internal.jam.JamIAE;
import org.codehaus.annogen.view.internal.javadoc.JavadocAnnogenTigerDelegate;
import org.codehaus.annogen.view.internal.AnnoViewerBase;
import org.codehaus.annogen.view.JamAnnoViewer;
import org.codehaus.annogen.view.AnnoViewerParams;
import org.codehaus.jam.internal.JamLoggerImpl;
import org.codehaus.jam.JamClassLoader;
import org.codehaus.jam.JClass;
import org.codehaus.jam.JMethod;
import org.codehaus.jam.provider.JamLogger;

import java.lang.reflect.Method;

/**
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class JamContext implements AnnogenContext {

  private JamLogger mLogger;

  private JavadocAnnogenTigerDelegate mJTiger;
  private ReflectAnnogenTigerDelegate mRTiger;
  private AnnoViewerBase mViewer;

  private JamClassLoader mClassloader;

  // ========================================================================
  // Constructors

  public JamContext(JamClassLoader cl) {
    if (cl == null) throw new IllegalArgumentException();
    mClassloader = cl;
    mLogger = new JamLoggerImpl();
    mRTiger = ReflectAnnogenTigerDelegate.create(mLogger);
    mJTiger = JavadocAnnogenTigerDelegate.create(mLogger);
  }

  // ========================================================================
  // ExtactorStuffer implementation

  public void stuffExtractor(TestElementId id) throws Exception {
    switch(id.getType()) {
      case ElementId.METHOD_TYPE:
        if (id.getSignature() != null && id.getSignature().length >1) {
          throw new IllegalStateException(); //FIXME
        }
        JClass clazz = mClassloader.loadClass(id.getContainingClass());
        JMethod method = getMethodOn(clazz,id.getName(),id.getSignature());
        id.setIAE(JamIAE.create(method,mLogger,mRTiger,mJTiger));
        return;

      default:
        throw new IllegalStateException();
    }

  }

  public AnnoViewerBase createViewer(AnnoViewerParams params) {
    return (AnnoViewerBase)JamAnnoViewer.Factory.create(params);
  }

  // ========================================================================
  // Private methods

  private JMethod getMethodOn(JClass clazz, String methodName, String[] sig) {
    if (sig != null && sig.length > 0) throw new IllegalStateException();
    JMethod[] methods = clazz.getDeclaredMethods();
    for(int i=0; i<methods.length; i++) {
      if (methods[i].getSimpleName().equals(methodName)) return methods[i];
    }
    throw new IllegalArgumentException(clazz.getQualifiedName()+"."+methodName);
  }
}
