package com.example.PPQ.respository;

import com.example.PPQ.Entity.UserEntity;
import com.example.PPQ.Payload.Projection_Interface.UserView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UsersRepository extends JpaRepository<UserEntity,Integer> {
    @Query(value = "SELECT " +
            "u.ID as id, " +
            "u.username as userName, " +
            "r.roleName as roleName " +
            "From Users u " +
            "left join Roles r on u.idRoles = r.ID ",nativeQuery = true)
    List<UserView> getAllUsersBasic();
    boolean existsByUsername(String username);
//    @Query("select u.username,u.ID from UserEntity u where u.idRoles = :idRole")
    List<UserEntity> findByIdRoles(int idRole);
    UserEntity findByUsername(String username);
    @Query(value = "SELECT " +
            "u.ID as id, " +
            "u.username as userName, " +
            "r.roleName as roleName " +
            "From Users u " +
            "left join Roles r on u.idRoles = r.ID where " +
            "(:username IS NULL OR :username = '' OR u.username LIKE %:username%) AND " +
            "(:role IS NULL OR u.idRoles = :role)",nativeQuery = true)
    List<UserView> findByUsernameAndRoles(String username, Integer role);
    List<UserEntity> findAllByIdIn(Set<Integer> id);
    @Modifying
    @Query(value = "with roleUsers as ( select r.ID from Roles as r where r.roleName = :roleName limit 1)\n" +
            "update Users u set u.idRoles = (select r.ID from roleUsers as r ) where u.ID = :idUser",nativeQuery = true)
    void setRolesUsers( String roleName,  int idUser);
    @Modifying
    @Query("update UserEntity as u \n" +
            "set u.idRoles = null\n" +
            "where u.idRoles =:idRoles")
    void removeUserRole(int idRoles);
    @Query("Select u.username from UserEntity u where u.ID = :id")
    String findUserNameById(int id);
}
