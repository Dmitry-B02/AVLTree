package Tree;

import java.util.*;

public class AVLTree<T extends Comparable<T>> extends AbstractSet<T> implements SortedSet<T> {

    private Node<T> root = null;

    private int size = 0;

    private Node<T> find(T value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<T> find(Node<T> startNode, T value) { // ищет ближайший к несуществующему узлу элемент либо сам этот элемент, если он существует

        int comparison = startNode.value.compareTo(value);

        if (comparison == 0) {
            return startNode;
        } else if (comparison > 0) {
            if (startNode.leftNode != null) return find(startNode.leftNode, value);
            return startNode;
        } else {
            if (startNode.rightNode != null) return find(startNode.rightNode, value);
            return startNode;
        }
    }

    public boolean contains(Object o) { // проверяет, содержится ли данный элемент в дереве
        T value = (T) o;
        Node<T> closestNode = find(value);
        return closestNode != null && closestNode.value == value;
    }

    public Node<T> getNode(T value) {
        if (contains(value)) return find(value);
        return null;
    }

    public Node<T> getRoot() {
        return root;
    }

    public int size() { // размер дерева
        return size;
    }

    public T first() { // возвращает самый первый элемент в дереве (первый в порядке увеличения value)
        if (root == null) throw new NoSuchElementException();
        Node<T> currentNode = root;
        while (currentNode.leftNode != null) {
            currentNode = currentNode.leftNode;
        }
        return currentNode.value;
    }

    public T last() { // возвращает самый последний элемент в дереве (последний в порядке увеличения value)
        if (root == null) throw new NoSuchElementException();
        Node<T> currentNode = root;
        while (currentNode.rightNode != null) {
            currentNode = currentNode.rightNode;
        }
        return currentNode.value;
    }

    public boolean isEmpty() { // пустое ли дерево
        return root == null;
    }

    private void leftRotate(Node<T> node) { // малый левый поворот
        Node<T> childNode = node.rightNode;
        node.rightNode = childNode.leftNode;
        if (childNode.leftNode != null) childNode.leftNode.parent = node;
        childNode.leftNode = node;
        childNode.parent = node.parent;
        Node<T> nodeParent = node.parent;
        node.parent = childNode;
        if (node == root) root = childNode;
        else if (childNode.value.compareTo(nodeParent.value) > 0) {
            nodeParent.rightNode = childNode;
        } else if (childNode.value.compareTo(nodeParent.value) < 0) {
            nodeParent.leftNode = childNode;
        }
        reestablishHeight(node); // восстановление высоты
    }

    private void rightRotate(Node<T> node) { // малый правый поворот
        Node<T> childNode = node.leftNode;
        node.leftNode = childNode.rightNode;
        if (childNode.rightNode != null) childNode.rightNode.parent = node;
        childNode.rightNode = node;
        childNode.parent = node.parent;
        Node<T> nodeParent = node.parent;
        node.parent = childNode;
        if (node == root) root = childNode;
        else if (childNode.value.compareTo(nodeParent.value) >= 0) {
            nodeParent.rightNode = childNode;
        } else if (childNode.value.compareTo(nodeParent.value) < 0) {
            nodeParent.leftNode = childNode;
        }
        reestablishHeight(node); // восстановление высоты
    }

    private void bigLeftRotate(Node<T> node) { // большой левый поворот
        rightRotate(node.rightNode);
        leftRotate(node);
    }

    private void bigRightRotate(Node<T> node) { // большой правый поворот
        leftRotate(node.leftNode);
        rightRotate(node);
    }

    public boolean add(T t) { // добавление элемента в дерево
        Node<T> closest = find(t); // ближайший узел к тому, который собираемся добавить
        Node<T> nodeToAdd = new Node<T>(t);
        int comparison = closest == null ? 1 : closest.value.compareTo(t);

        if (comparison == 0) return false; // если элемент уже есть в дереве

        if (closest == null) {
            root = nodeToAdd; // если в дереве 0 элементов, то новый элемент - корень
        } else if (closest.value.compareTo(t) > 0) {
            closest.leftNode = nodeToAdd;
        } else {
            closest.rightNode = nodeToAdd;
        }
        nodeToAdd.parent = closest;
        size++;
        reestablishHeight(nodeToAdd);
        // проверка условия баланса и корректировка высоты узлов:
        Node<T> currentParent = nodeToAdd.parent;
        while (currentParent != null) { // прохожу по всем узлам, начиная с родителя добавленного, вверх, для корректировки высоты
            int balanceFactor = Math.abs(currentParent.getBalanceFactor());
            if (balanceFactor > 1) balance(currentParent); // если балансировка нарушена, то корректируем
            currentParent = currentParent.parent;
        }
        return true;
    }

    private void reestablishHeight(Node<T> node) { // корректировка высоты дерева (для корректной работы методов выше)
        node.fixHeight();
        Node<T> currentParent = node.parent;
        while (currentParent != null) {
            currentParent.fixHeight();
            currentParent = currentParent.parent;
        }
        root.fixHeight();
    }

    // взял условия с сайта
    private void balance(Node<T> node) {
        if (node.rightNode != null && (node.getBalanceFactor() == -2 &&
                (node.rightNode.getBalanceFactor() == -1 || node.rightNode.getBalanceFactor() == 0))) {
            leftRotate(node);
        } else if (node.rightNode != null && node.rightNode.leftNode != null &&
                (node.getBalanceFactor() == -2 && node.rightNode.getBalanceFactor() == 1 &&
                        (node.rightNode.leftNode.getBalanceFactor() == -1 ||
                                node.rightNode.leftNode.getBalanceFactor() == 0 ||
                                node.rightNode.leftNode.getBalanceFactor() == 1))) {
            bigLeftRotate(node);
        } else if (node.leftNode != null && (node.getBalanceFactor() == 2 &&
                (node.leftNode.getBalanceFactor() == 1 || node.leftNode.getBalanceFactor() == 0))) {
            rightRotate(node);
        } else if (node.leftNode != null && node.leftNode.rightNode != null &&
                (node.getBalanceFactor() == 2 && node.leftNode.getBalanceFactor() == -1 &&
                        (node.leftNode.rightNode.getBalanceFactor() == -1 ||
                                node.leftNode.rightNode.getBalanceFactor() == 0 ||
                                node.leftNode.rightNode.getBalanceFactor() == 1))) {
            bigRightRotate(node);
        }
    }

    public boolean remove(Object o) {  // удаление элемента из дерева
        T nodeToRemoveValue = (T) o;
        Node<T> nodeToRemove = find(nodeToRemoveValue);
        if (nodeToRemove == null || nodeToRemove.value != nodeToRemoveValue)
            return false; // элемента, который хотим удалить, нет в дереве
        if (nodeToRemove == root) root = null;
        Node<T> leftNode = nodeToRemove.leftNode;
        Node<T> rightNode = nodeToRemove.rightNode;
        Node<T> currentParent; // для корректировки высот и поворотов
        if (leftNode != null) { // узел с левым и (возможно) правым потомком
            leftNode.parent = nodeToRemove.parent;

            if (rightNode != null) {
                leftNode.rightNode = nodeToRemove.rightNode;
                nodeToRemove.rightNode.parent = leftNode;
            }

            comparison(nodeToRemove, leftNode);
            currentParent = leftNode;
        } else if (rightNode != null) { // узел с правым потомком, но без левого
            rightNode.parent = nodeToRemove.parent;
            comparison(nodeToRemove, rightNode);
            currentParent = rightNode;
        } else { // если удаляем лист
            comparison(nodeToRemove, null);
            currentParent = nodeToRemove.parent;
        }
        if (currentParent != null) {
            reestablishHeight(currentParent);
        }
        while (currentParent != null) { // прохожу по всем узлам вверх для корректировки высоты
            int balanceFactor = Math.abs(currentParent.getBalanceFactor());
            if (balanceFactor > 1) balance(currentParent); // если балансировка нарушена, то корректируем
            currentParent = currentParent.parent;
        }
        size--;
        return true;
    }

    // nodeToReplace - узел, который занимает место удалённого
    private void comparison(Node<T> node, Node<T> nodeToReplace) { // определение того, каким потомком является узел: левым или правым
        int comparison = node.parent == null ? 0 : node.value.compareTo(node.parent.value);
        if (comparison > 0) {
            node.parent.rightNode = nodeToReplace;
        }
        else if (comparison < 0) {
            node.parent.leftNode = nodeToReplace;
        }
        else {
            root = nodeToReplace;
        }
    }

    public Iterator<T> iterator() {
        return new AVLTreeIterator();
    }

    public class AVLTreeIterator implements Iterator<T> {
        ArrayList<T> listOfNodesValues = new ArrayList<T>();
        int nodesAmount;
        T nodeValue;

        AVLTreeIterator() {
            this.nodesAmount = 0;
            nodesInIncreasingOrder(root);
        }

        void nodesInIncreasingOrder(Node<T> root) { // функция проходится по дереву и записывает элементы в лист в порядке возрастания value
            if (root != null) {
                nodesInIncreasingOrder(root.leftNode);
                listOfNodesValues.add(root.value);
                nodesInIncreasingOrder(root.rightNode);
            }
        }

        @Override
        public boolean hasNext() {
            return nodesAmount + 1 <= listOfNodesValues.size();
        }

        @Override
        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            nodeValue = listOfNodesValues.get(nodesAmount);
            nodesAmount++;
            return nodeValue;
        }

        @Override
        public void remove() {
            if (nodeValue == null) throw new IllegalStateException();
            AVLTree.this.remove(nodeValue);
            nodeValue = null;
        }
    }

    public Comparator<? super T> comparator() {
        return Comparable :: compareTo;
    }
    // ??????????????
    public SortedSet<T> subSet(T fromElement, T toElement) {
        if (!contains(fromElement) || !contains(toElement)) throw new NoSuchElementException(); // если в дереве нет одного из элементов
        if (fromElement.compareTo(toElement) > 0) throw new IllegalStateException();
        AVLTree<T> subTree = new AVLTree<>();
        AVLTreeIterator iterator = new AVLTreeIterator();
        while (iterator.hasNext()) {
            T value = iterator.next();
            if (value.compareTo(fromElement) >= 0 && value.compareTo(toElement) <= 0) {
                subTree.add(value);
            }
        }
        return subTree;
    }

    public SortedSet<T> headSet(T toElement) {
        return subSet(first(), toElement);
    }

    public SortedSet<T> tailSet(T fromElement) {
        return subSet(fromElement, last());
    }

    public Object[] toArray() {
        Object[] array = new Object[size()];
        AVLTreeIterator iterator = new AVLTreeIterator();
        for (int i = 0; i < size(); i++) {
            T value = iterator.next();
            array[i] = value;
        }
        return array;
    }
    // ?????????????
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            T value = (T) element;
            if (!contains(value)) return false;
        }
        return true;
    }

    public boolean addAll(Collection<? extends T> c) {
        for (Object element : c) {
            T value = (T) element;
            if (contains(value)) return false;
        }
        for (Object element : c) {
            T value = (T) element;
            add(value);
        }
        return true;
    }

    public boolean retainAll(Collection<?> c) {
        if (!containsAll(c)) return false;
        ArrayList<T> list = new ArrayList<>();
        for (Object element : c) {
            T value = (T) element;
            list.add(value);
        }
        AVLTreeIterator iterator = new AVLTreeIterator();
        while (iterator.hasNext()) {
            T currentValue = iterator.next();
            if (!list.contains(currentValue)) {
                remove(currentValue);
            }
        }
        return true;
    }

    public boolean removeAll(Collection<?> c) {
        if (!containsAll(c)) return false;
        for (Object element : c) {
            T value = (T) element;
            remove(value);
        }
        return true;
    }

    public void clear() {
        root = null;
        size = 0;
    }
}
