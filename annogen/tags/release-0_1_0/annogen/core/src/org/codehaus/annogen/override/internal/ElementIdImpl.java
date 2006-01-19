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
package org.codehaus.annogen.override.internal;

import org.codehaus.annogen.override.ElementId;
import org.codehaus.annogen.view.internal.IndigenousAnnoExtractor;

import java.io.StringWriter;

/**
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class ElementIdImpl implements ElementId {

  // ========================================================================
  // Variables

  private String mToString = null;
  private IndigenousAnnoExtractor mIAE;
  private String mName;
  private String mContainingClass = null;
  private int mType = -1;
  private int mParamNum = NO_PARAMETER;
  private String[] mSignature = null;

  // ========================================================================
  // Factory methods

  /**
   * Construct an ElementId for a method or constructor
   */
  public static ElementIdImpl forPackage(IndigenousAnnoExtractor iae,
                                         String packageName)
  {
    ElementIdImpl id = new ElementIdImpl();
    id.setType(PACKAGE_TYPE);
    id.setIAE(iae);
    id.setName(packageName);
    return id;
  }

  /**
   * Construct an ElementId for a method or constructor
   */
  public static ElementIdImpl forClass(IndigenousAnnoExtractor iae,
                                       String classname)
  {
    ElementIdImpl id = new ElementIdImpl();
    id.setType(CLASS_TYPE);
    id.setIAE(iae);
    id.setName(classname);
    return id;
  }

  /**
   * Construct an ElementId for a method or constructor
   */
  public static ElementIdImpl forField(IndigenousAnnoExtractor iae,
                                       String contClass,
                                       String name)
  {
    ElementIdImpl id = new ElementIdImpl();
    id.setType(FIELD_TYPE);
    id.setIAE(iae);
    id.setName(name);
    id.setContainingClass(contClass);
    return id;
  }

  /**
   * Construct an ElementId for a method or constructor
   */
  public static ElementIdImpl forConstructor(IndigenousAnnoExtractor iae,
                                             String containingClass,
                                             String[] signature)
  {
    ElementIdImpl id = new ElementIdImpl();
    id.setType(CONSTRUCTOR_TYPE);
    id.setIAE(iae);
    id.setName(containingClass.substring(containingClass.lastIndexOf('.')+1));
    id.setContainingClass(containingClass);
    id.setSignature(signature);
    return id;
  }

  /**
   * Construct an ElementId for a method or constructor
   */
  public static ElementIdImpl forMethod(IndigenousAnnoExtractor iae,
                                        String containingClass,
                                        String name,
                                        String[] signature)
  {
    ElementIdImpl id = new ElementIdImpl();
    id.setType(METHOD_TYPE);
    id.setIAE(iae);
    id.setName(name);
    id.setContainingClass(containingClass);
    id.setSignature(signature);
    return id;
  }

  /**
   * Construct an ElementId for a parameter
   */
  public static ElementIdImpl forParameter(IndigenousAnnoExtractor iae,
                                           String containingClass,
                                           String methodName,
                                           String[] signature,
                                           int paramNum) {
    ElementIdImpl id = new ElementIdImpl();
    id.setType(PARAMETER_TYPE);
    id.setIAE(iae);
    id.setName(methodName);
    id.setContainingClass(containingClass);
    id.setSignature(signature);
    id.setParamNum(paramNum);
    return id;
  }

  // ========================================================================
  // Constructor

  protected ElementIdImpl() {}

  // ========================================================================
  // InternalElementId implementation

  /**
   * We effectively slip the original artifiact (Class, ClassDoc,
   * JClass, whatever) into the ElementId - this is how we get it back
   * out when we need to get the original annotations.
   */ 
  public IndigenousAnnoExtractor getIAE() { return mIAE; }

  // ========================================================================
  // ElementId implementation

  public int getType() { return mType; }

  public String getName() { return mName; }

  public String getContainingClass() { return mContainingClass; }

  public String[] getSignature() { return mSignature; }

  public int getParameterNumber() { return mParamNum; }

  // ========================================================================
  // Object implementation

  public int hashCode() {
    return toString().hashCode();
  }

  public boolean equals(Object o) {
    return o instanceof ElementId && toString().equals(o.toString());
  }

  public String toString() {
    if (mToString == null) mToString = createToString();
    return mToString;
  }

  // ========================================================================
  // Initialization methods

  protected void setIAE(IndigenousAnnoExtractor iae) {
    if (iae == null) throw new IllegalArgumentException("null iae");
    mIAE = iae;
  }

  protected void setName(String name) {
    if (name == null) throw new IllegalArgumentException("null name");
    mName = name;
  }

  protected void setContainingClass(String cc) {
    if (cc == null) throw new IllegalArgumentException("null cc");
    mContainingClass = cc;
  }

  protected void setSignature(String[] sig) {
    mSignature = (sig == null) ? new String[0] : sig;
  }

  protected void setType(int type) {
    mType = type;
  }

  protected void setParamNum(int pnum) {
    if (pnum < 0) throw new IllegalArgumentException("invalid pnum "+pnum);
    mParamNum = pnum;
  }

  // ========================================================================
  // Private methods
  //

  private String createToString() {
    switch(getType()) {
      case CLASS_TYPE:
      case PACKAGE_TYPE:
        return getName();
      case FIELD_TYPE:
      case METHOD_TYPE:
      case CONSTRUCTOR_TYPE:
      case PARAMETER_TYPE:
        {
          StringWriter out = new StringWriter();
          out.write(getContainingClass());
          out.write('.');
          out.write(getName());
          if (getType() == FIELD_TYPE) return out.toString();
          out.write('(');
          String[] sig = getSignature();
          if (sig != null && sig.length > 0) {
            int i=0;
            while(true) {
              out.write(sig[i++]);
              if (i == sig.length) break;
              out.write(',');
            }
          }
          out.write(')');
          if (getType() != PARAMETER_TYPE) return out.toString();
          out.write('[');
          out.write(getParameterNumber());
          out.write(']');
          return out.toString();
        }
        default:
        throw new IllegalStateException();
    }
  }

}
