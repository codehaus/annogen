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

import org.codehaus.annogen.override.AnnoContext;
import org.codehaus.annogen.override.AnnoBean;
import org.codehaus.annogen.override.AnnoBeanMapping;
import org.codehaus.jam.internal.JamLoggerImpl;
import org.codehaus.jam.provider.JamLogger;

/**
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class AnnoContextImpl implements AnnoContext {

  // ========================================================================
  // Variables

  private JamLogger mLogger;
  private AnnoBeanMapping mPTM;
  private ClassLoader mClassLoader;

  // ========================================================================
  // Constructors

  public AnnoContextImpl() {
    mLogger = new JamLoggerImpl();
    mPTM = new DefaultAnnoBeanMapping(mLogger);
    mClassLoader = ClassLoader.getSystemClassLoader();
  }

  // ========================================================================
  // AnnoContext implementation


  public JamLogger getLogger() { return mLogger; }

  public AnnoBeanMapping getAnnoBeanMapping() { return mPTM; }

  public ClassLoader getClassLoader() { return mClassLoader; }

  public AnnoBean createAnnoBeanFor(Class beanClass) {
    try {
      AnnoBeanBase out = (AnnoBeanBase)beanClass.newInstance();
      out.init(this);
      return out;
    } catch (InstantiationException e) {
      mLogger.error(e);
    } catch (IllegalAccessException e) {
      mLogger.error(e);
    }
    return null;
  }
}
