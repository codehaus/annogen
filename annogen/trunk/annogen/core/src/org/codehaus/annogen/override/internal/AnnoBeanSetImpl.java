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

import org.codehaus.annogen.override.AnnoBean;
import org.codehaus.annogen.override.AnnoBeanSet;
import org.codehaus.annogen.override.AnnoContext;
import org.codehaus.jam.provider.JamLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of AnnoBeanSet.
 *
 * Encapsualtes a set of AnnoBeans which apply to some element in the java
 * type system, such as a class, method, or field.
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class AnnoBeanSetImpl implements AnnoBeanSet {

  // ========================================================================
  // Variables

  private Map mBeanClass2AnnoClass = new HashMap();
  private JamLogger mLogger = null;
  private AnnoContext mContext = null;

  // ========================================================================
  // Constructor

  public AnnoBeanSetImpl(AnnoContext ctx) {
    if (ctx == null) throw new IllegalArgumentException();
    mLogger = ctx.getLogger();
    mContext = ctx;
  }

  // ========================================================================
  // Public methods

  public boolean containsBeanFor(Class requestedClass) {
    Class beanClass;
    try {
      beanClass = mContext.getAnnobeanClassFor(requestedClass);
    } catch(ClassNotFoundException cnfe) {
      mLogger.verbose(cnfe,this);
      return false;
    }
    return mBeanClass2AnnoClass.containsKey(beanClass);
  }

  public AnnoBean findOrCreateBeanFor(Class requestedClass) {
    Class beanClass;
    try {
      beanClass = mContext.getAnnobeanClassFor(requestedClass);
    } catch(ClassNotFoundException cnfe) {
      mLogger.verbose(cnfe,this);
      return null;
    }
    AnnoBean ap = (AnnoBean)mBeanClass2AnnoClass.get(beanClass);
    if (ap != null) return ap;
    ap = mContext.createAnnoBeanFor(beanClass);
    if (ap != null) {
      mBeanClass2AnnoClass.put(beanClass,ap);
      return ap;
    }
    return null;
  }

  public void put(AnnoBean bean) {
    if (bean == null) throw new IllegalArgumentException();
    mBeanClass2AnnoClass.put(bean.getClass(),bean);
  }

  public AnnoBean removeBeanFor(Class requestedClass) {
    Class beanClass;
    try {
      beanClass = mContext.getAnnobeanClassFor(requestedClass);
    } catch(ClassNotFoundException cnfe) {
      mLogger.verbose(cnfe, this);
      return null;
    }
    return (AnnoBean)mBeanClass2AnnoClass.remove(beanClass);
  }

  public AnnoBean[] getAll() {
    AnnoBean[] out = new AnnoBean[mBeanClass2AnnoClass.values().size()];
    mBeanClass2AnnoClass.values().toArray(out);
    return out;
  }

  public int size() {
    return mBeanClass2AnnoClass.size();
  }

}
