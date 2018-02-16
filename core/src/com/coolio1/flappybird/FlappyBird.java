package com.coolio1.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] birds;
	Texture topTube;
	Texture bottomTube;
	Texture gameOver;
	int flapState;
	int birdY;
	int velocityY = 0;
	int jumpY = 20;
	double gravity = 1.5;
	int gamestate = 0;
	float gap = 400;
	float maxTubeOffset;
	float tubeVelocity = 4;
	int score = 0;
	int scoringTube = 0;

    int numberOfTubes = 4;
	float tubeX[] = new float[numberOfTubes];
	float tubeOffset[] = new float[numberOfTubes];
	float distanceBetweenTubes;

	Circle birdCircle;
	Rectangle topTubeRectangles[] = new Rectangle[numberOfTubes];
	Rectangle bottomTubeRectangles[] = new Rectangle[numberOfTubes];
	ShapeRenderer shapeRenderer;
	Random random;

	BitmapFont font;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		gameOver = new Texture("gameover.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
        flapState = 0;
        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");
        distanceBetweenTubes = Gdx.graphics.getWidth() * 3 / 4;

        maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
		random = new Random();
		birdCircle = new Circle();
		shapeRenderer = new ShapeRenderer();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

       startGame();


	}
	public void startGame()
	{
		birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() /2;

		for(int x = 0;x < numberOfTubes;x++)
		{
			tubeOffset[x] = (random.nextFloat() - 0.5f) * (maxTubeOffset * 2);
			tubeX[x] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() +  x * distanceBetweenTubes;
			topTubeRectangles[x] = new Rectangle();
			bottomTubeRectangles[x] = new Rectangle();
		}
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if(gamestate == 1) {

			if(tubeX[scoringTube] < (Gdx.graphics.getWidth() / 2))
			{
				score++;
				if(scoringTube < numberOfTubes - 1)
					scoringTube++;
				else scoringTube = 0;

			}

			if (Gdx.input.justTouched()) {

				velocityY -= jumpY;


			}
			for(int x = 0;x < numberOfTubes;x++) {

				if(tubeX[x] < -topTube.getWidth())
				{
					tubeOffset[x] = (random.nextFloat() - 0.5f) * (maxTubeOffset * 2);
					tubeX[x] += numberOfTubes * distanceBetweenTubes;

				}
				else
				tubeX[x] -= tubeVelocity;

				batch.draw(topTube, tubeX[x], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[x]);
				batch.draw(bottomTube, tubeX[x], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[x]);
				topTubeRectangles[x] = new Rectangle(tubeX[x],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[x], topTube.getWidth(), topTube.getHeight());
				bottomTubeRectangles[x] = new Rectangle(tubeX[x],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[x], bottomTube.getWidth(), bottomTube.getHeight() );

			}
			if(birdY > 0 ) {
				velocityY += gravity;
				birdY -= velocityY;
			}
			else gamestate = 2;
		}
		else if(gamestate == 0)
		{
			if(Gdx.input.justTouched())
			{
				gamestate = 1;

			}
		}
		else
		{
			batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2 , Gdx.graphics.getHeight() / 2 + gameOver.getHeight() / 2);
			if(Gdx.input.justTouched())
			{
				gamestate = 1;
				startGame();
				scoringTube = 0;
				score = 0;
				velocityY = 0;
			}
		}
		if(flapState == 0) flapState = 1;
		else flapState = 0;

		batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);
		font.draw(batch, String.valueOf(score), 100, 200);

		batch.end();

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);

		birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[flapState].getHeight() / 2, birds[flapState].getWidth() / 2);
		//shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);
		for(int x = 0;x < numberOfTubes;x++)
		{
			//shapeRenderer.rect(tubeX[x],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[x], topTube.getWidth(), topTube.getHeight());
			//shapeRenderer.rect(tubeX[x],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[x], bottomTube.getWidth(), bottomTube.getHeight());
			if(Intersector.overlaps(birdCircle, topTubeRectangles[x]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[x]))
			{
             gamestate = 2;
			}
		}
		shapeRenderer.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
