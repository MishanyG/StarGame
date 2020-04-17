package ru.geekbrains.stargame.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.base.Sprite;
import ru.geekbrains.stargame.exception.GameException;

public class Explosion extends Sprite
{
    private static final float ANIMATE_INTERVAL = 0.017f;
    private float animateTimer;
    private Sound exlosionSound;

    public Explosion(TextureAtlas atlas, Sound explosionSound) throws GameException
    {
        super(atlas.findRegion("boom"), 6, 8, 48);
        this.exlosionSound = explosionSound;
    }

    public void set(Vector2 pos, float height)
    {
        this.pos.set(pos);
        setHeightProportion(height);
        exlosionSound.play(0.2f);
    }

    @Override
    public void update(float delta)
    {
        animateTimer += delta;
        if (animateTimer >= ANIMATE_INTERVAL)
        {
            animateTimer = 0f;
            if (++frame == regions.length)
                destroy();
        }
    }

    @Override
    public void destroy()
    {
        super.destroy();
        frame = 0;
    }
}
