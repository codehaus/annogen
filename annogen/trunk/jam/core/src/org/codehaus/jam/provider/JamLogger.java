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
package org.codehaus.jam.provider;

/**
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public interface JamLogger {

  /**
   * <p>Outputs a debug message if appropriate for the given object.</p>
   */
  public void verbose(String msg, Object ifThisIsVerbose);

  /**
   * <p>Outputs a debug message if appropriate for the given object.</p>
   */
  public void verbose(Throwable t, Object ifThisIsVerbose);

  /**
   * <p>Outputs a debug message no matter what.</p>
   */
  public void verbose(String msg);

  /**
   * <p>Outputs a debug message no matter what.</p>
   */
  public void verbose(Throwable t);

  /**
   * <p>Outputs a debug message as appropriate.</p>
   */
  public void warning(Throwable t);

  /**
   * <p>Outputs a debug message as appropriate.</p>
   */
  public void warning(String w);

  /**
   * <p>Outputs an error as appropriate.</p>
   */
  public void error(Throwable t);

  /**
   * <p>Outputs an error as appropriate.</p>
   */
  public void error(String msg);


  /**
   * Enables verbose output in all JAM classes which are equal to or a
   * subclass of the given classs.
   */
  public void setVerbose(Class c);

  /**
   * Returns true if debugging is enabled for the given object.
   */
  public boolean isVerbose(Object o);

  /**
   * Returns true if debugging is enabled for the given class.
   */
  public boolean isVerbose(Class c);

  /**
   * Returns true if display of warnings is enabled.
   */
  public void setShowWarnings(boolean b);

  /**
   * @deprecated
   */
  public boolean isVerbose();
  
}
