var width = 800;
var height = 600;

var game = new Phaser.Game(width, height, Phaser.CANVAS, 'game', { preload: preload, create: create, update: update });

var zombies = [];
var zombie;

var zombieSpawnTimer = 0;
var zombieSpawnTimerLimit = 1000;
var ZOMBIE_SPAWN_TIMER_INC = 1000;

var ZOMBIE_MIN_SPEED_INITIAL = 20;
var ZOMBIE_MAX_SPEED_INITIAL = 100;
var ZOMBIE_SPEED_INC = 10;
var zombieMinSpeed = ZOMBIE_MIN_SPEED_INITIAL;
var zombieMaxSpeed = ZOMBIE_MAX_SPEED_INITIAL;

function preload() {

    game.load.atlas('zombie', 'assets/sprites/zombie1.png', 'assets/sprites/zombie1.json');

}

function create() {

    game.physics.startSystem(Phaser.Physics.ARCADE);
}

function update() {
    zombies.forEach(function(zombie){
        zombie.rotation = game.physics.arcade.moveToPointer(zombie, zombie.speed, game.input.activePointer);
    });

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
    zombie.speed = getRandomSpeed();
    console.log(zombie.speed);

    zombies.push(zombie);
}

function prepareZombieWalkAnimation(aZombie){
    var frameNames = Phaser.Animation.generateFrameNames('sprite', 1, 11);
    frameNames.push('sprite10');
    frameNames.push('sprite9');
    aZombie.animations.add('walk', frameNames, 5, true);
}

function getRandomPos(){
    var x = Math.random() * width;
    var y = Math.random() * height;

    return {x:x, y:y};
}

function getRandomSpeed(){
    return zombieMinSpeed + Math.random() * (zombieMaxSpeed - zombieMinSpeed);
}