var game = new Phaser.Game(800, 600, Phaser.CANVAS, 'game', { preload: preload, create: create });

function preload() {

    //  37x45 is the size of each frame

    //  There are 18 frames in the PNG - you can leave this value blank if the frames fill up the entire PNG, but in this case there are some
    //  blank frames at the end, so we tell the loader how many to load

    //game.load.spritesheet('mummy', 'assets/sprites/metalslug_mummy37x45.png', 37, 45, 18);

    //game.load.baseURL = 'http://examples.phaser.io/assets/';
    //game.load.baseURL = 'https://github.com/varomorf/programming_exercises/raw/master/miriadax/appmovil/modulo4/assets/';
    game.load.crossOrigin = 'Anonymous';

    //game.load.image('zombie', 'sprites/zombie-1.png');
    game.load.atlas('zombie', 'assets/sprites/zombie1.png', 'assets/sprites/zombie1.json');

}

function create() {

    //var mummy = game.add.sprite(300, 200, 'mummy');

    //  Here we add a new animation called 'walk'
    //  Because we didn't give any other parameters it's going to make an animation from all available frames in the 'mummy' sprite sheet
    //var walk = mummy.animations.add('walk');

    //  And this starts the animation playing by using its key ("walk")
    //  30 is the frame rate (30fps)
    //  true means it will loop when it finishes
    //mummy.animations.play('walk', 30, true);

    var zombie = game.add.sprite(0, 0, 'zombie');

    var walk = zombie.animations.add('walk', Phaser.Animation.generateFrameNames('sprite', 1, 11), 1, true);

    zombie.animations.play('walk');

}