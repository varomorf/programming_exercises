using System;
using System.Collections.Generic;

using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Audio;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;

namespace GameProject
{
    /// <summary>
    /// This is the main type for your game.
    /// </summary>
    public class Game1 : Game
    {
        GraphicsDeviceManager graphics;
        SpriteBatch spriteBatch;

        // game objects. Using inheritance would make this
        // easier, but inheritance isn't a GDD 1200 topic
        Burger burger;
        List<TeddyBear> bears = new List<TeddyBear>();
        static List<Projectile> projectiles = new List<Projectile>();
        List<Explosion> explosions = new List<Explosion>();

        // projectile and explosion sprites. Saved so they don't have to
        // be loaded every time projectiles or explosions are created
        static Texture2D frenchFriesSprite;
        static Texture2D teddyBearProjectileSprite;
        static Texture2D explosionSpriteStrip;

        // scoring support
        int score = 0;
        string scoreString = GameConstants.ScorePrefix + 0;

        // health support
        string healthString = GameConstants.HealthPrefix + GameConstants.BurgerInitialHealth;
        bool burgerDead = false;

        // text display support
        SpriteFont font;

        // sound effects
        SoundEffect burgerDamage;
        SoundEffect burgerDeath;
        SoundEffect burgerShot;
        SoundEffect explosion;
        SoundEffect teddyBounce;
        SoundEffect teddyShot;

        public Game1()
        {
            graphics = new GraphicsDeviceManager(this);
            Content.RootDirectory = "Content";

            // set resolution
            graphics.PreferredBackBufferWidth = GameConstants.WindowWidth;
            graphics.PreferredBackBufferHeight = GameConstants.WindowHeight;
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
            RandomNumberGenerator.Initialize();

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

            // load audio content
            burgerDamage = Content.Load<SoundEffect>(@"audio/BurgerDamage");
            burgerDeath = Content.Load<SoundEffect>(@"audio/BurgerDeath");
            burgerShot = Content.Load<SoundEffect>(@"audio/BurgerShot");
            explosion = Content.Load<SoundEffect>(@"audio/Explosion");
            teddyBounce = Content.Load<SoundEffect>(@"audio/TeddyBounce");
            teddyShot = Content.Load<SoundEffect>(@"audio/TeddyShot");

            // load sprite font
            font = Content.Load<SpriteFont>(@"fonts/Arial20");

            // load projectile and explosion sprites
            teddyBearProjectileSprite = Content.Load<Texture2D>(@"graphics/teddybearprojectile");
            frenchFriesSprite = Content.Load<Texture2D>(@"graphics/frenchfries");
            explosionSpriteStrip = Content.Load<Texture2D>(@"graphics/explosion");

            // add initial game objects

            // add burger object calculating x and y (no need to center sprite as Burger constructor does it)
            int burgerX = GameConstants.WindowWidth / 2;
            int burgerY = GameConstants.WindowHeight - (GameConstants.WindowHeight / 8);
            burger = new Burger(Content, @"graphics\burger", burgerX, burgerY, burgerShot);

            // spawn a bear
            for(int i = 0; i < GameConstants.MaxBears; i++)
            {
                SpawnBear();
            }

            // end adding initial game objects

            // set initial health and score strings
            burgerDamaged(0);
            updateScoreString();
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

            // get current keyboard state and update burger
            burger.Update(gameTime, Keyboard.GetState());

            // update other game objects
            foreach (TeddyBear bear in bears)
            {
                bear.Update(gameTime);
            }
            foreach (Projectile projectile in projectiles)
            {
                projectile.Update(gameTime);
            }
            foreach (Explosion explosion in explosions)
            {
                explosion.Update(gameTime);
            }

            // check and resolve collisions between teddy bears
            foreach(TeddyBear bear1 in bears)
            {
                foreach(TeddyBear bear2 in bears)
                {
                    if(bear1 != bear2 && bear1.Active && bear2.Active)
                    {
                        CollisionResolutionInfo collisionResolutionInfo = CollisionUtils.CheckCollision(gameTime.ElapsedGameTime.Milliseconds, GameConstants.WindowWidth, GameConstants.WindowHeight, bear1.Velocity, bear1.DrawRectangle, bear2.Velocity, bear2.DrawRectangle);
                        if(null != collisionResolutionInfo)
                        {
                            if (collisionResolutionInfo.FirstOutOfBounds)
                            {
                                bear1.Active = false;
                            } else
                            {
                                bear1.Velocity = collisionResolutionInfo.FirstVelocity;
                                bear1.DrawRectangle = collisionResolutionInfo.FirstDrawRectangle;
                                teddyBounce.Play();
                            }
                            if (collisionResolutionInfo.SecondOutOfBounds)
                            {
                                bear2.Active = false;
                            }
                            else
                            {
                                bear2.Velocity = collisionResolutionInfo.SecondVelocity;
                                bear2.DrawRectangle = collisionResolutionInfo.SecondDrawRectangle;
                                teddyBounce.Play();
                            }
                        }
                    }
                }
            }

            // check and resolve collisions between burger and teddy bears
            foreach(TeddyBear bear in bears)
            {
                if (burger.CollisionRectangle.Intersects(bear.CollisionRectangle))
                {
                    burgerDamaged(GameConstants.BearDamage);

                    explodeTeddy(bear);
                }
            }

            // check and resolve collisions between burger and projectiles
            foreach(Projectile projectile in projectiles)
            {
                if(projectile.Type == ProjectileType.TeddyBear && burger.CollisionRectangle.Intersects(projectile.CollisionRectangle))
                {
                    projectile.Active = false;

                    burgerDamaged(GameConstants.TeddyBearProjectileDamage);
                }
            }

            // check and resolve collisions between teddy bears and projectiles
            foreach(TeddyBear bear in bears)
            {
                if (bear.Active)
                {
                    foreach(Projectile projectile in projectiles)
                    {
                        if(projectile.Active && projectile.Type == ProjectileType.FrenchFries && bear.CollisionRectangle.Intersects(projectile.CollisionRectangle))
                        {
                            projectile.Active = false;
                            explodeTeddy(bear);
                            score += GameConstants.BearPoints;
                            updateScoreString();
                        }
                    }
                }
            }

            // clean out inactive teddy bears and add new ones as necessary
            for(int i = bears.Count - 1; i >= 0; i--)
            {
                if (!bears[i].Active)
                {
                    bears.RemoveAt(i);
                }
            }

            while(bears.Count < GameConstants.MaxBears)
            {
                SpawnBear();
            }

            // clean out inactive projectiles
            for (int i = projectiles.Count - 1; i >= 0; i--)
            {
                if (!projectiles[i].Active)
                {
                    projectiles.RemoveAt(i);
                }
            }

            // clean out finished explosions
            for (int i = explosions.Count - 1; i >= 0; i--)
            {
                if (explosions[i].Finished)
                {
                    explosions.RemoveAt(i);
                }
            }

            // check game end
            CheckBurgerKill();

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

            // draw game objects
            burger.Draw(spriteBatch);
            foreach (TeddyBear bear in bears)
            {
                bear.Draw(spriteBatch);
            }
            foreach (Projectile projectile in projectiles)
            {
                projectile.Draw(spriteBatch);
            }
            foreach (Explosion explosion in explosions)
            {
                explosion.Draw(spriteBatch);
            }

            // draw score and health
            spriteBatch.DrawString(font, healthString, GameConstants.HealthLocation, Color.White);
            spriteBatch.DrawString(font, scoreString, GameConstants.ScoreLocation, Color.White);

            spriteBatch.End();

            base.Draw(gameTime);
        }

        #region Public methods

        /// <summary>
        /// Gets the projectile sprite for the given projectile type
        /// </summary>
        /// <param name="type">the projectile type</param>
        /// <returns>the projectile sprite for the type</returns>
        public static Texture2D GetProjectileSprite(ProjectileType type)
        {
            if (type.Equals(ProjectileType.TeddyBear))
            {
                return teddyBearProjectileSprite;
            }
            // defaults to frenchFriesSprite
            return frenchFriesSprite;
        }

        /// <summary>
        /// Adds the given projectile to the game
        /// </summary>
        /// <param name="projectile">the projectile to add</param>
        public static void AddProjectile(Projectile projectile)
        {
            projectiles.Add(projectile);
        }

        #endregion

        #region Private methods

        /// <summary>
        /// Spawns a new teddy bear at a random location
        /// </summary>
        private void SpawnBear()
        {
            // generate random location
            int bearX = GetRandomLocation(GameConstants.SpawnBorderSize, GameConstants.WindowWidth - GameConstants.SpawnBorderSize);
            int bearY = GetRandomLocation(GameConstants.SpawnBorderSize, GameConstants.WindowHeight - GameConstants.SpawnBorderSize);

            // generate random velocity
            float velMagnitude = RandomNumberGenerator.NextFloat(GameConstants.BearSpeedRange);
            if(velMagnitude < GameConstants.MinBearSpeed)
            {
                velMagnitude = GameConstants.MinBearSpeed;
            }
            float velAngle = RandomNumberGenerator.NextFloat(2 * (float)Math.PI);
            Vector2 bearVelocity = new Vector2((float)(velMagnitude * Math.Cos((double)velAngle)), (float)(velMagnitude * Math.Sin((double)velAngle)));

            // create new bear
            TeddyBear newBear = new TeddyBear(Content, @"graphics\teddyBear", bearX, bearY, bearVelocity, teddyBounce, teddyShot);

            // make sure we don't spawn into a collision
            List<Rectangle> collisionRectangles = GetCollisionRectangles();
            while(CollisionUtils.IsCollisionFree(newBear.DrawRectangle, collisionRectangles))
            {
                bearX = GetRandomLocation(GameConstants.SpawnBorderSize, GameConstants.WindowWidth - GameConstants.SpawnBorderSize);
                bearY = GetRandomLocation(GameConstants.SpawnBorderSize, GameConstants.WindowHeight - GameConstants.SpawnBorderSize);

                newBear.X = bearX;
                newBear.Y = bearY;
            }

            // add new bear to list
            bears.Add(newBear);
        }

        /// <summary>
        /// Gets a random location using the given min and range
        /// </summary>
        /// <param name="min">the minimum</param>
        /// <param name="range">the range</param>
        /// <returns>the random location</returns>
        private int GetRandomLocation(int min, int range)
        {
            return min + RandomNumberGenerator.Next(range);
        }

        /// <summary>
        /// Gets a list of collision rectangles for all the objects in the game world
        /// </summary>
        /// <returns>the list of collision rectangles</returns>
        private List<Rectangle> GetCollisionRectangles()
        {
            List<Rectangle> collisionRectangles = new List<Rectangle>();
            collisionRectangles.Add(burger.CollisionRectangle);
            foreach (TeddyBear bear in bears)
            {
                collisionRectangles.Add(bear.CollisionRectangle);
            }
            foreach (Projectile projectile in projectiles)
            {
                collisionRectangles.Add(projectile.CollisionRectangle);
            }
            foreach (Explosion explosion in explosions)
            {
                collisionRectangles.Add(explosion.CollisionRectangle);
            }
            return collisionRectangles;
        }

        /// <summary>
        /// Checks to see if the burger has just been killed
        /// </summary>
        private void CheckBurgerKill()
        {
            if(burger.Health <= 0 && !burgerDead)
            {
                burgerDead = true;
                burgerDeath.Play();
            }
        }

        /// <summary>
        /// Explodes a teddy bear. Sets bear to inactive, adds explosion on teddy bear location.
        /// </summary>
        /// <param name="bear">The bear to explode.</param>
        private void explodeTeddy(TeddyBear bear)
        {
            bear.Active = false;
            Point bearCenter = bear.CollisionRectangle.Center;
            Explosion explosion = new Explosion(explosionSpriteStrip, bearCenter.X, bearCenter.Y, this.explosion);
            explosions.Add(explosion);
        }

        /// <summary>
        /// Updates the health string after damaging.
        /// </summary>
        /// <param name="amount">The amount of damage to the burger.</param>
        private void burgerDamaged(int amount)
        {
            burger.Health -= amount;
            healthString = GameConstants.HealthPrefix + burger.Health;
            burgerDamage.Play();
        }

        /// <summary>
        /// Updates the score string.
        /// </summary>
        private void updateScoreString()
        {
            scoreString = GameConstants.ScorePrefix + score;
        }

        #endregion
    }
}
