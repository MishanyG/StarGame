package ru.geekbrains.stargame.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Logo extends BaseScreen
{
    private static final float V_LEN = 0.5f;
    private Texture img;
    private Vector2 pos;
    private Vector2 v;
    private Vector2 touch;
    private Vector2 tmp;

    public Logo()
    {
        img = new Texture("badlogic.jpg");
        pos = new Vector2();
        v = new Vector2();
        touch = new Vector2();
        tmp = new Vector2();
    }

    public void render(SpriteBatch batch)
    {
        update();
        draw(batch);
    }

    @Override
    public void dispose()
    {
        batch.dispose();
        img.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        touch.set(screenX, Gdx.graphics.getHeight() - screenY);
        v.set(touch.cpy().sub(pos)).setLength(V_LEN);
        return false;
    }

    private void update ()
    {
        tmp.set(touch);
        float remainingDistance = (tmp.sub(pos).len());
        if (remainingDistance > V_LEN)
        {
            pos.add(v);
        }
        else
        {
            v.setZero();
            pos.set(touch);
        }
    }

    private void draw (SpriteBatch batch)
    {
        batch.begin();
        batch.draw(img, pos.x, pos.y);
        batch.end();
    }
}
