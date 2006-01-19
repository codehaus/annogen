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

import org.codehaus.annogen.generate.Annogen;
import org.codehaus.annogen.generate.internal.PropfileUtils;
import org.codehaus.annogen.override.AnnoBean;
import org.codehaus.annogen.override.AnnoContext;
import org.codehaus.annogen.override.AnnoOverrider;
import org.codehaus.annogen.override.internal.AnnoBeanBase;
import org.codehaus.annogen.view.AnnoViewerParams;
import org.codehaus.jam.internal.JamLoggerImpl;
import org.codehaus.jam.provider.JamLogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Implementation of both AnnoViewerParams and AnnoContext.  Those
 * two interfaces are really just write-only and read-only APIs, respectively,
 * to the same information - the params is the way the user sets up the
 * context in which the viewer will act.
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class AnnoViewerParamsImpl implements AnnoViewerParams, AnnoContext {

  // ========================================================================
  // Variables

  private LinkedList mPopulators = new LinkedList();
  private JamLogger mLogger = new JamLoggerImpl();
  private ClassLoader mClassLoader = ClassLoader.getSystemClassLoader();
  private Map mMappingCache = new HashMap();

  // ========================================================================
  // Constructors

  public AnnoViewerParamsImpl() {}

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

  public void setClassLoader(ClassLoader c) {
    mClassLoader = c;
  }

  public void setLogger(JamLogger logger) {
    mLogger = logger;
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

  public AnnoOverrider[] getOverriders() {
    AnnoOverrider[] out = new AnnoOverrider[mPopulators.size()];
    mPopulators.toArray(out);
    return out;
  }

  // ========================================================================
  // Provider context implementation

  public JamLogger getLogger() { return mLogger; }

  public Class getAnnobeanClassFor(Class clazz)
    throws ClassNotFoundException
  {
    Class out = (Class)mMappingCache.get(clazz);
    if (out == null) {
      mMappingCache.put(clazz, out = getAnnobeanClassForNocache(clazz));
    }
    return out;
  }

  public Class getJsr175ClassForAnnobeanClass(Class beanClass)
    throws ClassNotFoundException
  {
    if (!AnnoBean.class.isAssignableFrom(beanClass)) {
      throw new IllegalArgumentException(beanClass.getName()+
                                         " is not a AnnoBean");
    }
    Field f;
    try {
      f = beanClass.getField(Annogen.ANNOBEAN_FOR_FIELD);
    } catch(NoSuchFieldException nsfe) {
      throw new IllegalArgumentException
        (beanClass.getName()+" is an AnnoBean but does not have a "+
         Annogen.ANNOBEAN_FOR_FIELD+" field");
    }
    String declaredTypeName;
    try {
      declaredTypeName = (String)f.get(null);
      return beanClass.getClass().getClassLoader().loadClass(declaredTypeName);
    } catch (IllegalAccessException e) {
      try {
        throw new ClassNotFoundException().initCause(e);
      } catch(Throwable unlikely) {
        unlikely.printStackTrace();
        throw new IllegalStateException();
      }
    }
  }

  public ClassLoader getClassLoader() {
    return mClassLoader;
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

  // ========================================================================
  // Private methods

  private Class getAnnobeanClassForNocache(Class requestedClass)
    throws ClassNotFoundException
  {
    if (AnnoBean.class.isAssignableFrom(requestedClass)) {
      // if it already is one, we're good
      return requestedClass;
    } else {
      try {
        return PropfileUtils.getInstance().getAnnobeanTypeFor(requestedClass,
                                                              mClassLoader);
      } catch(IOException ioe) {
        throw new ClassNotFoundException("IO Error looking up bean class for "+
                                         requestedClass.getName(), ioe);
      }
    }
  }
}
