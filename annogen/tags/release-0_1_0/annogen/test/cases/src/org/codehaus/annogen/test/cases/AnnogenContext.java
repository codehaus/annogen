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
package org.codehaus.annogen.test.cases;

import org.codehaus.annogen.view.internal.AnnoViewerBase;
import org.codehaus.annogen.view.AnnoViewerParams;

/**
 * <p>Provides an abstraction layer around a particular Annogen use
 * case, e.g. Reflection, Javadoc, JAM.  The tests create several
 * instances of these things and then run the test cases using each one.
 * This allows us to easily re-use test case logic against each of the
 * annogen adapators.</p>
 *
 * <p>
 * Note that this is definitely not 'black box' testing, and there
 * is some danger that the tests which know so much about annogen's internals
 * will end up 'cheating' and not being good tests.  The alternative,
 * though, is to re-write the same test case logic for every adapter,
 * which is currently deemed to be too much work, not to mention an impediment
 * to the development of new adapters.  It's a fine line to walk.
 * </p>
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public interface AnnogenContext {

  /**
   * Creeates an AnnoViewer using the given params.
   */
  public AnnoViewerBase createViewer(AnnoViewerParams params);

  /**
   * Creates an appropriate IndigenousAnnotationExtractor and puts
   * it in the element is.
   */
  public void stuffExtractor(TestElementId id) throws Exception;

}
