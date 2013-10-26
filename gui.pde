class GHighScore
{
  int amnt = 8;
  int[] scores = new int[amnt];
  String[] names = new String[amnt];
  PImage img;
  GHighScore()
  {
    img = loadImage("splash.png");
    BufferedReader reader;
    reader = createReader("hScore.txt");
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
        scores[i] = int(pieces[1]);
      }
    }
  }
  String typedText="";
  boolean keyReady = true;
  boolean keyPut(int nScore)
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
    textFont(font,18);
    // this adds a blinking cursor after your text, at the expense of redrawing everything every frame

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
    text("Name: "+typedText+(frameCount/10 % 2 == 0 ? "_" : ""), width/2-150, 90);
    return false;
  }
  boolean compareToLowest(int nScore)
  {
    if(nScore>scores[amnt-1])
      return true;
    return false;
  }
  int jk=6;
  void update()
  {
    image(img,0,0);
    textSize(24);
    jk=6;
    fill(rColor[jk--]);
    text("Travel Right",width/2-50,3*45);
    fill(rColor[jk--]);
    text("Highscores:",width/2-50,4*45);
    for(int i=0; i<amnt; i++)
    {
      fill(rColor[jk]);
      if(jk>0)
        jk--;
      else
        jk=6;
      text("Rank:"+(i+1)+" "+names[i]+" catched "+scores[i]+" rainbows",width/2-200  ,  (5.0+i)*45 );
    }
    int rnd = int(noise(time)*6.0);
    fill(rColor[rnd]);
    text("Press space to continue...",width/2-50,(5.0+amnt)*45);


  }
  void newScore(int nScore,String name)
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
  void toFile()
  {
    PrintWriter out;
    out = createWriter("hScore.txt");
    for(int i=0; i<amnt; i++)
      out.println(names[i]+'/'+scores[i]);
    out.flush();
    out.close();
  }
  void sortByScore()
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
GRainbows rainBows;
class Gui
{
  GDistance dist=new GDistance();
  Gui()
  {
    rainBows = new GRainbows();
  }
  void update()
  {
    dist.update(rainBows);
    rainBows.update();
  }
}
class GDistance
{
  float gjor=0.0;
  boolean canDo=false;
  boolean first= true;
  void update(GRainbows rr)
  {
    textSize(16);
    if(0.01>terrain.extraX%terrain.modu)
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
    String dis = "Distance to rainbow: "+Float.toString(terrain.modu-(terrain.extraX-gjor));
    fill(255);
    text(dis,width/2.0-125,height/8.0);
  }
}
class GRainbows
{
  int rainbows = 0;
  void update()
  {
    text("Rainbows Collected: "+rainbows,100,50);
  }
}
