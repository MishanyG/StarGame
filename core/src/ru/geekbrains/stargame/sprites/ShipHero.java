package ru.geekbrains.stargame.sprites;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.base.Ships;
import ru.geekbrains.stargame.exception.GameException;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.pool.BulletPool;
import ru.geekbrains.stargame.pool.ExplosionPool;

public class ShipHero extends Ships
{
    private static final int HP = 100;

    private static final int INV_POINT = -1;

    private boolean pressLeft;
    private boolean pressRight;
    private int     leftPoint   = INV_POINT;
    private int     rightPoint  = INV_POINT;

    public ShipHero(TextureAtlas atlas, BulletPool bulletPool, ExplosionPool explosionPool, Sound shootSound) throws GameException {
        super(atlas.findRegion("shipHero"), 1, 2, 2);
        this.bulletPool     = bulletPool;
        this.explosionPool  = explosionPool;
        this.shootSound     = shootSound;
            bulletRegion    = atlas.findRegion("bullet1");
            bulletV         = new Vector2(0, 0.5f);
            bulletPos       = new Vector2();
            v0              = new Vector2(0.5f, 0);
            v               = new Vector2();
            bulletHeight    = 0.01f;
            damage          = 1;
            hp              = HP;
            animateInterval = 0.2f;
            animateTimer    = animateInterval;
    }

    public void startNewGame(Rect worldBounds)
    {
        flushDestroy();
        hp = HP;
        pressLeft = false;
        pressRight = false;
        leftPoint = INV_POINT;
        rightPoint = INV_POINT;
        stop();
        pos.x = worldBounds.pos.x;
    }

    @Override
    public void resize(Rect worldBounds)
    {
        this.worldBounds = worldBounds;
        setHeightProportion(0.15f);
        setBottom(worldBounds.getBottom() + 0.01f);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button)
    {
        if (touch.x < worldBounds.pos.x)
        {
            if (leftPoint != INV_POINT)
            {
                return false;
            }
            leftPoint = pointer;
            moveLeft();
        }
        else
        {
            if (rightPoint != INV_POINT)
            {
                return false;
            }
            rightPoint = pointer;
            moveRight();
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button)
    {
        if (pointer == leftPoint)
        {
            leftPoint = INV_POINT;
            if (rightPoint != INV_POINT)
            {
                moveRight();
            }
            else
            {
                stop();
            }
        }
        else
        {
            if (pointer == rightPoint)
            {
                rightPoint = INV_POINT;
                if (leftPoint != INV_POINT)
                {
                    moveLeft();
                }
                else
                {
                    stop();
                }
            }
        }
        return false;
    }

    public boolean keyDown(int keycode)
    {
        switch (keycode)
        {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressLeft = true;
                moveLeft();
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressRight = true;
                moveRight();
                break;
        }
        return false;
    }

    public boolean keyUp(int keycode)
    {
        switch (keycode)
        {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressLeft = false;
                if (pressLeft)
                {
                    moveLeft();
                }
                else
                {
                    stop();
                }
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressRight = false;
                if (pressRight)
                {
                    moveRight();
                }
                else
                {
                    stop();
                }
                break;
        }
        return false;
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);
        bulletPos.set(pos.x, pos.y + getHalfHeight());
        pos.mulAdd(v, delta);
        autoShoot(delta);
        if (getLeft() < worldBounds.getLeft())
        {
            setLeft(worldBounds.getLeft());
            stop();
        }
        if (getRight() > worldBounds.getRight())
        {
            setRight(worldBounds.getRight());
            stop();
        }
    }

    public boolean isBulletCollision(Rect bullet)
    {
        return !(bullet.getRight() < getLeft() || bullet.getLeft() > getRight() || bullet.getBottom() > pos.y || bullet.getTop() < getBottom());
    }

    private void moveRight ()
    {
        v.set(v0);
    }
    private void moveLeft ()
    {
        v.set(v0).rotate(180);
    }
    private void stop ()
    {
        v.setZero();
    }
}
