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
package org.codehaus.jam.internal.javadoc;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.ProgramElementDoc;
import org.codehaus.jam.internal.TigerDelegateHelper;
import org.codehaus.jam.internal.elements.ElementContext;
import org.codehaus.jam.mutable.MAnnotatedElement;
import org.codehaus.jam.mutable.MClass;

/**
 * Provides an interface to 1.5-specific functionality.  The impl of
 * this class is loaded by-name at runtime.
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public abstract class JavadocTigerDelegate {

  // ========================================================================
  // Constants

  private static final String JAVADOC_DELEGATE_IMPL =
    "org.codehaus.jam.internal.javadoc.JavadocTigerDelegateImpl_150";

  // This is part of a hack for EJBgen, which for some reason it behaves really
  // badly when we supply it default values.  This should be removed
  // when EJBgen can be fixed.

  public static final String ANNOTATION_DEFAULTS_DISABLED_PROPERTY =
    "ANNOTATION_DEFAULTS_DISABLED_PROPERTY";

  // ========================================================================
  // Factory

  /**
   */
  public static JavadocTigerDelegate create(ElementContext ctx)
  {
    if (!TigerDelegateHelper.isTigerJavadocAvailable(ctx.getLogger())) return null;
    // ok, if we could load that, let's new up the extractor delegate
    try {
      JavadocTigerDelegate out = (JavadocTigerDelegate)
        Class.forName(JAVADOC_DELEGATE_IMPL).newInstance();
      out.init(ctx);
      return out;
    } catch (ClassNotFoundException e) {
      ctx.getLogger().error(e);
    } catch (IllegalAccessException e) {
      ctx.getLogger().error(e);
    } catch (InstantiationException e) {
      ctx.getLogger().error(e);
    }
    return null;
  }

  // ========================================================================
  // Public methods

  /**
   * Returns true if the given ClassDoc represents an enum.
   */ 
  public abstract boolean isEnum(ClassDoc cd);

  public abstract void init(ElementContext ctx);

  public abstract void populateAnnotationTypeIfNecessary(ClassDoc cd,
                                                         MClass clazz,
                                                         JavadocClassBuilder builder);


  // ========================================================================
  // Deprecated stuff, trying to move away from this

  /**
   * @deprecated
   */
  public abstract void extractAnnotations(MAnnotatedElement dest,
                                          ProgramElementDoc src);

  /**
   * @deprecated
   */
  public abstract void extractAnnotations(MAnnotatedElement dest,
                                          ExecutableMemberDoc method,
                                          Parameter src);

}
