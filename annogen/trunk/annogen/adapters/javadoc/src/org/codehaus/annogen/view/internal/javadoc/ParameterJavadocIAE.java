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
import org.codehaus.annogen.override.AnnoBeanSet;
import org.codehaus.annogen.view.internal.IndigenousAnnoExtractor;
import org.codehaus.annogen.view.internal.NullIAE;

/**
 * @author Patrick Calahan &lt;email: codehaus-at-bea-dot-com&gt;
 */
public final class ParameterJavadocIAE implements IndigenousAnnoExtractor {

  // ========================================================================
  // Factory methods

  public static IndigenousAnnoExtractor create(ExecutableMemberDoc emd,
                                               int paramNum,
                                               JavadocAnnogenTigerDelegate tiger) {
    if (tiger == null) return NullIAE.getInstance();
    return new ParameterJavadocIAE(emd,paramNum,tiger);
  }

  // ========================================================================
  // Variables

  private ExecutableMemberDoc mEMD;
  private int mParamNum;
  private JavadocAnnogenTigerDelegate mTigerDelegate;

  // ========================================================================
  // Constructors

  private ParameterJavadocIAE(ExecutableMemberDoc emd,
                              int paramNum,
                              JavadocAnnogenTigerDelegate tiger)
  {
    if (emd == null) throw new IllegalArgumentException("null ped");
    if (tiger == null) throw new IllegalArgumentException("null tiger");
    if (paramNum < 0) throw new IllegalArgumentException("invalid paramNum "+
                                                         paramNum);
    mTigerDelegate = tiger;
    mEMD = emd;
    mParamNum = paramNum;
  }

  // ========================================================================
  // IAE implementation

  public boolean extractIndigenousAnnotations(AnnoBeanSet out) {
    return mTigerDelegate.extractAnnotations(out,mEMD,mParamNum);
  }
}
