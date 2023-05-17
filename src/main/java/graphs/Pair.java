package graphs;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Setter
public class Pair<T, V> {
    private T a;
    private V b;

    public Pair(T a, V b) {
        this.a = a;
        this.b = b;
    }

    public T getA() {
        return this.a;
    }

    public V getB() {
        return this.b;
    }
}

