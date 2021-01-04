package guides.operators;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FilterTest {

    @Test
    public void filter() {
        Multi<Integer> multi = Multi.createFrom().range(1, 11);
        // tag::filter[]
        List<Integer> list = multi
                .transform().byFilteringItemsWith(i -> i > 6)
                .collect().asList()
                .await().indefinitely();
        // end::filter[]

        // tag::test[]
        List<Integer> list2 = multi
                .transform().byTestingItemsWith(i -> Uni.createFrom().item(i > 6))
                .collect().asList()
                .await().indefinitely();
        // end::test[]

        assertThat(list).containsExactly(7, 8, 9, 10);
        assertThat(list2).containsExactly(7, 8, 9, 10);
    }

    @Test
    public void take() {
        Multi<Integer> multi = Multi.createFrom().range(1, 11);
        // tag::take[]
        List<Integer> list = multi
                .transform().byTakingFirstItems(2)
                .collect().asList()
                .await().indefinitely();

        List<Integer> list2 = multi
                .transform().byTakingItemsWhile(i -> i < 3)
                .collect().asList()
                .await().indefinitely();

        List<Integer> list3 = multi
                .transform().byTakingLastItems(2)
                .collect().asList()
                .await().indefinitely();
        // end::take[]
        assertThat(list).containsExactly(1, 2);
        assertThat(list2).containsExactly(1, 2);
        assertThat(list3).containsExactly(9, 10);
    }

    @Test
    public void skip() {
        Multi<Integer> multi = Multi.createFrom().range(1, 11);
        // tag::skip[]
        List<Integer> list = multi
                .transform().bySkippingFirstItems(8)
                .collect().asList()
                .await().indefinitely();

        List<Integer> list2 = multi
                .transform().bySkippingItemsWhile(i -> i < 9)
                .collect().asList()
                .await().indefinitely();

        List<Integer> list3 = multi
                .transform().bySkippingLastItems(8)
                .collect().asList()
                .await().indefinitely();
        // end::skip[]
        assertThat(list).containsExactly(9, 10);
        assertThat(list2).containsExactly(9, 10);
        assertThat(list3).containsExactly(1, 2);
    }

    @Test
    public void distinct() {
        Multi<Integer> multi = Multi.createFrom().items(1, 1, 2, 3, 4, 5, 5, 6);
        // tag::distinct[]
        List<Integer> list = multi
                .transform().byDroppingDuplicates()
                .collect().asList()
                .await().indefinitely();
        // end::distinct[]

        // tag::repetition[]
        List<Integer> list2 = multi
                .transform().byDroppingRepetitions()
                .collect().asList()
                .await().indefinitely();
        // end::repetition[]
        assertThat(list).containsExactly(1, 2, 3, 4, 5, 6);
        assertThat(list2).containsExactly(1, 2, 3, 4, 5, 6);
    }
}