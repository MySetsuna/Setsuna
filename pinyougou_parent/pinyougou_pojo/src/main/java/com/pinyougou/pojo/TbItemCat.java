package com.pinyougou.pojo;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "tb_item_cat")
public class TbItemCat implements Serializable {
    /**
     * 类目ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 父类目ID=0时，代表的是一级的类目
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 类目名称
     */
    private String name;

    /**
     * 类型id
     */
    @Column(name = "type_id")
    private Long typeId;
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

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    private static final long serialVersionUID = 1L;

    /**
     * 获取类目ID
     *
     * @return id - 类目ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置类目ID
     *
     * @param id 类目ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取父类目ID=0时，代表的是一级的类目
     *
     * @return parent_id - 父类目ID=0时，代表的是一级的类目
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置父类目ID=0时，代表的是一级的类目
     *
     * @param parentId 父类目ID=0时，代表的是一级的类目
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取类目名称
     *
     * @return name - 类目名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置类目名称
     *
     * @param name 类目名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取类型id
     *
     * @return type_id - 类型id
     */
    public Long getTypeId() {
        return typeId;
    }

    /**
     * 设置类型id
     *
     * @param typeId 类型id
     */
    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    @Override
    public String toString() {
        return "TbItemCat{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", typeId=" + typeId +
                ", sellerId='" + sellerId + '\'' +
                ", auditStatus='" + status + '\'' +
                '}';
    }
}