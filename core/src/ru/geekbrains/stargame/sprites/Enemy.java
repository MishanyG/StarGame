package ru.geekbrains.stargame.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.base.Sprite;
import ru.geekbrains.stargame.exception.GameException;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.pool.EnemyPool;

public class Enemy extends Sprite
{
    private static final float animateInterval = 0.7f;
    private float animateTimer = 0.5f;

    private Rect worldBounds;
    private EnemyPool enemyPool;
    private TextureRegion enemyRegion;
    private Vector2 enemyV;

    public Enemy(TextureAtlas atlas, EnemyPool enemyPool) throws GameException {
        super(atlas.findRegion("enemy2"), 1, 2, 2);
        this.enemyPool = enemyPool;
        enemyRegion = atlas.findRegion("enemy2");
        enemyRegion.setRegionWidth(enemyRegion.getRegionWidth()/2);
        enemyV = new Vector2(0, -0.5f);
    }
    @Override
    public void resize(Rect worldBounds)
    {
        this.worldBounds = worldBounds;
    }

    @Override
    public void update(float delta)
    {
        animateTimer += delta;
        if (animateTimer >= animateInterval) {
            nextSh();
            animateTimer = 0;
        }
    }

    public void nextSh()
    {
        EnemyShip enemyShip = enemyPool.obtain();
        enemyShip.set(this, enemyRegion, pos, enemyV, 0.15f, worldBounds, 1);
    }
}
