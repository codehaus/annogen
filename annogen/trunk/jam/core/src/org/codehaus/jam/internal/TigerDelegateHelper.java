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
package org.codehaus.jam.internal;

import org.codehaus.jam.provider.JamLogger;

/**
 * <p>
 * Base for classes which expose 1.5 (aka 'tiger')-specific functionality.
 * </p>
 *
 * This class should be moved into a common directory between annogen
 * and jam.
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public final class TigerDelegateHelper {

  // ========================================================================
  // Constants

  private static final String SOME_TIGER_SPECIFIC_JAVADOC_CLASS =
    "com.sun.javadoc.AnnotationDesc";

  private static final String SOME_TIGER_SPECIFIC_REFLECT_CLASS =
    "java.lang.annotation.Annotation";

  // ========================================================================
  // Variables

  private static boolean tigerReflectionAvailable   = false;
  private static boolean tigerJavadocAvailable = false;

  private static boolean tigerReflectionCheckDone   = false;
  private static boolean tigerJavadocCheckDone = false;
    
  private static boolean m14RuntimeWarningDone   = false;
  private static boolean m14BuildWarningDone = false;

  // ========================================================================
  // Public methods


  /**
   * Displays a warning indicating that the current build of JAM was
   * done under 1.4 (or earlier), which precludes the use of 1.5-specific
   * features.
   */
  public static void issue14BuildWarning(Throwable t, JamLogger log) {
    if (!m14BuildWarningDone) {
      log.warning("This build of JAM was not made with JDK 1.5." +
                  "Even though you are now running under JDK 1.5, "+
                  "JSR175-style annotations will not be available");
      if (log.isVerbose(TigerDelegateHelper.class)) log.verbose(t);
      m14BuildWarningDone = true;
    }
  }

  /**
   * Displays a warning indicating that JAM is running under 1.4 (or earlier),
   * which precludes the use of 1.5-specific features.
   */
  public static void issue14RuntimeWarning(Throwable t, JamLogger log) {
    if (!m14RuntimeWarningDone) {
      log.warning("You are running under a pre-1.5 JDK.  JSR175-style "+
                  "source annotations will not be available");
      if (log.isVerbose(TigerDelegateHelper.class)) log.verbose(t);
      m14RuntimeWarningDone = true;
    }
  }

  public static boolean isTigerJavadocAvailable(JamLogger logger) {
	if (! tigerJavadocCheckDone)
    try {
      tigerJavadocCheckDone = true;
      // class for name this because it's 1.5 specific.  if it fails, we
      // don't want to use the extractor
      Class.forName(SOME_TIGER_SPECIFIC_JAVADOC_CLASS);
	  tigerJavadocAvailable = true;
      return true;
    } catch (ClassNotFoundException e) {
      issue14RuntimeWarning(e,logger);
    }
    return tigerJavadocAvailable;
  }

  public static boolean isTigerReflectionAvailable(JamLogger logger) {
  	if (! tigerReflectionCheckDone)
	try {
		tigerReflectionCheckDone = true;
	  // class for name this because it's 1.5 specific.  if it fails, we
	  // don't want to use the extractor
	  Class.forName(SOME_TIGER_SPECIFIC_REFLECT_CLASS);
	  tigerReflectionAvailable = true;
	  return true;
	} catch (ClassNotFoundException e) {
	  issue14RuntimeWarning(e,logger);
	}
	return tigerReflectionAvailable;
  }
}