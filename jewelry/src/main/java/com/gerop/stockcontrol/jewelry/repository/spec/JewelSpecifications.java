package com.gerop.stockcontrol.jewelry.repository.spec;

import com.gerop.stockcontrol.jewelry.model.entity.Jewel;
import com.gerop.stockcontrol.jewelry.model.entity.enums.StockCondition;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class JewelSpecifications {

    public static Specification<Jewel> belongsToUser(Long userId) {
        return (root, query, cb) ->
                userId == null ? null : cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Jewel> nameContains(String name) {
        return (root, query, cb) ->
                (name == null || name.isBlank())
                        ? null
                        : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Jewel> skuEquals(String sku) {
        return (root, query, cb) ->
                sku == null ? null : cb.equal(root.get("sku"), sku);
    }

    public static Specification<Jewel> categoryIs(Long categoryId) {
        return (root, query, cb) ->
                categoryId == null ? null : cb.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Jewel> subcategoryIs(Long subcategoryId) {
        return (root, query, cb) ->
                subcategoryId == null ? null : cb.equal(root.get("subcategory").get("id"), subcategoryId);
    }

    public static Specification<Jewel> hasMetals(Set<Long> metalIds) {
        return (root, query, cb) -> {
            if (metalIds == null || metalIds.isEmpty()) return null;

            query.distinct(true);

            Join<Jewel, ?> join = root.join("metals", JoinType.INNER);

            Predicate inClause = join.get("id").in(metalIds);

            return inClause;
        };
    }

    public static Specification<Jewel> hasStones(Set<Long> stoneIds) {
        return (root, query, cb) -> {
            if (stoneIds == null || stoneIds.isEmpty()) return null;

            query.distinct(true);

            Join<Jewel, ?> join = root.join("stones", JoinType.INNER);

            return join.get("id").in(stoneIds);
        };
    }

    public static Specification<Jewel> inInventory(Long inventoryId) {
        return (root, query, cb) -> {
            if (inventoryId == null) return null;

            query.distinct(true);

            Join<Jewel, ?> join = root.join("inventories", JoinType.INNER);

            return cb.equal(join.get("id"), inventoryId);
        };
    }

    public static Specification<Jewel> weightBetween(Float min, Float max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return null;
            if (min != null && max != null) return cb.between(root.get("weight"), min, max);
            if (min != null) return cb.greaterThanOrEqualTo(root.get("weight"), min);
            return cb.lessThanOrEqualTo(root.get("weight"), max);
        };
    }

    public static Specification<Jewel> sizeBetween(Float min, Float max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return null;
            if (min != null && max != null) return cb.between(root.get("size"), min, max);
            if (min != null) return cb.greaterThanOrEqualTo(root.get("size"), min);
            return cb.lessThanOrEqualTo(root.get("size"), max);
        };
    }

    public static Specification<Jewel> activeIs(Boolean active) {
        return (root, query, cb) ->
                active == null ? null : cb.equal(root.get("active"), active);
    }
    
    public static Specification<Jewel> joinStock(Long inventoryId) {
        return (root, query, cb) -> {
            if (inventoryId == null) return null;

            root.join("stockByInventory", JoinType.LEFT)
                    .on(cb.equal(root.get("stockByInventory").get("inventory").get("id"), inventoryId));

            return cb.conjunction();
        };
    }

    public static Specification<Jewel> hasPendingRestock(Long inventoryId, Boolean hasPending) {
        return (root, query, cb) -> {
            if (hasPending == null || inventoryId == null) return null;

            query.distinct(true);

            Join<Object, Object> join = root.join("pendingRestock", JoinType.LEFT);

            Predicate sameInventory =
                    cb.equal(join.get("inventory").get("id"), inventoryId);

            if (hasPending) {
                return sameInventory; // existe pending en ese inventario
            } else {
                // joyas SIN pending en ese inventario
                return cb.or(cb.isNull(join.get("id")), cb.not(sameInventory));
            }
        };
    }

    public static Specification<Jewel> stockCondition(Long inventoryId, StockCondition condition) {
        return (root, query, cb) -> {
            if (condition == null || inventoryId == null) return null;

            query.distinct(true);

            Join<Object, Object> stock = root.join("stockByInventory", JoinType.LEFT);

            Predicate sameInventory =
                    cb.equal(stock.get("inventory").get("id"), inventoryId);

            return switch (condition) {
                case POSITIVE -> cb.and(sameInventory, cb.greaterThan(stock.get("stock"), 0));
                case ZERO     -> cb.and(sameInventory, cb.equal(stock.get("stock"), 0));
            };
        };
    }
}
