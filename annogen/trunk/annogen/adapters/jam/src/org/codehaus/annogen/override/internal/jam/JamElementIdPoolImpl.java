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
package org.codehaus.annogen.override.internal.jam;

import org.codehaus.annogen.override.ElementId;
import org.codehaus.annogen.override.JamElementIdPool;
import org.codehaus.annogen.override.internal.ElementIdImpl;
import org.codehaus.annogen.view.internal.IndigenousAnnoExtractor;
import org.codehaus.annogen.view.internal.jam.JamIAE;
import org.codehaus.annogen.view.internal.javadoc.JavadocAnnogenTigerDelegate;
import org.codehaus.annogen.view.internal.reflect.ReflectAnnogenTigerDelegate;
import org.codehaus.jam.JAnnotatedElement;
import org.codehaus.jam.JAnnotation;
import org.codehaus.jam.JClass;
import org.codehaus.jam.JComment;
import org.codehaus.jam.JConstructor;
import org.codehaus.jam.JField;
import org.codehaus.jam.JInvokable;
import org.codehaus.jam.JMethod;
import org.codehaus.jam.JPackage;
import org.codehaus.jam.JParameter;
import org.codehaus.jam.JProperty;
import org.codehaus.jam.provider.JamLogger;
import org.codehaus.jam.visitor.JVisitor;

/**
 * Note that keeping the ElementIdFactories separate ensures that users
 * won't be forced at runtime to drag in classes they don't care about.
 * i.e., we don't want them to get NoClassDefFound when tools.jar isn't
 * in their classpath but they don't care about javadoc.
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class JamElementIdPoolImpl implements JamElementIdPool {


  // ========================================================================
  // Variables

  private ReflectAnnogenTigerDelegate mRTiger;
  private JavadocAnnogenTigerDelegate mJTiger;
  private JamLogger mLogger;

  // ========================================================================
  // Constructors

  public JamElementIdPoolImpl(JamLogger logger) {
    mRTiger = ReflectAnnogenTigerDelegate.create(logger);
    mJTiger = JavadocAnnogenTigerDelegate.create(logger);
    mLogger = logger;
  }

  // ========================================================================
  // JamElementIdPool implementation

  public ElementId getIdFor(final JAnnotatedElement element) {

    final IndigenousAnnoExtractor iae = JamIAE.create(element,
                                                      mLogger,
                                                      mRTiger,
                                                      mJTiger);

    class MyVisitor extends JVisitor {

      public ElementId result = null;

      public void visit(JPackage pkg) {
        result = ElementIdImpl.forPackage(iae, element.getQualifiedName());
      }

      public void visit(JClass clazz) {
        result = ElementIdImpl.forClass(iae, element.getQualifiedName());
      }

      public void visit(JConstructor ctor) {
        result = ElementIdImpl.forConstructor
          (iae,
           ctor.getContainingClass().getQualifiedName(),
           getSignature(ctor));
      }

      public void visit(JField field) {
        result = ElementIdImpl.forField
          (iae,
           field.getContainingClass().getQualifiedName(),
           field.getSimpleName());
      }

      public void visit(JMethod method) {
        result = ElementIdImpl.forMethod
          (iae,
           method.getContainingClass().getQualifiedName(),
           method.getSimpleName(),
           getSignature(method));
      }

      public void visit(JParameter param) {
        JInvokable parent = (JInvokable)param.getParent();
        result = ElementIdImpl.forParameter
          (iae,
           parent.getContainingClass().getQualifiedName(),
           parent.getSimpleName(),
           getSignature(parent),
           JamIAE.getParameterNumber(param));
      }

      public void visit(JAnnotation ann) {
        throw new IllegalStateException("annotations not supported here");
      }

      public void visit(JComment comment) {
        throw new IllegalStateException("comments not supported here");
      }
    }
    MyVisitor myVisitor = new MyVisitor();
    element.accept(myVisitor);
    if (myVisitor.result == null) throw new IllegalStateException("no result");
    return myVisitor.result;
  }


  // ========================================================================
  // Private methods

  private String[] getSignature(JInvokable ji) {
    if (ji == null) throw new IllegalArgumentException();
    JParameter[] params = ji.getParameters();
    if (params == null || params.length == 0) return null;
    String[] out = new String[params.length];
    for(int i=0; i<out.length; i++) {
      out[i] = params[i].getType().getQualifiedName();
    }
    return out;
  }
}
