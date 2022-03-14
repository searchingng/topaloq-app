package com.company.topaloq.spec;

import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Objects;

public class SpecificationBuilder<T> {
    private final Specification<T> specification;

    public SpecificationBuilder(String notNullField) {
        specification = Specification.where(
                (root, query, criteriaBuilder) ->
                        criteriaBuilder.isNotNull(root.get(notNullField))
        );
    }

    public SpecificationBuilder(Specification<T> spec) {
        this.specification = spec;
    }

    public SpecificationBuilder<T> isNotNull(String field) {
        return new SpecificationBuilder<>(
                specification.and(
                        (root, query, criteriaBuilder) ->
                                criteriaBuilder.isNotNull(root.get(field))
                ));
    }

    public SpecificationBuilder<T> isNull(String field) {
        return new SpecificationBuilder<>(
                specification.and(
                        (root, query, criteriaBuilder) ->
                                criteriaBuilder.isNull(root.get(field))
                ));
    }

    public SpecificationBuilder<T> in(String field, Collection<?> collection) {
        if (!Objects.isNull(collection) && !collection.isEmpty()) {
            return new SpecificationBuilder<>(
                    specification.and(
                            (root, query, criteriaBuilder) ->
                                    (root.get(field).in(collection))
                    ));
        }
        return new SpecificationBuilder<T>(specification);
    }

    public SpecificationBuilder<T> in(String field, Object... objs) {
        if (!Objects.isNull(objs)) {
            return new SpecificationBuilder<>(
                    specification.and(
                            (root, query, criteriaBuilder) ->
                                    (root.get(field).in(objs))
                    ));
        }
        return new SpecificationBuilder<T>(specification);
    }

    public SpecificationBuilder<T> equal(String field, Object obj) {
        if (!Objects.isNull(obj)) {
            return new SpecificationBuilder<>(
                    specification.and(
                            (root, query, criteriaBuilder) ->
                                    criteriaBuilder.equal(root.get(field), obj)
                    ));
        }
        return new SpecificationBuilder<T>(specification);
    }

    public SpecificationBuilder<T> notEqual(String field, Object obj) {
        if (!Objects.isNull(obj)) {
            return new SpecificationBuilder<>(
                    specification.and(
                            (root, query, criteriaBuilder) ->
                                    criteriaBuilder.notEqual(root.get(field), obj)
                    ));
        }
        return new SpecificationBuilder<T>(specification);
    }

    public SpecificationBuilder<T> like(String field, String val) {
        if (!Objects.isNull(val)) {
            return new SpecificationBuilder<>(
                    specification.and(
                            (root, query, criteriaBuilder) ->
                                    criteriaBuilder.like(root.get(field), val)
                    ));
        }
        return new SpecificationBuilder<T>(specification);
    }

    public SpecificationBuilder<T> contains(String field, String val) {
        if (!Objects.isNull(val)) {
            return new SpecificationBuilder<>(
                    specification.and(
                            (root, query, criteriaBuilder) ->
                                    criteriaBuilder.like(root.get(field), "%" + val + "%")
                    ));
        }
        return new SpecificationBuilder<T>(specification);
    }

    public SpecificationBuilder<T> endWith(String field, String val) {
        if (!Objects.isNull(val)) {
            return new SpecificationBuilder<>(
                    specification.and(
                            (root, query, criteriaBuilder) ->
                                    criteriaBuilder.like(root.get(field), "%" + val)
                    ));
        }
        return new SpecificationBuilder<T>(specification);
    }

    public SpecificationBuilder<T> startWith(String field, String val) {
        if (!Objects.isNull(val)) {
            return new SpecificationBuilder<>(
                    specification.and(
                            (root, query, criteriaBuilder) ->
                                    criteriaBuilder.like(root.get(field), val + "%")
                    ));
        }
        return new SpecificationBuilder<T>(specification);
    }

    public <Y extends Comparable<? super Y>> SpecificationBuilder<T> greaterThanOrEqualTo(String field, Y val) {
        if (!Objects.isNull(val)) {
            return new SpecificationBuilder<>(
                    specification.and(
                            (root, query, criteriaBuilder) ->
                                    criteriaBuilder.greaterThanOrEqualTo(root.get(field), val)
                    ));
        }
        return new SpecificationBuilder<T>(specification);
    }

    public <Y extends Comparable<? super Y>> SpecificationBuilder<T> lessThanOrEqualTo(String field, Y val) {
        if (!Objects.isNull(val)) {
            return new SpecificationBuilder<>(
                    specification.and(
                            (root, query, criteriaBuilder) ->
                                    criteriaBuilder.lessThanOrEqualTo(root.get(field), val)
                    ));
        }
        return new SpecificationBuilder<T>(specification);
    }

    public <Y extends Comparable<? super Y>> SpecificationBuilder<T> fromDate(String field, LocalDate date) {
        if (!Objects.isNull(date)) {
            return new SpecificationBuilder<>(
                    specification.and(
                            (root, query, criteriaBuilder) ->
                                    criteriaBuilder.greaterThanOrEqualTo(root.get(field), LocalDateTime
                                            .of(date, LocalTime.MIN))
                    ));
        }
        return new SpecificationBuilder<T>(specification);
    }

    public <Y extends Comparable<? super Y>> SpecificationBuilder<T> toDate(String field, LocalDate date) {
        if (!Objects.isNull(date)) {
            return new SpecificationBuilder<>(
                    specification.and(
                            (root, query, criteriaBuilder) ->
                                    criteriaBuilder.lessThanOrEqualTo(root.get(field), LocalDateTime
                                            .of(date, LocalTime.MAX))
                    ));

        }
        return new SpecificationBuilder<T>(specification);
    }

    public <Y extends Comparable<? super Y>> SpecificationBuilder<T> greaterThan(String field, Y val) {
        if (!Objects.isNull(val)) {
            return new SpecificationBuilder<>(
                    specification.and(
                            (root, query, criteriaBuilder) ->
                                    criteriaBuilder.greaterThan(root.get(field), val)
                    ));
        }
        return new SpecificationBuilder<T>(specification);
    }

    public <Y extends Comparable<? super Y>> SpecificationBuilder<T> lessThan(String field, Y val) {
        if (!Objects.isNull(val)) {
            return new SpecificationBuilder<>(
                    specification.and(
                            (root, query, criteriaBuilder) ->
                                    criteriaBuilder.lessThan(root.get(field), val)
                    ));
        }
        return new SpecificationBuilder<T>(specification);
    }

    public <Y extends Comparable<? super Y>> SpecificationBuilder<T> between(String field, Y from, Y to) {
        if (!Objects.isNull(from) && !Objects.isNull(to)) {
            return new SpecificationBuilder<>(
                    specification.and(
                            (root, query, criteriaBuilder) ->
                                    criteriaBuilder.between(root.get(field), from, to)
                    ));
        }
        return new SpecificationBuilder<T>(specification);
    }

    public Specification<T> build() {
        return specification;
    }


}
