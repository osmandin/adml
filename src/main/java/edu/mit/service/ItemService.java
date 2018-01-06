package edu.mit.service;

import edu.mit.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface ItemService {

    Page<Item> findAll(Specification<Item> var1, Pageable pageable);

    Page<Item> findByItemId(long id, Pageable pageable);

    Iterable<Item> save(Iterable<Item> items);

}
