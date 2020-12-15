package Tree;

public class Node<T> {

    T value;
    private int height;
    Node<T> leftNode;
    Node<T> rightNode;
    Node<T> parent;

    Node(T value) {
        this.value = value;
        leftNode = null;
        rightNode = null;
        parent = null;
        height = 1;
    }

    public T getValue() {
        return value;
    }

    public Node<T> getParent() {
        return parent;
    }

    public Node<T> getLeftNode() {
        return leftNode;
    }

    public Node<T> getRightNode() {
        return rightNode;
    }

    int getBalanceFactor() { // если abs > 1, то нарушено условие баланса дерева
        int leftNodeHeight = leftNode == null ? 0 : leftNode.height;
        int rightNodeHeight = rightNode == null ? 0 : rightNode.height;
        return leftNodeHeight - rightNodeHeight;
    }

    void fixHeight() { // корректирует высоту текущего узла (считая этот узел, поэтому +1)
        int leftNodeHeight = leftNode == null ? 0 : leftNode.height;
        int rightNodeHeight = rightNode == null ? 0 : rightNode.height;
        height = Math.max(leftNodeHeight, rightNodeHeight) + 1;
    }
}
