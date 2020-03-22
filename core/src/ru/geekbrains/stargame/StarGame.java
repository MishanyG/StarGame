package ru.geekbrains.stargame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.awt.Image;

import javax.swing.text.html.ImageView;

import static com.badlogic.gdx.Input.Keys.R;

public class StarGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	//TextureRegion region;

	@Override
	public void create() {
		batch = new SpriteBatch();
		img = new Texture("sky.jpg"); // Выбираем изображение для фона
		//region = new TextureRegion(img, 10, 10, 150, 150);
	}

	@Override
	public void render()
	{
		//Gdx.gl.glClearColor(0.5f, 0.7f, 0.8f, 1);
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		//batch.setColor(1, 1, 1, 1);
		batch.draw(img, 0, 0);  // Отрисовка фона
		//batch.setColor(0.1f, 0.3f, 0.7f, 0.4f);
		//batch.draw(region, 200, 200);
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		img.dispose();
	}
}