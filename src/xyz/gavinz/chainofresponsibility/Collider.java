package xyz.gavinz.chainofresponsibility;

import xyz.gavinz.AbstractGameObject;

public interface Collider {
    boolean collide(AbstractGameObject o1, AbstractGameObject o2);
}
