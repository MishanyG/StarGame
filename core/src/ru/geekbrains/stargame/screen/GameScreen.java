package ru.geekbrains.stargame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import ru.geekbrains.stargame.base.BaseScreen;
import ru.geekbrains.stargame.base.Font;
import ru.geekbrains.stargame.exception.GameException;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.math.Rnd;
import ru.geekbrains.stargame.pool.BulletPool;
import ru.geekbrains.stargame.pool.EnemyPool;
import ru.geekbrains.stargame.pool.ExplosionPool;
import ru.geekbrains.stargame.sprites.Background;
import ru.geekbrains.stargame.sprites.Bullet;
import ru.geekbrains.stargame.sprites.ButtonNewGame;
import ru.geekbrains.stargame.sprites.Enemy;
import ru.geekbrains.stargame.sprites.FirstAid;
import ru.geekbrains.stargame.sprites.GameOver;
import ru.geekbrains.stargame.sprites.ShipHero;
import ru.geekbrains.stargame.sprites.Star;
import ru.geekbrains.stargame.utils.EnemyEmitter;

public class GameScreen extends BaseScreen
{
    private enum State {PLAYING, PAUSE, GAME_OVER}

    private static final int    STAR_COUNT  = 64;
    private static final float  FONT_MARGIN = 0.01f;
    private static final float  FONT_SIZE   = 0.02f;
    private static final String FRAGS       = "Frags: ";
    private static final String HP          = "HP: ";
    private static final String LEVEL       = "Level: ";

    private Texture         bg;
    private Background      background;
    private GameOver        gameOver;
    private ButtonNewGame   buttonNewGame;
    private Music           fonMus;
    private Sound           shootSound;
    private Sound           bulletSound;
    private Sound           explosion;

    private TextureAtlas    atlas;
    private TextureAtlas    atlasNew;

    private Star[]          stars;
    private FirstAid        firstAid;
    private BulletPool      bulletPool;
    private ShipHero        shipHero;
    private EnemyPool       enemyPool;
    private EnemyEmitter    enemyEmitter;
    private ExplosionPool   explosionPool;

    private State           state;
    private State           prevState;

    private int             frags;
    private Font            font;
    private StringBuilder   sbFrags;
    private StringBuilder   sbHP;
    private StringBuilder   sbLevel;

    private float animateInterval;
    private float animateTimer;
    private boolean aid;

    @Override
    public void show() {
        super.show();
        bg              = new Texture("textures/bg.png");
        atlas           = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));
        atlasNew        = new TextureAtlas(Gdx.files.internal("textures/Atlas/mainAtlasNew.tpack"));
        font            = new Font("font/font.fnt", "font/font.png");
        shootSound      = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        bulletSound     = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        explosion       = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        fonMus          = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        font.setSize(FONT_SIZE);
        sbFrags         = new StringBuilder();
        sbHP            = new StringBuilder();
        sbLevel         = new StringBuilder();
        bulletPool      = new BulletPool();
        explosionPool   = new ExplosionPool(atlasNew, explosion);
        enemyPool       = new EnemyPool(bulletPool, explosionPool, worldBounds);
        enemyEmitter    = new EnemyEmitter(atlasNew, enemyPool, worldBounds, bulletSound);
        fonMus.setLooping(true);
        fonMus.play();
        initSprites();
        state           = State.PLAYING;
        prevState       = State.PLAYING;
        frags           = 0;
        animateInterval = Rnd.nextFloat(10f, 100f);
        animateTimer    = Rnd.nextFloat(0f, 5f);
        aid             = false;
    }

    public void startNewGame()
    {
        state = State.PLAYING;
        shipHero.startNewGame(worldBounds);
        frags = 0;
        bulletPool.freeAllActiveObjects();
        enemyPool.freeAllActiveObjects();
        explosionPool.freeAllActiveObjects();
        aid = true;
    }

    @Override
    public void pause()
    {
        prevState = state;
        state = State.PAUSE;
        fonMus.pause();
        aid = false;
    }

    @Override
    public void resume()
    {
        state = prevState;
        fonMus.play();
    }

    @Override
    public void render(float delta)
    {
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
        firstAid.resize(worldBounds);
        shipHero.resize(worldBounds);
        gameOver.resize(worldBounds);
        buttonNewGame.resize(worldBounds);
    }

    @Override
    public void dispose()
    {
        bg.dispose();
        atlas.dispose();
        atlasNew.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        fonMus.dispose();
        shootSound.dispose();
        explosion.dispose();
        font.dispose();
        super.dispose();
    }

    @Override
    public boolean keyDown(int keycode)
    {
        if (state == State.PLAYING)
        shipHero.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        if (state == State.PLAYING)
        shipHero.keyUp(keycode);
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button)
    {
        if (state == State.PLAYING)
            shipHero.touchDown(touch, pointer, button);
        else  if (state == State.GAME_OVER)
                buttonNewGame.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button)
    {
        if (state == State.PLAYING)
            shipHero.touchUp(touch, pointer, button);
        else  if (state == State.GAME_OVER)
            buttonNewGame.touchUp(touch, pointer, button);
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
            firstAid = new FirstAid(atlasNew);
            gameOver = new GameOver(atlas);
            buttonNewGame = new ButtonNewGame(atlasNew, this);
            shipHero = new ShipHero(atlasNew, bulletPool, explosionPool, shootSound);
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
        explosionPool.updateActiveSprites(delta);
        if (state == State.PLAYING)
        {
            shipHero.update(delta);
            enemyPool.updateActiveSprites(delta);
            bulletPool.updateActiveSprites(delta);
            enemyEmitter.generate(delta, frags);
        }
        else if (state == State.GAME_OVER)
            buttonNewGame.update(delta);
        if (!aid)
        {
            animateTimer += delta;
            if (animateTimer >= animateInterval)
            {
                animateInterval = Rnd.nextFloat(5f, 100f);
                animateTimer = Rnd.nextFloat(0f, 5f);
                aid = true;
            }
            firstAid.update(delta, false);
        }
        else firstAid.update(delta, true);
        if (firstAid.pos.y < worldBounds.getBottom())
            aid = false;

    }

    private void checkCollisions()
    {
        if (state != State.PLAYING) return;
        List<Enemy>  enemyList  = enemyPool.getActiveObjects();
        List<Bullet> bulletList = bulletPool.getActiveObjects();
        for (Enemy enemy : enemyList)
        {
            if (enemy.isDestroyed()) continue;
            float minDist = enemy.getHalfWidth() + shipHero.getHalfWidth();
            if (shipHero.pos.dst(enemy.pos) < minDist)
            {
                enemy.destroy();
                frags++;
                shipHero.damage(enemy.getDamage());
            }
            for (Bullet bullet : bulletList)
            {
                if (bullet.getOwner() != shipHero || bullet.isDestroyed()) continue;
                if (enemy.isBulletCollision(bullet))
                {
                    enemy.damage(bullet.getDamage());
                    bullet.destroy();
                    if (enemy.isDestroyed()) frags++;
                }
            }
        }
        for (Bullet bullet : bulletList)
        {
            if (bullet.getOwner() == shipHero || bullet.isDestroyed()) continue;
            if (shipHero.isBulletCollision(bullet))
            {
                shipHero.damage(bullet.getDamage());
                bullet.destroy();
            }
        }
        if (shipHero.isDestroyed()) state = State.GAME_OVER;
        if (!(firstAid.getOwner() == shipHero) && aid)
        {
            if (shipHero.isBulletCollision(firstAid))
            {
                shipHero.aid(FirstAid.getAid());
                aid = false;
            }
        }
    }

    private void freeAllDestroyed()
    {
        bulletPool.freeAllDestroyedActiveObjects();
        enemyPool.freeAllDestroyedActiveObjects();
        explosionPool.freeAllDestroyedActiveObjects();
    }

    private void draw() {
        Gdx.gl.glClearColor(0.5f, 0.7f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (Star star : stars) {
            star.draw(batch);
        }
        switch (state)
        {
            case PLAYING:
                bulletPool.drawActiveSprites(batch);
                if (aid)
                    firstAid.draw(batch);
                shipHero.draw(batch);
                enemyPool.drawActiveSprites(batch);
                break;
            case GAME_OVER:
                gameOver.draw(batch);
                buttonNewGame.draw(batch);
                break;
        }
        explosionPool.drawActiveSprites(batch);
        printInfo();
        batch.end();
    }

    private void printInfo()
    {
        sbFrags.setLength(0);
        sbHP.setLength(0);
        sbLevel.setLength(0);
        font.draw(batch, sbFrags.append(FRAGS).append(frags), worldBounds.getLeft() + FONT_MARGIN, worldBounds.getTop() - FONT_MARGIN);
        font.draw(batch, sbHP.append(HP).append(shipHero.getHp()), worldBounds.pos.x, worldBounds.getTop() - FONT_MARGIN, Align.center);
        font.draw(batch, sbLevel.append(LEVEL).append(enemyEmitter.getLevel()), worldBounds.getRight() - FONT_MARGIN, worldBounds.getTop() - FONT_MARGIN, Align.right);
    }
}
