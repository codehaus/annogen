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
package org.codehaus.annogen.override.internal.javadoc;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ConstructorDoc;
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.ProgramElementDoc;
import org.codehaus.annogen.override.ElementId;
import org.codehaus.annogen.override.JavadocElementIdPool;
import org.codehaus.annogen.override.internal.ElementIdImpl;
import org.codehaus.annogen.view.internal.IndigenousAnnoExtractor;
import org.codehaus.annogen.view.internal.javadoc.JavadocAnnogenTigerDelegate;
import org.codehaus.annogen.view.internal.javadoc.ParameterJavadocIAE;
import org.codehaus.annogen.view.internal.javadoc.ProgramElementJavadocIAE;
import org.codehaus.jam.provider.JamLogger;

/**
 * Note that keeping the ElementIdFactories separate ensures that users
 * won't be forced at runtime to drag in classes they don't care about.
 * i.e., we don't want them to get NoClassDefFound when tools.jar isn't
 * in their classpath but they don't care about javadoc.
 * 
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class JavadocElementIdPoolImpl implements JavadocElementIdPool {


  // ========================================================================
  // Variables

  private JavadocAnnogenTigerDelegate mJTiger;

  // ========================================================================
  // Constructors

  public JavadocElementIdPoolImpl(JamLogger logger) {
    mJTiger = JavadocAnnogenTigerDelegate.create(logger);
  }

  // ========================================================================
  // ElementIdFactory implementation

  public ElementId getIdFor(ProgramElementDoc ped) {
    if (ped == null) throw new IllegalArgumentException("null ped");
    IndigenousAnnoExtractor iae = ProgramElementJavadocIAE.create(ped,mJTiger);
    if (ped instanceof PackageDoc) {
      return ElementIdImpl.forPackage(iae,ped.name());
    } else if (ped instanceof ClassDoc) {
      return ElementIdImpl.forClass(iae, ped.name());
    } else if (ped instanceof FieldDoc) {
      return ElementIdImpl.forField(iae,
                                    ped.containingClass().name(),
                                    ped.name());
    } else if (ped instanceof ConstructorDoc) {
      return ElementIdImpl.forConstructor(iae,
                                          ped.containingClass().name(),
                                          getSignature((ConstructorDoc)ped));
    } else if (ped instanceof MethodDoc) {
      return ElementIdImpl.forMethod(iae,
                                     ped.containingClass().name(),
                                     ped.name(),
                                     getSignature((MethodDoc)ped));
    } else {
      throw new IllegalStateException(ped.getClass().getName());
    }
  }

  public ElementId getIdFor(ExecutableMemberDoc emd, int paramNum) {
    if (emd == null) throw new IllegalArgumentException("null emd");
    IndigenousAnnoExtractor iae = ParameterJavadocIAE.create(emd,paramNum,mJTiger);
    return ElementIdImpl.forParameter(iae,
                                      emd.containingClass().name(),
                                      emd.name(),
                                      getSignature(emd),
                                      paramNum);
  }

  // ========================================================================
  // Private methods

  private String[] getSignature(ExecutableMemberDoc emd) {
    if (emd == null) throw new IllegalArgumentException("null emd");
    Parameter[] params = emd.parameters();
    if (params == null || params.length == 0) return new String[0];
    String[] out = new String[params.length];
    for(int i=0; i<out.length; i++) out[i] = params[i].name();
    return out;
  }

}
