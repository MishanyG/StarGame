package ru.geekbrains.stargame.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.base.Ships;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.pool.BulletPool;
import ru.geekbrains.stargame.pool.ExplosionPool;

public class Enemy extends Ships
{
    private final Vector2 descentV;
    public Enemy(BulletPool bulletPool, ExplosionPool explosionPool, Rect worldBounds)
    {
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.worldBounds = worldBounds;
        v = new Vector2();
        v0 = new Vector2();
        bulletV = new Vector2();
        bulletPos = new Vector2();
        descentV = new Vector2(0, -0.3f);
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);
        bulletPos.set(pos.x, pos.y - getHalfHeight());
        if (getTop() <= worldBounds.getTop())
        {
            v.set(v0);
            autoShoot(delta);
        }
        if (getBottom() <= worldBounds.getBottom())
            destroy();
    }

    public void set(TextureRegion[] regions, Vector2 v0, TextureRegion bulletRegion, float bulletHeight, float bulletVY,
                    int damage, float reloadInterval, Sound shootSound, int hp, float height)
    {
        this.regions = regions;
        this.v0.set(v0);
        this.bulletRegion = bulletRegion;
        this.bulletHeight = bulletHeight;
        this.bulletV.set(0, bulletVY);
        this.damage = damage;
        this.animateInterval = reloadInterval;
        this.animateTimer = animateInterval;
        this.shootSound = shootSound;
        this.hp = hp;
        this.v.set(descentV);
        setHeightProportion(height);
    }

    public boolean isBulletCollision(Rect bullet)
    {
        return !(bullet.getRight() < getLeft() || bullet.getLeft() > getRight() || bullet.getBottom() > getTop() || bullet.getTop() < pos.y);
    }
}
