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

import org.codehaus.jam.mutable.MComment;
import org.codehaus.jam.visitor.JVisitor;
import org.codehaus.jam.visitor.MVisitor;

/**
 * <p>Implementation
 *
 * @author Patrick Calahan &lt;email: codehaus-at-bea-dot-com&gt;
 */
public final class CommentImpl extends ElementImpl implements MComment {

  // ========================================================================
  // Variables

  private String mText = null;

  // ========================================================================
  // Constructors

  /*package*/ CommentImpl(ElementImpl parent) { super(parent); }

  // ========================================================================
  // MComment implementation

  public void setText(String text) { mText = text; }

  public String getText() { return (mText == null) ? "" : mText; }

  // ========================================================================
  // JElement implementation

  public void accept(MVisitor visitor) { visitor.visit(this); }

  public void accept(JVisitor visitor) { visitor.visit(this); }

  public String getQualifiedName() {
    return getParent().getQualifiedName()+".{comment}"; //REVIEW
  }

}
