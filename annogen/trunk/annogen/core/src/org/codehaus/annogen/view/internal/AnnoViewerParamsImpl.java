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
package org.codehaus.annogen.view.internal;

import org.codehaus.annogen.override.AnnoContext;
import org.codehaus.annogen.override.AnnoOverrider;
import org.codehaus.annogen.override.AnnoBean;
import org.codehaus.annogen.override.AnnoBeanMapping;
import org.codehaus.annogen.override.internal.AnnoBeanBase;
import org.codehaus.annogen.override.internal.DefaultAnnoBeanMapping;
import org.codehaus.annogen.view.AnnoViewerParams;
import org.codehaus.jam.internal.JamLoggerImpl;
import org.codehaus.jam.provider.JamLogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.LinkedList;

//FIXME factor out the stuf that is now redundant with AnnoContextImpl

/**
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class AnnoViewerParamsImpl implements AnnoViewerParams, AnnoContext {

  // ========================================================================
  // Variables

  private LinkedList mPopulators = new LinkedList();
  private JamLogger mLogger = new JamLoggerImpl();
  private AnnoBeanMapping mAnnoTypeRegistry;

  // ========================================================================
  // Constructors

  public AnnoViewerParamsImpl() {
    mAnnoTypeRegistry = new DefaultAnnoBeanMapping(mLogger);
  }

  // ========================================================================
  // Public methods

  public void addXmlOverrides(File file) throws FileNotFoundException {
    addXmlOverrides(new FileReader(file));
  }

  public void addXmlOverrides(Reader in) {
    throw new IllegalArgumentException("NYI");
    // addNewPopulator(new XmlPopulator(in));
  }

  public void addOverrider(AnnoOverrider over) {
    if (over == null) throw new IllegalArgumentException("null overrider");
    mPopulators.addFirst(over);
  }

  public void setVerbose(Class c) {
    mLogger.setVerbose(c);
  }

  public void appendAnnoOverrider(AnnoOverrider over) {
    if (over == null) throw new IllegalArgumentException("null overrider");
    mPopulators.addLast(over);
  }

  // ========================================================================
  // Internal use only

  public AnnoOverrider[] getPopulators() {
    AnnoOverrider[] out = new AnnoOverrider[mPopulators.size()];
    mPopulators.toArray(out);
    return out;
  }

  // ========================================================================
  // Provider context implementation

  public JamLogger getLogger() { return mLogger; }

  public AnnoBeanMapping getAnnoBeanMapping() {
    return mAnnoTypeRegistry;
  }

  public ClassLoader getClassLoader() {
    return ClassLoader.getSystemClassLoader(); //FIXME
  }

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
