package com.badlogic.gdx.math;

/**
 * <p>Liggdx adaptation of MonoGame's Rectangle2D.</p>
 * Created by alfergon on 19/01/17.
 */
public class Rectangle2D extends Rectangle {

    /**
     * Constructs a new rectangle with all values set to zero
     */
    public Rectangle2D() {
        super();
    }

    /**
     * Constructs a new rectangle with the given corner point in the bottom left and dimensions.
     *
     * @param x      The corner point x-coordinate
     * @param y      The corner point y-coordinate
     * @param width  The width
     * @param height The height
     */
    public Rectangle2D(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    /**
     * Constructs a rectangle based on the given rectangle
     *
     * @param rect The rectangle
     */
    public Rectangle2D(Rectangle rect) {
        super(rect);
    }

    /**
     * Returns the bottommost coordinate of the rectangle.
     *
     * @return The bottommost coordinate of the rectangle.
     */
    public float getBottom() {
        return y;
    }

    /**
     * Sets the Y coordinate so that the bottom of the rectangle is at the given value.
     *
     * @param y The value for the bottom.
     */
    public void setBottom(float y) {
        this.y = y;
    }

    /**
     * Returns the topmost coordinate of the rectangle.
     *
     * @return The topmost coordinate of the rectangle.
     */
    public float getTop() {
        return y + height;
    }

    /**
     * Sets the Y coordinate so that the top of the rectangle is at the given value.
     *
     * @param y The value for the top.
     */
    public void setTop(float y) {
        this.y = y - height;
    }

    /**
     * Returns the rightmost coordinate of the rectangle.
     *
     * @return The rightmost coordinate of the rectangle.
     */
    public float getRight() {
        return x + width;
    }

    /**
     * Sets the X coordinate so that the right of the rectangle is at the given value.
     *
     * @param x The value for the right.
     */
    public void setRight(float x) {
        this.x = x - width;
    }

    /**
     * Returns the leftmost coordinate of the rectangle.
     *
     * @return The leftmost coordinate of the rectangle.
     */
    public float getLeft() {
        return x;
    }

    /**
     * Sets the X coordinate so that the left of the rectangle is at the given value.
     *
     * @param x The value for the left.
     */
    public void setLeft(float x) {
        this.x = x;
    }
}
