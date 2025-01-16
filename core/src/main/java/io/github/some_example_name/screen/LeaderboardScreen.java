package io.github.some_example_name.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.List;

import io.github.some_example_name.assetUtil.AssetDescriptors;
import io.github.some_example_name.assetUtil.RegionNames;
import io.github.some_example_name.Main;
import io.github.some_example_name.classes.PlayerScore;
import io.github.some_example_name.common.GameManager;
import io.github.some_example_name.config.GameConfig;

public class LeaderboardScreen extends ScreenAdapter {
    private final Main game;
    private final AssetManager assetManager;
    private final Preferences prefs = Gdx.app.getPreferences("My Preferences");
    private Viewport viewport;
    private Stage stage;

    private Skin skin;
    private TextureAtlas gameplayAtlas;

    public LeaderboardScreen(Main game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());

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
        Table table = new Table();
        table.defaults().pad(20);

        Table tableTitle = new Table(skin);
        tableTitle.setBackground("window-c");
        Label title = new Label("Leaderboard", skin);
        title.setFontScale(2f);
        tableTitle.add(title);



        Table tableScores = new Table(skin);
        tableScores.setBackground("window-c");
        tableScores.setBounds(0, 0, 300f, 300f);

        List<PlayerScore> allScores  = GameManager.INSTANCE.getPlayerScore();
        if (allScores != null){
            for (int i = 0; i < allScores.size(); i++){
                Label labelName = new Label(allScores.get(i).name, skin);
                tableScores.add(labelName).pad(10f).padRight(20f).left();
                String scoreNum = allScores.get(i).score;
                Label labelScore = new Label(scoreNum, skin);
                tableScores.add(labelScore).expandX().pad(10f).right();
                tableScores.row();
            }
        }




        /*String names[] = {"Vid", "Rok", "Tadej", "Jaka", "dada", "ssadasd","sasa","sadasd","sadasd","sadasda"};
        Float scores[] = {10213f, 5654f, 10f, 100f, 100f, 100f, 100f, 100f, 100f, 100f};
        Table tableScores = new Table(skin);
        tableScores.setBackground("window-c");
        tableScores.setBounds(0, 0, 300f, 300f);

        for (int i = 0; i < 10; i++) {

            Label labelName = new Label(names[i], skin);
            tableScores.add(labelName).pad(10f).padRight(20f).left();
            Label labelScore = new Label(scores[i].toString(), skin);
            tableScores.add(labelScore).expandX().pad(10f).right();
            tableScores.row();
        }*/

        ScrollPane scrollPane = new ScrollPane(tableScores);
        scrollPane.layout();


        Table tableMenu = new Table(skin);
        tableMenu.setBackground("window-c");
        TextButton menuButton = new TextButton("Menu", skin);
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });
        tableMenu.add(menuButton);



        Table buttonTable = new Table();
        buttonTable.defaults().padLeft(30).padRight(30);

        buttonTable.add(tableMenu).padBottom(15).expandX().fillX().width(350).row();

        buttonTable.center();

        table.add(tableTitle).width(350);
        table.row();
        table.add(scrollPane).width(350);
        table.row();
        table.add(buttonTable).width(350);
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
