package ru.geekbrains.stargame.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.base.Sprite;
import ru.geekbrains.stargame.exception.GameException;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.pool.BulletPool;

public class Ship extends Sprite {

    private static final int INV_POINT = -1;

    private static final float animateInterval = 0.3f;
    private float animateTimer = 0.01f;

    private Rect worldBounds;
    private BulletPool bulletPool;
    private TextureRegion bulletRegion;
    private Vector2 bulletV;
    private final Vector2 v0;
    private final Vector2 v;
    private boolean pressLeft;
    private boolean pressRight;
    private int leftPoint = INV_POINT;
    private int rightPoint = INV_POINT;
    private Sound shootMus;

    public Ship(TextureAtlas atlas, BulletPool bulletPool) throws GameException {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        this.bulletPool = bulletPool;
        bulletRegion = atlas.findRegion("bulletMainShip");
        bulletV = new Vector2(0, 0.5f);
        v0 = new Vector2(0.5f, 0);
        v = new Vector2();
        shootMus = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
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
            case Input.Keys.UP:
                shoot();
                shootMus.play(0.1f);
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
        pos.mulAdd(v, delta);
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
        animateTimer += delta;
        if (animateTimer >= animateInterval) {
            shoot();
            shootMus.play(0.1f);
            animateTimer = 0;
        }
    }

    public void shoot()
    {
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, pos, bulletV, 0.01f, worldBounds, 1);
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
