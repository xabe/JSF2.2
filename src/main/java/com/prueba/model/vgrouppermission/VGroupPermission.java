package com.prueba.model.vgrouppermission;

import java.io.Serializable;

import com.prueba.model.EntityBase;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import com.prueba.constraints.XSS;
import com.prueba.gui.importer.ConstraintImportField;
import com.prueba.gui.importer.ConstraintImportTable;

@XmlRootElement
@ConstraintImportTable(keys={})
public class VGroupPermission extends EntityBase implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column v_group_permission.id
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
	@NotNull
	@ConstraintImportField(columnName="id",messageError="error.vgrouppermission.id",pattern="^(\\-)?\\d+(\\.\\d+)?$",isPK=false,isCompulsory=false)
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column v_group_permission.id_group
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
	@NotNull
	@ConstraintImportField(columnName="idGroup",messageError="error.vgrouppermission.idGroup",pattern="^(\\-)?\\d+(\\.\\d+)?$",isPK=false,isCompulsory=false)
    private Integer idGroup;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column v_group_permission.name_group
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
	@XSS
	@NotNull
	@Size(min=0,max=45)
	@ConstraintImportField(columnName="nameGroup",messageError="error.vgrouppermission.nameGroup",pattern=".+",isPK=false,isCompulsory=false)
    private String nameGroup;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column v_group_permission.id_permission
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
	@NotNull
	@ConstraintImportField(columnName="idPermission",messageError="error.vgrouppermission.idPermission",pattern="^(\\-)?\\d+(\\.\\d+)?$",isPK=false,isCompulsory=false)
    private Integer idPermission;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column v_group_permission.name_permission
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
	@XSS
	@NotNull
	@Size(min=0,max=255)
	@ConstraintImportField(columnName="namePermission",messageError="error.vgrouppermission.namePermission",pattern=".+",isPK=false,isCompulsory=false)
    private String namePermission;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table v_group_permission
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column v_group_permission.id
     *
     * @return the value of v_group_permission.id
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column v_group_permission.id
     *
     * @param id the value for v_group_permission.id
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column v_group_permission.id_group
     *
     * @return the value of v_group_permission.id_group
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public Integer getIdGroup() {
        return idGroup;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column v_group_permission.id_group
     *
     * @param idGroup the value for v_group_permission.id_group
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public void setIdGroup(Integer idGroup) {
        this.idGroup = idGroup;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column v_group_permission.name_group
     *
     * @return the value of v_group_permission.name_group
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public String getNameGroup() {
        return nameGroup;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column v_group_permission.name_group
     *
     * @param nameGroup the value for v_group_permission.name_group
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public void setNameGroup(String nameGroup) {
        this.nameGroup = nameGroup == null ? null : nameGroup.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column v_group_permission.id_permission
     *
     * @return the value of v_group_permission.id_permission
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public Integer getIdPermission() {
        return idPermission;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column v_group_permission.id_permission
     *
     * @param idPermission the value for v_group_permission.id_permission
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public void setIdPermission(Integer idPermission) {
        this.idPermission = idPermission;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column v_group_permission.name_permission
     *
     * @return the value of v_group_permission.name_permission
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public String getNamePermission() {
        return namePermission;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column v_group_permission.name_permission
     *
     * @param namePermission the value for v_group_permission.name_permission
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    public void setNamePermission(String namePermission) {
        this.namePermission = namePermission == null ? null : namePermission.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table v_group_permission
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
        VGroupPermission other = (VGroupPermission) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getIdGroup() == null ? other.getIdGroup() == null : this.getIdGroup().equals(other.getIdGroup()))
            && (this.getNameGroup() == null ? other.getNameGroup() == null : this.getNameGroup().equals(other.getNameGroup()))
            && (this.getIdPermission() == null ? other.getIdPermission() == null : this.getIdPermission().equals(other.getIdPermission()))
            && (this.getNamePermission() == null ? other.getNamePermission() == null : this.getNamePermission().equals(other.getNamePermission()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table v_group_permission
     *
     * @mbggenerated Tue Oct 08 08:20:18 CEST 2013
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getIdGroup() == null) ? 0 : getIdGroup().hashCode());
        result = prime * result + ((getNameGroup() == null) ? 0 : getNameGroup().hashCode());
        result = prime * result + ((getIdPermission() == null) ? 0 : getIdPermission().hashCode());
        result = prime * result + ((getNamePermission() == null) ? 0 : getNamePermission().hashCode());
        return result;
    }
}
