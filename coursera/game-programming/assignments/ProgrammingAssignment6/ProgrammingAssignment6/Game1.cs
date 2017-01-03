using System.Collections.Generic;

using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;

using XnaCards;

namespace ProgrammingAssignment6
{
    /// <summary>
    /// This is the main type for your game.
    /// </summary>
    public class Game1 : Game
    {
        GraphicsDeviceManager graphics;
        SpriteBatch spriteBatch;

        const int WindowWidth = 800;
        const int WindowHeight = 600;

        // max valid blockjuck score for a hand
        const int MaxHandValue = 21;
        const int MinDealerHittinValue = 16;

        // deck and hands
        Deck deck;
        List<Card> dealerHand = new List<Card>();
        List<Card> playerHand = new List<Card>();

        // hand placement
        const int TopCardOffset = 100;
        const int HorizontalCardOffset = 150;
        const int VerticalCardSpacing = 125;

        // messages
        SpriteFont messageFont;
        const string ScoreMessagePrefix = "Score: ";
        Message playerScoreMessage;
        Message dealerScoreMessage;
        Message winnerMessage;
        List<Message> messages = new List<Message>();
        const string dealerWonMessage = "Dealer Won!";
        const string playerWonMessage = "Player Won!";
        const string tieMessage = "Tie!";

        // message placement
        const int ScoreMessageTopOffset = 25;
        const int HorizontalMessageOffset = HorizontalCardOffset;
        Vector2 winnerMessageLocation = new Vector2(WindowWidth / 2,
            WindowHeight / 2);

        // menu buttons
        Texture2D quitButtonSprite;
        Texture2D hitButtonSprite;
        MenuButton hitButton;
        Texture2D standButtonSprite;
        MenuButton standButton;
        List<MenuButton> menuButtons = new List<MenuButton>();

        // menu button placement
        const int TopMenuButtonOffset = TopCardOffset;
        const int QuitMenuButtonOffset = WindowHeight - TopCardOffset;
        const int HorizontalMenuButtonOffset = WindowWidth / 2;
        const int VerticalMenuButtonSpacing = 125;

        // use to detect hand over when player and dealer didn't hit
        bool playerHit = false;
        bool dealerHit = false;

        // game state tracking
        static GameState currentState = GameState.WaitingForPlayer;

        public Game1()
        {
            graphics = new GraphicsDeviceManager(this);
            Content.RootDirectory = "Content";

            // set resolution and show mouse
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

            // create and shuffle deck
            deck = new Deck(Content, 0, 0);
            deck.Shuffle();

            // first player card
            addCardToPlayer();

            // first dealer card
            Card card = deck.TakeTopCard();
            card.X = WindowWidth - HorizontalCardOffset;
            card.Y = TopCardOffset;
            dealerHand.Add(card);

            // second player card
            addCardToPlayer();

            // second dealer card
            addCardToDealer();

            // load sprite font, create message for player score and add to list
            messageFont = Content.Load<SpriteFont>(@"fonts\Arial24");
            playerScoreMessage = new Message(ScoreMessagePrefix + GetBlockjuckScore(playerHand).ToString(),
                messageFont,
                new Vector2(HorizontalMessageOffset, ScoreMessageTopOffset));
            messages.Add(playerScoreMessage);

            // load quit button sprite for later use
            quitButtonSprite = Content.Load<Texture2D>("quitbutton");

            // create hit button and add to list
            hitButtonSprite = Content.Load<Texture2D>("hitbutton");
            hitButton = new MenuButton(hitButtonSprite, new Vector2(HorizontalMenuButtonOffset, TopMenuButtonOffset), GameState.PlayerHitting);
            menuButtons.Add(hitButton);

            // create stand button and add to list
            standButtonSprite = Content.Load<Texture2D>("standbutton");
            standButton = new MenuButton(standButtonSprite, new Vector2(HorizontalMenuButtonOffset, TopMenuButtonOffset + VerticalMenuButtonSpacing), GameState.WaitingForDealer);
            menuButtons.Add(standButton);
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

            // update menu buttons as appropriate
            if (currentState.Equals(GameState.WaitingForPlayer) || currentState.Equals(GameState.DisplayingHandResults))
            {
                foreach (MenuButton menuButton in menuButtons)
                {
                    menuButton.Update(mouse);
                }
            }

            // game state-specific processing
            switch (currentState)
            {
                case GameState.PlayerHitting:
                    addCardToPlayer();
                    playerScoreMessage.Text = ScoreMessagePrefix + GetBlockjuckScore(playerHand).ToString();
                    playerHit = true;
                    ChangeState(GameState.WaitingForDealer);
                    break;

                case GameState.WaitingForDealer:
                    int dealerScore = GetBlockjuckScore(dealerHand);
                    if (dealerScore <= MinDealerHittinValue)
                    {
                        ChangeState(GameState.DealerHitting);
                    }
                    else
                    {
                        ChangeState(GameState.CheckingHandOver);
                    }
                    break;

                case GameState.DealerHitting:
                    addCardToDealer();
                    dealerHit = true;
                    ChangeState(GameState.CheckingHandOver);
                    break;

                case GameState.CheckingHandOver:
                    int playerScore = GetBlockjuckScore(playerHand);
                    dealerScore = GetBlockjuckScore(dealerHand);

                    if (playerScore > MaxHandValue || dealerScore > MaxHandValue)
                    {
                        // either one o both busted
                        if(playerScore == dealerScore)
                        {
                            tie();
                        }
                        if (playerScore > MaxHandValue)
                        {
                            dealerWon();
                        }
                        else
                        {
                            playerWon();
                        }

                        displayWinnerMessage();
                        ChangeState(GameState.DisplayingHandResults);
                    }
                    else
                    {
                        if (playerHit || dealerHit)
                        {
                            // either one of them hit -> new turn -> reset flags
                            playerHit = false;
                            dealerHit = false;

                            ChangeState(GameState.WaitingForPlayer);
                        }
                        else
                        {
                            if (playerScore == dealerScore)
                            {
                                tie();
                            }
                            else if (playerScore > dealerScore)
                            {
                                playerWon();
                            }
                            else
                            {
                                dealerWon();
                            }

                            displayWinnerMessage();
                            ChangeState(GameState.DisplayingHandResults);
                        }

                    }
                    break;

                case GameState.Exiting:
                    Exit();
                    break;
            }


            base.Update(gameTime);
        }

        /// <summary>
        /// This is called when the game should draw itself.
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        protected override void Draw(GameTime gameTime)
        {
            GraphicsDevice.Clear(Color.Goldenrod);

            spriteBatch.Begin();

            // draw hands
            foreach (Card card in playerHand)
            {
                card.Draw(spriteBatch);
            }

            foreach (Card card in dealerHand)
            {
                card.Draw(spriteBatch);
            }

            // draw messages
            foreach (Message message in messages)
            {
                message.Draw(spriteBatch);
            }

            // draw menu buttons
            foreach (MenuButton menuButton in menuButtons)
            {
                menuButton.Draw(spriteBatch);
            }

            spriteBatch.End();

            base.Draw(gameTime);
        }

        /// <summary>
        /// Calculates the Blockjuck score for the given hand
        /// </summary>
        /// <param name="hand">the hand</param>
        /// <returns>the Blockjuck score for the hand</returns>
        private int GetBlockjuckScore(List<Card> hand)
        {
            // add up score excluding Aces
            int numAces = 0;
            int score = 0;
            foreach (Card card in hand)
            {
                if (card.Rank != Rank.Ace)
                {
                    score += GetBlockjuckCardValue(card);
                }
                else
                {
                    numAces++;
                }
            }

            // if more than one ace, only one should ever be counted as 11
            if (numAces > 1)
            {
                // make all but the first ace count as 1
                score += numAces - 1;
                numAces = 1;
            }

            // if there's an Ace, score it the best way possible
            if (numAces > 0)
            {
                if (score + 11 <= MaxHandValue)
                {
                    // counting Ace as 11 doesn't bust
                    score += 11;
                }
                else
                {
                    // count Ace as 1
                    score++;
                }
            }

            return score;
        }

        /// <summary>
        /// Gets the Blockjuck value for the given card
        /// </summary>
        /// <param name="card">the card</param>
        /// <returns>the Blockjuck value for the card</returns>
        private int GetBlockjuckCardValue(Card card)
        {
            switch (card.Rank)
            {
                case Rank.Ace:
                    return 11;
                case Rank.King:
                case Rank.Queen:
                case Rank.Jack:
                case Rank.Ten:
                    return 10;
                case Rank.Nine:
                    return 9;
                case Rank.Eight:
                    return 8;
                case Rank.Seven:
                    return 7;
                case Rank.Six:
                    return 6;
                case Rank.Five:
                    return 5;
                case Rank.Four:
                    return 4;
                case Rank.Three:
                    return 3;
                case Rank.Two:
                    return 2;
                default:
                    return 0;
            }
        }

        /// <summary>
        /// Adds a card to the player.
        /// </summary>
        private void addCardToPlayer()
        {
            addCardToHand(HorizontalCardOffset, playerHand);
        }

        /// <summary>
        /// Adds a flipped over card for the dealer.
        /// </summary>
        private void addCardToDealer()
        {
            addCardToHand(WindowWidth - HorizontalCardOffset, dealerHand);
        }

        /// <summary>
        /// Adds a flipped over card to a hand in an X position.
        /// </summary>
        /// <param name="x">The X position.</param>
        /// <param name="hand">The hand in which to add the card.</param>
        private void addCardToHand(int x, List<Card> hand)
        {
            Card card = deck.TakeTopCard();
            card.FlipOver();
            card.X = x;
            card.Y = TopCardOffset + (VerticalCardSpacing * hand.Count);
            hand.Add(card);
        }

        /// <summary>
        /// Adds the player won message.
        /// </summary>
        private void playerWon()
        {
            winnerMessage = new Message(playerWonMessage, messageFont, winnerMessageLocation);
        }

        /// <summary>
        /// Adds the dealer won message.
        /// </summary>
        private void dealerWon()
        {
            winnerMessage = new Message(dealerWonMessage, messageFont, winnerMessageLocation);
        }

        /// <summary>
        /// Adds the tie message.
        /// </summary>
        private void tie()
        {
            winnerMessage = new Message(tieMessage, messageFont, winnerMessageLocation);
        }

        /// <summary>
        /// Displays the result.
        /// </summary>
        private void displayWinnerMessage()
        {
            // prepare dealer score message 
            int dealerScore = GetBlockjuckScore(dealerHand);
            Message dealerScoreMessage = new Message(ScoreMessagePrefix + dealerScore, messageFont,
                new Vector2(WindowWidth - HorizontalMessageOffset, ScoreMessageTopOffset));
            messages.Add(dealerScoreMessage);

            // flip dealer card
            dealerHand[0].FlipOver();

            // add winner message
            messages.Add(winnerMessage);

            // remove playing buttons
            menuButtons.Remove(hitButton);
            menuButtons.Remove(standButton);

            // add quiting button
            menuButtons.Add(new MenuButton(quitButtonSprite, new Vector2(HorizontalMenuButtonOffset, WindowHeight - TopMenuButtonOffset), GameState.Exiting));
        }

        /// <summary>
        /// Changes the state of the game
        /// </summary>
        /// <param name="newState">the new game state</param>
        public static void ChangeState(GameState newState)
        {
            currentState = newState;
        }
    }
}
