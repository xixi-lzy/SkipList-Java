/**
 * 跳表节点
 * @param <T>
 */
class SkipListNode<T extends Comparable<T>>{
    public T value;//节点的值
    public SkipListNode<T>[] next;//节点每一层的下一个节点

    public SkipListNode(T value,int level){
        this.value=value;
        this.next=new SkipListNode[level+1];
        for(int i=1;i<=level;i++){
            this.next[i]=null;
        }
    }
}
