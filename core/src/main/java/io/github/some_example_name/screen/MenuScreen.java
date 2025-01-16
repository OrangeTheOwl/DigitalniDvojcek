package io.github.some_example_name.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.some_example_name.Main;
import io.github.some_example_name.assetUtil.AssetDescriptors;
import io.github.some_example_name.assetUtil.RegionNames;
import io.github.some_example_name.common.GameManager;
import io.github.some_example_name.config.GameConfig;

public class MenuScreen extends ScreenAdapter {
    private final Main game;
    private final AssetManager assetManager;

    private Viewport viewport;
    private Stage stage;
    private Skin skin;

    private TextureAtlas gameplayAtlas;

    public MenuScreen(Main game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());
        /*skin = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));*/
        skin = assetManager.get(AssetDescriptors.UI_SKIN);
        gameplayAtlas = assetManager.get(AssetDescriptors.GAMEPLAY);

        stage.addActor(createBackround());
        stage.addActor(createUi());
        Gdx.input.setInputProcessor(stage);

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


    private Actor createUi() {
        Table table = new Table(skin);
        table.defaults().pad(20);
        // TextButton introButton = new TextButton("Intro screen", skin);
        // introButton.addListener(new ClickListener() {
        //     @Override
        //     public void clicked(InputEvent event, float x, float y) {
        //         game.setScreen(new IntroScreen(game));
        //     }
        // });
        Table tablePlay = new Table(skin);
        tablePlay.setBackground("window-c");

        TextButton playButton = new TextButton("Play", skin);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MapScreen(game));
            }
        });

        tablePlay.add(playButton);

        Table tableLeaderboard = new Table(skin);
        tableLeaderboard.setBackground("window-c");
        TextButton leaderboardButton = new TextButton("Leaderboard", skin);
        leaderboardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LeaderboardScreen(game));
            }
        });
        tableLeaderboard.add(leaderboardButton);

        /*TextButton settingsButton = new TextButton("Settings", skin);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game));
            }
        });*/

        /*Table tableSettings = new Table(skin);
        tableSettings.setBackground("window-c");
        TextButton settingsButton = new TextButton("Settings", skin);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game));
            }
        });
        tableSettings.add(settingsButton);*/



        Table tableQuit = new Table(skin);
        tableQuit.setBackground("window-c");
        TextButton quitButton = new TextButton("Quit", skin);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        tableQuit.add(quitButton);






        Table buttonTable = new Table();
        buttonTable.defaults().padLeft(30).padRight(30);

        // buttonTable.add(introButton).padBottom(15).expandX().fillX().row();
        buttonTable.add(tablePlay).padBottom(15).expandX().fill().width(350).row();
        buttonTable.add(tableLeaderboard).padBottom(15).fillX().row();
        /*buttonTable.add(tableSettings).padBottom(15).fillX().row();
        buttonTable.add(tableAddTemp).padBottom(15).fillX().row();*/
        buttonTable.add(tableQuit).fillX().width(350);

        buttonTable.center();

        table.add(buttonTable);
        table.center();
        table.setFillParent(true);
        table.pack();

        return table;
    }

    private Table createBackround(){
        Table root = new Table();
        root.setFillParent(true);

        TextureRegion backgroundRegion = gameplayAtlas.findRegion(RegionNames.BACKGROUND);
        root.setBackground(new TextureRegionDrawable(backgroundRegion));

        return root;

    }
}
