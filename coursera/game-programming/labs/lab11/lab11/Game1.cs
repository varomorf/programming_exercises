using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;

using ExplodingTeddies;
using System;

namespace lab11
{
    /// <summary>
    /// This is the main type for your game.
    /// </summary>
    public class Game1 : Game
    {
        public const int WindowWidth = 800;
        public const int WindowHeight = 600;

        Random rand = new Random();

        GraphicsDeviceManager graphics;
        SpriteBatch spriteBatch;

        TeddyBear teddyBear;
        Explosion explosion;

        public Game1()
        {
            graphics = new GraphicsDeviceManager(this);
            Content.RootDirectory = "Content";

            graphics.PreferredBackBufferWidth = WindowWidth;
            graphics.PreferredBackBufferHeight = WindowHeight;

            IsMouseVisible = true;
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

            // load content for teddy bear and explosion
            Vector2 velocity = new Vector2((float)rand.NextDouble(), (float)rand.NextDouble());
            velocity *= 0.2f;
            teddyBear = new TeddyBear(Content, WindowWidth, WindowHeight, "teddybear", WindowWidth/2, WindowHeight/2, velocity);
            explosion = new Explosion(Content, "explosion");
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

            MouseState mouse = Mouse.GetState();
            if (teddyBear.DrawRectangle.Contains(mouse.Position) && mouse.LeftButton == ButtonState.Pressed){
                teddyBear.Active = false;
                Point location = teddyBear.DrawRectangle.Center;
                explosion.Play(location.X, location.Y);

                Vector2 velocity = new Vector2((float)rand.NextDouble(), (float)rand.NextDouble());
                velocity *= 0.2f;
                teddyBear = new TeddyBear(Content, WindowWidth, WindowHeight, "teddybear", rand.Next(WindowWidth), rand.Next(WindowHeight), velocity);
            }

            teddyBear.Update(gameTime);
            explosion.Update(gameTime);

            base.Update(gameTime);
        }

        /// <summary>
        /// This is called when the game should draw itself.
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        protected override void Draw(GameTime gameTime)
        {
            GraphicsDevice.Clear(Color.CornflowerBlue);

            spriteBatch.Begin();
            teddyBear.Draw(spriteBatch);
            explosion.Draw(spriteBatch);
            spriteBatch.End();

            base.Draw(gameTime);
        }
    }
}
