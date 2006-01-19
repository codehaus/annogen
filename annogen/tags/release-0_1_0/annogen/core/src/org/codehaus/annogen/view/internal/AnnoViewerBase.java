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

import org.codehaus.annogen.override.AnnoBean;
import org.codehaus.annogen.override.AnnoBeanSet;
import org.codehaus.annogen.override.AnnoContext;
import org.codehaus.annogen.override.AnnoOverrider;
import org.codehaus.annogen.override.ElementId;
import org.codehaus.annogen.override.internal.AnnoBeanSetImpl;
import org.codehaus.annogen.override.internal.CompositeAnnoOverrider;
import org.codehaus.annogen.override.internal.ElementIdImpl;
import org.codehaus.jam.provider.JamLogger;

/**
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public abstract class AnnoViewerBase {

  // ========================================================================
  // Variables

  private AnnoContext mContext;
  private AnnoOverrider mOverrider;
  protected JamLogger mLogger;

  // ========================================================================
  // Constructors

  public AnnoViewerBase(AnnoViewerParamsImpl asp) {
    if (asp == null) throw new IllegalArgumentException("null asp");
    AnnoOverrider[] pps = asp.getOverriders();
    if (pps == null || pps.length == 0) {
      mOverrider = null;
    } else if (pps.length == 1) {
      mOverrider = pps[0];
    } else {
      mOverrider = new CompositeAnnoOverrider(pps);
    }
    if (mOverrider != null) mOverrider.init(asp);
    mLogger = asp.getLogger();
    mContext = (AnnoContext)asp;
  }

  // ========================================================================
  // Public methods

  //FIXME add some caching here, please

  /**
   * This method is public only because it makes writing tests a little easier.
   */
   public AnnoBean[] getAnnotations(ElementId id) {
    if (id == null) throw new IllegalArgumentException("null id");
    AnnoBeanSet apsi = new AnnoBeanSetImpl(mContext);
    getIndigenousAnnotations(id,apsi);
    if (mOverrider != null) mOverrider.modifyAnnos(id,apsi);
    return apsi.getAll();
  }

  /**
   * This method really should be proected, but we leave it public because
   * it makes writing tests a lot easier.
   */
  /*protected*/ public AnnoBean getAnnotation(Class what, ElementId where) {
    Class beanClass;
    try {
      beanClass = mContext.getAnnobeanClassFor(what);
    } catch(ClassNotFoundException cnfe) {
      mLogger.verbose(cnfe,this);
      return null;
    }
    AnnoBean[] annos = getAnnotations(where);
    for(int i=0; i<annos.length; i++) {
      if (beanClass.isAssignableFrom(annos[i].getClass())) {
        return annos[i];
      }
    }
    return null;
  }

  // ========================================================================
  // Private methods

  private void getIndigenousAnnotations(ElementId id, AnnoBeanSet out) {
    if (id == null) throw new IllegalArgumentException("null id");
    if (out == null) throw new IllegalArgumentException("null out");
    IndigenousAnnoExtractor iae = ((ElementIdImpl)id).getIAE();
    if (iae == null) throw new IllegalStateException();
    iae.extractIndigenousAnnotations(out);
  }
}