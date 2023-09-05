package application;
public class CANode {

    private String element;
    private int next;

    public CANode(String element, int next) {
        this.element = element;
        this.next = next;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    @Override
    public java.lang.String toString() {
        return "CANode{" +
                "element=" + element +
                ", next=" + next +
                '}';
    }
}
