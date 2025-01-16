package io.github.some_example_name.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.some_example_name.BaseActor;
import io.github.some_example_name.Main;
import io.github.some_example_name.assetUtil.AssetDescriptors;
import io.github.some_example_name.assetUtil.RegionNames;
import io.github.some_example_name.classes.Maze;
import io.github.some_example_name.classes.Player;
import io.github.some_example_name.config.GameConfig;

public class GameScreen extends ScreenAdapter {
    private final Main game;
    private final AssetManager assetManager;

    private Viewport viewport;
    private Stage stage;
    private Skin skin;

    private TextureAtlas gameplayAtlas;



    Maze maze;

    Player player;

    Long startTime;





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

        //stage.addActor(createBackround());

        maze = new Maze(stage);

        player = new Player(0, 0, stage);
        player.centerAtActor(maze.getRoom(0, 0));
        System.out.println("player " +  player.getX() + " " + player.getY());

/*
        stage.addActor(planeOne());
        stage.addActor(planTwo());
*/

        Gdx.input.setInputProcessor(stage);
        startTime = System.currentTimeMillis();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 0f);
        for (BaseActor wall : BaseActor.getList(stage, "io.github.some_example_name.classes.Wall")) {
            player.preventOverlap(wall);
        }

        if (player.getY() > GameConfig.HUD_HEIGHT-64 && player.getX() > GameConfig.HUD_WIDTH-64){
            long elapsedTime = TimeUtils.timeSinceMillis(startTime);
            game.setScreen(new OverScreen(game, elapsedTime));
            System.out.println("player " + player.getX() + " " + player.getY());
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

    private Table createBackround(){
        Table root = new Table();
        root.setFillParent(true);

        TextureRegion backgroundRegion = gameplayAtlas.findRegion(RegionNames.BACKGROUND);
        root.setBackground(new TextureRegionDrawable(backgroundRegion));

        return root;

    }


}
