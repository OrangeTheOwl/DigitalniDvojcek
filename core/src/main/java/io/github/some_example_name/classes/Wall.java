package io.github.some_example_name.classes;

import com.badlogic.gdx.scenes.scene2d.Stage;

import io.github.some_example_name.BaseActor;

public class Wall extends BaseActor {
    public Wall(float x, float y, float width, float height, Stage stage) {
        super(x, y, stage);
        loadTexture("assets/rawImages/square.jpg");
        setSize(width, height);
        setBoundaryRectangle();
    }
}
