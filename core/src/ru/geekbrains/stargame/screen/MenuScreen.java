package ru.geekbrains.stargame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.awt.event.MouseMotionListener;

import javax.swing.JButton;

import ru.geekbrains.stargame.base.BaseScreen;

import static com.badlogic.gdx.math.Vector2.dot;
import static java.lang.Math.acos;

public class MenuScreen extends BaseScreen
{
    private Texture img;
    private Vector2 pos;
    private Vector2 v;
    private Vector2 touch;
    private Vector2 tmp;

    @Override
    public void show() {
        super.show();
        img = new Texture("badlogic.jpg");
        pos = new Vector2();
        v = new Vector2();
        touch = new Vector2();
        tmp = new Vector2();
    }

    @Override
    public void render(float delta)
    {
        update(delta);
        draw();
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

    private void update (float delta)
    {
        tmp.set(touch);
        float remainingDistance = (tmp.sub(pos).len());
        if (remainingDistance = V_LEN)
        {
            pos.add(v);
        }
        else
        {
            v.setZero();
            pos.set(touch);
        }
    }

    private void draw ()
    {
        Gdx.gl.glClearColor(0.5f, 0.7f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, pos.x, pos.y);
        batch.end();
    }

}
