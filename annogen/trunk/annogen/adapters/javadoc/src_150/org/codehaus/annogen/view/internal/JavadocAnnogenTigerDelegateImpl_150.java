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
package org.codehaus.annogen.view.internal;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationTypeElementDoc;
import com.sun.javadoc.AnnotationValue;
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.ProgramElementDoc;
import org.codehaus.annogen.override.AnnoBean;
import org.codehaus.annogen.override.AnnoBeanSet;
import org.codehaus.annogen.view.internal.javadoc.JavadocAnnogenTigerDelegate;
import org.codehaus.jam.provider.JamLogger;



/**
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public final class JavadocAnnogenTigerDelegateImpl_150
  extends JavadocAnnogenTigerDelegate
 {

  // ========================================================================
  // Variables

  private JamLogger mLogger;

  // ========================================================================
  // Javadoc15Delegate implementation

  public void init(JamLogger logger) { mLogger = logger; }

  public boolean extractAnnotations(AnnoBeanSet out,
                                    ProgramElementDoc src) {
    AnnotationDesc[] anns = src.annotations();
    if (anns == null || anns.length == 0) return false;
    for(int i=0; i<anns.length; i++) {
      Class annType = getClassFor(anns[i]);
      if (annType == null) continue;
      AnnoBean proxy = out.findOrCreateBeanFor(annType);
      if (proxy == null) continue;
      copyValues(anns[i],proxy);
    }
    return true;
  }

  public boolean extractAnnotations(AnnoBeanSet out,
                                    ExecutableMemberDoc method,
                                    int paramNum) {
    throw new IllegalStateException("parameter annos NYI");
  }

  // ========================================================================
  // Private methods

  private void copyValues(AnnotationDesc src, AnnoBean dest) {
    AnnotationDesc.ElementValuePair[] values = src.elementValues();
    if (values == null || values.length == 0) return;
    for(int i=0; i<values.length; i++) {
      AnnotationTypeElementDoc ated = values[i].element();
      String name = ated.name();
      Object value;
      {
        AnnotationValue avalue = values[i].value();
        if (avalue == null) continue; //REVIEW
        value = avalue.value();
      }
      if (value == null) continue; //REVIEW
      if (value instanceof AnnotationDesc) {
        Class nestedClass = getClassFor((AnnotationDesc)value);
        if (nestedClass == null) continue;
        AnnoBean nested = dest.createNestableBean(nestedClass);
        if (nested == null) continue;
        copyValues((AnnotationDesc)value,nested);
        dest.setValue(name,nested);
      } else if (value.getClass().isArray()) {
        throw new IllegalStateException("arrays NYI"); //FIXME
      } else {
        dest.setValue(name,value);
      }
    }
  }

  private Class getClassFor(AnnotationDesc javadocAnn) {
    try {
      return
        Class.forName(javadocAnn.annotationType().qualifiedTypeName());
    } catch(ClassNotFoundException cnfe) {
      mLogger.error(cnfe);
      return null;
    } catch(ClassCastException cce) {
      // This is part of the fix/workaround for a javadoc bug
      // as described in issue 14: http://jira.codehaus.org/browse/ANNOGEN-14
      mLogger.error(cce);
      return null;
    }
  }
}
