package ru.geekbrains.stargame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import ru.geekbrains.stargame.base.BaseScreen;
import ru.geekbrains.stargame.exception.GameException;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.pool.BulletPool;
import ru.geekbrains.stargame.pool.EnemyPool;
import ru.geekbrains.stargame.sprites.Background;
import ru.geekbrains.stargame.sprites.Enemy;
import ru.geekbrains.stargame.sprites.ShipHero;
import ru.geekbrains.stargame.sprites.Star;
import ru.geekbrains.stargame.utils.EnemyEmitter;

public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 64;

    private Texture bg;
    private Background background;
    private Music fonMus;
    private Sound shootSound;
    private Sound bulletSound;

    private TextureAtlas atlas;

    private Star[] stars;
    private BulletPool bulletPool;
    private ShipHero shipHero;
    private EnemyPool enemyPool;
    private EnemyEmitter enemyEmitter;

    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/bg.png");
        atlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));
        shootSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        fonMus = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        fonMus.setLooping(true);
        fonMus.play();
        bulletPool = new BulletPool();
        enemyPool = new EnemyPool(bulletPool, worldBounds);
        enemyEmitter = new EnemyEmitter(atlas, enemyPool, worldBounds, bulletSound);
        initSprites();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        checkCollisions();
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
        shipHero.resize(worldBounds);
    }

    @Override
    public void dispose()
    {
        bg.dispose();
        atlas.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        fonMus.dispose();
        shootSound.dispose();
        super.dispose();
    }

    @Override
    public boolean keyDown(int keycode)
    {
        shipHero.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        shipHero.keyUp(keycode);
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button)
    {
        shipHero.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button)
    {
        shipHero.touchUp(touch, pointer, button);
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
            shipHero = new ShipHero(atlas, bulletPool, shootSound);
        } catch (GameException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void update(float delta)
    {
        for (Star star : stars)
        {
            star.update(delta);
        }
        shipHero.update(delta);
        enemyPool.updateActiveSprites(delta);
        bulletPool.updateActiveSprites(delta);
        enemyEmitter.generate(delta);
    }

    private void checkCollisions()
    {
        List<Enemy> enemyList = enemyPool.getActiveObjects();
        for (Enemy enemy : enemyList)
        {
            if (enemy.isDestroyed()) continue;
            float minDist = enemy.getHalfWidth() + shipHero.getHalfWidth();
            if (shipHero.pos.dst(enemy.pos) < minDist) enemy.destroy();
        }
    }

    private void freeAllDestroyed()
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
        shipHero.draw(batch);
        enemyPool.drawActiveSprites(batch);
        bulletPool.drawActiveSprites(batch);
        batch.end();
    }
}
