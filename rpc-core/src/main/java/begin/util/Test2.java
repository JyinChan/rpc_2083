package begin.util;

import java.util.Stack;

class Node {

    String name;
    Node next;

    Node(String name, Node next) {
        this.name = name;
        this.next = next;
    }
}


public class Test2 {

    public static void main(String[] args) {

        Node l1 = new Node("l1", null);
        Node l2 = new Node("l2", l1);
        Node l3 = new Node("l3", l2);
        Node l4 = new Node("l4", l3);
        Node root = new Node("root", l4);

        visit(root);
        System.out.println("===============");
        reverseVisitByStack(root);
        System.out.println("===============");
        reverseVisit(root);

        //Arrays.sort();/

    }

    private static void visit(Node root) {
        while (root != null) {
            System.out.println(root.name + " ");
            root = root.next;
        }
    }

    private static void reverseVisit(Node root) {
        //root => l2 => l1 => null
        //l2 => root => null l1
        if (root != null) {
            Node h = root;
            Node c = root.next;
            while (c != null) {
                Node tmp = c.next;
                c.next = h;
                h = c;
                c = tmp;
            }
            root.next = null;
            while (h != null) {
                System.out.println(h.name + " ");
                h = h.next;
            }
        }
    }

    private static void reverseVisitByStack(Node root) {
        Stack<Node> stack = new Stack<>();
        while (root != null) {
            stack.push(root);
            root = root.next;
        }
        while (!stack.isEmpty()) {
            System.out.println(stack.pop().name + " ");
        }
    }
}
