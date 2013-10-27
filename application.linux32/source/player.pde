class Player
{
  Player(float x, float y)
  {
    body = new FCircle(50);
    body.setAllowSleeping(false);
    body.setStroke(255);
    body.setStrokeWeight(2);
    body.setFill(255);
    body.setFriction(0);
    body.setPosition(x,y);
    world.add(body);
  }
  boolean left=false;
  boolean right=false;
  boolean up = true;
  boolean jump = false;
  boolean down = false;
  void updateKeys(boolean state)
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
  float pwr=0.0;
  void updateMovement()
  {
    float xSpeed = dt*20.0;
    float xMovement = 0.0;
    if(right)
      xMovement += xSpeed;
    terrain.xSpeed = xMovement;

    float zSpeed = 1000;
    float zMovement = 0.0;
    if(up)
      zMovement += zSpeed;
    terrain.controlMovement(zMovement);
    pwr = 0.0;
    for(int i=0; i<128; i++)
      pwr += fft.getBand(i);
    body.setFill(pwr);
    body.setStrokeWeight(pwr/200.0);
    body.setStroke(pwr);
    body.addForce(0,-pwr*2.0);

  }
}
int count = 0; 
void keyPressed()
{
  if(player!=null)
  player.updateKeys(true);
}
void keyReleased()
{
  if(player!=null)
  player.updateKeys(false);
}
