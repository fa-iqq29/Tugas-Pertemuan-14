package pertemuan14;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Faiq
 */
@Entity
@Table(name = "mata_kuliah")
@NamedQueries({
    @NamedQuery(name = "MataKuliah.findAll", query = "SELECT m FROM MataKuliah m ORDER BY m.kodeMatkul"),
    @NamedQuery(name = "MataKuliah.findByKodeMatkul", query = "SELECT m FROM MataKuliah m WHERE m.kodeMatkul = :kodeMatkul"),
    @NamedQuery(name = "MataKuliah.findByNamaMatkul", query = "SELECT m FROM MataKuliah m WHERE m.namaMatkul = :namaMatkul"),
    @NamedQuery(name = "MataKuliah.findBySksMatkul", query = "SELECT m FROM MataKuliah m WHERE m.sksMatkul = :sksMatkul"),
    @NamedQuery(name = "MataKuliah.findBySemesterMatkul", query = "SELECT m FROM MataKuliah m WHERE m.semesterMatkul = :semesterMatkul")})
public class MataKuliah implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "kode_matkul")
    private String kodeMatkul;
    @Basic(optional = false)
    @Column(name = "nama_matkul")
    private String namaMatkul;
    @Basic(optional = false)
    @Column(name = "sks_matkul")
    private int sksMatkul;
    @Basic(optional = false)
    @Column(name = "semester_matkul")
    private int semesterMatkul;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "kodeMatkul")
    private Collection<Mengajar> mengajarCollection;

    public MataKuliah() {
    }

    public MataKuliah(String kodeMatkul) {
        this.kodeMatkul = kodeMatkul;
    }

    public MataKuliah(String kodeMatkul, String namaMatkul, int sksMatkul, int semesterMatkul) {
        this.kodeMatkul = kodeMatkul;
        this.namaMatkul = namaMatkul;
        this.sksMatkul = sksMatkul;
        this.semesterMatkul = semesterMatkul;
    }

    public String getKodeMatkul() {
        return kodeMatkul;
    }

    public void setKodeMatkul(String kodeMatkul) {
        this.kodeMatkul = kodeMatkul;
    }

    public String getNamaMatkul() {
        return namaMatkul;
    }

    public void setNamaMatkul(String namaMatkul) {
        this.namaMatkul = namaMatkul;
    }

    public int getSksMatkul() {
        return sksMatkul;
    }

    public void setSksMatkul(int sksMatkul) {
        this.sksMatkul = sksMatkul;
    }

    public int getSemesterMatkul() {
        return semesterMatkul;
    }

    public void setSemesterMatkul(int semesterMatkul) {
        this.semesterMatkul = semesterMatkul;
    }

    public Collection<Mengajar> getMengajarCollection() {
        return mengajarCollection;
    }

    public void setMengajarCollection(Collection<Mengajar> mengajarCollection) {
        this.mengajarCollection = mengajarCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kodeMatkul != null ? kodeMatkul.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MataKuliah)) {
            return false;
        }
        MataKuliah other = (MataKuliah) object;
        if ((this.kodeMatkul == null && other.kodeMatkul != null) || (this.kodeMatkul != null && !this.kodeMatkul.equals(other.kodeMatkul))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return namaMatkul;
    }
    
}
