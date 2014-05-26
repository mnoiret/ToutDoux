package tout.doux.app.comparator;

import java.util.Comparator;

import tout.doux.app.model.Todo;

/**
 * Created by FERD on 26/05/2014.
 */
public class ComparatorByAlphabeticalOrder implements Comparator<Todo> {
    public int compare(Todo todo1, Todo todo2) {
        int res = String.CASE_INSENSITIVE_ORDER.compare(todo1.getTitle(), todo2.getTitle());
        if (res == 0) {
            res = todo1.getTitle().compareTo(todo2.getTitle());
        }
        return res;
    }
}
