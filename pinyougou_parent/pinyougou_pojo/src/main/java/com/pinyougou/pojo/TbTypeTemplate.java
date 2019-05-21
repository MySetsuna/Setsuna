package com.pinyougou.pojo;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "tb_type_template")
public class TbTypeTemplate implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 关联规格
     */
    @Column(name = "spec_ids")
    private String specIds;

    /**
     * 关联品牌
     */
    @Column(name = "brand_ids")
    private String brandIds;

    /**
     * 自定义属性
     */
    @Column(name = "custom_attribute_items")
    private String customAttributeItems;
    /**
     * 申请的商家名称
     */
    @Column(name = "seller_id")
    private String sellerId;


    /**
     * 状态
     */
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    private static final long serialVersionUID = 1L;

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

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
     * 获取模板名称
     *
     * @return name - 模板名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置模板名称
     *
     * @param name 模板名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取关联规格
     *
     * @return spec_ids - 关联规格
     */
    public String getSpecIds() {
        return specIds;
    }

    /**
     * 设置关联规格
     *
     * @param specIds 关联规格
     */
    public void setSpecIds(String specIds) {
        this.specIds = specIds;
    }

    /**
     * 获取关联品牌
     *
     * @return brand_ids - 关联品牌
     */
    public String getBrandIds() {
        return brandIds;
    }

    /**
     * 设置关联品牌
     *
     * @param brandIds 关联品牌
     */
    public void setBrandIds(String brandIds) {
        this.brandIds = brandIds;
    }

    /**
     * 获取自定义属性
     *
     * @return custom_attribute_items - 自定义属性
     */
    public String getCustomAttributeItems() {
        return customAttributeItems;
    }

    /**
     * 设置自定义属性
     *
     * @param customAttributeItems 自定义属性
     */
    public void setCustomAttributeItems(String customAttributeItems) {
        this.customAttributeItems = customAttributeItems;
    }

    @Override
    public String toString() {
        return "TbTypeTemplate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", specIds='" + specIds + '\'' +
                ", brandIds='" + brandIds + '\'' +
                ", customAttributeItems='" + customAttributeItems + '\'' +
                ", sellerId='" + sellerId + '\'' +
                ", auditStatus='" + status + '\'' +
                '}';
    }
}