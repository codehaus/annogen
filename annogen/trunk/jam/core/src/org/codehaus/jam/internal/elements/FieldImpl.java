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
import org.codehaus.jam.mutable.MField;
import org.codehaus.jam.visitor.JVisitor;
import org.codehaus.jam.visitor.MVisitor;

import java.io.StringWriter;
import java.lang.reflect.Modifier;

/**
 * <p>Implementation of JField and MField.</p>
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public final class FieldImpl extends MemberImpl implements MField {

  // ========================================================================
  // Variables

  private JClassRef mTypeClassRef;

  // ========================================================================
  // Constructors

  /*package*/ FieldImpl(String simpleName,
                         ClassImpl containingClass,
                         String qualifiedTypeClassName) {
    super(containingClass);
    super.setSimpleName(simpleName);
    mTypeClassRef = QualifiedJClassRef.create
            (qualifiedTypeClassName,containingClass);
  }

  // ========================================================================
  // MField implementation

  public void setType(JClass type) {
    if (type == null) throw new IllegalArgumentException("null type");
    mTypeClassRef = DirectJClassRef.create(type);
  }

  public void setType(String qcname) {
    if (qcname == null) throw new IllegalArgumentException("null qcname");
    mTypeClassRef = QualifiedJClassRef.create
            (qcname,(ClassImpl)getContainingClass());
  }

  public void setUnqualifiedType(String ucname) {
    if (ucname == null) throw new IllegalArgumentException("null ucname");
    mTypeClassRef = UnqualifiedJClassRef.create
            (ucname,(ClassImpl)getContainingClass());
  }

  // ========================================================================
  // JField implementation

  public JClass getType() {
    if (mTypeClassRef == null) throw new IllegalStateException();
    return mTypeClassRef.getRefClass();
  }

  public boolean isFinal() {
    return Modifier.isFinal(getModifiers());
  }

  public boolean isStatic() {
    return Modifier.isStatic(getModifiers());
  }

  public boolean isVolatile() {
    return Modifier.isVolatile(getModifiers());
  }

  public boolean isTransient() {
    return Modifier.isTransient(getModifiers());
  }

  // ========================================================================
  // M/JElement implementation

  public void accept(MVisitor visitor) { visitor.visit(this); }

  public void accept(JVisitor visitor) { visitor.visit(this); }

  public String getQualifiedName() {
    StringWriter sbuf = new StringWriter();
    sbuf.write(Modifier.toString(getModifiers()));
    sbuf.write(' ');
    sbuf.write(getType().getQualifiedName());
    sbuf.write(' ');
    sbuf.write(getContainingClass().getQualifiedName());
    sbuf.write('.');
    sbuf.write(getSimpleName());
    return sbuf.toString();
  }



}