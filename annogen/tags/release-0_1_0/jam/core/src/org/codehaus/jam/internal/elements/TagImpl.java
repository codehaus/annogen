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

import org.codehaus.jam.mutable.MTag;
import org.codehaus.jam.visitor.JVisitor;
import org.codehaus.jam.visitor.MVisitor;

import java.util.Properties;

/**
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class TagImpl extends ElementImpl implements MTag  {

  // ========================================================================
  // Variables

  private String mText;

  // ========================================================================
  // Constructor

  public TagImpl(ElementImpl parent) {
    super(parent);
  }

  // ========================================================================
  // JTag implementation

  public String getName() { return super.getSimpleName(); }

  public String getText() { return mText; }

  public Properties getProperties_lineDelimited() {
    Properties out = new Properties();
    LinebreakTagPropertyParser.getInstance().parse(out,mText);
    return out;
  }

  public Properties getProperties_whitespaceDelimited() {
    Properties out = new Properties();
    WhitespaceTagPropertyParser.getInstance().parse(out,mText,getLogger());
    return out;
  }

  // ========================================================================
  // MTag implementation

  public void setName(String name) { super.setSimpleName(name); }

  public void setValue(String val) { mText = val; }

  // ========================================================================
  // MElement implementation

  public void accept(MVisitor visitor) { visitor.visit(this); }

  // ========================================================================
  // JElement implementation

  public String getQualifiedName() { return getSimpleName(); }

  public void accept(JVisitor visitor) { visitor.visit(this); }

}


