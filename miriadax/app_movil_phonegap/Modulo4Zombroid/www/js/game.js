function inicio(){
    var width = document.documentElement.clientWidth;
    var height = document.documentElement.clientHeight;

    console.log('*********************************************************');
    console.log('Start app with width: ' + width + ' and height: ' + height);
    console.log('*********************************************************');
    
    var game = new Phaser.Game(width, height, Phaser.CANVAS, 'game', { preload: preload, create: create, update: update });

    var ZOMBIE_DEFAULT_TIMER_LIMIT = 1000;
    var ZOMBIE_SPAWN_TIMER_INC = 1000;

    var ZOMBIE_MIN_SPEED_INITIAL = 20;
    var ZOMBIE_MAX_SPEED_INITIAL = 100;

    var MAX_COLLISIONS = 3;

    var timeCounter = 0;
    var timeText;
    var hearts;

    var zombies;
    var ball = null;

    var zombieSpawnTimer = 0;
    var zombieSpawnTimerLimit = ZOMBIE_DEFAULT_TIMER_LIMIT;

    var ZOMBIE_SPEED_INC = 10;
    var zombieMinSpeed = ZOMBIE_MIN_SPEED_INITIAL;
    var zombieMaxSpeed = ZOMBIE_MAX_SPEED_INITIAL;

    var brainsSound;
    var hitSound;
    var endSound;

    var collisionNum = 0;

    var gameLostText;

    var speedX = 0;
    var speedY = 0;

    function preload() {
        game.load.image('ball', 'assets/sprites/shinyball.png');
        game.load.atlas('zombie', 'assets/sprites/zombie1.png', 'assets/sprites/zombie1.json');
        game.load.image('hearts0', 'assets/sprites/hearts0.png');
        game.load.image('hearts1', 'assets/sprites/hearts1.png');
        game.load.image('hearts2', 'assets/sprites/hearts2.png');
        game.load.image('hearts3', 'assets/sprites/hearts3.png');

        game.load.audio('brainsSound', 'assets/audio/sfx/brains.mp3');
        game.load.audio('hitSound', 'assets/audio/sfx/hit.mp3');
        game.load.audio('endSound', 'assets/audio/music/end.mp3');
    }

    function create() {
        game.physics.startSystem(Phaser.Physics.ARCADE);

        zombies = game.add.physicsGroup(Phaser.Physics.ARCADE);

        var style = { font: "32px Arial", fill: "#FFF" };
        timeText = game.add.text(10, 10, '00:00', style);

        ball = game.add.sprite(game.world.centerX, game.world.centerY, 'ball');
        game.physics.enable(ball, Phaser.Physics.ARCADE);
        ball.body.collideWorldBounds = true;

        addHearstSprite();

        game.time.events.loop(Phaser.Timer.SECOND, updateTime, this);

        brainsSound = game.add.audio('brainsSound');
        hitSound = game.add.audio('hitSound');
        endSound = game.add.audio('endSound');
    }

    function update() {
        // move ball
        console.log('Moving ball at:' + speedX + ',' + speedY);
        ball.body.velocity.setTo(speedX, speedY);

        // make zombies go for ball
        zombies.forEach(function(zombie){
            zombie.rotation = game.physics.arcade.moveToObject(zombie, ball, zombie.speed);
        });

        // check collisions between zombies and ball
        game.physics.arcade.collide(ball, zombies, collisionWithZombie);

        // spawn new zombies when timer goes off if not paused
        if(!game.physics.arcade.isPaused){
            zombieSpawnTimer += game.time.elapsed;
        }
        if(zombieSpawnTimer >= zombieSpawnTimerLimit){
            zombieSpawnTimer = 0;
            zombieSpawnTimerLimit += ZOMBIE_SPAWN_TIMER_INC;
            zombieMinSpeed += ZOMBIE_SPEED_INC;
            zombieMaxSpeed += ZOMBIE_SPEED_INC;

            spawnZombie();
        }
    }

    function spawnZombie(){
        var randomPos = getRandomPos();
        var speed = game.rnd.integerInRange(zombieMinSpeed, zombieMaxSpeed);
        console.log('Creating new zombie on: ' + randomPos.x + ',' + randomPos.y + ' with speed: ' + speed);

        var zombie = game.add.sprite(randomPos.x, randomPos.y, 'zombie');
        zombie.speed = speed;
        zombie.anchor.setTo(0.5, 0.5);

        prepareZombieWalkAnimation(zombie);

        zombie.animations.play('walk');

        //  Enable Arcade Physics for the sprite
        game.physics.enable(zombie, Phaser.Physics.ARCADE);

        //  Tell it we don't want physics to manage the rotation
        zombie.body.allowRotation = false;

        zombies.add(zombie);
        brainsSound.play();
    }

    function prepareZombieWalkAnimation(aZombie){
        var frameNames = Phaser.Animation.generateFrameNames('sprite', 1, 11);
        frameNames.push('sprite10');
        frameNames.push('sprite9');
        aZombie.animations.add('walk', frameNames, 5, true);
    }

    function getRandomPos(){
        var x = game.rnd.integerInRange(0, width);
        var y = game.rnd.integerInRange(0, height);

        return {x:x, y:y};
    }

    function updateTime(){
        if(!game.physics.arcade.isPaused){
            timeCounter++;

            var minutes = Math.floor(timeCounter / 60);
            var seconds = timeCounter % 60;

            var text = '';
            if(minutes <= 9){
                text += '0';
            }
            text += minutes;
            text += ':';
            if(seconds <= 9){
                text += '0';
            }
            text += seconds;

            timeText.setText(text);
        }
    }

    function collisionWithZombie(ball, zombie){
        hitSound.play();
        zombies.remove(zombie);

        collisionNum++;
        console.log('Collision: ' + collisionNum);

        hearts.destroy();
        addHearstSprite();
        
        if(collisionNum >= MAX_COLLISIONS){
            gameEnd();
        }
    }

    function addHearstSprite(){
        hearts = game.add.sprite(timeText.width + 20, 10, 'hearts' + (3 - collisionNum));
        hearts.scale.setTo(2, 2);
    }

    function gameEnd(){
        var style = { font: "48px Arial", fill: "#8A0707", fontWeight: 'bold', backgroundColor: '#FFF' };
        gameLostText = game.add.text(game.world.centerX, game.world.centerY, 'YOU LOOSE', style);
        gameLostText.anchor.x = 0.5;
        gameLostText.anchor.y = 0.5;
        game.physics.arcade.isPaused = true;
        endSound.loopFull();
        setTimeout(restartGame, 10000);
    }

    function restartGame(){
        zombies.removeAll();
        zombieSpawnTimer = 0;
        zombieSpawnTimerLimit = ZOMBIE_DEFAULT_TIMER_LIMIT;
        zombieMinSpeed = ZOMBIE_MIN_SPEED_INITIAL;
        zombieMaxSpeed = ZOMBIE_MAX_SPEED_INITIAL;

        timeCounter = 0;
        collisionNum = 0;

        hearts.destroy();
        addHearstSprite();
        updateTime();

        gameLostText.visible = false;

        endSound.stop();

        game.physics.arcade.isPaused = false;
    }

    function vigilaSensores(){
    
        function onError() {
            console.log('onError!');
        }

        function onSuccess(datosAceleracion){
          detectaAgitacion(datosAceleracion);
          registraDireccion(datosAceleracion);
        }

        navigator.accelerometer.watchAcceleration(onSuccess, onError,{ frequency: 10 });
    }

    function detectaAgitacion(datosAceleracion){
        var agitacionX = datosAceleracion.x > 10;
        var agitacionY = datosAceleracion.y > 10;

        if (agitacionX || agitacionY){
          setTimeout(app.recomienza, 1000);
        }
    }

    function recomienza(){
        document.location.reload(true);
    }

    function registraDireccion(datosAceleracion){
        speedX = -1 * datosAceleracion.x * 75;
        speedY = datosAceleracion.y * 75;
    }

    vigilaSensores();
}

if ('addEventListener' in document) {
    document.addEventListener('deviceready', function() {
        inicio();
    }, false);
}