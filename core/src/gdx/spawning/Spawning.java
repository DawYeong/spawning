package gdx.spawning;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import java.util.ArrayList;
import java.util.Random;

public class Spawning extends ApplicationAdapter implements InputProcessor {

    SpriteBatch batch;
    ShapeRenderer SR;
    Random random;
    BitmapFont font;
    ArrayList<gdx.spawning.enemies> enemies = new ArrayList<enemies>();
    float fX, fY, fWidth, fHeight, fUserWidth, fUserHeight, fProgress, fXP, fDamage;
    static float fUserX, fUserY, fHealth, fHealthMax;
    int nNum, nMax, nSpawnDelay, nLvl = 1, nSkillpoints = 0;

    @Override
    public void create() {
        SR = new ShapeRenderer();
        batch = new SpriteBatch();
        random = new Random();
        font = new BitmapFont();
        fUserWidth = 50;
        fUserHeight = 50;
        fUserX = 0;
        fUserY = 0;
        fWidth = 30;
        fHeight = 30;
        fHealthMax = 200;
        fDamage = 1;
        fXP = 50;
        fProgress = 0;
        nSpawnDelay = 120;
        fHealth = fHealthMax;
        nNum = 0;
        nMax = 0;
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        player();
        spawn();
        enemy();
        text();
    }

    public void spawn() {
        for (int i = enemies.size() - 1; i >= 0; i--) {
            enemies.get(i).Update();
            if (fUserX - 75 <= enemies.get(i).fX + fWidth
                    && fUserX + 125 >= enemies.get(i).fX
                    && fUserY - 75 <= enemies.get(i).fY + fHeight
                    && fUserY + 125 >= enemies.get(i).fY) {
                enemies.get(i).isTouching = true;
                if (enemies.get(i).isTouching == true) {
                    if (Gdx.input.getX() >= enemies.get(i).fX
                            && Gdx.input.getX() <= enemies.get(i).fX + fWidth
                            && (Gdx.graphics.getHeight() - Gdx.input.getY()) >= enemies.get(i).fY
                            && (Gdx.graphics.getHeight() - Gdx.input.getY()) <= enemies.get(i).fY + fHeight) {
                        enemies.get(i).isMouseTouch = true;
                    } else {
                        enemies.get(i).isMouseTouch = false;
                    }
                }
            } else {
                enemies.get(i).isMouseTouch = false;
                enemies.get(i).isTouching = false;
            }
            if (((fUserX >= enemies.get(i).fX - 35) || (fUserX + 50 >= enemies.get(i).fX - 35))
                    && fUserX <= enemies.get(i).fX + 65
                    && ((fUserY >= enemies.get(i).fY - 35) || (fUserY + 50 >= enemies.get(i).fY - 35))
                    && fUserY <= enemies.get(i).fY + 65) {
                enemies.get(i).isNear = true;
            }
            if (enemies.get(i).isNear == true) {
                enemies.get(i).move();
            }
            if (enemies.get(i).fProgress <= 0) {
                enemies.remove(i);
                nNum++;
                nMax--;
                fProgress += fXP;

            }
        }
    }

    public void enemy() {
        if (nSpawnDelay >= 120 && nMax <= 10) {
            nMax++;
            fX = random.nextInt(Gdx.graphics.getWidth()) + 1;
            fY = random.nextInt(Gdx.graphics.getHeight()) + 1;
            enemies.add(new enemies(fX, fY, fWidth, fHeight));
        }
        if (nSpawnDelay >= 120) {
            nSpawnDelay = 0;
        }
        nSpawnDelay++;
    }

    public void player() {
        SR.begin(ShapeType.Filled);
        SR.setColor(Color.CYAN);
        SR.rect(fUserX, fUserY, fUserWidth, fUserHeight);
        SR.end();
        SR.begin(ShapeType.Line);
        SR.setColor(Color.RED);
        SR.ellipse(fUserX - 75, fUserY - 75, 200, 200);
        SR.end();
        stats();
        lvlUP();
        move();
    }

    public void stats() {
        SR.begin(ShapeType.Filled);
        SR.setColor(Color.RED);
        SR.rect(0, Gdx.graphics.getHeight() - 20, fHealth, 20);
        SR.end();
        SR.begin(ShapeType.Filled);
        SR.setColor(Color.YELLOW);
        SR.rect(0, Gdx.graphics.getHeight() - 30, fProgress, 10);
        SR.end();
        if (fHealth <= 0) {
            fHealth = fHealthMax;
        }
    }

    public void lvlUP() {
        if (fProgress >= 200) {
            fXP *= 0.85;
            fHealth = fHealthMax;
            nLvl++;
            nSkillpoints += 3;
            fProgress = 0;
        }
        if (nSkillpoints > 0) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                fHealthMax += 5;
                fHealth = fHealthMax;
                nSkillpoints--;
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                fDamage *= 1.25;
                nSkillpoints--;
            }
        }
    }

    public void move() {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            fUserY += 2;
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                fUserX -= 2;
            } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                fUserX += 2;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            fUserY -= 2;
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                fUserX -= 2;
            } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                fUserX += 2;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            fUserX -= 2;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            fUserX += 2;
        }
    }

    public void text() {
        batch.begin();
        font.setColor(Color.BLACK);
        font.draw(batch, "Kills : " + nNum, 0, 25);
        font.draw(batch, "Level : " + nLvl, 0, 50);
        font.draw(batch, "Skillpoints : " + nSkillpoints, 0, 75);
        font.draw(batch, "Damage : " + fDamage, 0, 100);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        SR.dispose();
        font.dispose();
    }

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        for (int w = enemies.size() - 1; w >= 0; w--) {
            if (enemies.get(w).isMouseTouch == true) {
                enemies.get(w).fProgress -= fDamage;
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }
}
