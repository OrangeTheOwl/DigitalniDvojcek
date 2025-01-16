package io.github.some_example_name.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.some_example_name.Main;
import io.github.some_example_name.assetUtil.AssetDescriptors;
import io.github.some_example_name.assetUtil.RegionNames;
import io.github.some_example_name.config.GameConfig;

public class IntroScreen  extends ScreenAdapter {
    private final Preferences prefs = Gdx.app.getPreferences("My Preferences");
    public static final float INTRO_DURATION_IN_SEC = 0f;   // duration of the (intro) animation
    private final Main game;
    private final AssetManager assetManager;
    private Viewport viewport;
    private TextureAtlas gameplayAtlas;
    private float duration = 0f;

    private Stage stage;
    public IntroScreen(Main game) {
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

        stage.addActor(createBackround());

        stage.addActor(planeOne());
        stage.addActor(planTwo());
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(65 / 255f, 159 / 255f, 221 / 255f, 0f);

        duration += delta;

        // go to the MenuScreen after INTRO_DURATION_IN_SEC seconds
        if (duration > INTRO_DURATION_IN_SEC) {
            game.setScreen(new MenuScreen(game));
        }

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

    private Actor planeOne() {
        Image key = new Image(gameplayAtlas.findRegion(RegionNames.PLANE_RIGHT));

        key.setHeight(key.getHeight()/5f);
        key.setWidth(key.getWidth()/5f);


        key.setPosition(0 - key.getWidth() / 2f, viewport.getWorldHeight() / 2f + 100f);
        key.addAction(
            Actions.moveTo(viewport.getWorldWidth()+key.getWidth() / 2f, viewport.getWorldHeight() / 2f + 100f, 1.5f)   // // move image to the center of the window
        );
        return key;
    }


    private Actor planTwo() {
        Image key = new Image(gameplayAtlas.findRegion(RegionNames.PLANE_LEFT));

        key.setHeight(key.getHeight()/2f);
        key.setWidth(key.getWidth()/2f);


        key.setPosition(viewport.getWorldWidth()+key.getWidth() / 2f, viewport.getWorldHeight() / 2f - key.getHeight()/2f * 2f);
        key.addAction(
            Actions.moveTo(0 - key.getWidth(), viewport.getWorldHeight() / 2f - key.getHeight()/2f * 2f, 1.5f)   // // move image to the center of the window
        );
        return key;
    }


    private Table createBackround(){
        Table root = new Table();
        root.setFillParent(true);

        TextureRegion backgroundRegion = gameplayAtlas.findRegion(RegionNames.BACKGROUND);
        root.setBackground(new TextureRegionDrawable(backgroundRegion));

        return root;

    }
}
