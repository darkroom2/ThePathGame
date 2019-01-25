package Controllers;

import Model.GameObject;
import Model.ID;

import java.awt.*;
import java.util.LinkedList;

public class Handler {
    public final LinkedList<GameObject> objects = new LinkedList<>();

    public void updateVelY(int i) {
        synchronized (objects) {
            for (GameObject tempObj : objects)
                tempObj.setVelY(tempObj.getVelY() + i);
        }
    }

    public void addObject(GameObject object) {
        synchronized (objects) {
            objects.add(object);
        }
    }

    public void removeObject(GameObject object) {
        synchronized (objects) {
            objects.remove(object);
        }
    }

    public GameObject getObject(ID id) {
        for (GameObject tempObj : objects) {
            if (tempObj.id == id)
                return tempObj;
        }
        return null;
    }

    public void removeObject(ID id) {
        for (GameObject tempObject : objects)
            if (tempObject.getId() == id)
                this.objects.remove(tempObject);
    }

    /**
     * metoda dla wszystkich obiektów w liście, każdy obiekt ma własną metodę render
     */
    public void render(Graphics g) {
        synchronized (objects) {

            for (GameObject tempObject : objects) {
                tempObject.render(g);
            }
        }

    }

    /**
     * każdy game object ma inną metodę tick
     */
    public void tick() {
        for (GameObject tempObject : objects) {
            tempObject.tick();
        }
    }
}
