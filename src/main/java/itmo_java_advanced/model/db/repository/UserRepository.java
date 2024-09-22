package itmo_java_advanced.model.db.repository;

import itmo_java_advanced.model.db.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where lower(u.name) like %:filter% or lower(u.surname) like %:filter%")
    Page<User> findAllWithNameAndSurnameFilter(Pageable pageRequest, @Param("filter") String filter);
}
