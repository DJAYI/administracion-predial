package com.agmdesarrollos.geopix.config;

import org.springframework.data.jpa.domain.Specification;

public class GenericSpecification {

    /**
     * Filters by 'deleted' field.
     * If includeDeletes is true, it returns all records.
     * If includeDeletes is false, it returns only non-deleted records (deleted = false).
     */
    public static <T> Specification<T> filterDeleted(boolean includeDeletes) {
        return (root, query, cb) -> {
            if (includeDeletes) {
                return cb.conjunction(); // No filter (1=1)
            }
            return cb.equal(root.get("deleted"), false);
        };
    }
}
