package ru.geekbrains.stargame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.base.BaseScreen;
import ru.geekbrains.stargame.exception.GameException;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.pool.BulletPool;
import ru.geekbrains.stargame.pool.EnemyPool;
import ru.geekbrains.stargame.sprites.Background;
import ru.geekbrains.stargame.sprites.Enemy;
import ru.geekbrains.stargame.sprites.Ship;
import ru.geekbrains.stargame.sprites.Star;

public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 64;

    private Texture bg;
    private Background background;
    private Music fonMus;

    private TextureAtlas atlas;

    private Star[] stars;
    private BulletPool bulletPool;
    private Ship ship;
    private Enemy enemy;
    private EnemyPool enemyPool;

    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/bg.png");
        atlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));
        fonMus = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        bulletPool = new BulletPool();
        enemyPool = new EnemyPool();
        initSprites();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        freeAllDestroyed();
        draw();
    }

    @Override
    public void resize(Rect worldBounds)
    {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (Star star : stars)
        {
            star.resize(worldBounds);
        }
        ship.resize(worldBounds);
        enemy.resize(worldBounds);
    }

    @Override
    public void dispose()
    {
        bg.dispose();
        atlas.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        fonMus.dispose();
        super.dispose();
    }

    @Override
    public boolean keyDown(int keycode)
    {
        ship.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        ship.keyUp(keycode);
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button)
    {
        ship.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button)
    {
        ship.touchUp(touch, pointer, button);
        return false;
    }

    private void initSprites()
    {
        try {
            background = new Background(bg);
            stars = new Star[STAR_COUNT];
            for (int i = 0; i < STAR_COUNT; i++)
            {
                stars[i] =  new Star(atlas);
            }
            ship = new Ship(atlas, bulletPool);
            enemy = new Enemy(atlas, enemyPool);
        } catch (GameException e)
        {
            throw new RuntimeException(e);
        }
        fonMus.play();
    }

    private void update(float delta)
    {
        for (Star star : stars)
        {
            star.update(delta);
        }
        fonMus.setLooping(true);
        ship.update(delta);
        enemy.update(delta);
        enemyPool.updateActiveSprites(delta);
        bulletPool.updateActiveSprites(delta);
    }

    public void freeAllDestroyed()
    {
        bulletPool.freeAllDestroyedActiveObjects();
        enemyPool.freeAllDestroyedActiveObjects();
    }

    private void draw() {
        Gdx.gl.glClearColor(0.5f, 0.7f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (Star star : stars) {
            star.draw(batch);
        }
        ship.draw(batch);
        enemyPool.drawActiveSprites(batch);
        bulletPool.drawActiveSprites(batch);
        batch.end();
    }
}
