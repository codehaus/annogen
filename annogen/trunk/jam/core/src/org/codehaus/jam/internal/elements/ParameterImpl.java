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

package org.codehaus.jam.internal.elements;

import org.codehaus.jam.JClass;
import org.codehaus.jam.internal.classrefs.DirectJClassRef;
import org.codehaus.jam.internal.classrefs.JClassRef;
import org.codehaus.jam.internal.classrefs.QualifiedJClassRef;
import org.codehaus.jam.internal.classrefs.UnqualifiedJClassRef;
import org.codehaus.jam.mutable.MParameter;
import org.codehaus.jam.visitor.JVisitor;
import org.codehaus.jam.visitor.MVisitor;

/**
 * <p>Implementation of JParameter and MParameter.</p>
 *
 * @author Patrick Calahan &lt;email: codehaus-at-bea-dot-com&gt;
 */
public class ParameterImpl extends MemberImpl implements MParameter {

  // ========================================================================
  // Variables

  private JClassRef mTypeClassRef;

  // ========================================================================
  // Constructors

  /*package*/ ParameterImpl(String simpleName,
                            InvokableImpl containingMember,
                            String typeName)
  {
    super(containingMember);
    setSimpleName(simpleName);
    setType(typeName);
  }

  // ========================================================================
  // JElement implementation

  public String getQualifiedName() {
    return getSimpleName();//FIXME
  }

  // ========================================================================
  // MParameter implementation

  public void setType(String qcname) {
    if (qcname == null) throw new IllegalArgumentException("null typename");
    mTypeClassRef = QualifiedJClassRef.create
            (qcname,(ClassImpl)getContainingClass());
  }

  public void setType(JClass qcname) {
    if (qcname == null) throw new IllegalArgumentException("null qcname");
    mTypeClassRef = DirectJClassRef.create(qcname);
  }

  public void setUnqualifiedType(String ucname) {
    if (ucname == null) throw new IllegalArgumentException("null ucname");
    mTypeClassRef = UnqualifiedJClassRef.create
            (ucname,(ClassImpl)getContainingClass());
  }

  // ========================================================================
  // JParameter implementation

  public JClass getType() {
    return mTypeClassRef.getRefClass();
  }

  // ========================================================================
  // JElement implementation

  public void accept(MVisitor visitor) { visitor.visit(this); }

  public void accept(JVisitor visitor) { visitor.visit(this); }

}
