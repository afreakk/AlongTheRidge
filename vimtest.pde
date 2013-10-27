
import ddf.minim.analysis.*;
import ddf.minim.*;
import fisica.*;
class Main
{
  void setup(processing.core.PApplet that)
  {
    smooth();
    lights();
    initCam();
    world= new FWorld();
    world.setGravity(0,500);
    player = new Player(width/2.0,10);
    rainbow = new Rainbow(body);
    terrain = new Terrain();
    terrain.init();
    soundH.init();
    gui = new Gui();
  }
  void draw()
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
  void rendering()
  {
    pushMatrix();
    translate(width/2.0,height/2.0,-1000.0); //center
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
void setup()
{
  frame.setTitle("AlongTheRidge");
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
void draw()
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
