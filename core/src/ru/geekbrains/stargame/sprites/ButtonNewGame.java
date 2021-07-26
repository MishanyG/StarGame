package ru.geekbrains.stargame.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.stargame.base.ScaledButton;
import ru.geekbrains.stargame.exception.GameException;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.screen.GameScreen;

public class ButtonNewGame extends ScaledButton
{
    private static final float MAX_SCALE = 1.05f;
    private static final float MIN_SCALE = 1f;
    private static final float ANIMATE_INTERVAL = 0.05f;

    private final GameScreen gameScreen;
    private boolean isGrow;
    private float animateTimer;

    public ButtonNewGame(TextureAtlas atlas, GameScreen gameScreen) throws GameException
    {
        super(atlas.findRegion("button_new_game"));
        this.gameScreen = gameScreen;
        this.isGrow = true;
    }

    @Override
    public void update(float delta)
    {
        animateTimer += delta;
        if (animateTimer < ANIMATE_INTERVAL)
            return;
        animateTimer = 0f;
        if (isGrow)
        {
            scale += delta;
            if (scale >= MAX_SCALE)
            {
                scale = MAX_SCALE;
                isGrow = false;
            }
        }
        else
        {
            scale -= delta;
            if (scale <= MIN_SCALE)
            {
                scale = MIN_SCALE;
                isGrow = true;
            }
        }

    }

    @Override
    public void resize(Rect worldBounds)
    {
        setHeightProportion(0.07f);
        setTop(-0.025f);
    }

    @Override
    public void action()
    {
        gameScreen.startNewGame();
    }
}
