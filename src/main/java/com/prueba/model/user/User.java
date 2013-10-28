package com.prueba.model.user;

import java.io.Serializable;
import java.util.Date;

import com.prueba.model.EntityBase;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import com.prueba.constraints.XSS;
import com.prueba.gui.importer.ConstraintImportField;
import com.prueba.gui.importer.ConstraintImportTable;

@XmlRootElement
@ConstraintImportTable(keys={"id"})
public class User extends EntityBase implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user.id
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
	@NotNull
	@ConstraintImportField(columnName="id",messageError="error.user.id",pattern="^(\\-)?\\d+(\\.\\d+)?$",isPK=true,isCompulsory=true)
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user.name
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
	@XSS
	@NotNull
	@Size(min=0,max=255)
	@ConstraintImportField(columnName="name",messageError="error.user.name",pattern=".+",isPK=false,isCompulsory=false)
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user.surname1
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
	@XSS
	@NotNull
	@Size(min=0,max=255)
	@ConstraintImportField(columnName="surname1",messageError="error.user.surname1",pattern=".+",isPK=false,isCompulsory=false)
    private String surname1;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user.email
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
	@XSS
	@NotNull
	@Size(min=0,max=255)
	@ConstraintImportField(columnName="email",messageError="error.user.email",pattern=".+",isPK=false,isCompulsory=false)
    private String email;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user.telephone
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
	@NotNull
	@ConstraintImportField(columnName="telephone",messageError="error.user.telephone",pattern="^(\\-)?\\d+(\\.\\d+)?$",isPK=false,isCompulsory=false)
    private Integer telephone;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user.enable
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
	@NotNull
	@ConstraintImportField(columnName="enable",messageError="error.user.enable",pattern="^(\\-)?\\d+(\\.\\d+)?$",isPK=false,isCompulsory=false)
    private Integer enable;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user.blocked
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
	@NotNull
	@ConstraintImportField(columnName="blocked",messageError="error.user.blocked",pattern="^(\\-)?\\d+(\\.\\d+)?$",isPK=false,isCompulsory=false)
    private Integer blocked;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user.attempts_login
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
	@NotNull
	@ConstraintImportField(columnName="attemptsLogin",messageError="error.user.attemptsLogin",pattern="^(\\-)?\\d+(\\.\\d+)?$",isPK=false,isCompulsory=false)
    private Integer attemptsLogin;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user.surname2
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
	@XSS
	@NotNull
	@Size(min=0,max=255)
	@ConstraintImportField(columnName="surname2",messageError="error.user.surname2",pattern=".+",isPK=false,isCompulsory=false)
    private String surname2;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user.username
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
	@XSS
	@NotNull
	@Size(min=0,max=255)
	@ConstraintImportField(columnName="username",messageError="error.user.username",pattern=".+",isPK=false,isCompulsory=false)
    private String username;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user.password
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
	@XSS
	@NotNull
	@Size(min=0,max=255)
	@ConstraintImportField(columnName="password",messageError="error.user.password",pattern=".+",isPK=false,isCompulsory=false)
    private String password;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user.picture
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
	@XSS
	@NotNull
	@Size(min=0,max=255)
	@ConstraintImportField(columnName="picture",messageError="error.user.picture",pattern=".+",isPK=false,isCompulsory=false)
    private String picture;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user.date_last_password
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
	@NotNull
	@ConstraintImportField(columnName="dateLastPassword",messageError="error.user.dateLastPassword",pattern="^^(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d$",isPK=false,isCompulsory=false)
    private Date dateLastPassword;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_user.date_last_login
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
	@NotNull
	@ConstraintImportField(columnName="dateLastLogin",messageError="error.user.dateLastLogin",pattern="^^(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d$",isPK=false,isCompulsory=false)
    private Date dateLastLogin;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_user
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user.id
     *
     * @return the value of t_user.id
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user.id
     *
     * @param id the value for t_user.id
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user.name
     *
     * @return the value of t_user.name
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user.name
     *
     * @param name the value for t_user.name
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user.surname1
     *
     * @return the value of t_user.surname1
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public String getSurname1() {
        return surname1;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user.surname1
     *
     * @param surname1 the value for t_user.surname1
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public void setSurname1(String surname1) {
        this.surname1 = surname1 == null ? null : surname1.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user.email
     *
     * @return the value of t_user.email
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public String getEmail() {
        return email;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user.email
     *
     * @param email the value for t_user.email
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user.telephone
     *
     * @return the value of t_user.telephone
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public Integer getTelephone() {
        return telephone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user.telephone
     *
     * @param telephone the value for t_user.telephone
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public void setTelephone(Integer telephone) {
        this.telephone = telephone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user.enable
     *
     * @return the value of t_user.enable
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public Integer getEnable() {
        return enable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user.enable
     *
     * @param enable the value for t_user.enable
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user.blocked
     *
     * @return the value of t_user.blocked
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public Integer getBlocked() {
        return blocked;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user.blocked
     *
     * @param blocked the value for t_user.blocked
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public void setBlocked(Integer blocked) {
        this.blocked = blocked;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user.attempts_login
     *
     * @return the value of t_user.attempts_login
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public Integer getAttemptsLogin() {
        return attemptsLogin;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user.attempts_login
     *
     * @param attemptsLogin the value for t_user.attempts_login
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public void setAttemptsLogin(Integer attemptsLogin) {
        this.attemptsLogin = attemptsLogin;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user.surname2
     *
     * @return the value of t_user.surname2
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public String getSurname2() {
        return surname2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user.surname2
     *
     * @param surname2 the value for t_user.surname2
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public void setSurname2(String surname2) {
        this.surname2 = surname2 == null ? null : surname2.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user.username
     *
     * @return the value of t_user.username
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public String getUsername() {
        return username;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user.username
     *
     * @param username the value for t_user.username
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user.password
     *
     * @return the value of t_user.password
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user.password
     *
     * @param password the value for t_user.password
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user.picture
     *
     * @return the value of t_user.picture
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public String getPicture() {
        return picture;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user.picture
     *
     * @param picture the value for t_user.picture
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public void setPicture(String picture) {
        this.picture = picture == null ? null : picture.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user.date_last_password
     *
     * @return the value of t_user.date_last_password
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public Date getDateLastPassword() {
        return dateLastPassword;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user.date_last_password
     *
     * @param dateLastPassword the value for t_user.date_last_password
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public void setDateLastPassword(Date dateLastPassword) {
        this.dateLastPassword = dateLastPassword;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_user.date_last_login
     *
     * @return the value of t_user.date_last_login
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public Date getDateLastLogin() {
        return dateLastLogin;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_user.date_last_login
     *
     * @param dateLastLogin the value for t_user.date_last_login
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public void setDateLastLogin(Date dateLastLogin) {
        this.dateLastLogin = dateLastLogin;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        User other = (User) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getSurname1() == null ? other.getSurname1() == null : this.getSurname1().equals(other.getSurname1()))
            && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
            && (this.getTelephone() == null ? other.getTelephone() == null : this.getTelephone().equals(other.getTelephone()))
            && (this.getEnable() == null ? other.getEnable() == null : this.getEnable().equals(other.getEnable()))
            && (this.getBlocked() == null ? other.getBlocked() == null : this.getBlocked().equals(other.getBlocked()))
            && (this.getAttemptsLogin() == null ? other.getAttemptsLogin() == null : this.getAttemptsLogin().equals(other.getAttemptsLogin()))
            && (this.getSurname2() == null ? other.getSurname2() == null : this.getSurname2().equals(other.getSurname2()))
            && (this.getUsername() == null ? other.getUsername() == null : this.getUsername().equals(other.getUsername()))
            && (this.getPassword() == null ? other.getPassword() == null : this.getPassword().equals(other.getPassword()))
            && (this.getPicture() == null ? other.getPicture() == null : this.getPicture().equals(other.getPicture()))
            && (this.getDateLastPassword() == null ? other.getDateLastPassword() == null : this.getDateLastPassword().equals(other.getDateLastPassword()))
            && (this.getDateLastLogin() == null ? other.getDateLastLogin() == null : this.getDateLastLogin().equals(other.getDateLastLogin()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getSurname1() == null) ? 0 : getSurname1().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getTelephone() == null) ? 0 : getTelephone().hashCode());
        result = prime * result + ((getEnable() == null) ? 0 : getEnable().hashCode());
        result = prime * result + ((getBlocked() == null) ? 0 : getBlocked().hashCode());
        result = prime * result + ((getAttemptsLogin() == null) ? 0 : getAttemptsLogin().hashCode());
        result = prime * result + ((getSurname2() == null) ? 0 : getSurname2().hashCode());
        result = prime * result + ((getUsername() == null) ? 0 : getUsername().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getPicture() == null) ? 0 : getPicture().hashCode());
        result = prime * result + ((getDateLastPassword() == null) ? 0 : getDateLastPassword().hashCode());
        result = prime * result + ((getDateLastLogin() == null) ? 0 : getDateLastLogin().hashCode());
        return result;
    }
}