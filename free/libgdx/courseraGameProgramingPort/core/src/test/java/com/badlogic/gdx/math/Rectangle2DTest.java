package com.badlogic.gdx.math;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class Rectangle2DTest {

    Rectangle2D rectangle;
    Rectangle2D rectangle2;

    @Before
    public void setup(){
        /*
        * 6 | * * * *
        *
        * 5 | *     *
        *
        * 4 | *  C  *
        *
        * 3 | *     *
        *
        * 2 | * * * *
        *
        * 1 |
        *
        * 0 + - - - - - - - - - -
        *   0 1 2 3 4 5 6 7 8 9 X
        * */
        rectangle = new Rectangle2D(1, 2, 3, 4);
        /*
        * 6 |
        *
        * 5 |
        *
        * 4 |
        *
        * 3 |
        *
        * 2 | * *
        *      C
        * 1 | * *
        *
        * 0 + - - - - - - - - - -
        *   0 1 2 3 4 5 6 7 8 9 X
        * */
        rectangle2 = new Rectangle2D(1, 1, 1, 1);
    }

    @Test
    public void givenARectangle_whenGettingItsSides_returnCorrectValues() throws Exception {
        //GIVEN default rectangle on 1,2 with width 3 and height 4

        //EXPECT
        assertEquals("Wrong bottom value", 2, rectangle.getBottom(), 0);
        assertEquals("Wrong top value", 6, rectangle.getTop(), 0);
        assertEquals("Wrong left value", 1, rectangle.getLeft(), 0);
        assertEquals("Wrong right value", 4, rectangle.getRight(), 0);
    }

    @Test
    public void givenARectangle_whenSettingItsSides_setCorrectValues() throws Exception {
        //GIVEN default rectangle on 1,2 with width 3 and height 4

        //WHEN
        rectangle.setLeft(5);

        //THEN
        assertEquals("Wrong left value", 5, rectangle.getLeft(), 0);
        assertEquals("Wrong X value", 5, rectangle.x, 0);
        assertEquals("Moved Y coordinate when not necessary", 2, rectangle.y, 0);

        //WHEN
        rectangle.setRight(5);

        //THEN
        assertEquals("Wrong right value", 5, rectangle.getRight(), 0);
        assertEquals("Wrong X value", 2, rectangle.x, 0);
        assertEquals("Moved Y coordinate when not necessary", 2, rectangle.y, 0);

        //WHEN
        rectangle.setTop(5);

        //THEN
        assertEquals("Wrong top value", 5, rectangle.getTop(), 0);
        assertEquals("Moved X coordinate when not necessary", 2, rectangle.x, 0);
        assertEquals("Wrong Y value", 1, rectangle.y, 0);

        //WHEN
        rectangle.setBottom(5);

        //THEN
        assertEquals("Wrong bottom value", 5, rectangle.getBottom(), 0);
        assertEquals("Moved X coordinate when not necessary", 2, rectangle.x, 0);
        assertEquals("Wrong Y value", 5, rectangle.y, 0);
    }

    @Test
    public void givenARectangle_whenGettingCenterWithoutVector_returnCenterCorrectly() throws Exception {
        //GIVEN default rectangle on 1,2 with width 3 and height 4
        Vector2 center = new Vector2();

        //WHEN
        rectangle.getCenter(center);

        //THEN
        assertEquals("Wrong X value", 2.5, center.x, 0);
        assertEquals("Wrong Y value", 4, center.y, 0);

        //GIVEN default rectangle on 1,1 with width 1 and height 1

        //WHEN
        rectangle2.getCenter(center);

        //THEN
        assertEquals("Wrong X value", 1.5, center.x, 0);
        assertEquals("Wrong Y value", 1.5, center.y, 0);
    }

    @Test
    public void givenARectangle_whenSettingCenter_setCorrectValues() throws Exception {
        //GIVEN default rectangle on 1,2 with width 3 and height 4

        //WHEN
        rectangle.setCenter(5, 5);

        //THEN
        assertEquals("Wrong X value", 3.5, rectangle.x, 0);
        assertEquals("Wrong Y value", 3, rectangle.y, 0);

        //GIVEN default rectangle on 1,1 with width 1 and height 1

        //WHEN
        rectangle2.setCenter(5, 5);

        //THEN
        assertEquals("Wrong X value", 4.5, rectangle2.x, 0);
        assertEquals("Wrong Y value", 4.5, rectangle2.y, 0);
    }

}
