package ru.geekbrains.stargame.pool;

import ru.geekbrains.stargame.base.SpritesPool;
import ru.geekbrains.stargame.sprites.EnemyShip;

public class EnemyPool extends SpritesPool<EnemyShip>
{
   @Override
    protected EnemyShip newObject() {
       return new EnemyShip();
   }
}
