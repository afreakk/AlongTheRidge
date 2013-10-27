float dt = 0.0;
float time = 0.0;
float totalTime=0.0;
void handleTime()
{
  dt = (millis()-totalTime)/1000.0;
  totalTime = millis();
  time = totalTime/1000.0;
  float fps = 1.0/dt;
  String fpsTitle = "FPS: "+fps;
  frame.setTitle(fpsTitle);
}
