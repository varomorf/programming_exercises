var width = 800;
var height = 600;

var game = new Phaser.Game(width, height, Phaser.CANVAS, 'game', { preload: preload, create: create, update: update });

var timeCounter = 0;
var timeText = '';

var zombies;
var ball = null;

var zombieSpawnTimer = 0;
var zombieSpawnTimerLimit = 1000;
var ZOMBIE_SPAWN_TIMER_INC = 1000;

var ZOMBIE_MIN_SPEED_INITIAL = 20;
var ZOMBIE_MAX_SPEED_INITIAL = 100;
var ZOMBIE_SPEED_INC = 10;
var zombieMinSpeed = ZOMBIE_MIN_SPEED_INITIAL;
var zombieMaxSpeed = ZOMBIE_MAX_SPEED_INITIAL;

var collisionNum = 0;
var MAX_COLLISIONS = 3;

function preload() {
    game.load.image('ball', 'assets/sprites/shinyball.png');
    game.load.atlas('zombie', 'assets/sprites/zombie1.png', 'assets/sprites/zombie1.json');
}

function create() {
    game.physics.startSystem(Phaser.Physics.ARCADE);

    zombies = game.add.physicsGroup(Phaser.Physics.ARCADE);

    var style = { font: "32px Arial", fill: "#FFF" };
    timeText = game.add.text(10, 10, '00:00', style);

    ball = game.add.sprite(game.world.centerX, game.world.centerY, 'ball');
    game.physics.enable(ball, Phaser.Physics.ARCADE);
    ball.body.collideWorldBounds = true;

    game.time.events.loop(Phaser.Timer.SECOND, updateTime, this);
}

function update() {
    // move ball
    game.physics.arcade.moveToPointer(ball, 400);
    //  if it's overlapping the mouse, don't move any more
    if (Phaser.Rectangle.contains(ball.body, game.input.x, game.input.y))
    {
        ball.body.velocity.setTo(0, 0);
    }

    // make zombies go for ball
    zombies.forEach(function(zombie){
        zombie.rotation = game.physics.arcade.moveToObject(zombie, ball, zombie.speed);
    });

    // check collisions between zombies and ball
    game.physics.arcade.collide(ball, zombies, collisionWithZombie);

    // spawn new zombies when timer goes off
    zombieSpawnTimer += game.time.elapsed;
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
    var zombie = game.add.sprite(randomPos.x, randomPos.y, 'zombie');
    zombie.anchor.setTo(0.5, 0.5);

    prepareZombieWalkAnimation(zombie);

    zombie.animations.play('walk');

    //  Enable Arcade Physics for the sprite
    game.physics.enable(zombie, Phaser.Physics.ARCADE);

    //  Tell it we don't want physics to manage the rotation
    zombie.body.allowRotation = false;
    zombie.speed = game.rnd.integerInRange(zombieMinSpeed, zombieMaxSpeed);
    console.log(zombie.speed);

    zombies.add(zombie);
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

function collisionWithZombie(ball, zombie){
    zombies.remove(zombie);

    collisionNum++;
    console.log('Collision: ' + collisionNum);
}
