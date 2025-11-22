package main;

import com.jogamp.opengl.GL2;

public interface Entity {

    public void render(GL2 gl);
    public void move(float speed, float angle);
    public void moveTo(float speed, float destinationX, float destinationY);
    public void update();
    void initializeTextures();

}
