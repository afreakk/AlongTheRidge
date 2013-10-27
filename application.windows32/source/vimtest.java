import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.analysis.*; 
import ddf.minim.*; 
import fisica.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class vimtest extends PApplet {





class Main
{
  public void setup(processing.core.PApplet that)
  {
    smooth();
    lights();
    initCam();
    world= new FWorld();
    world.setGravity(0,500);
    player = new Player(width/2.0f,10);
    rainbow = new Rainbow(body);
    terrain = new Terrain();
    terrain.init();
    soundH.init();
    gui = new Gui();
  }
  public void draw()
  {
    soundH.update();
    background(0);
    lights();
    rainbow.update();
    rendering();
    player.updateMovement();
    world.step(dt);
    world.draw();
    gui.update();
  }
  public void rendering()
  {
    pushMatrix();
    translate(width/2.0f,height/2.0f,-1000.0f); //center
    terrain.render();
    popMatrix();
  }
}
FWorld world;
Terrain terrain;
Player player=null;
FCircle body=null;
Rainbow rainbow;
Gui gui;
Main main= new Main();
GHighScore hScore;  
PFont font;
FFT fft;
SoundH soundH;
Minim minim;
AudioPlayer audio;
public void setup()
{
  minim = new Minim(this);
  soundH = new SoundH();
  initrColors();
  Fisica.init(this);
  font = createFont("Helvetica",18);
  textFont(font);
  size(800,600,P3D);
  hScore = new GHighScore();
}
int gameMode = 1;
public void draw()
{
  handleTime();
  if(body!=null)
  {
    if(body.isResting())
      exit();
        
    if(body.getY()>height&&gameMode==0)
    {
      if(hScore.compareToLowest(rainBows.rainbows))
        gameMode = 4;
      else
        gameMode = 1;
    }
  }
  switch(gameMode)
  {
    case 0: main.draw();break;
    case 1: hScore.update();
            if(key==' ')
              gameMode=3;break;
    case 3: main.setup(this); gameMode = 0; break;
    case 4: if(hScore.keyPut(rainBows.rainbows))
              gameMode=1;break;
  }
}

AudioPlayer sfx;
class SoundH
{
  int parts;
  SoundH()
  {
    sfx = minim.loadFile("pickup.mp3");
   audio = minim.loadFile("aud.mp3");
  }
  public void init()
  {
    parts = PApplet.parseInt(audio.length()/terrain.modu);
    println(parts);
    audio.loop();
    fft = new FFT(audio.bufferSize(), audio.sampleRate() );
  }

  boolean playy=false;
  int canCh = 0;
  public void update()
  {
    if(playy!=player.right)
    {
      canCh ++;
      if(canCh>4)
      {
        playy = player.right;
        canCh=0;
      }
    }
    if(playy)
    {
      if(!audio.isPlaying())
      {
        if(!(audio.length()>audio.position()))
          audio.cue(0);
        audio.play();
      }
    }
    else 
    {
        audio.pause();
    }
    fft.forward(audio.mix);
  }

}
public void initCam()
{
  perspective(45, PApplet.parseFloat(width)/PApplet.parseFloat(height), 500.0f , 10000.0f);
}
class GHighScore
{
  int amnt = 8;
  int[] scores = new int[amnt];
  String[] names = new String[amnt];
  PImage img = loadImage("splash.png");
 
  GHighScore()
  {
    BufferedReader reader;
    reader = createReader("data/hScore.txt");
    for(int i=0; i<amnt; i++)
    {
      String line;
      try
      {
        line = reader.readLine();
      }
      catch(IOException e)
      {
        e.printStackTrace();
        line = null;
      }
      if(line == null)
        return;
      else
      {
        String[] pieces = split(line,'/');
        names[i] = pieces[0];
        scores[i] = PApplet.parseInt(pieces[1]);
      }
    }
  }
  String typedText="";
  boolean keyReady = true;
  public boolean keyPut(int nScore)
  {
    if (keyPressed&&keyReady) 
    {
      switch(key) 
      {
      case BACKSPACE:
        typedText = typedText.substring(0,max(0,typedText.length()-1));
        break;
      case TAB:
        typedText += "    ";
        break;
      case ENTER:
      case RETURN:
        // comment out the following two lines to disable line-breaks
        newScore(nScore,typedText);
        return true;
      case ESC:
      case DELETE:
        break;
      default:
        typedText += key;
      }
      keyReady=false;
    }
    else if(keyPressed==false)
    {
      keyReady=true;
    }
    image(img,0,0);

    fill(rColor[jk]);
    if(jk>0)
      jk--;
    else
      jk=6;
    text("You made it in the highscore list!",width/2-75,45);
    fill(rColor[jk]);
    if(jk>0)
      jk--;
    else
      jk=6;
    text("Try to type your name!",width/2,90);
    fill(rColor[jk]);
    if(jk>0)
      jk--;
    else
      jk=6;
    text("Name: "+typedText+(frameCount/10 % 2 == 0 ? "_" : ""), width/2-150, 90+45);
    return false;
  }
  public boolean compareToLowest(int nScore)
  {
    if(nScore>scores[amnt-1])
      return true;
    return false;
  }
  int jk=6;
  public void update()
  {
    tint(255);
    image(img,0,0);
    textSize(24);
    jk=6;
    text("Along The Ridge",width/2,1*45);
    fill(0);
    text("Instructions: Catch rainbows by traveling right",width/2,2*45);
    text("Controls: W-Forward, D-Right.",width/2,3*45);
    fill(rColor[jk--]);
    text("Highscores:",width/2,4*45);
    textAlign(LEFT);
    for(int i=0; i<amnt; i++)
    {
      fill(rColor[jk]);
      if(jk>0)
        jk--;
      else
        jk=6;
      text("#"+(i+1)+" "+names[i]+" : "+scores[i]+" rainbows",width/2-200  ,  (5.0f+i)*45 );
    }
    int rnd = PApplet.parseInt(noise(time)*6.0f);
    fill(rColor[rnd]);
    textAlign(CENTER);
    text("Press space to continue...",width/2,(5.0f+amnt)*45);


  }
  public void newScore(int nScore,String name)
  {
    for(int i=0; i<amnt; i++)
    {
      if(nScore>scores[i])
      {
        scores[i] = nScore;
        names[i] = name;
        toFile();
        return;
      }
    }
  }
  public void toFile()
  {
    PrintWriter out;
    out = createWriter("data/hScore.txt");
    for(int i=0; i<amnt; i++)
      out.println(names[i]+'/'+scores[i]);
    out.flush();
    out.close();
  }
  public void sortByScore()
  {
    boolean swapped = false;
    while(swapped)
    {
      for(int i=0; i<amnt-1; i++)
      {
        if(scores[i]<scores[i+1])
        {
          int tScore = scores[i+1];
          String tName = names[i+1];
          scores[i+1] =scores[i];
          names[i+1] = names[i];
          scores[i] = tScore;
          names[i] = tName;
          swapped = true;
        }
      }
    }
  }
}
GDistance dist;
GRainbows rainBows;
class Gui
{
  Gui()
  {
    dist=new GDistance();
    rainBows = new GRainbows();
  }
  public void update()
  {
    dist.update(rainBows);
    rainBows.update();
  }
}
class GDistance
{
  float gjor=0.0f;
  boolean canDo=false;
  boolean first= true;
  public void update(GRainbows rr)
  {
    textSize(16);
    if(1.0f>terrain.extraX%terrain.modu)
    {
      if(canDo)
      {
        gjor += terrain.modu;
        canDo=false;
      }
    }
    else if(!canDo)
    {
      canDo=true;
      if(!first)
        rr.rainbows++;
      else
        first=false;
    }
    String dis = "Distance to rainbow: "+PApplet.parseInt(terrain.modu-(terrain.extraX-gjor));
    String distToLoss = "Distance from loss: "+PApplet.parseInt(height-body.getY());
    textAlign(CENTER);
    if(rainbow.y>height)
    {
      fill(255);
      text(dis,width/2.0f,height/8.0f);
      text(distToLoss,width/4.0f,height-height/8.0f);
    }
  }
}
class GRainbows
{
  int rainbows = 0;
  public void update()
  {
    textAlign(CENTER);
    if(rainbow.y>height)
      text("Rainbows Collected: "+rainbows,width/2,height/16.0f);
    else
    {
      textSize(24);
      text("Rainbows Collected: "+rainbows+"!!!",width/2,height/8.0f);
    }
  }
}
class Physics
{
  Physics(processing.core.PApplet that)
  {
//    world.setGravity(0,900);
  }
  public void update()
  {
  }

}
class Player
{
  Player(float x, float y)
  {
    body = new FCircle(50);
    body.setAllowSleeping(false);
    body.setStroke(255);
    body.setStrokeWeight(2);
    body.setFill(255,255,255);
    body.setFriction(0);
    body.setPosition(x,y);
    world.add(body);
  }
  boolean left=false;
  boolean right=false;
  boolean up = true;
  boolean jump = false;
  boolean down = false;
  public void updateKeys(boolean state)
  {
    if(key == 'a')
      left = state;
    if(key == 'd')
      right = state;
    if(key == 'w')
      up = state;
    if(key == 's')
      down = state;
    if(key == ' ')
      jump = state;
  }
  float pwr=0.0f;
  public void updateMovement()
  {
    float xSpeed = dt*20.0f;
    float xMovement = 0.0f;
    if(right)
      xMovement += xSpeed;
    terrain.xSpeed = xMovement;

    float zSpeed = 1000;
    float zMovement = 0.0f;
    if(up)
      zMovement += zSpeed;
    terrain.controlMovement(zMovement);
    pwr = 0.0f;
    for(int i=0; i<128; i++)
      pwr += fft.getBand(i);
    body.setStrokeWeight(pwr/200.0f);
    body.setStroke(pwr);
    body.addForce(0,-pwr*2.0f);

  }
}
int count = 0; 
public void keyPressed()
{
  if(player!=null)
  player.updateKeys(true);
}
public void keyReleased()
{
  if(player!=null)
  player.updateKeys(false);
}
int[] rColor = new int[7];
  public void initrColors()
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
  public void doRainbow()
  {
    y=0;
  }
  float x=0;
  float y=height;
  float mSpeed = 100.0f;
  int jj=0;
  boolean hasChanged = false;
  public void rainbowUpdate()
  {
    y += dt*mSpeed;
    if(player.pwr>2.0f)
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
  float highest = 0.0f;
  public void update()
  {
    rainbowUpdate();
  }

}
class Terrain
{
  float cameraRotation = -PI/4.0f;
  public void render()
  {
    pushMatrix();
    translate(0.0f,+height*2.0f,0.0f);
    rotate(cameraRotation,1,0,0);
    makeCubes();
    popMatrix();//rotate
  }
  int gap = 60;
  int xS = 50;
  int yS = 1;
  int zS = 50;
  float xo = ((PApplet.parseFloat(xS)*gap)/2.0f);
  float yo = ((PApplet.parseFloat(yS)*gap)/2.0f);
  float zo = ((PApplet.parseFloat(zS)*gap)/2.0f);
  float[] zArr = new float[xS*yS*zS];
  float[] zExtra = new float[xS*yS*zS];
  float[] xExtra = new float[xS*yS*zS];
  float[] musExtra = new float[xS*yS*zS];
  float mSpeed = 0.0f;
  boolean first = true;
  public void controlMovement(float zMovement)
  {
      mSpeed = zMovement;
  }
  float extraX=0.0f;
  float xSpeed = 0.0f;
  float[] active = new float[xS];
  boolean rainB = false;
  float modu=300.0f;
  boolean firstHax=true;
  float haxValue =0.0f;
  private void makeCubes()
  {
    haxValue += dt*mSpeed;
    float heightMax = 6000.0f;
    int j=0;
    int index = 0;
    extraX += xSpeed;
    if(firstHax&&haxValue>1000.0f)
    {
      firstHax=false;
      player.up=false;
    }
    if(1.0f>extraX%modu)
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
      translate(xPos, 0.0f, 0.0f);

      for(int y=0; y<yS; y++)
      {
        float yPos = y*gap-yo;
        pushMatrix();
        translate(0.0f, yPos, 0.0f);

        for(int z=0; z<zS; z++)
        {
          zArr[index] += dt*mSpeed;
          float zP = z*gap-zo+zArr[index];
          if(zP>zo/8.0f)
          {
            zArr[index] -= zo*2;
            zExtra[index] -= zS-1;
            if(j<xS)
              active[j] = index;
            j++;
          }
          float noize = noise( (PApplet.parseFloat(z)+zExtra[index])/PApplet.parseFloat(zS), ((PApplet.parseFloat(x))+extraX)/PApplet.parseFloat(xS) )*heightMax;
          pushMatrix();
          translate(0.0f, 0.0f, zP+200.0f);
          translate(0.0f, noize-heightMax/4.0f, 0.0f);
          renderCube(noize-heightMax/4.0f,index,zP,xPos,yPos,x);
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
      musExtra[index] = (hght)/5000.0f;
      float lMod = musExtra[index]+0.1f;
      float hMod = 1.0f-musExtra[index]+0.1f;
      int seaGreen = color(32*lMod,117*lMod,255*lMod);
      int forestGreen = color(34*hMod,255*hMod,34*hMod);
      rotate(-cameraRotation,1,0,0);
      fill(seaGreen*forestGreen);
      float mg = gap/2.0f;
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
  public void init()
  {
    initPhysObjs();
  }
  private void addPhys(float x,float y,int index,int clr)
  {
    boxArr[index].setFill(clr);
    boxArr[index].setPosition(((x/3600)/2.0f)*(width*2.0f)+width/2.0f,((y/3600)/2.0f)*(height*1.0f)+height/4.0f);

  }

}
float dt = 0.0f;
float time = 0.0f;
float totalTime=0.0f;
public void handleTime()
{
  dt = (millis()-totalTime)/1000.0f;
  totalTime = millis();
  time = totalTime/1000.0f;
  float fps = 1.0f/dt;
  String fpsTitle = "FPS: "+fps;
  frame.setTitle(fpsTitle);
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "vimtest" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
