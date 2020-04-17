package ru.geekbrains.stargame.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.base.Sprite;
import ru.geekbrains.stargame.exception.GameException;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.math.Rnd;

public class FirstAid extends Sprite
{
    private static final int aid = 10;
    private static final float HEIGHT = 0.08f;
    private Rect worldBounds;
    private final Vector2 v;
    private Sprite owner;
    private float animateInterval = 0.5f;
    private float animateTimer;

    public FirstAid(TextureAtlas atlas) throws GameException
    {
        super(atlas.findRegion("FirstAid"));
        float vx = Rnd.nextFloat(0f, 0.005f);
        v = new Vector2(vx, -0.08f);
        animateTimer = Rnd.nextFloat(0, 0.5f);
    }

    @Override
    public void resize(Rect worldBounds)
    {
        this.worldBounds = worldBounds;
        setHeightProportion(HEIGHT);
        float posX = Rnd.nextFloat(worldBounds.getLeft(), worldBounds.getRight());
        this.pos.set(posX, -0.5f);
        setBottom(worldBounds.getTop());
    }

    public void update(float delta, boolean fa)
    {
        if (fa)
        {
            pos.mulAdd(v, delta);
            scale += 0.01f;
            animateTimer += delta;
            if (animateTimer >= animateInterval)
            {
                animateTimer = 0;
                scale = 1;
            }
        }
        else
        {
            float posX = Rnd.nextFloat(worldBounds.getLeft(), worldBounds.getRight());
            this.pos.set(posX, -0.5f);
            setBottom(worldBounds.getTop());
        }
    }

    public static int getAid() {
        return aid;
    }

    public Object getOwner() {
        return owner;
    }
}
