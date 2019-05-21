package com.pinyougou.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "tb_areas")
public class TbAreas implements Serializable {
    /**
     * 唯一ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 区域ID
     */
    private String areaid;

    /**
     * 区域名称
     */
    private String area;

    /**
     * 城市ID
     */
    private String cityid;

    private static final long serialVersionUID = 1L;

    /**
     * 获取唯一ID
     *
     * @return id - 唯一ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置唯一ID
     *
     * @param id 唯一ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取区域ID
     *
     * @return areaid - 区域ID
     */
    public String getAreaid() {
        return areaid;
    }

    /**
     * 设置区域ID
     *
     * @param areaid 区域ID
     */
    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    /**
     * 获取区域名称
     *
     * @return area - 区域名称
     */
    public String getArea() {
        return area;
    }

    /**
     * 设置区域名称
     *
     * @param area 区域名称
     */
    public void setArea(String area) {
        this.area = area;
    }

    /**
     * 获取城市ID
     *
     * @return cityid - 城市ID
     */
    public String getCityid() {
        return cityid;
    }

    /**
     * 设置城市ID
     *
     * @param cityid 城市ID
     */
    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", areaid=").append(areaid);
        sb.append(", area=").append(area);
        sb.append(", cityid=").append(cityid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}