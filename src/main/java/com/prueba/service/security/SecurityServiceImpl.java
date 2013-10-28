package com.prueba.service.security;

															
			
			

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.prueba.gui.BeanItem;
import com.prueba.model.userLogged.UserLogged;
import com.prueba.model.group.Group;
import com.prueba.model.group.GroupExample;
import com.prueba.model.grouppermission.GroupPermission;
import com.prueba.model.grouppermission.GroupPermissionExample;
import com.prueba.model.grouppermission.GroupPermissionExample.Criteria;
import com.prueba.model.permission.Permission;
import com.prueba.model.permission.PermissionExample;
import com.prueba.model.user.User;
import com.prueba.model.user.UserExample;
import com.prueba.model.usergroup.UserGroup;
import com.prueba.model.usergroup.UserGroupExample;
import com.prueba.model.vgrouppermission.VGroupPermission;
import com.prueba.model.vgrouppermission.VGroupPermissionExample;
import com.prueba.model.vusergroup.VUserGroup;
import com.prueba.model.vusergroup.VUserGroupExample;
import com.prueba.persistence.group.GroupMapper;
import com.prueba.persistence.grouppermission.GroupPermissionMapper;
import com.prueba.persistence.permission.PermissionMapper;
import com.prueba.persistence.user.UserMapper;
import com.prueba.persistence.usergroup.UserGroupMapper;
import com.prueba.persistence.vgrouppermission.VGroupPermissionMapper;
import com.prueba.persistence.vusergroup.VUserGroupMapper;
import com.prueba.util.Constants;
import com.prueba.util.DateUtils;

@Service("securityService")
public class SecurityServiceImpl implements SecurityService{
	@Autowired
	private GroupPermissionMapper groupPermissionMapper;
	@Autowired
	private UserGroupMapper userGroupMapper;
	@Autowired
	private VGroupPermissionMapper vgroupPermissionMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private VUserGroupMapper vuserGroupMapper;
	@Autowired
	private GroupMapper groupMapper;
	@Autowired
	private PermissionMapper permissionMapper;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Value("3")
	private int maxAttemptsLogin;
	@Value("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.-+_")
	private String passwordCharacter;
	@Value("4")
	private int passwordLength;
	@Value("^([a-z|A-Z|0-9|\\+|\\.|\\-|_|,])+$")
	private String passwordPattern;
	@Value("^(\\+?\\d{9,12})$")
	private String telephonePattern;
	private static final String EMAIL_PATTERN = "(\\w([_.\\-]*))+@(\\w([_.\\-]*))+\\.\\w{2,6}";
	@Value("10")
	private int timeBlockedLogin;
	@Value("6")
	private int timeValidatePasswordLogin;

	public void setMaxAttemptsLogin(int maxAttemptsLogin) {
		this.maxAttemptsLogin = maxAttemptsLogin;
	}

	public void setPasswordCharacter(String passwordCharacter) {
		this.passwordCharacter = passwordCharacter;
	}

	public void setPasswordLength(int passwordLength) {
		this.passwordLength = passwordLength;
	}

	public void setPasswordPattern(String passwordPattern) {
		this.passwordPattern = passwordPattern;
	}

	public void setTelephonePattern(String telephonePattern) {
		this.telephonePattern = telephonePattern;
	}

	public void setTimeBlockedLogin(int timeBlockedLogin) {
		this.timeBlockedLogin = timeBlockedLogin;
	}

	public void setTimeValidatePasswordLogin(int timeValidatePasswordLogin) {
		this.timeValidatePasswordLogin = timeValidatePasswordLogin;
	}

	@Transactional(readOnly=true)
	public List<VGroupPermission> getGroupPermissionExample(
			VGroupPermissionExample example) {
		return vgroupPermissionMapper.selectByExample(example);
	}

	public UserLogged getUserlogged() {
		Object obj = SecurityContextHolder.getContext().getAuthentication() == null ? null
				: SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal();
		if (obj instanceof UserLogged)
			return (UserLogged) obj;
		return null;
	}

	@Transactional(readOnly=false, propagation = Propagation.REQUIRES_NEW)
	public void deleteUser(User aUser) {
		UserGroupExample userGroupExample = new UserGroupExample();
		userGroupExample.createCriteria().andIdUserEqualTo(aUser.getId());
		userGroupMapper.deleteByExample(userGroupExample);
		userMapper.deleteByPrimaryKey(aUser.getId());
	}

	@Transactional(readOnly=false, propagation = Propagation.REQUIRES_NEW)
	public void updateUser(User aUser) {
		userMapper.updateByPrimaryKey(aUser);
	}

	@Transactional(readOnly=false, propagation = Propagation.REQUIRES_NEW)
	public void attemptsIncreaseLogin(String usuario) {
		int loginAttempts = 0;
		UserExample userExample = new UserExample();

		com.prueba.model.user.UserExample.Criteria criteria = userExample
				.createCriteria();
		criteria.andUsernameEqualTo(usuario);
		List<User> lista = userMapper.selectByExample(userExample);

		if (lista.size() > 0) {
			User user = lista.get(0);
			if (Constants.getBoolean(user.getEnable())
					&& !Constants.getBoolean(user.getBlocked())) {
				// Ponemos los intentos de login a 0 cuando pasa un dia desde
				// ultimo intento de login
				if (user.getDateLastLogin() != null
						&& user.getDateLastLogin().getTime() != 0) {
					Calendar currentDay = Calendar.getInstance();
					currentDay.setTime(new Date());
					currentDay.set(Calendar.HOUR_OF_DAY, 0);
					currentDay.set(Calendar.MINUTE, 0);
					currentDay.set(Calendar.SECOND, 0);
					currentDay.set(Calendar.MILLISECOND, 0);

					Calendar lastDayLogin = Calendar.getInstance();
					lastDayLogin.setTime(user.getDateLastLogin());
					lastDayLogin.set(Calendar.HOUR_OF_DAY, 0);
					lastDayLogin.set(Calendar.MINUTE, 0);
					lastDayLogin.set(Calendar.SECOND, 0);
					lastDayLogin.set(Calendar.MILLISECOND, 0);
					long valor = currentDay.getTime().getTime()
							- lastDayLogin.getTime().getTime();
					if (valor >= 86400000)
						user.setAttemptsLogin(Constants.getCero(user
								.getAttemptsLogin()));
				}

				if (user.getAttemptsLogin() != null) {
					loginAttempts = user.getAttemptsLogin().intValue();
				}

				loginAttempts++;

				if (loginAttempts < maxAttemptsLogin) {
					user.setAttemptsLogin(Constants.getValor(
							user.getAttemptsLogin(), loginAttempts));
					userMapper.updateByPrimaryKey(user);
				} else {
					user.setBlocked(Constants.getTrue(user.getId()));
					user.setAttemptsLogin(Constants.getValor(
							user.getAttemptsLogin(), loginAttempts));
					userMapper.updateByPrimaryKey(user);
				}
			} else if (Constants.getBoolean(user.getEnable())
					&& Constants.getBoolean(user.getBlocked())) {
				if (checKCurrentBlockedUser(user)) {
					user.setBlocked(Constants.getFalse(user.getBlocked()));
					user.setAttemptsLogin(Constants.getCero(user
							.getAttemptsLogin()));
					userMapper.updateByPrimaryKey(user);
				}
			}
		}
	}
	
	@Transactional(readOnly=true)
	public List<User> getSelectExample(UserExample userExample) {
		return userMapper.selectByExample(userExample);
	}

	@Transactional(readOnly=false, propagation = Propagation.REQUIRES_NEW)
	public void addUser(User aUser) {
		userMapper.insert(aUser);
	}

	@Transactional(readOnly=false, propagation = Propagation.REQUIRES_NEW)
	public void deleteAllGroup(String idGroup) {
		UserGroupExample example = new UserGroupExample();
		com.prueba.model.usergroup.UserGroupExample.Criteria criteria = example
				.createCriteria();
		criteria.andIdGroupEqualTo(Constants.parseNumerInteger(idGroup));
		userGroupMapper.deleteByExample(example);
	}

	@Transactional(readOnly=true)
	public List<VUserGroup> getUserGroupExample(VUserGroupExample example) {
		return vuserGroupMapper.selectByExample(example);
	}

	@Transactional(readOnly=false, propagation = Propagation.REQUIRES_NEW)
	public void deleteGroup(Group aGroup) {
		UserGroupExample example = new UserGroupExample();
		com.prueba.model.usergroup.UserGroupExample.Criteria criteria = example
				.createCriteria();
		criteria.andIdGroupEqualTo(aGroup.getId());
		userGroupMapper.deleteByExample(example);

		GroupPermissionExample example2 = new GroupPermissionExample();
		Criteria criteria2 = example2.createCriteria();
		criteria2.andIdGroupEqualTo(aGroup.getId());
		groupPermissionMapper.deleteByExample(example2);

		groupMapper.deleteByPrimaryKey(aGroup.getId());
	}

	@Transactional(readOnly=false, propagation = Propagation.REQUIRES_NEW)
	public void deletePermission(Permission aPermission) {
		GroupPermissionExample example2 = new GroupPermissionExample();
		Criteria criteria2 = example2.createCriteria();
		criteria2.andIdPermissionEqualTo(aPermission.getId());
		groupPermissionMapper.deleteByExample(example2);

		permissionMapper.deleteByPrimaryKey(aPermission.getId());
	}

	@Transactional(readOnly=true)
	public List<Permission> getPermissions() {
		return permissionMapper.selectByExample(new PermissionExample());
	}

	@Transactional(readOnly=true)
	public List<Group> getGroups() {
		return groupMapper.selectByExample(new GroupExample());
	}

	@Transactional(readOnly=true)
	public List<User> getUsers() {
		return userMapper.selectByExample(new UserExample());
	}

	public String generatePassword() {
		String pswd = "";
		for (int i = 0; i < passwordLength; i++) {
			pswd += (passwordCharacter
					.charAt((int) (Math.random() * passwordCharacter.length())));
		}
		return pswd;
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRES_NEW)
	public void zeroattemptsLogin(String usuario) {
		UserExample example = new UserExample();
		
		com.prueba.model.user.UserExample.Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(usuario);	
		List<User> lista = userMapper.selectByExample(example);
		
		if (lista.size() > 0) {
			User user = lista.get(0);
			user.setAttemptsLogin(Constants.getValor(user.getAttemptsLogin(),0));
			userMapper.updateByPrimaryKey(user);
		}		
	}

	public boolean checkPasswordCharacter(String password) {
		return Pattern.compile(passwordPattern).matcher(password).find();
	}

	public boolean checkTelephone(String telephone) {
		return Pattern.compile(telephonePattern).matcher(telephone).find();
	}

	public boolean checkEmail(String email) {
		return Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
	}

	public boolean checkPasswordLenght(String password) {
		return password.trim().length() >= passwordLength;
	}

	public boolean checkValidatePasswordUser(User user) {
		if (user.getDateLastPassword() == null)
			return false;
		else {
			Timestamp currentTime = new Timestamp(new Date().getTime());
			Timestamp lastPassword = DateUtils.diaSumandoMeses(
					timeValidatePasswordLogin, user.getDateLastPassword());
			return currentTime.after(lastPassword);
		}
	}

	public boolean checKCurrentBlockedUser(User user) {
		if (Constants.getBoolean(user.getBlocked())) {
			Timestamp currentTime = new Timestamp(new Date().getTime());
			Timestamp lastPassword = DateUtils.diaSumandoMinutos(
					timeBlockedLogin, user.getDateLastLogin());
			return currentTime.after(lastPassword);
		}
		return true;
	}

	public int getMinLengthPassword() {
		return passwordLength;
	}

	public UserDetails obtenerUsuario() {
		UserDetails userDetails = new UserLogged(new User());
		((UserLogged) userDetails).getUser().setUsername(Constants.ANONYMOUSLY);
	
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
				Object object = SecurityContextHolder.getContext()
						.getAuthentication().getPrincipal();
			if (object instanceof UserDetails)
				userDetails = (UserDetails) object;
		}
		return userDetails;
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRES_NEW)
	public void addManyGroupPermission(String idGroup,
			List<BeanItem> listPermission) {
		for(int i=0; i < listPermission.size(); i++){
			BeanItem item = listPermission.get(i);
			GroupPermission groupPermission = new GroupPermission();
			groupPermission.setIdGroup(Constants.parseNumerInteger(idGroup));
			groupPermission.setIdPermission(Constants.parseNumerInteger(item.getId()));
			groupPermissionMapper.insert(groupPermission);
		}
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRES_NEW)
	public void addManyGroupPermissionEdit(String idGroup,
			List<BeanItem> listPermission) {
		GroupPermissionExample example = new GroupPermissionExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdGroupEqualTo(Constants.parseNumerInteger(idGroup));
		groupPermissionMapper.deleteByExample(example);		
		addManyGroupPermission(idGroup, listPermission);
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRES_NEW)
	public void addManyGroupUser(String idUser, List<BeanItem> listGroup) {
		for(int i=0; i < listGroup.size(); i++){
			BeanItem item = listGroup.get(i);
			UserGroup userGroup = new UserGroup();
			userGroup.setIdGroup(Constants.parseNumerInteger(item.getId()));
			userGroup.setIdUser(Constants.parseNumerInteger(idUser));
			userGroupMapper.insert(userGroup);
		}
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRES_NEW)
	public void addManyGroupUserEdit(String idUser, List<BeanItem> listGroup) {
		UserGroupExample example = new UserGroupExample();
		com.prueba.model.usergroup.UserGroupExample.Criteria criteria = example.createCriteria();
		criteria.andIdUserEqualTo(Constants.parseNumerInteger(idUser));
		userGroupMapper.deleteByExample(example);		
		addManyGroupUser(idUser, listGroup);
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRES_NEW)
	public void addGroupUser(String idUser) {
		UserGroupExample example = new UserGroupExample();
		com.prueba.model.usergroup.UserGroupExample.Criteria criteria = example.createCriteria();
		criteria.andIdUserEqualTo(Constants.parseNumerInteger(idUser));
		userGroupMapper.deleteByExample(example);		
		UserGroup userGroup = new UserGroup();
		userGroup.setIdGroup(Constants.parseNumerInteger("2"));
		userGroup.setIdUser(Constants.parseNumerInteger(idUser));
		userGroupMapper.insert(userGroup);
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRES_NEW)
	public void addManyUserGroupEdit(String idGroup, List<BeanItem> listUser) {
		UserGroupExample example = new UserGroupExample();
		com.prueba.model.usergroup.UserGroupExample.Criteria criteria = example.createCriteria();
		criteria.andIdGroupEqualTo(Constants.parseNumerInteger(idGroup));
		userGroupMapper.deleteByExample(example);		
		addManyUserGroup(idGroup, listUser);
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRES_NEW)
	public void addManyUserGroup(String idGroup, List<BeanItem> listUser) {
		for(int i=0; i < listUser.size(); i++){
			BeanItem item = listUser.get(i);
			UserGroup userGroup = new UserGroup();
			userGroup.setIdGroup(Constants.parseNumerInteger(idGroup));
			userGroup.setIdUser(Constants.parseNumerInteger(item.getId()));
			userGroupMapper.insert(userGroup);
		}
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRES_NEW)
	public int changePassword(String oldPassword,String newPassword){
		String oldEncode = encodePassword(oldPassword);
		if (!oldEncode.equals(getUserlogged().getPassword())) {
			return -1;
		}
		if(!checkPasswordLenght(newPassword))
		{
			return -2;
		}
		if(!checkPasswordCharacter(newPassword))
		{
			return -3;
		}
		String newEncode = encodePassword(newPassword);
		UserLogged userLogged = getUserlogged();
		userLogged.getUser().setPassword(newEncode);
		userMapper.updateByPrimaryKey(userLogged.getUser());
		return 0;
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRES_NEW)
	public int changePasswordRequired(String oldPassword, String newPassword) {
		String oldEncode = encodePassword(oldPassword);
		if (!oldEncode.equals(getUserlogged().getPassword())) {
			return -1;
		}
		if(!checkPasswordLenght(newPassword))
		{
			return -2;
		}
		if(!checkPasswordCharacter(newPassword))
		{
			return -3;
		}
		String newEncode = encodePassword(newPassword);
		UserLogged userLogged = getUserlogged();
		userLogged.getUser().setPassword(newEncode);
		userLogged.setValidatePassword(true);
		userLogged.getUser().setDateLastPassword(new Timestamp(new Date().getTime()));
		userMapper.updateByPrimaryKey(userLogged.getUser());
		return 0;
	}
	
	@Transactional(readOnly=false, propagation = Propagation.REQUIRES_NEW)
	public void deleteAllGroupPermission(String idGroup) {
		GroupPermissionExample example = new GroupPermissionExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdGroupEqualTo(Constants.parseNumerInteger(idGroup));
		groupPermissionMapper.deleteByExample(example);	
	}
	
	public String encodePassword(String password) {
		return passwordEncoder.encodePassword(password, null);
	}
}