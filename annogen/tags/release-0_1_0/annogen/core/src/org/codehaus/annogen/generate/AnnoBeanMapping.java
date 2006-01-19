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
package org.codehaus.annogen.generate;



/**
 * Encapsulates the delcaration of a one-to-one mapping of some set of
 * 175 annotation types to their corresponding annotation beans.  The mapping
 * is declared as a pair of strings, e.g.
 *
 * typePattern = com.foo.MyAnnotation
 * beanPattern = com.foo.annobeans.MyAnnotationBean
 *
 * This implementation also supports a simple wildcard substituion using '*',
 * e.g.
 *
 *
 * typePattern = com.foo.*Annotation
 * beanPattern = com.foo.annobeans.*AnnotationBean
 *
 * The rule for matching is that a type matches if the type name startsWith
 * the part of the pattern before the *, and endsWith everything after the
 * *.  Everything inbetween is substituted for the '*' in the beanPattern.
 * It is an error for only one fo the type and beanPatterns to contain
 * a *, or for either to contain more than one *.
 *
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class AnnoBeanMapping {


  // ========================================================================
  // Constants

  private static final char WILD = '*';
  private static final boolean VERBOSE = false;

  // ========================================================================
  // Variables

  private String mTypePattern;
  private String mBeanPattern;
  private String mTypeBeforeStar = null;
  private String mTypeAfterStar = null;
  private String mBeanBeforeStar = null;
  private String mBeanAfterStar = null;

  // ========================================================================
  // Constructor

  public AnnoBeanMapping() {}

  public AnnoBeanMapping(String typePattern, String beanPattern) {
    setType(typePattern);
    setBean(beanPattern);
  }

  // ========================================================================
  // Public methods

  /**
   * If the annnotation type represented by the named class matches this
   * mapping's typePattern, returns the name of the annobean which it should
   * be mapped to.  If the type doesn't match, null is returned.
   */
  public String getAnnoBeanFor(String classname) {
    if (classname == null) throw new IllegalArgumentException("null classname");
    classname = classname.trim();
    if (VERBOSE) {
      verbose("=============");
      verbose("checking "+classname);
      verbose("against  "+mTypePattern);
    }
    if (mBeanBeforeStar == null && mTypeBeforeStar == null) {
      if (mTypePattern.equals(classname)) {
        if (VERBOSE) verbose("No star, match!");
        return mBeanPattern;
      } else {
        if (VERBOSE) verbose("No star, no match");
        return null;
      }
    }
    if (mBeanBeforeStar == null) {
      throw new IllegalStateException(WILD+" must be specified in both bean and type");
    }
    if (mTypeBeforeStar == null) {
      throw new IllegalStateException(WILD+" must be specified in both bean and type");
    }
    if (VERBOSE) verbose("typeBefore="+mTypeBeforeStar);
    if (VERBOSE) verbose("typeAfter="+mTypeAfterStar);
    if (classname.startsWith(mTypeBeforeStar) &&
        (mTypeAfterStar == null || classname.endsWith(mTypeAfterStar))) {
      if (VERBOSE) verbose("...matched the front "+classname.startsWith(mTypeBeforeStar));
      String sub = classname.substring(mTypeBeforeStar.length(),
                                       classname.length() -
                                       (mTypeAfterStar == null ? 0 : mTypeAfterStar.length()));
      String out = mBeanBeforeStar + sub +
                   (mBeanAfterStar == null ? "" : mBeanAfterStar);
      if (VERBOSE) verbose("result is "+out);
      return out;
    }
    return null;
  }

  // ========================================================================
  // Properties

  public String getType() { return mTypePattern; }

  public void setType(String type) {
    if (type == null) throw new IllegalArgumentException("null type");
    if (type.length() == 0) throw new IllegalArgumentException("empty type");
    mTypePattern = type.trim();
    int wild;
    if ((wild = type.indexOf(WILD)) != -1) {
      mTypeBeforeStar = mTypePattern.substring(0,wild);
      if (wild == mTypePattern.length() -1) { //if it's the last char
        mTypeAfterStar = null;
      } else {
        mTypeAfterStar = mTypePattern.substring(wild+1);
      }
    }
  }

  public String getBean() { return mBeanPattern; }

  public void setBean(String bean) {
    if (bean == null) throw new IllegalArgumentException("null bean");
    if (bean.length() == 0) throw new IllegalArgumentException("empty bean");
    mBeanPattern = bean;
    int wild;
    if ((wild = bean.indexOf(WILD)) != -1) {
      mBeanBeforeStar= mBeanPattern.substring(0,wild);
      if (wild == mBeanPattern.length()) {
        mBeanBeforeStar= null;
      } else {
        mBeanAfterStar = mBeanPattern.substring(wild+1);
      }
    }
  }


  // ========================================================================
  // Private methods

  private static final void verbose(String msg) {
    System.out.println("[AnnoBeanMapping] "+ msg);
  }

}