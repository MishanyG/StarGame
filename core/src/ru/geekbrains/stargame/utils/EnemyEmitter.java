package ru.geekbrains.stargame.utils;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.math.Rnd;
import ru.geekbrains.stargame.pool.EnemyPool;
import ru.geekbrains.stargame.sprites.Enemy;

public class EnemyEmitter
{
    private static final float  ENEMY_SMALL_HEIGHT = 0.1f;
    private static final float  ENEMY_SMALL_BULLET_HEIGHT = 0.01f;
    private static final float  ENEMY_SMALL_BULLET_VY = -0.3f;
    private static final int    ENEMY_SMALL_DAMAGE = 1;
    private static final float  ENEMY_SMALL_RELOAD_INTERVAL = 2f;
    private static final int    ENEMY_SMALL_HP = 1;

    private static final float  ENEMY_MORE_HEIGHT = 0.15f;
    private static final float  ENEMY_MORE_BULLET_HEIGHT = 0.02f;
    private static final float  ENEMY_MORE_BULLET_VY = -0.3f;
    private static final int    ENEMY_MORE_DAMAGE = 4;
    private static final float  ENEMY_MORE_RELOAD_INTERVAL = 3f;
    private static final int    ENEMY_MORE_HP = 4;

    private static final float  ENEMY_MEDIUM_HEIGHT = 0.15f;
    private static final float  ENEMY_MEDIUM_BULLET_HEIGHT = 0.02f;
    private static final float  ENEMY_MEDIUM_BULLET_VY = -0.25f;
    private static final int    ENEMY_MEDIUM_DAMAGE = 5;
    private static final float  ENEMY_MEDIUM_RELOAD_INTERVAL = 4f;
    private static final int    ENEMY_MEDIUM_HP = 5;

    private static final float  ENEMY_BIG_HEIGHT = 0.2f;
    private static final float  ENEMY_BIG_BULLET_HEIGHT = 0.04f;
    private static final float  ENEMY_BIG_BULLET_VY = -0.3f;
    private static final int    ENEMY_BIG_DAMAGE = 10;
    private static final float  ENEMY_BIG_RELOAD_INTERVAL = 1f;
    private static final int    ENEMY_BIG_HP = 10;

    private Rect    worldBounds;
    private Sound   shootSound;
    private int     level = 1;

    private float generateInterval = 4f;
    private float generateTimer;

    private       TextureRegion bulletRegion;
    private final TextureRegion[] enemySmallRegion;
    private final TextureRegion[] enemyMoreRegion;
    private final TextureRegion[] enemyMediumRegion;
    private final TextureRegion[] enemyBigRegion;

    private final Vector2 enemySmallV;
    private final Vector2 enemyMoreV;
    private final Vector2 enemyMediumV;
    private final Vector2 enemyBigV;

    private final EnemyPool enemyPool;

    public EnemyEmitter(TextureAtlas atlas, EnemyPool enemyPool, Rect worldBounds, Sound shootSound)
    {
        this.worldBounds        = worldBounds;
        this.shootSound         = shootSound;
        this.bulletRegion       = atlas.findRegion("bullet2");
        TextureRegion enemy0    = atlas.findRegion("enemy0");
        this.enemySmallRegion   = Regions.split(enemy0, 1, 2, 2);
        TextureRegion enemy3    = atlas.findRegion("enemy3");
        this.enemyMoreRegion   = Regions.split(enemy3, 1, 2, 2);
        TextureRegion enemy1    = atlas.findRegion("enemy1");
        this.enemyMediumRegion  = Regions.split(enemy1, 1, 2, 2);
        TextureRegion enemy2    = atlas.findRegion("enemy2");
        this.enemyBigRegion     = Regions.split(enemy2, 1, 2, 2);
        this.enemyPool          = enemyPool;
        this.enemySmallV        = new Vector2(0, -0.2f);
        this.enemyMoreV         = new Vector2(0, -0.07f);
        this.enemyMediumV       = new Vector2(0, -0.03f);
        this.enemyBigV          = new Vector2(0, -0.005f);
    }

    public void generate(float delta, int frags)
    {
        level = frags / 10 + 1;
        generateTimer += delta;
        if (generateTimer >= generateInterval)
        {
            generateTimer = 0f;
            Enemy enemy = enemyPool.obtain();
            float type = (float) Math.random();
            if (type < 0.6f)
                enemy.set(enemySmallRegion, enemySmallV, bulletRegion, ENEMY_SMALL_BULLET_HEIGHT, ENEMY_SMALL_BULLET_VY,
                        ENEMY_SMALL_DAMAGE * level, ENEMY_SMALL_RELOAD_INTERVAL, shootSound, ENEMY_SMALL_HP, ENEMY_SMALL_HEIGHT);
            else if (type < 0.7f)
                enemy.set(enemyMoreRegion, enemyMoreV, bulletRegion, ENEMY_MORE_BULLET_HEIGHT, ENEMY_MORE_BULLET_VY,
                        ENEMY_MORE_DAMAGE * level, ENEMY_MORE_RELOAD_INTERVAL, shootSound, ENEMY_MORE_HP, ENEMY_MORE_HEIGHT);
            else if (type < 0.85f)
                enemy.set(enemyMediumRegion, enemyMediumV, bulletRegion, ENEMY_MEDIUM_BULLET_HEIGHT, ENEMY_MEDIUM_BULLET_VY,
                        ENEMY_MEDIUM_DAMAGE * level, ENEMY_MEDIUM_RELOAD_INTERVAL, shootSound, ENEMY_MEDIUM_HP, ENEMY_MEDIUM_HEIGHT);
            else
                enemy.set(enemyBigRegion, enemyBigV, bulletRegion, ENEMY_BIG_BULLET_HEIGHT, ENEMY_BIG_BULLET_VY,
                        ENEMY_BIG_DAMAGE * level, ENEMY_BIG_RELOAD_INTERVAL, shootSound, ENEMY_BIG_HP, ENEMY_BIG_HEIGHT);
            enemy.pos.x = Rnd.nextFloat(worldBounds.getLeft() + enemy.getHalfWidth(), worldBounds.getRight() - enemy.getHalfHeight());
            enemy.setBottom(worldBounds.getTop());
        }
    }

    public int getLevel()
    {
        return level;
    }
}
