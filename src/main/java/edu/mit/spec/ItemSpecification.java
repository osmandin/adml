package edu.mit.spec;

import edu.mit.domain.Item;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ItemSpecification implements Specification<Item> {

    private SpecSearchCriteria criteria;

    public ItemSpecification(final SpecSearchCriteria criteria) {
        super();
        this.criteria = criteria;
    }

    public SpecSearchCriteria getCriteria() {
        return criteria;
    }

    @Override
    public Predicate toPredicate(final Root<Item> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {
        switch (criteria.getOperation()) {
            case EQUALITY:
                return builder.equal(builder.lower(root.get(criteria.getKey())), criteria.getValue());
            case NEGATION:
                return (builder.notLike((builder.lower(root.get(criteria.getKey()))), "%" + (String) criteria.getValue() + "%"));
            case GREATER_THAN:
                return builder.greaterThan(root.<String>get(criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN:
                return builder.lessThan(root.<String>get(criteria.getKey()), criteria.getValue().toString());
            case LIKE:
                return builder.like(root.<String>get(criteria.getKey()), criteria.getValue().toString());
            case STARTS_WITH:
                return builder.like(root.<String>get(criteria.getKey()), criteria.getValue() + "%");
            case ENDS_WITH:
                return builder.like(root.<String>get(criteria.getKey()), "%" + criteria.getValue());
            case CONTAINS:
                return builder.like((builder.lower(root.<String>get(criteria.getKey()))), "%" + criteria.getValue() + "%");
            case CONTAINS_WITH_SPACE:
                return builder.like((builder.lower(root.<String>get(criteria.getKey()))), "% " + criteria.getValue() + "%");
            case CONTAINS_ENDING:
                return builder.like((builder.lower(root.<String>get(criteria.getKey()))), criteria.getValue() + "%");
            default:
                return null;
        }
    }

}
