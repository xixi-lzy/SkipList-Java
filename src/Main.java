public class Main {
    public static void main(String[] args) {
        SkipList<Integer> skipList = new SkipList<>();

        // 插入测试
        skipList.insert(3);
        skipList.insert(6);
        skipList.insert(7);
        skipList.insert(9);
        skipList.insert(12);
        skipList.insert(19);
        skipList.insert(17);
        skipList.insert(26);
        skipList.insert(21);
        skipList.insert(25);

        skipList.print();

        // 查找测试
        System.out.println("是否存在 19: " + skipList.contains(19));
        System.out.println("是否存在 20: " + skipList.contains(20));

        // 删除测试
        skipList.delete(19);
        skipList.delete(25);

        skipList.print();
    }
}
