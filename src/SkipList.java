import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 跳表Java实现
 */
public class SkipList<T extends Comparable<T>> {

    private static final double PROBABILITY = 0.5; // 节点晋升概率
    private SkipListNode<T> head; // 头节点(位于最顶层)
    private int maxLevel; // 最大层数
    private int listLevel; // 列表当前层数
    private int size; // 元素数量
    private final Random random; // 随机数生成器

    //跳表初始化
    public SkipList() {
        this.random = new Random();
        this.maxLevel = 16;
        this.listLevel = 0;
        this.head = new SkipListNode<>(null, maxLevel);
        this.size = 0;
    }

    /**
     * 随机生成节点层数
     */
    public int getRandomLevel() {
        int level=0;
        while(random.nextDouble() < PROBABILITY && level < maxLevel) {
            level++;
        }
        return level;
    }

    /**
     * 查找元素
     */
    public boolean contains(T value) {
        // 从最高层开始查找
        SkipListNode<T> current = head;
        for(int i=listLevel; i>=0; i--) {
            // 找到小于目标值的最大节点
            while(current.next[i] != null&& current.next[i].value.compareTo(value)<0) {
                current = current.next[i];
            }
        }
        // 检查下一个节点是否为目标值
        current = current.next[0];
        return current!=null&&current.value.equals(value);
    }

    //插入元素
    public void insert(T value) {
        // update数组记录每一层需要更新的节点
        SkipListNode<T>[] update = new SkipListNode[maxLevel+1];

        // 从最高层开始查找插入位置
        SkipListNode<T> current = head;
        for(int i=listLevel; i>=0; i--) {
            while(current.next[i] != null&& current.next[i].value.compareTo(value)<0) {
                current = current.next[i];
            }
            update[i] = current;
        }

        // 检查下一个节点是否为目标值
        current = current.next[0];

        // 如果当前节点为null或值不相等，则插入
        if(current==null||!current.value.equals(value)) {
            // 随机生成新节点的层数
            int level = getRandomLevel();

            // 如果新节点的层数大于当前listLevel，则更新listLevel
            if(level>listLevel){
                for (int i = listLevel + 1; i <= level; i++) {
                    update[i] = head;
                }
                listLevel = level;
            }

            // 创建新节点
            SkipListNode<T> newNode = new SkipListNode<>(value, level);

            //更新每一层的next
            for(int i=0; i<=level; i++) {
                newNode.next[i]=update[i].next[i];
                update[i].next[i]=newNode;
            }
        }

        //更新元素数量
        size++;
    }

    /**
     * 删除元素
     */
    public void delete(T value) {
        // update数组记录每一层需要更新的节点
        SkipListNode<T>[] update = new SkipListNode[maxLevel + 1];

        SkipListNode<T> current = head;
        // 从最高层开始查找要删除的节点
        for (int i = maxLevel; i >= 0; i--) {
            while (current.next[i] != null &&
                    current.next[i].value.compareTo(value) < 0) {
                current = current.next[i];
            }
            update[i] = current;
        }

        // 检查下一个节点是否为删除目标
        current = current.next[0];

        // 如果当前节点不为null且值相等，则删除节点
        if(current!=null&&current.value.equals(value)) {
            for(int i=0;i<listLevel;i++) {
                if(update[i].next[i]!=current) {
                    break;
                }
                update[i].next[i]=current.next[i];
            }
        }

        // 如果删除的是最高层节点，更新listLevel
        while(listLevel>0&&head.next[listLevel]==null) {
            maxLevel--;
        }
        size--;
    }

    //返回元素数量
    public int size() {
        return size;
    }

    /**
     * 打印跳表结构，使同一元素在不同层竖直对齐
     */
    public void print() {
        System.out.println("SkipList (大小：" + size + ", 最大层数：" + listLevel + ")");

        // 收集所有节点（按底层顺序）
        List<SkipListNode<T>> nodes = new ArrayList<>();
        SkipListNode<T> current = head.next[0];
        while (current != null) {
            nodes.add(current);
            current = current.next[0];
        }

        // 计算每个节点的最大显示宽度
        int maxWidth = 0;
        for (SkipListNode<T> node : nodes) {
            int width = node.value.toString().length();
            if (width > maxWidth) {
                maxWidth = width;
            }
        }
        maxWidth += 2; // 添加一些边距

        // 从最高层到最底层打印
        for (int i = listLevel; i >= 0; i--) {
            System.out.print("Level " + String.format("%2d", i) + ": ");
            current = head.next[i];

            // 遍历所有节点，判断是否出现在当前层
            for (SkipListNode<T> node : nodes) {
                if (isNodeInLevel(node, i)) {
                    System.out.print(String.format("%-" + maxWidth + "s", node.value));
                } else {
                    System.out.print(String.format("%-" + maxWidth + "s", ""));
                }
            }
            System.out.println();
        }
    }

    /**
     * 检查节点是否出现在指定层
     */
    private boolean isNodeInLevel(SkipListNode<T> node, int level) {
        if (node == null) return false;
        // 节点的层数必须大于等于当前检查的层
        return node.next.length > level;
    }
}
