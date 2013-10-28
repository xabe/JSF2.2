package com.prueba.service.security;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.prueba.gui.BeanItem;
import com.prueba.model.userLogged.UserLogged;
import com.prueba.model.group.Group;
import com.prueba.model.permission.Permission;
import com.prueba.model.user.User;
import com.prueba.model.user.UserExample;
import com.prueba.model.vgrouppermission.VGroupPermission;
import com.prueba.model.vgrouppermission.VGroupPermissionExample;
import com.prueba.model.vusergroup.VUserGroup;
import com.prueba.model.vusergroup.VUserGroupExample;

public interface SecurityService {

	List<VGroupPermission> getGroupPermissionExample(
			VGroupPermissionExample example);
	
	List<User> getSelectExample(UserExample userExample);
	
	UserLogged getUserlogged();
	
	UserDetails obtenerUsuario();
	
	void deleteUser(User aUser);
	
	void updateUser(User aUser);

	void attemptsIncreaseLogin(String usuario);
	
	void addUser(User aUser);

	void deleteAllGroup(String idGroup);
	
	List<VUserGroup> getUserGroupExample(VUserGroupExample example);
	
	void deleteGroup(Group aGroup);
	
	void deletePermission(Permission aPermission);
	
	List<Permission> getPermissions();
	
	List<User> getUsers();
	
	List<Group> getGroups();
	
	String generatePassword();
	
	void zeroattemptsLogin(String usuario);
	
	boolean checkPasswordCharacter(String password);
	
	boolean checkPasswordLenght(String password);
	
	int getMinLengthPassword();
	
	boolean checkTelephone(String telephone);
	
	boolean checkEmail(String email);
	
	boolean checkValidatePasswordUser(User user);
	
	boolean checKCurrentBlockedUser(User user);
	
	void addManyGroupPermission(String idGroup,List<BeanItem> listPermission);

	void addManyGroupPermissionEdit(String idGroup,List<BeanItem> listPermission);

	void deleteAllGroupPermission(String idGroup);
	
	void addManyUserGroupEdit(String idGroup, List<BeanItem> listUser);
	
	void addManyGroupUserEdit(String idUser, List<BeanItem> listGroup);
	
	void addGroupUser(String idUser);
	
	void addManyUserGroup(String idGroup, List<BeanItem> listUser);
	
	void addManyGroupUser(String idUser, List<BeanItem> listGroup);
	
	String encodePassword(String password);
	
	int changePassword(String oldPassword, String newPassword);
	
	int changePasswordRequired(String oldPassword, String newPassword);
}