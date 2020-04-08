package ru.geekbrains.stargame.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.base.Ships;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.pool.BulletPool;

public class Enemy extends Ships
{
    public Enemy(BulletPool bulletPool, Rect worldBounds)
    {
        this.bulletPool = bulletPool;
        this.worldBounds = worldBounds;
        v = new Vector2();
        v0 = new Vector2();
        bulletV = new Vector2();
    }

    @Override
    public void update(float delta)
    {
        Vector2 vec = new Vector2(0f, -0.5f);
        if (worldBounds.getHalfHeight() <= (pos.y * 1.2f))
            pos.mulAdd(vec, delta);
        else
            super.update(delta);
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
        this.v.set(v0);
        setHeightProportion(height);
    }
}
