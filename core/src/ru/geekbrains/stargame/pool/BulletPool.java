package ru.geekbrains.stargame.pool;

import ru.geekbrains.stargame.base.SpritesPool;
import ru.geekbrains.stargame.sprites.Bullet;

public class BulletPool extends SpritesPool<Bullet>
{
    @Override
    protected Bullet newObject()
    {
        return new Bullet();
    }
}
