package ru.geekbrains.stargame.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.base.Sprite;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.math.Rnd;

public class EnemyShip extends Sprite
{
    private Rect worldBounds;
    private final Vector2 v = new Vector2();
    private int damage;
    private Object owner;

    public EnemyShip()
    {
        regions = new TextureRegion[1];
    }

    public void set(Object owner, TextureRegion region, Vector2 pos0, Vector2 v0, float height, Rect worldBounds, int damage)
    {
        this.owner = owner;
        this.regions[0] = region;
        this.pos.set(pos0);
        this.v.set(v0);
        setHeightProportion(height);
        setTop(worldBounds.getTop() + 0.13f);
        float posX = Rnd.nextFloat(worldBounds.getLeft(), worldBounds.getRight());
        this.pos.set(posX, pos.y);
        this.worldBounds = worldBounds;
        this.damage = damage;
    }

    @Override
    public void update(float delta)
    {
        pos.mulAdd(v, delta);
        if (isOutside(worldBounds)) destroy();
    }
}