package org.ih.dao.model;

import org.ih.dao.DatabaseModel;
import org.ih.dto.District;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "District")
public class DistrictModel implements DatabaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "district_id")
    @SequenceGenerator(name = "district_id", sequenceName = "district_id_seq", allocationSize = 1)
    private long id;

    @Column(name = "label")
    private String label;

    @Column(name = "description")
    private String description;

    @OneToMany(fetch = FetchType.LAZY)
    private final Set<SchoolModel> schools = new HashSet<>();

    public long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<SchoolModel> getSchools() {
        return schools;
    }

    @Override
    public District toDataObject() {
        District district = new District();
        district.setId(this.id);
        district.setLabel(this.label);
        district.setDescription(this.description);
        return district;
    }
}
