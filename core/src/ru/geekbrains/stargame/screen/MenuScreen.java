package ru.geekbrains.stargame.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.stargame.base.BaseScreen;
import ru.geekbrains.stargame.exception.GameException;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.sprites.Background;

public class MenuScreen extends BaseScreen
{
    private Texture bg;
    private Background background;
    private Vector2 pos;

    public MenuScreen()
    {
        super.show();
        bg = new Texture("textures/bg.png");
        try {
            background = new Background(bg);
        } catch (GameException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        pos = new Vector2();
    }

    public void render(SpriteBatch batch)
    {
        draw(batch);
    }

    @Override
    public void dispose()
    {
        batch.dispose();
        bg.dispose();
        super.dispose();
    }

    @Override
    public void resize(Rect worldBounds)
    {
        background.resize(worldBounds);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button)
    {
        pos.set(touch);
        return false;
    }

    private void draw(SpriteBatch batch)
    {
        batch.begin();
        batch.draw(bg, 0, 0);
        batch.end();
    }
}