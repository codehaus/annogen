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
package org.codehaus.annogen.examples.stored_override;

import org.codehaus.annogen.override.AnnoOverrider;
import org.codehaus.annogen.view.ReflectAnnoViewer;

/**
 * <p>
 * This class demonstrates how to create a simple AnnoViewer and use
 * it to retrieve potentially-overridden JSR175 annotations.
 * </p>
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class DeployerTool {

  // ========================================================================
  // Constructor

  /**
   * In order to support the given annotation overrides, all the tool needs
   * to do is use them to create an AnnoViewer using an externally-supplied
   * AnnoOverrier.
   */
  public DeployerTool(AnnoOverrider overrider) {
    mAnnoViewer = ReflectAnnoViewer.Factory.create(overrider);
  }

  private ReflectAnnoViewer mAnnoViewer;

  // ========================================================================
  // Public methods

  /**
   * Now that our AnnoViewer is all set up, we're ready to process deployment
   * requests for the User.
   */
  public void deployBean(Class beanClass) {
    log("\n--------------------------------------------------------------");
    log("Deploying "+beanClass.getName()+"...");

    /**
     * We simply ask our AnnoViewer for the the DeploymentInfo annotation
     * on the given beanClass.  Note that this is exactly like calling
     *
     *   beanClass.getAnnotation(DeploymentInfo.class)
     *
     * with the key difference being that...
     */
    DeploymentInfo deploymentInfo = (DeploymentInfo)
      mAnnoViewer.getAnnotation(DeploymentInfo.class, beanClass);

    /**
     * ...the overrides that the user gave us will be in effect.  The nice
     * thing is, though, that our deployment code didn't get all muddied up
     * with extra code for dealing with annotations - that was all handled
     * by the User.
     */
    int cacheSize = deploymentInfo.cacheSize();
    log("...cacheSize="+cacheSize);
  }



  private static void log(String msg) {
    System.out.println(msg);
  }
}