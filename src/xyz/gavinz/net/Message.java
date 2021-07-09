package xyz.gavinz.net;

/**
 * 抽象的消息
 *
 * @author Gavin.Zhao
 */
public abstract class Message {
    public abstract byte[] toBytes();
    public abstract void parse(byte[] bytes);
    public abstract void handle();
    public abstract MsgType getType();
}
