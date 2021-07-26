package ru.geekbrains.stargame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ru.geekbrains.stargame.base.Logo;
import ru.geekbrains.stargame.screen.MenuScreen;

public class StarGame extends Game
{
	MenuScreen menuScreen;
	SpriteBatch batch;
	Logo logo;

	@Override
	public void create()
	{
		menuScreen = new MenuScreen();
		batch = new SpriteBatch();
		logo = new Logo();
		logo.show();
	}
	@Override
	public void render()
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		menuScreen.render(batch);
		logo.render(batch);
	}
}