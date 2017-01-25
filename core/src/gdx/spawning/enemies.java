/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gdx.spawning;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 *
 * @author benny
 */
public class enemies {

    float fX, fY, fWidth, fHeight, fProgress;
    int nDelay = 15;
    boolean isTouching, isNear, isMouseTouch;
    ShapeRenderer SR = new ShapeRenderer();

    enemies(float fX, float fY, float fWidth, float fHeight) {
        this.fX = fX;
        this.fY = fY;
        this.fWidth = fWidth;
        this.fHeight = fHeight;
        fProgress = fWidth;
    }

    public void Update() {
        enemy();
        damage();
    }

    public void enemy() {
        SR.begin(ShapeType.Filled);
        SR.setColor(Color.BLACK);
        SR.rect(fX, fY, fWidth, fHeight);
        SR.end();
        SR.begin(ShapeType.Filled);
        SR.setColor(Color.GREEN);
        SR.rect(fX, fY - 5, fProgress, 5);
        SR.end();
        SR.begin(ShapeType.Line);
        SR.ellipse(fX - 35, fY - 35, 100, 100);
        SR.end();
    }

    public void move() {
        if (fX < Spawning.fUserX) {
            fX++;
        } else if (fX > Spawning.fUserX) {
            fX--;
        }
        if (fY >= Spawning.fUserY) {
            fY--;
        } else if (fY <= Spawning.fUserY) {
            fY++;
        }
    }

    public void damage() {
        if (nDelay >= 15) {
            if (fX + fWidth >= Spawning.fUserX && fX <= Spawning.fUserX + 50
                    && fY + fHeight >= Spawning.fUserY && fY <= Spawning.fUserY + 50) {
                Spawning.fHealth -= 10;
            }
            nDelay = 0;
        }
        nDelay ++;
    }
}
