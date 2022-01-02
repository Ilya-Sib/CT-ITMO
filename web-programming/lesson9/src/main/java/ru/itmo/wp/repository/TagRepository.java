package ru.itmo.wp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.wp.domain.Role;
import ru.itmo.wp.domain.Tag;

import javax.validation.constraints.NotNull;

public interface TagRepository extends JpaRepository<Tag, Long> {
    int countByName(String name);
    Tag findTagByName(String name);
}
