
AudioPlayer sfx;
class SoundH
{
  int parts;
  SoundH()
  {
    sfx = minim.loadFile("pickup.mp3");
   audio = minim.loadFile("aud.mp3");
  }
  void init()
  {
    parts = int(audio.length()/terrain.modu);
    println(parts);
    audio.loop();
    fft = new FFT(audio.bufferSize(), audio.sampleRate() );
  }

  boolean playy=false;
  int canCh = 0;
  void update()
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
