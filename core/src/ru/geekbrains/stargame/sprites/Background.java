package ru.geekbrains.stargame.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.geekbrains.stargame.base.Sprite;
import ru.geekbrains.stargame.exception.GameException;
import ru.geekbrains.stargame.math.Rect;

public class Background extends Sprite {

    public Background(Texture texture) throws GameException {
        super(new TextureRegion(texture));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(1f);
        pos.set(worldBounds.pos);
    }
}
