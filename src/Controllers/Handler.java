package Controllers;

import Model.GameObject;
import Model.ID;

import java.awt.*;
import java.util.LinkedList;

class Handler {
    LinkedList<GameObject> objects = new LinkedList<GameObject>();

    public void addObject(GameObject object) {
        this.objects.add(object);
    }

    public void removeObject(GameObject object) {
        this.objects.remove(object);
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
        for (GameObject tempObject : objects) {
            tempObject.render(g);
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
