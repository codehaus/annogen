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
 * @author Patrick Calahan &lt;email: codehaus-at-bea-dot-com&gt;
 */
public interface JamLogger {

  public void setVerbose(Class c);

  public boolean isVerbose(Object o);

  public boolean isVerbose(Class c);  

  public void setShowWarnings(boolean b);

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
   * @deprecated
   *
   * @return
   */
  public boolean isVerbose();
  
}
