package com.pinyougou.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "tb_address")
public class TbAddress implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 省
     */
    @Column(name = "province_id")
    private String provinceId;

    /**
     * 市
     */
    @Column(name = "city_id")
    private String cityId;

    /**
     * 县/区
     */
    @Column(name = "town_id")
    private String townId;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 是否是默认 1默认 0否
     */
    @Column(name = "is_default")
    private String isDefault;

    /**
     * 备注
     */
    private String notes;

    /**
     * 创建日期
     */
    @Column(name = "create_date")
    private Date createDate;

    /**
     * 别名
     */
    private String alias;

    private static final long serialVersionUID = 1L;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取用户ID
     *
     * @return user_id - 用户ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置用户ID
     *
     * @param userId 用户ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取省
     *
     * @return province_id - 省
     */
    public String getProvinceId() {
        return provinceId;
    }

    /**
     * 设置省
     *
     * @param provinceId 省
     */
    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    /**
     * 获取市
     *
     * @return city_id - 市
     */
    public String getCityId() {
        return cityId;
    }

    /**
     * 设置市
     *
     * @param cityId 市
     */
    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    /**
     * 获取县/区
     *
     * @return town_id - 县/区
     */
    public String getTownId() {
        return townId;
    }

    /**
     * 设置县/区
     *
     * @param townId 县/区
     */
    public void setTownId(String townId) {
        this.townId = townId;
    }

    /**
     * 获取手机
     *
     * @return mobile - 手机
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置手机
     *
     * @param mobile 手机
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取详细地址
     *
     * @return address - 详细地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置详细地址
     *
     * @param address 详细地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取联系人
     *
     * @return contact - 联系人
     */
    public String getContact() {
        return contact;
    }

    /**
     * 设置联系人
     *
     * @param contact 联系人
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * 获取是否是默认 1默认 0否
     *
     * @return is_default - 是否是默认 1默认 0否
     */
    public String getIsDefault() {
        return isDefault;
    }

    /**
     * 设置是否是默认 1默认 0否
     *
     * @param isDefault 是否是默认 1默认 0否
     */
    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    /**
     * 获取备注
     *
     * @return notes - 备注
     */
    public String getNotes() {
        return notes;
    }

    /**
     * 设置备注
     *
     * @param notes 备注
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * 获取创建日期
     *
     * @return create_date - 创建日期
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 设置创建日期
     *
     * @param createDate 创建日期
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取别名
     *
     * @return alias - 别名
     */
    public String getAlias() {
        return alias;
    }

    /**
     * 设置别名
     *
     * @param alias 别名
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", provinceId=").append(provinceId);
        sb.append(", cityId=").append(cityId);
        sb.append(", townId=").append(townId);
        sb.append(", mobile=").append(mobile);
        sb.append(", address=").append(address);
        sb.append(", contact=").append(contact);
        sb.append(", isDefault=").append(isDefault);
        sb.append(", notes=").append(notes);
        sb.append(", createDate=").append(createDate);
        sb.append(", alias=").append(alias);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}