
package com.ahmadmadany.frontend.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public abstract class GameObject implements Collidable {

    protected float x;
    protected float y;
    protected float width;
    protected float height;
    protected float speed;
    protected Color color;
    protected boolean active = true;

    // Pre-CS6: Graphics and animation
    protected TextureRegion sprite;
    protected Animation<TextureRegion> animation;
    protected float stateTime = 0f;

    public GameObject(float x, float y, float width,
                      float height, float speed, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.color = color;
    }

    // Position
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    // Size
    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        if (width > 0) {
            this.width = width;
        }
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        if (height > 0) {
            this.height = height;
        }
    }

    // Movement
    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        if (speed >= 0) {
            this.speed = speed;
        }
    }

    // Color
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    // Object state
    public boolean isDestroyed() {
        return !active;
    }

    public void destroy() {
        active = false;
    }

    public boolean isOffScreen(float screenWidth, float screenHeight) {
        return x < -50 ||
            x > screenWidth + 50 ||
            y < -50 ||
            y > screenHeight + 50;
    }

    // Pre-CS6: Update animation time
    public void update(float delta) {
        stateTime += delta;
    }

    // Old rendering method (Module 3-5)
    public void render(ShapeRenderer shapeRenderer) {
        if (shapeRenderer != null && color != null && active) {
            shapeRenderer.setColor(color);
            shapeRenderer.rect(x, y, width, height);
        }
    }

    // Pre-CS6: Render sprites and animations
    public void render(SpriteBatch batch) {
        if (batch != null && active) {
            if (animation != null) {
                TextureRegion currentFrame =
                    animation.getKeyFrame(stateTime, true);

                batch.draw(currentFrame, x, y, width, height);

            } else if (sprite != null) {
                batch.draw(sprite, x, y, width, height);
            }
        }
    }

    // Sprite getter and setter
    public TextureRegion getSprite() {
        return sprite;
    }

    public void setSprite(TextureRegion sprite) {
        this.sprite = sprite;
    }

    // Animation getter and setter
    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    public void setAnimation(Animation<TextureRegion> animation) {
        this.animation = animation;
    }

    // Animation timer getter
    public float getStateTime() {
        return stateTime;
    }

    // Core collision hitbox
    @Override
    public Rectangle getCoreHitbox() {
        return new Rectangle(x, y, width, height);
    }

    // Graze hitbox
    @Override
    public Rectangle getGrazeHitbox() {
        return new Rectangle(
            x - 10,
            y - 10,
            width + 20,
            height + 20
        );
    }

    // Base collision handler
    @Override
    public void onCollision(Collidable other) {
        // Subclasses can override collision behavior.
    }
}
