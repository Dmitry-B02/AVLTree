import Tree.AVLTree;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class tests {

    @Test
    void test() {
        AVLTree<Integer> testTree = new AVLTree<>();

        testTree.add(1);
        assertEquals(1, testTree.size());

        testTree.add(2);
        assertEquals(2, testTree.size());

        testTree.add(2); // добавляю уже имеющийся элемент
        assertEquals(2, testTree.size());
        assertEquals(1, testTree.getRoot().getValue());

        testTree.add(3); // добавляю 3, производится малый левый поворот дерева, root меняется с 1 на 2, проверяю все связи
        assertEquals(3, testTree.size());
        assertEquals(2, testTree.getRoot().getValue());
        assertEquals(1, testTree.first());
        assertEquals(2, testTree.getNode(1).getParent().getValue());
        assertEquals(2, testTree.getNode(3).getParent().getValue());
        assertEquals(3, testTree.getNode(2).getRightNode().getValue());
        assertEquals(1, testTree.getNode(2).getLeftNode().getValue());

        assertNull(testTree.getNode(100)); // данного элемента нет

        testTree.remove(3);
        assertEquals(2, testTree.size());

        testTree.add(3);
        assertEquals(3, testTree.size());

        testTree.add(4);

        testTree.remove(2); // удаляю 2, снова производится малый левый поворот
        assertEquals(3, testTree.getNode(1).getParent().getValue());
        assertEquals(3, testTree.getNode(4).getParent().getValue());
        assertEquals(4, testTree.getNode(3).getRightNode().getValue());
        assertEquals(1, testTree.getNode(3).getLeftNode().getValue());
        assertEquals(3, testTree.getRoot().getValue());

        assertFalse(testTree.remove(2)); // удаляю элемент, которого нет

        assertEquals(testTree.first(), 1);
        assertEquals(testTree.last(), 4); // имеем дерево с элементами 1, 3, 4

        testTree.remove(1);
        testTree.add(2);
        testTree.add(1);
        testTree.add(0); // добавляю 0, производится малый правый поворот
        assertEquals(1, testTree.getNode(2).getParent().getValue());
        assertEquals(3, testTree.getRoot().getValue());
        // проверяю все связи:
        assertEquals(0, testTree.getNode(1).getLeftNode().getValue());
        assertEquals(2, testTree.getNode(1).getRightNode().getValue());
        assertEquals(1, testTree.getNode(2).getParent().getValue());
        assertEquals(1, testTree.getNode(0).getParent().getValue());

        testTree.clear();
        assertEquals(0, testTree.size()); // проверяю корректность очистки

        testTree.add(3);
        testTree.add(2);
        testTree.add(1); // правый поворот, root меняется с 3 на 2, проверяю только это, т.к. проверка связей уже была
        assertEquals(2, testTree.getRoot().getValue());
        // проверять большие повороты нет смысла, т.к. они состоят из маленьких поворотов,
        // а т.к. те работают корректно, значит, большие работают корректно тоже

        testTree.clear();
        testTree.add(5);
        assertFalse(testTree.isEmpty());
        assertFalse(testTree.contains(4));
        assertTrue(testTree.contains(5));

        assertEquals(5, testTree.getRoot().getValue());
        testTree.remove(5);
        assertTrue(testTree.isEmpty());

        for (int i = 0; i < 10; i++) {
            testTree.add(i);
        }
        assertEquals(3, testTree.getRoot().getValue());
        assertEquals(1, testTree.getRoot().getLeftNode().getValue());
        assertEquals(7, testTree.getRoot().getRightNode().getValue());
        assertEquals(5, testTree.getNode(7).getLeftNode().getValue());
        assertEquals(8, testTree.getNode(7).getRightNode().getValue());
        assertEquals(3, testTree.getNode(7).getParent().getValue());
        assertTrue(testTree.contains(9));

        Iterator<Integer> iterator1 = testTree.iterator();
        Object[] array1 = testTree.toArray();
        System.out.println(Arrays.toString(array1));
        for (Object o : array1) { // проверяю корректность работы итератора и toArray
            assertEquals(iterator1.next(), o);
        }

        Iterator<Integer> iterator2 = testTree.iterator();
        Iterator<Integer> iterator3 = testTree.iterator();
        while (iterator2.hasNext()) { // проверяю корреткность работы двух итераторов одновременно
            assertEquals(iterator2.next(), iterator3.next());
        }

        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        // проверяю containsAll, removeAll, addAll, retainAll
        assertTrue(testTree.containsAll(list));

        testTree.removeAll(list);
        assertEquals(0, testTree.size());

        assertFalse(testTree.removeAll(list)); // не удалить элементы, т.к. их нет

        testTree.addAll(list);
        assertEquals(10, testTree.size());
        assertEquals(3, testTree.getRoot().getValue());

        list.add(15);
        assertFalse(testTree.containsAll(list));

        ArrayList<Integer> list1 = new ArrayList<>();
        list1.add(20);
        list1.add(0);
        assertFalse(testTree.addAll(list1)); // если добавляются [20, 0], то, т.к. "0" уже есть, не добавляется ни один
        assertFalse(testTree.contains(20)); // проверяю, действительно ли правда то, что я написал выше

        assertEquals(10, testTree.size());
        assertFalse(testTree.retainAll(list1));

        list1.remove(0);
        list1.add(9);
        assertTrue(testTree.retainAll(list1));
        assertEquals(2, testTree.size());
        assertEquals(0, testTree.getRoot().getValue());

        testTree.clear();
        for (int i = 0; i < 100; i++) {
            testTree.add(i);
        }

        TreeSet<Integer> testSet = new TreeSet<>();

        for (int i = 5; i < 80; i++) {
            testSet.add(i);
        }

        assertEquals(testSet, testTree.subSet(5, 79)); // проверять headSet и tailSet нет смысла, т.к. они реализованы через subSet

        testTree.clear();
        testTree.add(1);
        TreeSet<Integer> testSet1 = new TreeSet<>();
        testSet1.add(1);
        assertEquals(testSet1, testTree.subSet(1, 1));
    }
}

































