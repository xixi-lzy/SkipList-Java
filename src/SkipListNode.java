class SkipListNode<T extends Comparable<T>>{
    public T value;
    public SkipListNode<T>[] next;

    public SkipListNode(T value,int level){
        this.value=value;
        this.next=new SkipListNode[level+1];
        for(int i=1;i<=level;i++){
            this.next[i]=null;
        }
    }
}
