import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

public class b_tree {
    int minDegreeT;  // minimum degree T  설정

    // node 구조 생성
    public class Node {
        int count;
        boolean leaf = true;
        int key[] = new int[2 * minDegreeT - 1];    // 모든 노드는 최대 2t-1개의 key를 갖을 수 있다.
        Node child[] = new Node[2 * minDegreeT];   // 최대 2t-1개의 key를 갖기 때문에 자식은 2t개를 갖는다
    }

    // b_tree 생성 시 root 생성 및 minimum degree 설정
    b_tree(int t) {
        minDegreeT = t;  // minimum degree 설정
        root = new Node();  // root 노드 생성
        root.count = 0;
        root.leaf = true;
    }

    Node root;  // root key node

    // Splitting the node
    void Split(Node x, int pos, Node y) {
        Node z = new Node();
        z.leaf = y.leaf;
        z.count = minDegreeT - 1;   // 최소 키 값 개수
        for (int j = 0; j < minDegreeT - 1; j++) {
            z.key[j] = y.key[j + minDegreeT];
        }
        if (!y.leaf) {
            for (int j = 0; j < minDegreeT; j++) {
                z.child[j] = y.child[j + minDegreeT];
            }
        }
        y.count = minDegreeT - 1;
        for (int j = x.count; j >= pos + 1; j--) {
            x.child[j + 1] = x.child[j];
        }
        x.child[pos + 1] = z;

        for (int j = x.count - 1; j >= pos; j--) {
            x.key[j + 1] = x.key[j];
        }
        x.key[pos] = y.key[minDegreeT - 1];
        x.count++;
    }

    // Inserting a value
    void Insertion(int key) {
        Node r = root;
        if (r.count == 2 * minDegreeT - 1) {    // 갖을 수 있는 키 값이 꽉 차있다면
            Node subnode = new Node();
            root = subnode;
            subnode.leaf = false;
            subnode.count = 0;
            subnode.child[0] = r;
            Split(subnode, 0, r);
            valInsert(subnode, key);
        } else {
            valInsert(r, key);
        }
    }

    // Insert the node
   void valInsert(Node newNode, int keyVal) {
        if (newNode.leaf) {
            int index = 0;
            for (index = newNode.count - 1; index >= 0 && keyVal < newNode.key[index] ; index--) {
                newNode.key[index + 1] = newNode.key[index];
            }
            newNode.key[index + 1] = keyVal;
            newNode.count++;
        } else {
            int index = 0;
            for (index = newNode.count - 1; index >= 0 && keyVal < newNode.key[index]  ; index--) {
            }
            index++;
            Node temp = newNode.child[index];  // 자식 노드 생성
            if (temp.count == 2 * minDegreeT - 1) {  // 최대 키 값을 넘어 간 경우 SPLIT
                Split(newNode, index, temp);
                if (keyVal > newNode.key[index]) {    // 키 값에 맞는 위치 를 선정 (오름차 순)
                    index++;
                }
            }
            valInsert(newNode.child[index], keyVal);
        }
    }

    // Display
    void Show(Node x) {
        if(x == null){
            System.exit(0);
        }
        for (int i = 0; i < x.count; i++) {
            System.out.print(x.key[i] + " ");
        }
        if (!x.leaf) {
            for (int i = 0; i < x.count + 1; i++) {
                Show(x.child[i]);
            }
        }
    }

    void Show() {
        Show(root);
    }

    // main
    public static void main(String[] args) {
        Random random = new Random();
        Scanner sc = new Scanner(System.in);
        System.out.print("SETTING MINIMUM DEGREE T: ");
        int minT = sc.nextInt();    // minT = minimum degree t 입력을 받는다
        b_tree b = new b_tree(minT);

        // insert time calculate
        long start = System.currentTimeMillis();    // insert start time
        for (int i=0; i<1000; i++){
            int rand = random.nextInt(10000);
            b.Insertion(rand);  // 0 ~ 9999 사이의 데이터 1000개 삽입 (랜덤 수 삽입)
        }
        long finish = System.currentTimeMillis();   // insert end time
        System.out.println("Insert time: " + (finish-start) + "ms");
        b.Show();
    }
}