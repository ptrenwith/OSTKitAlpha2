package org.digitalpassport.ui;

import java.io.Serializable;

/**
 *
 * @author Philip M. Trenwith
 */
public class cWindowBounds implements Serializable
{

  private int main_x;
  private int main_y;
  private int main_w;
  private int main_h;

  private int api_x;
  private int api_y;
  private int api_w;
  private int api_h;

  public int get_main_x()
  {
    return main_x;
  }

  public void set_main_x(int main_x)
  {
    this.main_x = main_x;
  }

  public int get_main_y()
  {
    return main_y;
  }

  public void set_main_y(int main_y)
  {
    this.main_y = main_y;
  }

  public int get_main_w()
  {
    return main_w;
  }

  public void set_main_w(int main_w)
  {
    this.main_w = main_w;
  }

  public int get_main_h()
  {
    return main_h;
  }

  public void set_main_h(int main_h)
  {
    this.main_h = main_h;
  }

  public int get_api_x()
  {
    return api_x;
  }

  public void set_api_x(int api_x)
  {
    this.api_x = api_x;
  }

  public int get_api_y()
  {
    return api_y;
  }

  public void set_api_y(int api_y)
  {
    this.api_y = api_y;
  }

  public int get_api_w()
  {
    return api_w;
  }

  public void set_api_w(int api_w)
  {
    this.api_w = api_w;
  }

  public int get_api_h()
  {
    return api_h;
  }

  public void set_api_h(int api_h)
  {
    this.api_h = api_h;
  }
}
