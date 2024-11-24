import java.util.Objects;

public class Pair<T1 extends Comparable<T1>, T2> implements Comparable<Pair<T1, T2>> {    public T1 first ;
    public T2 second ;

    Pair(T1 first , T2 second)
    {
        this.first = first ;
        this.second= second;
    }
    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public int compareTo(Pair<T1, T2> other) {
        return this.first.compareTo(other.first);
    }
}
