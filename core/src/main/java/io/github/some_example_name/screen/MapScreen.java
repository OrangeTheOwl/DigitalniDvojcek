package io.github.some_example_name.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;

import io.github.some_example_name.Main;
import io.github.some_example_name.assetUtil.AssetDescriptors;
import io.github.some_example_name.assetUtil.RegionNames;
import io.github.some_example_name.classes.Airport;
import io.github.some_example_name.config.GameConfig;
import io.github.some_example_name.databaseUtil.DatabaseUtil;
import io.github.some_example_name.utils.Constants;
import io.github.some_example_name.utils.Geocoding;
import io.github.some_example_name.utils.Geolocation;
import io.github.some_example_name.utils.MapRasterTiles;
import io.github.some_example_name.utils.Markers;
import io.github.some_example_name.utils.ZoomXY;

public class MapScreen extends ScreenAdapter implements GestureDetector.GestureListener, InputProcessor {
    private final Main game;
    private final AssetManager assetManager;

    private Viewport viewport;
    private Stage stage;
    private Skin skin;

    private TextureAtlas gameplayAtlas;

    private ShapeRenderer shapeRenderer;

    private Vector3 touchPosition;

    private Texture markerIcon = new Texture("assets/rawImages/marker.png");
    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;
    private OrthographicCamera camera;

    private Texture[] mapTiles;
    private ZoomXY beginTile;   // top left tile

    // center geolocation
    private final Geolocation CENTER_GEOLOCATION = new Geolocation(46.557314, 15.637771);

    // test marker
    private final Geolocation MARKER_GEOLOCATION = new Geolocation(46.559070, 15.638100);

    private List<Airport> airports = new ArrayList<Airport>();

    public MapScreen(Main game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        airports = DatabaseUtil.getAirportLocations();
        System.out.println("Num of airports: " + airports.size());
        gameplayAtlas = assetManager.get(AssetDescriptors.GAMEPLAY);

        shapeRenderer = new ShapeRenderer();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.MAP_WIDTH, Constants.MAP_HEIGHT);
        camera.position.set(Constants.MAP_WIDTH / 2f, Constants.MAP_HEIGHT / 2f, 0);
        camera.viewportWidth = Constants.MAP_WIDTH / 2f;
        camera.viewportHeight = Constants.MAP_HEIGHT / 2f;
        camera.zoom = 2f;
        camera.update();

        touchPosition = new Vector3();

        try {
            //in most cases, geolocation won't be in the center of the tile because tile borders are predetermined (geolocation can be at the corner of a tile)
            ZoomXY centerTile = MapRasterTiles.getTileNumber(CENTER_GEOLOCATION.lat, CENTER_GEOLOCATION.lng, Constants.ZOOM);
            mapTiles = MapRasterTiles.getRasterTileZone(centerTile, Constants.NUM_TILES);
            //you need the beginning tile (tile on the top left corner) to convert geolocation to a location in pixels.
            beginTile = new ZoomXY(Constants.ZOOM, centerTile.x - ((Constants.NUM_TILES - 1) / 2), centerTile.y - ((Constants.NUM_TILES - 1) / 2));

        } catch (IOException e) {
            e.printStackTrace();
        }

        tiledMap = new TiledMap();
        MapLayers layers = tiledMap.getLayers();

        TiledMapTileLayer layer = new TiledMapTileLayer(Constants.NUM_TILES, Constants.NUM_TILES, MapRasterTiles.TILE_SIZE, MapRasterTiles.TILE_SIZE);
        int index = 0;
        for (int j = Constants.NUM_TILES - 1; j >= 0; j--) {
            for (int i = 0; i < Constants.NUM_TILES; i++) {
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                cell.setTile(new StaticTiledMapTile(new TextureRegion(mapTiles[index], MapRasterTiles.TILE_SIZE, MapRasterTiles.TILE_SIZE)));
                layer.setCell(i, j, cell);
                index++;
            }
        }
        layers.add(layer);

        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        Gdx.input.setInputProcessor(this);

    }

    /*@Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }*/
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        try {
            handleInput();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        camera.update();

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        drawMarkers();


        game.getBatch().begin();
       /* Vector2 marker = MapRasterTiles.getPixelPosition(MARKER_GEOLOCATION.lat, MARKER_GEOLOCATION.lng, beginTile.x, beginTile.y);

        game.getBatch().draw(markerIcon,marker.x - markerIcon.getWidth()/2f ,marker.y);
        System.out.println("batch " + marker.x + " " + marker.y);*/
        game.getBatch().setProjectionMatrix(camera.combined);
        drawAirportMarkers();
        game.getBatch().end();

    }

    private void drawMarkers() {
        Vector2 marker = MapRasterTiles.getPixelPosition(MARKER_GEOLOCATION.lat, MARKER_GEOLOCATION.lng, beginTile.x, beginTile.y);
        //System.out.println("shape " + marker.x + " " + marker.y);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(marker.x, marker.y, 10);
        shapeRenderer.end();



    }

    private void drawAirportMarkers() {
        for (Airport a : airports){

            Vector2 marker = MapRasterTiles.getPixelPosition(a.location.lat, a.location.lng, beginTile.x, beginTile.y);

            game.getBatch().draw(markerIcon,marker.x - markerIcon.getWidth()/2f ,marker.y);
            //System.out.println("batch " + marker.x + " " + marker.y);
/*
            Vector2 marker = MapRasterTiles.getPixelPosition(a.location.lat, a.location.lng, beginTile.x, beginTile.y);

            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(marker.x, marker.y, 10);
            shapeRenderer.end();*/
        }

    }




    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        touchPosition.set(x, y, 0);
        camera.unproject(touchPosition);
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        camera.translate(-deltaX, deltaY);
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        if (initialDistance >= distance)
            camera.zoom += 0.02;
        else
            camera.zoom -= 0.02;
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }

    private void handleInput() throws IOException, InterruptedException {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.zoom += 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.zoom -= 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.translate(-3, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.translate(3, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.translate(0, -3, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.translate(0, 3, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.T)){
            Texture test = Markers.getMarkerIcon();

        }

        camera.zoom = MathUtils.clamp(camera.zoom, 0.2f, 2f);

        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

        camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, Constants.MAP_WIDTH - effectiveViewportWidth / 2f);
        camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, Constants.MAP_HEIGHT - effectiveViewportHeight / 2f);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.println("Clicked on " + screenX + " " + screenY);
        Vector3 temp = new Vector3();
        temp.set(screenX,screenY,0);
        camera.unproject(temp);
        System.out.println("Clicked on unprojected " + temp.x + " " + temp.y);
        Vector2 clicked = MapRasterTiles.getPixelPosition(screenX, screenY, beginTile.x, beginTile.y);
        System.out.println("Clicked on " + clicked.x + " " + clicked.y);
        for (Airport a : airports){
            Vector2 airportMarker = MapRasterTiles.getPixelPosition(a.location.lat, a.location.lng, beginTile.x, beginTile.y);
            airportMarker.x -= markerIcon.getWidth()/2f;
            System.out.println("airportMarker " + airportMarker.x + " " + airportMarker.y);
            if (temp.x >= airportMarker.x && temp.x <= airportMarker.x + markerIcon.getWidth()){
                if (temp.y >= airportMarker.y && temp.y <= airportMarker.y + markerIcon.getHeight()){


                    System.out.println("Clicked on " + a.address);
                    game.setScreen(new GameScreen(game));
                }
            }

        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

}
