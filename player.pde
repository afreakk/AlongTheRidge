class Player
{
  Player(float x, float y)
  {
    body = new FCircle(50);
    body.setAllowSleeping(false);
    body.setStroke(0);
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
  void updateKeys()
  {
    if(key == 'a')
      left = keyPressed;
    if(key == 'd')
      right = keyPressed;
    if(key == 'w')
      up = keyPressed;
    if(key == 's')
      down = keyPressed;
    if(key == ' ')
      jump = keyPressed;
  }
  void updateMovement()
  {
    float xSpeed = 1.0;
    float xMovement = 0.0;
    if(right)
      xMovement += xSpeed;
    terrain.xSpeed = xMovement;

    float zSpeed = 1000;
    float zMovement = 0.0;
    if(up)
      zMovement += zSpeed;
    terrain.controlMovement(zMovement);

  }
}
int count = 0; 
void keyPressed()
{
  if(player!=null)
  player.updateKeys();
}
void keyReleased()
{
  if(player!=null)
  player.updateKeys();
}
