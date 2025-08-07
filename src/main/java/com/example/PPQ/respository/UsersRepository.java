package com.example.PPQ.respository;

import com.example.PPQ.Entity.UserEntity;
import com.example.PPQ.Payload.Projection_Interface.UserView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UsersRepository extends JpaRepository<UserEntity, Integer> {
    @Query(value = """
            SELECT 
                u.ID as id, 
                u.username as userName, 
                r.roleName as roleName
            FROM Users u 
            LEFT JOIN Roles r ON u.idRoles = r.ID
            """,
            countQuery = "SELECT COUNT(*) FROM Users",
            nativeQuery = true)
    Page<UserView> getAllUsersPagination(Pageable pageable);  // spring tu them limit va offset vao value

    boolean existsByUsername(String username);

    //    @Query("select u.username,u.ID from UserEntity u where u.idRoles = :idRole")
    List<UserEntity> findByIdRoles(int idRole);

    UserEntity findByUsername(String username);

    @Query(value = """
            SELECT 
                u.ID as id, 
                u.username as userName, 
                r.roleName as roleName
            FROM Users u 
            LEFT JOIN Roles r ON u.idRoles = r.ID
            WHERE 
                (:username IS NULL OR :username = '' OR u.username LIKE %:username%) 
                AND (:role IS NULL OR u.idRoles = :role)
            """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM Users u 
                    LEFT JOIN Roles r ON u.idRoles = r.ID
                    WHERE 
                        (:username IS NULL OR :username = '' OR u.username LIKE %:username%) 
                        AND (:role IS NULL OR u.idRoles = :role)
                    """,
            nativeQuery = true)
    Page<UserView> searchUsersWithPagination(String username, Integer role, Pageable pageable);


    List<UserEntity> findAllByIdIn(Set<Integer> id);

    @Modifying
    @Query(value = "with roleUsers as ( select r.ID from Roles as r where r.roleName = :roleName limit 1)\n" +
            "update Users u set u.idRoles = (select r.ID from roleUsers as r ) where u.ID = :idUser", nativeQuery = true)
    void setRolesUsers(String roleName, int idUser);

    @Modifying
    @Query("update UserEntity as u \n" +
            "set u.idRoles = null\n" +
            "where u.idRoles =:idRoles")
    void removeUserRole(int idRoles);

    @Query("Select u.username from UserEntity u where u.Id = :id")
    String findUserNameById(int id);
}
