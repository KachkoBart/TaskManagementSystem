package org.example.taskmanagementsystem.Api.Repository;

import org.example.taskmanagementsystem.Api.Repository.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
