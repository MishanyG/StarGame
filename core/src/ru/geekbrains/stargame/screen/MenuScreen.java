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
    private Vector2 vClick;
    private Vector2 vAng;
    int speed = 2;
    private float angle;

    @Override
    public void show() {
        super.show();
        img = new Texture("badlogic.jpg");
        pos = new Vector2();
        v = new Vector2();
        vClick = new Vector2();
        vAng = new Vector2();
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
        vClick.add(screenX, (Gdx.graphics.getHeight() - screenY));
        v.add(vClick);              // Сохраняем вектор для сравнения с позицией
        vAng.add(screenX, 0);    // Вектор, относительно которого дальше будем вычислять угол
        vClick.nor();
        vAng.nor();
        angle = (float) Math.acos(vClick.dot(vAng));   // Расчёт траектории движения изображения (угол в радианах)
        return false;
    }

    private void update (float delta)
    {
        while (v.x > pos.x)
        {
            pos.x += speed * Math.cos(angle);
            pos.y += speed * Math.sin(angle);       // Расчёты для плавного перемещения изображения в точку нажатия кнопки мыши
            return;
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
