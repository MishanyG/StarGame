package ru.geekbrains.stargame.base;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.exception.GameException;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.pool.BulletPool;
import ru.geekbrains.stargame.sprites.Bullet;

public abstract class Ships extends Sprite
{
    protected Rect              worldBounds;
    protected BulletPool        bulletPool;
    protected TextureRegion     bulletRegion;
    protected Vector2           bulletV;
    protected float             bulletHeight;
    protected int               damage;
    protected int               hp;
    protected Sound             shootSound;
    protected Vector2           v0;
    protected Vector2           v;
    protected float             animateInterval;
    protected float             animateTimer;

    public Ships() {
    }

    public Ships(TextureRegion region, int rows, int cols, int frame) throws GameException {
        super(region, rows, cols, frame);
    }

    @Override
    public void update(float delta)
    {
        pos.mulAdd(v, delta);
        animateTimer += delta;
        if (animateTimer >= animateInterval)
        {
            shoot();
            animateTimer = 0f;
        }
    }

    private void shoot()
    {
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, pos, bulletV, bulletHeight, worldBounds, damage);
        shootSound.play(0.1f);
    }
}
