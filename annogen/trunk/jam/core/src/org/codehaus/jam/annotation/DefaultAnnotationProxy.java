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
package org.codehaus.jam.annotation;

import org.codehaus.jam.JAnnotationValue;
import org.codehaus.jam.JClass;
import org.codehaus.jam.internal.elements.AnnotationValueImpl;
import org.codehaus.jam.internal.elements.ElementContext;
import org.codehaus.jam.provider.JamLogger;
import org.codehaus.jam.provider.JamServiceContext;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Implementation of AnnotationProxy which is used when no user-defined
 * type has been registered for a given annotation..  All it does is stuff
 * values into a ValueMap.  Note that it inherits all of the default tag and
 * annotation processing behaviors from AnnotationProxy.</p>
 *
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
/**
 * @deprecated do not use, moving into internal
 */
public class DefaultAnnotationProxy  {

  // ========================================================================
  // Constants

  /**
   * <p>Name of the member of annotations which have only a single member.
   * As specified in JSR175, that name is "value", but you should use
   * this constant to prevent typos.</p>
   */
  public static final String SINGLE_MEMBER_NAME = "value";


  // ========================================================================
  // Variables

  protected JamServiceContext mContext;

  // ========================================================================
  // Initialization methods - called by JAM, don't implement

  /**
   * <p>Called by JAM to initialize the proxy.  Do not try to call this
   * yourself.</p>
   */
  public void init(JamServiceContext ctx) {
    if (ctx == null) throw new IllegalArgumentException("null logger");
    mContext = ctx;
  }


  //docme
  public JAnnotationValue getValue(String named) {
    if (named == null) throw new IllegalArgumentException("null name");
    //FIXME this impl is very gross
    named = named.trim();
    JAnnotationValue[] values = getValues();
    for(int i=0; i<values.length; i++) {

      if (named.equals(values[i].getName())) return values[i];
    }
    return null;
  }

  // ========================================================================
  // Protected methods

  // ========================================================================
  // Variables

  private List mValues = new ArrayList();

  // ========================================================================
  // Constructors

  public DefaultAnnotationProxy() {}

  // ========================================================================
  // Public methods

  public JAnnotationValue[] getValues() {
    JAnnotationValue[] out = new JAnnotationValue[mValues.size()];
    mValues.toArray(out);
    return out;
  }

  // ========================================================================
  // TypedAnnotationProxyBase implementation

  /**
   * <p>Overrides this behavior to simply stuff the value into our
   * annotation map.  The super class' implementation would try to
   * find a bunch of setters that we don't have.</p>
   */
  public void setValue(String name, Object value, JClass type) {
    if (name == null) throw new IllegalArgumentException("null name");
    if (type == null) throw new IllegalArgumentException("null type");
    if (value == null) throw new IllegalArgumentException("null value");
    name = name.trim();
    mValues.add(new AnnotationValueImpl((ElementContext)mContext,//yikes, nasty.  FIXME
                                        name,value,type));
  }

  // ========================================================================
  // Private methods

  /**
   * <p>Returns an instance of JamLogger that this AnnotationProxy should use
   * for logging debug and error messages.</p>
   */
  private JamLogger getLogger() { return mContext.getLogger(); }


}

