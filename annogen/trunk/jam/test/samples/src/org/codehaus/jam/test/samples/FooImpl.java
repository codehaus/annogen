package org.codehaus.jam.test.samples;

import org.codehaus.jam.test.samples.ejb.MyEjbException;
import org.codehaus.jam.test.samples.Base;
import org.codehaus.jam.test.samples.Foo;

import java.net.MalformedURLException;

/**
 *  Dummy class for JAM tests.
 *
 *  @author codehaus Nov 25, 2003
 */
public abstract class FooImpl extends Base implements Foo {

  public int getId() { return -1; }

  public void setId(int id) {}

  private final static void setId2(double id) {}

  protected synchronized void setId3(double id, double id2) {}

  protected abstract void setId4(double id, double id2, double id3);

  String[][] methodDealingWithArrays(int[] foo, Object[] bar) {
    return null;
  }

  protected abstract void iThrowExceptions(int p1, String p2) throws
          IllegalArgumentException,
          NoSuchMethodError,
          MyException,
          MyEjbException,
          MalformedURLException,
          OutOfMemoryError,
          NullPointerException;




}
