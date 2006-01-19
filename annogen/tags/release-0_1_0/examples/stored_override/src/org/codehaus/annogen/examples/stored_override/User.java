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

import org.codehaus.annogen.examples.stored_override.myjar.TinyCacheBean;
import org.codehaus.annogen.examples.stored_override.myjar.HugeCacheBean;
import org.codehaus.annogen.override.AnnoOverrider;
import org.codehaus.annogen.override.StoredAnnoOverrider;
import org.codehaus.annogen.override.ReflectElementIdPool;
import org.codehaus.annogen.override.ElementId;
import org.codehaus.annogen.override.AnnoBeanSet;

/**
 * <p>
 * This class demonstrates how to create a simple AnnoOverrider using
 * the StoredAnnoOverrider helper class.
 * </p>
 *
 * <p>
 * It simulates a User of the DeployerTool who wants to deploy the
 * two beans in MyJar.  Unfortunately, the developer of TinyCacheBean set
 * the cacheSize way too small for us (3), and we need to increase it.
 * But fortunately, our DeployerTool supports an AnnoOverrider which will
 * allow the User specify alternate values for cacheSize.
 * </p>
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class User {

  // ========================================================================
  // Main method

  public static void main(String[] args) {

    /**
     * We're going to pretend that our deployment contains an special file
     * called annotation-overides.xml which lists the classes whose cacheSize
     * needs to be bigger than the original developer thought...
     */
    log("\n--------------------------------------------------------------");
    log("Reading annotation overrides from annotation-overrides.xml...");

    /**
     * ...but for brevity's sake we'll just make them up here:
     */
    final Class[] ANNOTATION_OVERRIDES_XML = { TinyCacheBean.class };

    /**
     * Now that we've 'read' these overrides, we can create an instance of
     * DeployerTool that will override instance of the DeployerTool.
     */
    AnnoOverrider overrider = createOverrider(ANNOTATION_OVERRIDES_XML,123456);

    /**
     * Now we can create the DeployerTool and tell it about our
     * overrides...
     */
    DeployerTool deployer = new DeployerTool(overrider);

    /**
     * ...and finally get down to business deploying our beans.
     * Observe what the deployer reports the cacheSize values to be; the
     * HugeCacheBean has the same value as in the source, but TinyCacheBean's
     * cache has been increased.
     */
    deployer.deployBean(HugeCacheBean.class);
    deployer.deployBean(TinyCacheBean.class);
  }


  /**
   * ...but before we get ahead of ourselves, take a look at how that
   * AnnoOverrider was actually put together.  We need one that simply
   * creates brand new annotations (with the given new values for cacheSize)
   * for the given classes.
   */
  private static AnnoOverrider createOverrider(Class[] classes,
                                               int newCacheSize) {

    log("\n--------------------------------------------------------------");
    log("Initializing annotation overrides");
    log("\n");

    /**
     * StoredAnnoOverrider is a helper class that makes simple overrides like
     * this a lot more convenient.
     */
    StoredAnnoOverrider storedOverrides = StoredAnnoOverrider.Factory.create();

    /**
     * In order to work with the StoredAnnoOverrider, we need one of
     * these pools for getting ElementIds.  In the interests of efficiency,
     * it's a good idea to create one of these and re-use it as much as
     * possible.
     */
    ReflectElementIdPool elementPool =  ReflectElementIdPool.Factory.create();

    /**
     * We loop through all of the specified classes and change the cacheSize
     * on each...
     */
    for(int i=0; i<classes.length; i++) {
      log("The User thinks the cache size for\n  "+
          classes[i].getName()+"\nis too small, so...");

      /**
       * We have to create an ElementId for the class...
       */
      ElementId myBeanId = elementPool.getIdFor(classes[i]);

      /**
       * ...which we then use to create a set of AnnoBeans...
       */
      AnnoBeanSet annos = storedOverrides.findOrCreateStoredAnnoSetFor(myBeanId);

      /**
       * ...in which we create an AnnoBean for the class' DeploymentInfo
       * annotation.  The StoredAnnoOverrider works by simply handing this
       * object back to the AnnoViewer later on,
       */
      DeploymentInfoAnnoBean annoBean = (DeploymentInfoAnnoBean)
        annos.findOrCreateBeanFor(DeploymentInfo.class);

      /**
       * so we can take this opporunity to go ahead and modify the cache size.
       */
      annoBean.set_cacheSize(newCacheSize);

      log("...we have increased it to "+annoBean.cacheSize());
    }
    return storedOverrides;
  }

  private static void log(String msg) {
    System.out.println(msg);
  }

}
