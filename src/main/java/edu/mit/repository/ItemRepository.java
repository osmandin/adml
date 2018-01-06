package edu.mit.repository;

import edu.mit.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository
        extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {
    Page<Item> findByItemId(long id, Pageable pageable);
}
