class Terrain
{
  float cameraRotation = -PI/4.0;
  void render()
  {
    pushMatrix();
    translate(0.0,+height*2.0,0.0);
    rotate(cameraRotation,1,0,0);
    makeCubes();
    popMatrix();//rotate
  }
  int gap = 60;
  int xS = 50;
  int yS = 1;
  int zS = 50;
  float xo = ((float(xS)*gap)/2.0);
  float yo = ((float(yS)*gap)/2.0);
  float zo = ((float(zS)*gap)/2.0);
  float[] zArr = new float[xS*yS*zS];
  float[] zExtra = new float[xS*yS*zS];
  float[] xExtra = new float[xS*yS*zS];
  float[] musExtra = new float[xS*yS*zS];
  float mSpeed = 0.0;
  boolean first = true;
  void controlMovement(float zMovement)
  {
      mSpeed = zMovement;
  }
  float extraX=0.0;
  float xSpeed = 0.0;
  float[] active = new float[xS];
  boolean rainB = false;
  float modu=300.0;
  boolean firstHax=true;
  float haxValue =0.0;
  private void makeCubes()
  {
    haxValue += dt*mSpeed;
    float heightMax = 6000.0;
    int j=0;
    int index = 0;
    extraX += xSpeed;
    if(firstHax&&haxValue>1000.0)
    {
      firstHax=false;
      player.up=false;
    }
    if(1.0>extraX%modu)
    {
      if(rainB)
      {
        rainbow.doRainbow();
        sfx.cue(0);
        sfx.play();
        rainB=false;
      }
    }
    else
      rainB=true;
    for(int x=0; x<xS; x++)
    {
      float xPos = x*gap-xo;
      pushMatrix();
      translate(xPos, 0.0, 0.0);

      for(int y=0; y<yS; y++)
      {
        float yPos = y*gap-yo;
        pushMatrix();
        translate(0.0, yPos, 0.0);

        for(int z=0; z<zS; z++)
        {
          zArr[index] += dt*mSpeed;
          float zP = z*gap-zo+zArr[index];
          if(zP>zo/8.0)
          {
            zArr[index] -= zo*2;
            zExtra[index] -= zS-1;
            if(j<xS)
              active[j] = index;
            j++;
          }
          float noize = noise( (float(z)+zExtra[index])/float(zS), ((float(x))+extraX)/float(xS) )*heightMax;
          pushMatrix();
          translate(0.0, 0.0, zP+200.0);
          translate(0.0, noize-heightMax/4.0, 0.0);
          renderCube(noize-heightMax/4.0,index,zP,xPos,yPos,x);
          popMatrix();
          index++;
        }

        popMatrix();
      }

      popMatrix();
    }
  }

  private void renderCube(float hght,int index,float zP,float xPos,float yPos,int x)
  {
      musExtra[index] = (hght)/5000.0;
      float lMod = musExtra[index]+0.1;
      float hMod = 1.0-musExtra[index]+0.1;
      color seaGreen = color(32*lMod,117*lMod,255*lMod);
      color forestGreen = color(34*hMod,255*hMod,34*hMod);
      rotate(-cameraRotation,1,0,0);
      fill(seaGreen*forestGreen);
      float mg = gap/2.0;
      beginShape();
      vertex(-mg, -mg,mg);
      vertex(-mg, mg,-mg);
      vertex(mg, mg,-mg);
      vertex(mg, -mg,mg);
      endShape(CLOSE); 
      boolean isIt=false;
      for(int kk=0; kk<xS; kk++)
      {
        if(index == active[kk])
          isIt=true;
      }
      if(isIt)
      {
        addPhys(xPos,yPos+hght,x,forestGreen*seaGreen);
      }
  }
  FCircle[] boxArr = new FCircle[xS];
  private void initPhysObjs()
  {
    for(int i=0; i<xS; i++)
    {
      boxArr[i] = new FCircle(gap/4);
      boxArr[i].setStatic(true);
      world.add(boxArr[i]);
    }
  }
  void init()
  {
    initPhysObjs();
  }
  private void addPhys(float x,float y,int index,int clr)
  {
    boxArr[index].setFill(clr);
    boxArr[index].setPosition(((x/3600)/2.0)*(width*2.0)+width/2.0,((y/3600)/2.0)*(height*1.0)+height/4.0);

  }

}
