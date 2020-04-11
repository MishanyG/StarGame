package ru.geekbrains.stargame.sprites;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.stargame.base.ScaledButton;
import ru.geekbrains.stargame.exception.GameException;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.screen.GameScreen;

public class ButtonNewGame extends ScaledButton
{
    private final Game game;
    public ButtonNewGame(TextureAtlas atlas, Game game) throws GameException
    {
        super(atlas.findRegion("button_new_game"));
        this.game = game;
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
        game.setScreen(new GameScreen(game));
    }
}
