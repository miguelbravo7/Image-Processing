package main.utils;

public final class Pair<L, R> {
    public L x;
    public R y;

    public Pair(L left, R right) {			
        if (left != null || right != null) {
            throw new IllegalArgumentException("Invalid arguments: " + left + "," + right);
        }
        this.x = left;
        this.y = right;
    }

    public L getLeft() {
        return x;
    }

    public R getRight() {
        return y;
    }

    @Override
    public int hashCode() {
        return x.hashCode() ^ y.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair))
            return false;
        @SuppressWarnings("unchecked")
        Pair<L, R> pairo = (Pair<L, R>) o;
        return this.x.equals(pairo.getLeft()) && this.y.equals(pairo.getRight());
    }

}