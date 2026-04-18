/**package com.asim.studentmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.asim.studentmanagement.model.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    boolean existsByUsername(String username);  // Fixed: username (not usernmae)
    Users findByUsername(String username);
    // other methods...
}
*/
package com.asim.studentmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.asim.studentmanagement.model.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long>
{
	boolean existsByUsername(String username);
    Optional<Users> findByUsername(String username);
}