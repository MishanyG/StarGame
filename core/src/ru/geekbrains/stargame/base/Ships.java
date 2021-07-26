package ru.geekbrains.stargame.base;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.exception.GameException;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.pool.BulletPool;
import ru.geekbrains.stargame.pool.ExplosionPool;
import ru.geekbrains.stargame.sprites.Bullet;
import ru.geekbrains.stargame.sprites.Explosion;

public abstract class Ships extends Sprite
{
    private static final float  DAMAGE_ANIMATE_INTERVAL = 0.1f;
    private static final float  DELTA_COEFF = 1.2f;
    private   float savedDelta  = 0f;

    protected Rect              worldBounds;
    protected BulletPool        bulletPool;
    protected ExplosionPool     explosionPool;
    protected TextureRegion     bulletRegion;
    protected Vector2           bulletV;
    protected Vector2           bulletPos;
    protected float             bulletHeight;
    protected int               damage;
    protected int               hp;
    protected Sound             shootSound;
    protected Vector2           v0;
    protected Vector2           v;
    protected float             animateInterval;
    protected float             animateTimer;
    protected float             damageAnimateTimer = DAMAGE_ANIMATE_INTERVAL;

    public Ships() {
    }

    public Ships(TextureRegion region, int rows, int cols, int frame) throws GameException {
        super(region, rows, cols, frame);
    }

    @Override
    public void update(float delta)
    {
        if (savedDelta == 0f) savedDelta = delta;
        if (delta > savedDelta * DELTA_COEFF) delta = savedDelta;
        pos.mulAdd(v, delta);
        damageAnimateTimer += delta;
        if (damageAnimateTimer >= DAMAGE_ANIMATE_INTERVAL) frame = 0;
    }

    @Override
    public void destroy()
    {
        super.destroy();
        boom();
    }

    public void damage (int damage)
    {
        damageAnimateTimer = 0f;
        frame = 1;
        hp -= damage;
        if (hp <= 0)
        {
            hp = 0;
            destroy();
        }
    }

    public void aid(int aid)
    {
        hp += aid;
    }

    public int getDamage()
    {
        return damage;
    }

    public int getHp()
    {
        return hp;
    }

    protected void autoShoot (float delta)
    {
        animateTimer += delta;
        if (animateTimer >= animateInterval)
        {
            animateTimer = 0f;
            shoot();
        }
    }

    private void shoot()
    {
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, bulletPos, bulletV, bulletHeight, worldBounds, damage);
        shootSound.play(0.1f);
    }

    private void boom()
    {
        Explosion explosion = explosionPool.obtain();
        explosion.set(pos, getHeight());
    }
}
