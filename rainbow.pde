color[] rColor = new color[7];
  void initrColors()
  {
    rColor[0] = color(255,0,0);
    rColor[1] = color(255,127,0);
    rColor[2] = color(255,255,0);
    rColor[3] = color(0,255,0);
    rColor[4] = color(0,0,255);
    rColor[5] = color(75,0,130);
    rColor[6] = color(143,0,255);
  }
class Rainbow
{
  FPoly myBlob;
  PImage img;
  Rainbow(FBody lbody)
  {
    img= loadImage("rainbow.png");
  }
  void doRainbow()
  {
    y=0;
  }
  float x=0;
  float y=height;
  float mSpeed = 100.0;
  int jj=0;
  boolean hasChanged = false;
  void rainbowUpdate()
  {
    y += dt*mSpeed;
    if(player.pwr>2.0)
    {
      hasChanged=true;
    }
    else
    {
      if(hasChanged)
      {
        if(jj<6)
          jj++;
        else
          jj=0;
        hasChanged=false;
      }
    }
    tint(player.pwr);
    image(img,x,y);
  }
  float highest = 0.0;
  void update()
  {
    rainbowUpdate();
  }

}
