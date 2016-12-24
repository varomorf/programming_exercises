using System.Collections.Generic;

using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;
using TeddyMineExplosion;
using System;

namespace Programming_Assignment_5
{
    /// <summary>
    /// This is the main type for your game.
    /// </summary>
    public class Game1 : Game
    {
        GraphicsDeviceManager graphics;
        SpriteBatch spriteBatch;

        Random rand;

        const int WindowWidth = 800;
        const int WindowHeight = 600;

        const int minMillisForTeddySpawn = 1000;
        const int maxMillisForTeddySpawn = 3000;
        const float teddyVelocity = 0.5f;

        Texture2D mineSprite;
        Texture2D teddySprite;
        Texture2D explosionSprite;

        List<Mine> mines = new List<Mine>();
        List<TeddyBear> teddys = new List<TeddyBear>();
        List<Explosion> explosions = new List<Explosion>();

        bool mineAdded = false;

        int millisUntilNewTeddy = 0;

        public Game1()
        {
            graphics = new GraphicsDeviceManager(this);
            Content.RootDirectory = "Content";

            // set resolution
            graphics.PreferredBackBufferWidth = WindowWidth;
            graphics.PreferredBackBufferHeight = WindowHeight;
            IsMouseVisible = true;

            rand = new Random();

            millisUntilNewTeddy = rand.Next(minMillisForTeddySpawn, maxMillisForTeddySpawn + 1);
        }

        /// <summary>
        /// Allows the game to perform any initialization it needs to before starting to run.
        /// This is where it can query for any required services and load any non-graphic
        /// related content.  Calling base.Initialize will enumerate through any components
        /// and initialize them as well.
        /// </summary>
        protected override void Initialize()
        {
            // TODO: Add your initialization logic here

            base.Initialize();
        }

        /// <summary>
        /// LoadContent will be called once per game and is the place to load
        /// all of your content.
        /// </summary>
        protected override void LoadContent()
        {
            // Create a new SpriteBatch, which can be used to draw textures.
            spriteBatch = new SpriteBatch(GraphicsDevice);

            // TODO: use this.Content to load your game content here
            mineSprite = Content.Load<Texture2D>("mine");
            teddySprite = Content.Load<Texture2D>("teddybear");
            explosionSprite = Content.Load<Texture2D>("explosion");
        }

        /// <summary>
        /// UnloadContent will be called once per game and is the place to unload
        /// game-specific content.
        /// </summary>
        protected override void UnloadContent()
        {
            // TODO: Unload any non ContentManager content here
        }

        /// <summary>
        /// Allows the game to run logic such as updating the world,
        /// checking for collisions, gathering input, and playing audio.
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        protected override void Update(GameTime gameTime)
        {
            if (GamePad.GetState(PlayerIndex.One).Buttons.Back == ButtonState.Pressed || Keyboard.GetState().IsKeyDown(Keys.Escape))
                Exit();

            // get current mouse state and add mine if necessary
            MouseState mouse = Mouse.GetState();
            if (!mineAdded && mouse.LeftButton == ButtonState.Pressed)
            {
                mines.Add(new Mine(mineSprite, mouse.X, mouse.Y));

                // no more mines until mouse is released
                mineAdded = true;
            }

            // reset flag to allow adding more mines
            if (mineAdded && mouse.LeftButton == ButtonState.Released)
            {
                mineAdded = false;
            }

            // spawn a teddy bear if time is up
            if (millisUntilNewTeddy <= 0)
            {
                // time is up -> spawn new teddy
                teddys.Add(new TeddyBear(teddySprite, generateNewRandomTeddyVelocity(), WindowWidth, WindowHeight));

                // get new millis for teddy bear spawning
                millisUntilNewTeddy = generateNewRandomMillisForTeddy();
            }

            // decrease timer
            millisUntilNewTeddy -= gameTime.ElapsedGameTime.Milliseconds;

            // update teddys
            for (int i = teddys.Count - 1; i >= 0; i--)
            {
                TeddyBear teddy = teddys[i];

                // if teddy is inactive -> remove teddy
                if (!teddy.Active)
                {
                    teddys.Remove(teddy);
                    continue;
                }

                // update the teddy
                teddy.Update(gameTime);

                // check collision
                for (int j = mines.Count - 1; j >= 0; j--)
                {
                    Mine mine = mines[j];

                    // only check active mines
                    if (mine.Active)
                    {
                        if (teddy.CollisionRectangle.Intersects(mine.CollisionRectangle))
                        {
                            // add explosion on mine center
                            Point center = mine.CollisionRectangle.Center;
                            explosions.Add(new Explosion(explosionSprite, center.X, center.Y));

                            // deactivate teddy and mine
                            teddy.Active = false;
                            mine.Active = false;
                        }
                    }
                    else
                    {
                        // remove inactive mine
                        mines.Remove(mine);
                    }
                }
            }

            // update explosions
            for (int i = explosions.Count - 1; i >= 0; i--)
            {
                Explosion explosion = explosions[i];

                if (explosion.Playing)
                {
                    // update active explosion
                    explosion.Update(gameTime);
                }
                else
                {
                    // update inactive explosion
                    explosions.Remove(explosion);
                }
            }


            base.Update(gameTime);
        }

        /// <summary>
        /// This is called when the game should draw itself.
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        protected override void Draw(GameTime gameTime)
        {
            // brown because you plan mines on soil, not on cornflowers DUH :P
            GraphicsDevice.Clear(Color.SaddleBrown);

            spriteBatch.Begin();

            // draw mines
            foreach (Mine mine in mines)
            {
                mine.Draw(spriteBatch);
            }

            // draw teddys
            foreach (TeddyBear teddy in teddys)
            {
                teddy.Draw(spriteBatch);
            }

            // updatedraw explosions
            foreach (Explosion explosion in explosions)
            {
                explosion.Draw(spriteBatch);
            }

            spriteBatch.End();

            base.Draw(gameTime);
        }

        /// <summary>
        /// Generates a new number of millis (inside limits) for the new teddy to appear.
        /// </summary>
        /// <returns>The new millis</returns>
        protected int generateNewRandomMillisForTeddy()
        {
            return rand.Next(minMillisForTeddySpawn, maxMillisForTeddySpawn + 1);
        }


        /// <summary>
        /// Generates a new vector with random values for a teddy velocity (inside limits).
        /// </summary>
        /// <returns>The new velocity vector</returns>
        protected Vector2 generateNewRandomTeddyVelocity()
        {
            // from 0.0 to 1.0 minus 0.5 => from -0.5 to 0.5
            float x = (float)rand.NextDouble() - teddyVelocity;
            float y = (float)rand.NextDouble() - teddyVelocity;

            return new Vector2(x, y);
        }
    }
}
