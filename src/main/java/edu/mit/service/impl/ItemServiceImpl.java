package edu.mit.service.impl;

import edu.mit.domain.Item;
import edu.mit.repository.ItemRepository;
import edu.mit.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemServiceImpl implements ItemService {

    private ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Page<Item> findAll(Specification<Item> var1, Pageable pageable) {
        return itemRepository.findAll(var1, pageable);
    }

    @Override
    public Page<Item> findByItemId(long id, Pageable pageable) {
        return itemRepository.findByItemId(id, pageable);
    }


    @Transactional
    @Override
    public Iterable<Item> save(Iterable<Item> items) {
        return itemRepository.save(items);
    }

}
