package org.example.island.object;

import java.io.Serializable;

public class TableGroup implements Serializable { //Record?
    private Integer id;
    private Integer key;
    private String gr_name;
    private Long st_count;
    private Long shouldExp;
    private String form;
    private String sem;
    private Float c_x;
    private Double c_y;
    private String p_name;
    private Float p_height;
    private Double p_weight;
    private String p_color;
    private Long l_x;
    private Long l_y;
    private Integer l_z;
    private String l_name;
    private String creationDate;
    private String owner;

    public TableGroup(int id, int key, String gr_name, long st_count, long shouldExp, String form, String sem, float c_x, double c_y, String p_name, Float p_height, double p_weight, String p_color, Long l_x, long l_y, int l_z, String l_name, String creationDate, String owner) {
        this.id = id;
        this.key = key;
        this.gr_name = gr_name;
        this.st_count = st_count;
        this.shouldExp = shouldExp;
        this.form = form;
        this.sem = sem;
        this.c_x = c_x;
        this.c_y = c_y;
        this.p_name = p_name;
        this.p_height = p_height;
        this.p_weight = p_weight;
        this.p_color = p_color;
        this.l_x = l_x;
        this.l_y = l_y;
        this.l_z = l_z;
        this.l_name = l_name;
        this.creationDate = creationDate;
        this.owner = owner;
    }

    public TableGroup() {}

    public void setId(int id) {
        this.id = id;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setGr_name(String gr_name) {
        this.gr_name = gr_name;
    }

    public void setSt_count(Long st_count) {
        this.st_count = st_count;
    }

    public void setShouldExp(Long shouldExp) {
        this.shouldExp = shouldExp;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }

    public void setC_x(Float c_x) {
        this.c_x = c_x;
    }

    public void setC_y(Double c_y) {
        this.c_y = c_y;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public void setP_height(Float p_height) {
        this.p_height = p_height;
    }

    public void setP_weight(Double p_weight) {
        this.p_weight = p_weight;
    }

    public void setP_color(String p_color) {
        this.p_color = p_color;
    }

    public void setL_x(Long l_x) {
        this.l_x = l_x;
    }

    public void setL_y(Long l_y) {
        this.l_y = l_y;
    }

    public void setL_z(int l_z) {
        this.l_z = l_z;
    }

    public void setL_name(String l_name) {
        this.l_name = l_name;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public int getKey() {
        return key;
    }

    public String getGr_name() {
        return gr_name;
    }

    public long getSt_count() {
        return st_count;
    }

    public long getShouldExp() {
        return shouldExp;
    }

    public String getForm() {
        return form;
    }

    public String getSem() {
        return sem;
    }

    public float getC_x() {
        return c_x;
    }

    public double getC_y() {
        return c_y;
    }

    public String getP_name() {
        return p_name;
    }

    public Float getP_height() {
        return p_height;
    }

    public double getP_weight() {
        return p_weight;
    }

    public String getP_color() {
        return p_color;
    }

    public Long getL_x() {
        return l_x;
    }

    public long getL_y() {
        return l_y;
    }

    public int getL_z() {
        return l_z;
    }

    public String getL_name() {
        return l_name;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getOwner() {
        return owner;
    }
}
