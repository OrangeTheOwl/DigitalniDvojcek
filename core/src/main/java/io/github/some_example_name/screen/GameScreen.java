package io.github.some_example_name.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.some_example_name.Main;
import io.github.some_example_name.assetUtil.AssetDescriptors;
import io.github.some_example_name.config.GameConfig;

public class GameScreen extends ScreenAdapter {
    private final Main game;
    private final AssetManager assetManager;

    private Viewport viewport;
    private Stage stage;
    private Skin skin;

    private TextureAtlas gameplayAtlas;

    public GameScreen(Main game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }


    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());
        assetManager.load(AssetDescriptors.GAMEPLAY);
        assetManager.finishLoading();
        gameplayAtlas = assetManager.get(AssetDescriptors.GAMEPLAY);
/*
        stage.addActor(createBackround());

        stage.addActor(planeOne());
        stage.addActor(planTwo());*/
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 0f);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }


}
